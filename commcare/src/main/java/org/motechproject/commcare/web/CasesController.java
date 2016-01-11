package org.motechproject.commcare.web;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.CaseXml;
import org.motechproject.commcare.events.CaseEvent;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.exception.CaseParserException;
import org.motechproject.commcare.exception.EndpointNotSupported;
import org.motechproject.commcare.parser.CaseParser;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.metrics.service.MetricRegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.motechproject.commcare.events.constants.EventDataKeys.FIELD_VALUES;

/**
 * Controller that handles the incoming case feed from CommCareHQ. It is capable of handling multiple configurations by
 * parameterizing the endpoint URL.
 */
@Controller
@RequestMapping("/cases")
public class CasesController extends CommcareController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CasesController.class);
    private static final String CASE_PUSH_DELAY = "commcare.push.case.delay";
    private static final String CASE_PUSH_METER = "commcare.push.case.meter";

    private static final String FULL_DATA_EVENT = "full";
    private static final String PARTIAL_DATA_EVENT = "partial";

    private EventRelay eventRelay;
    private CommcareConfigService configService;
    private MetricRegistryService metricRegistryService;

    @Autowired
    public CasesController(final EventRelay eventRelay, final CommcareConfigService configService, final MetricRegistryService metricRegistryService) {
        this.eventRelay = eventRelay;
        this.configService = configService;
        this.metricRegistryService = metricRegistryService;
    }

    @RequestMapping
    public ModelAndView receiveCaseForDefaultConfig(HttpServletRequest request) throws EndpointNotSupported {
        return doReceiveCase(request, configService.getDefault());
    }

    @RequestMapping("/{configName}")
    public ModelAndView receiveCase(HttpServletRequest request, @PathVariable String configName) throws EndpointNotSupported {
        return doReceiveCase(request, configService.getByName(configName));
    }

    private String getRequestBodyAsString(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        boolean end = false;
        StringBuilder forwardedRequest = new StringBuilder();
        while (!end) {
            String line = reader.readLine();
            if (line == null) {
                end = true;
            } else {
                forwardedRequest.append(line);
            }
        }

        return forwardedRequest.toString();
    }

    private ModelAndView doReceiveCase(HttpServletRequest request, Config config) throws EndpointNotSupported {

        String caseXml = "";

        try {
            caseXml = getRequestBodyAsString(request);
        } catch (IOException e1) {
            LOGGER.error(e1.getMessage(), e1);
        }
        LOGGER.trace("Received request for mapping /cases: {}", caseXml);
        metricRegistryService.meter(CASE_PUSH_METER).mark();

        if (!config.isForwardCases()) {
            throw new EndpointNotSupported(String.format("Configuration \"%s\" doesn't support endpoint for cases!", config.getName()));
        }

        CaseParser<CaseXml> parser = new CaseParser<>(CaseXml.class, caseXml);

        CaseXml caseInstance = null;
        try {
            caseInstance = parser.parseCase();
        } catch (CaseParserException e) {
            MotechEvent motechEvent = new MotechEvent(
                    EventSubjects.MALFORMED_CASE_EXCEPTION);
            motechEvent.getParameters().put(EventDataKeys.MESSAGE,
                    "Incoming case xml did not parse correctly");
            eventRelay.sendEventMessage(motechEvent);
        }

        if (caseInstance != null) {

            caseInstance.setServerModifiedOn(request.getHeader("Server-Modified-On"));
            CaseEvent caseEvent = new CaseEvent(caseInstance.getCaseId());
            caseEvent.setConfigName(config.getName());
            caseEvent.setCaseType(caseInstance.getCaseType());

            updateCasePushDelay(caseInstance.getServerModifiedOn());

            MotechEvent motechCaseEvent;
            String caseEventStrategy = config.getEventStrategy();

            if (caseEventStrategy.equals(FULL_DATA_EVENT)) {
                caseEvent = CaseEvent.fromCaseXml(caseInstance, config.getName());
                motechCaseEvent = caseEvent.toMotechEventWithData();
                motechCaseEvent.getParameters().put(FIELD_VALUES, caseEvent.getFieldValues());
            } else if (caseEventStrategy.equals(PARTIAL_DATA_EVENT)) {
                motechCaseEvent = caseEvent.toMotechEventWithData();
            } else {
                motechCaseEvent = caseEvent.toMotechEventWithoutData();
            }

            eventRelay.sendEventMessage(motechCaseEvent);
        }

        return null;
    }

    private void updateCasePushDelay(String commcareTimeStamp) {
        Instant commcareReceivedOn = Instant.parse(commcareTimeStamp);
        long delay = ChronoUnit.SECONDS.between(commcareReceivedOn, Instant.now());
        metricRegistryService.histogram(CASE_PUSH_DELAY).update(delay);
    }
}
