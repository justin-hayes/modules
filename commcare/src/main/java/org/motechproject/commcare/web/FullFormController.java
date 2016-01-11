package org.motechproject.commcare.web;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.events.FullFormEvent;
import org.motechproject.commcare.events.FullFormFailureEvent;
import org.motechproject.commcare.events.MalformedFormStatusMessageEvent;
import org.motechproject.commcare.exception.EndpointNotSupported;
import org.motechproject.commcare.exception.FullFormParserException;
import org.motechproject.commcare.parser.FullFormParser;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.metrics.service.MetricRegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Controller that handles the incoming full form feed from CommCareHQ. The path to this endpoint has to be configured
 * on the CommCareHQ side. It is capable of handling multiple configurations by parameterizing the endpoint URL.
 */
@Controller
@RequestMapping("/forms")
public class FullFormController extends CommcareController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FullFormController.class);
    private static final String FULL_FORM_PUSH_DELAY = "commcare.push.fullform.delay";
    private static final String FULL_FORM_PUSH_METER = "commcare.push.fullform.meter";

    private CommcareConfigService configService;
    private EventRelay eventRelay;
    private final MetricRegistryService metricRegistryService;

    @Autowired
    public FullFormController(EventRelay eventRelay, CommcareConfigService configService, MetricRegistryService metricRegistryService) {
        this.eventRelay = eventRelay;
        this.configService = configService;
        this.metricRegistryService = metricRegistryService;
    }

    @RequestMapping
    @ResponseStatus(HttpStatus.OK)
    public void receiveFormForDefaultConfig(@RequestBody String body, HttpServletRequest request) throws EndpointNotSupported {
        doReceiveForm(body, request, configService.getDefault());
    }

    @RequestMapping(value = "/{configName}")
    @ResponseStatus(HttpStatus.OK)
    public void receiveForm(@RequestBody String body, HttpServletRequest request) throws EndpointNotSupported {
        doReceiveForm(body, request, configService.getByName(getConfigName(request)));
    }

    private void doReceiveForm(String body, HttpServletRequest request, Config config) throws EndpointNotSupported {

        LOGGER.trace("Received request for mapping /forms: {}", body);

        metricRegistryService.meter(FULL_FORM_PUSH_METER).mark();

        if (!config.isForwardForms()) {
            throw new EndpointNotSupported(String.format("Configuration \"%s\" doesn't support endpoint for forms!", config.getName()));
        }

        FullFormParser parser = new FullFormParser(body);

        try {
            FormValueElement formValueElement = parser.parse();

            String receivedTimestamp = request.getHeader("received-on");

            updateFullFormPushDelay(receivedTimestamp);

            FullFormEvent fullFormEvent = new FullFormEvent(formValueElement, receivedTimestamp,
                    config.getName());

            eventRelay.sendEventMessage(fullFormEvent.toMotechEvent());
        } catch (FullFormParserException | RuntimeException e) {
            LOGGER.error("Error while receiving form from Commcare", e);
            handleError(e, config);
        }
    }

    private void handleError(Exception e, Config config) {
        FullFormFailureEvent failureEvent = new FullFormFailureEvent(config.getName(), e.getMessage());
        eventRelay.sendEventMessage(failureEvent.toMotechEvent());
        // publish a status message in the Admin module
        String msg = "Error while receiving a form from Commcare: " + e.getMessage();
        MalformedFormStatusMessageEvent statusMessageEvent = new MalformedFormStatusMessageEvent(msg);
        eventRelay.sendEventMessage(statusMessageEvent.toMotechEvent());
    }

    private String getConfigName(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return pathInfo.substring(pathInfo.lastIndexOf('/') + 1);
    }

    private void updateFullFormPushDelay(String commcareTimeStamp) {
        Instant commcareReceivedOn = Instant.parse(commcareTimeStamp);
        long difference = ChronoUnit.SECONDS.between(commcareReceivedOn, Instant.now());
        metricRegistryService.histogram(FULL_FORM_PUSH_DELAY).update(difference);
    }
}
