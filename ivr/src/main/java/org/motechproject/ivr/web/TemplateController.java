package org.motechproject.ivr.web;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.domain.Template;
import org.motechproject.ivr.event.EventSubjects;
import org.motechproject.ivr.exception.ConfigNotFoundException;
import org.motechproject.ivr.exception.IvrTemplateException;
import org.motechproject.ivr.exception.TemplateNotFoundException;
import org.motechproject.ivr.metric.service.IvrMetricsService;
import org.motechproject.ivr.repository.CallDetailRecordDataService;
import org.motechproject.ivr.service.ConfigService;
import org.motechproject.ivr.service.TemplateService;
import org.motechproject.mds.service.MDSLookupService;
import org.motechproject.osgi.web.util.OSGiServiceUtils;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import static org.motechproject.ivr.util.Constants.HAS_MANAGE_IVR_ROLE;
import static org.motechproject.ivr.web.LogAndEventHelper.sendAndLogEvent;

/**
 * Responds to HTTP queries to {motech-server}/module/ivr/template/{configName}/{templateName} by creating a
 * CallDetailRecord entry in the database, posting a corresponding MOTECH event on the queue and returning the text
 * corresponding to the given template name and where all variable are replaced by the values passed as query parameters
 * See https://velocity.apache.org/ for the template language rules.
 */
