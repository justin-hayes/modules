package org.motechproject.ivr.metrics;

public final class MetricNames {
    public static final String INITIATED_CALLS_METER = "ivr.initiated_calls";
    public static final String INCOMPLETE_CALLS_COUNTER = "ivr.incomplete_calls";
    public static final String CALL_STATUS_COUNTER_TEMPLATE = "ivr.call_statuses.%s";
    public static final String CALL_STATUS_RATIO_TEMPLATE = "ivr.call_statuses.%s.ratio";

    private MetricNames() {}
}
