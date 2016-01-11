package org.motechproject.ivr.metrics;

public final class MetricNames {
    public static final String INITIATED_CALLS_METER = "ivr.calls.outbound.initiated";
    public static final String INCOMPLETE_CALLS_COUNTER = "ivr.calls.outbound.incomplete";
    public static final String CALL_STATUS_COUNTER_TEMPLATE = "ivr.calls.statuses.%s.count";
    public static final String CALL_STATUS_RATIO_TEMPLATE = "ivr.calls.statuses.%s.ratio";

    private MetricNames() {}
}