@Controller
public class TemplateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);

    private static final String LOG4J = "org.apache.velocity.runtime.log.Log4JLogChute";
    private static final String LOGSYSTEM_CLASS = "runtime.log.logsystem.class";
    private static final String LOGSYSTEM_LOGGER = "runtime.log.logsystem.log4j.logger";

    private CallDetailRecordDataService callDetailRecordDataService;
    private TemplateService templateService;
    private ConfigService configService;
    private StatusMessageService statusMessageService;
    private EventRelay eventRelay;
    private MDSLookupService mdsLookupService;
    private IvrMetricsService ivrMetricsService;
    private BundleContext bundleContext;

    @PostConstruct
    public void setUpVelocityProperties() {
        try {
            Velocity.setProperty(LOGSYSTEM_CLASS, LOG4J);
            Velocity.setProperty(LOGSYSTEM_LOGGER, LOG4J);
            Velocity.init();
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Error initializing template engine: %s", e.toString()), e);
        }
    }

    /**
     * Listens to HTTP calls to http://{server}:{port}/module/ivr/template/{config}/{id}?key1=val1&key2=val2&... from
     * IVR providers. Creates a corresponding CDR entity in the database. Sends a MOTECH message with the CDR data in
     * the payload and the call status as the subject. Returns the template corresponding to the given id.
     *
     * @param configName the name of the configuration which should be used when processing this template
     * @param params the request parameters coming from the provider, will be passed to the template
     * @return static XML content with an OK response element.
     */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/template/{configName}/{templateName}", produces = "text/xml")
    public String handle(@PathVariable String configName, @PathVariable String templateName,
                         @RequestParam Map<String, String> params, @RequestHeader Map<String, String> headers) {
        LOGGER.debug(String.format("handle(configName = %s, templateName = %s, parameters = %s, headers = %s)",
                configName, templateName, params, headers));

        sendAndLogEvent(EventSubjects.TEMPLATE_REQUEST, configService, callDetailRecordDataService,
                statusMessageService, ivrMetricsService, eventRelay, configName, templateName, params);

        Template template = templateService.getTemplate(templateName);

        // No need to test for the existence of the config since it's already been done in the sendAndLogEvent() call
        Config config = configService.getConfig(configName);

        // Populate the context
        VelocityContext context = new VelocityContext();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            context.put(config.mapStatusField(entry.getKey()), entry.getValue());
        }


        context.put("headers", headers);

        // Add MDS access
        context.put("dataServices", mdsLookupService);

        StringBuilder notFoundServices = new StringBuilder();

        for (Map.Entry<String, String> entry : config.getServicesMap().entrySet()) {
            Object service = OSGiServiceUtils.findService(bundleContext, entry.getValue());
            if (service != null) {
                context.put(entry.getKey(), service);
            } else {
                notFoundServices.append(entry.getValue());
                notFoundServices.append("\n");
            }
        }

        if (!notFoundServices.toString().isEmpty()) {
            throw new IllegalStateException("Cannot load following services:\n" + notFoundServices.toString());
        }
        // Merge the template
        StringWriter writer = new StringWriter();
        try {
            Velocity.evaluate(context, writer, String.format("%s-%s", configName, templateName), template.getValue());
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Error evaluating template: %s", e.toString()), e);
        }

        LOGGER.debug("Merged {}:\n**********\n**********\n{}\n**********\n**********\n", templateName,
                writer.toString());

        return writer.toString();
    }

    /**
     * Retrieves all templates.
     * @return all templates stored
     */
    @RequestMapping(value = "/ivr-templates", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize(HAS_MANAGE_IVR_ROLE)
    public List<Template> getTemplates() {
        return templateService.allTemplates();
    }


    /**
     * Updates the templates. The new templates collection will overwrite the old one.
     * @param templates the new collection of templates to persist
     * @return the newly saved templates
     */
    @RequestMapping(value = "/ivr-templates", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize(HAS_MANAGE_IVR_ROLE)
    public List<Template> updateTemplates(@RequestBody List<Template> templates) {
        templateService.updateTemplates(templates);
        return templateService.allTemplates();
    }

    /**
     * Handles {@link org.motechproject.ivr.exception.ConfigNotFoundException} and {@link org.motechproject.ivr.exception.TemplateNotFoundException}.
     * Will return error 404 and the message from the exception as the response body.
     * @param e the exception to handle
     * @return the message coming from the exception
     */
    @ExceptionHandler({ConfigNotFoundException.class, TemplateNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleNotFoundException(Exception e) {
        LOGGER.error("Error processing template", e);
        return e.getMessage();
    }

    /**
     * Handles the {@link org.motechproject.ivr.exception.IvrTemplateException}. The error code from the exception
     * will be return (this allows templates to manipulate the error code), code 500 is returned if it is not set.
     * The message of the exception will be returned as the response body.
     * @param e the exception to handle
     * @param response the response object
     * @return the message from the exception
     * @see org.motechproject.ivr.exception.IvrTemplateException
     */
    @ExceptionHandler(IvrTemplateException.class)
    @ResponseBody
    public String handleCustomException(IvrTemplateException e, HttpServletResponse response) {
        LOGGER.error("Error processing template", e);

        if (e.getErrorCode() != null) {
            response.setStatus(e.getErrorCode().value());
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return e.getMessage();
    }

    /**
     * Handles all exception that do not match other handlers. Returns error code 500. The exception message
     * is returned as the response body.
     * @param e the exception to handle
     * @return the message from the exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(Exception e) {
        LOGGER.error("Error processing template", e);
        return e.getMessage();
    }

    @Autowired
    public void setCallDetailRecordDataService(CallDetailRecordDataService callDetailRecordDataService, IvrMetricsService ivrMetricsService) {
        this.callDetailRecordDataService = callDetailRecordDataService;
        this.ivrMetricsService = ivrMetricsService;
    }

    @Autowired
    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Autowired
    public void setMdsLookupService(MDSLookupService mdsLookupService) {
        this.mdsLookupService = mdsLookupService;
    }

    @Autowired
    public void setEventRelay(EventRelay eventRelay) {
        this.eventRelay = eventRelay;
    }

    @Autowired
    public void setStatusMessageService(StatusMessageService statusMessageService) {
        this.statusMessageService = statusMessageService;
    }

    @Autowired
    @Qualifier("configService")
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    @Autowired
    public void setTemplateService(TemplateService templateService) {
        this.templateService = templateService;
    }
}
