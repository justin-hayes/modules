package org.motechproject.ivr.metric.service.impl;

import org.motechproject.ivr.metric.service.IvrMetricsService;
import org.motechproject.metrics.api.Counter;
import org.motechproject.metrics.api.Histogram;
import org.motechproject.metrics.api.Meter;
import org.motechproject.metrics.service.MetricRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service("ivrMetricsService")
public class IvrMetricsServiceImpl implements IvrMetricsService {
    private final MetricRegistryService metricRegistryService;
    private final Meter initiatedCallsMeter;
    private final Counter incompleteCallsCounter;
    private final Histogram callDurationHistogram;

    @Autowired
    public IvrMetricsServiceImpl(MetricRegistryService metricRegistryService) {
        this.metricRegistryService = metricRegistryService;
        this.initiatedCallsMeter = metricRegistryService.meter("ivr.initiated_calls");
        this.incompleteCallsCounter = metricRegistryService.counter("ivr.incomplete_calls");
        this.callDurationHistogram = metricRegistryService.histogram("ivr.call_duration");
    }

    @Override
    public void markInitiatedCallMeter() {
        initiatedCallsMeter.mark();
        incompleteCallsCounter.inc();
    }

    @Override
    public void countCallStatus(String callStatus, List<String> terminalCallStatuses) {
        Counter callStatusCounter = metricRegistryService.counter(String.format("ivr.call_statuses.%s", callStatus));
        callStatusCounter.inc();

        // Create a ratio gauge of this call status against total number of call statuses
        String callStatusRatioName = String.format("ivr.call_statuses.%s.ratio", callStatus);

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
            incompleteCallsCounter.dec();
        }
    }

    @Override
    public void updateCallDurationHistogram(int callDuration) {
        callDurationHistogram.update(callDuration);
    }
}
