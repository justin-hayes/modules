package org.motechproject.ivr.metric.service;

import java.util.List;

public interface IvrMetricsService {
    void markInitiatedCallMeter();
    void countCallStatus(String callStatus, List<String> terminalCallStatuses);
    void updateCallDurationHistogram(int callDuration);
}
