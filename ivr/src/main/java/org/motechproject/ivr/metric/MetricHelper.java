package org.motechproject.ivr.metric;

import org.motechproject.metrics.api.Counter;
import org.motechproject.metrics.api.Meter;
import org.motechproject.metrics.service.MetricRegistryService;

import java.util.List;
import java.util.function.Supplier;

import static org.motechproject.ivr.metric.MetricNames.CALL_STATUS_COUNTER_TEMPLATE;
import static org.motechproject.ivr.metric.MetricNames.CALL_STATUS_RATIO_TEMPLATE;
import static org.motechproject.ivr.metric.MetricNames.INCOMPLETE_CALLS_COUNTER;
import static org.motechproject.ivr.metric.MetricNames.INITIATED_CALLS_METER;

public final class MetricHelper {
    public static void countCallStatus(MetricRegistryService metricRegistryService, String callStatus, List<String> terminalCallStatuses) {
        Counter callStatusCounter = metricRegistryService.counter(String.format(CALL_STATUS_COUNTER_TEMPLATE, callStatus));
        Meter initiatedCallsMeter = metricRegistryService.meter(INITIATED_CALLS_METER);

        callStatusCounter.inc();

        // Create a ratio gauge of this call status against total number of call statuses
        String callStatusRatioName = String.format(CALL_STATUS_RATIO_TEMPLATE, callStatus);

        if (!metricRegistryService.isRegistered(callStatusRatioName)) {
            metricRegistryService.registerRatioGauge(callStatusRatioName, new Supplier<Double>() {
                @Override
                public Double get() {
                    return (double) callStatusCounter.getCount();
                }
            }, new Supplier<Double>() {
                @Override
                public Double get() {
                    return (double) initiatedCallsMeter.getCount();
                }
            });
        }

        if (terminalCallStatuses != null && terminalCallStatuses.contains(callStatus)) {
            metricRegistryService.counter(INCOMPLETE_CALLS_COUNTER).dec();
        }
    }


    private MetricHelper() {}
}
