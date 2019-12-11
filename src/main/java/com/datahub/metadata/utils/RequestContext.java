package com.datahub.metadata.utils;

import java.util.HashMap;
import java.util.Map;

public final class RequestContext {

    public static final String COMPANY = "company";
    public static final String TRACKING_ID = "trackingId";

    private static final ThreadLocal<Map<String, String>> LOCAL_THREAD_CONTEXT = ThreadLocal.withInitial(HashMap::new);

    private RequestContext(){
        super();
    }

    public static String getCompany() {
        return LOCAL_THREAD_CONTEXT.get().get(COMPANY);
    }

    public static void setCompany(String company) {
        LOCAL_THREAD_CONTEXT.get().put(COMPANY, company);
    }

    public static String getTrackingId() {
        return LOCAL_THREAD_CONTEXT.get().get(TRACKING_ID);
    }

    public static void setTrackingId(String trackingId) {
        LOCAL_THREAD_CONTEXT.get().put(TRACKING_ID, trackingId);
    }

    public static String getProperty(String name) {
        return LOCAL_THREAD_CONTEXT.get().get(name);
    }

    public static void setProperty(String name, String value) {
        LOCAL_THREAD_CONTEXT.get().put(name, value);
    }

    public static void clear() {
        LOCAL_THREAD_CONTEXT.remove();
    }
}
