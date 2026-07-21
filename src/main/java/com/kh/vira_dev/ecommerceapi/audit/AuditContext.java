package com.kh.vira_dev.ecommerceapi.audit;

import tools.jackson.databind.JsonNode;

public class AuditContext {

    private static final ThreadLocal<JsonNode> OLD_VALUES = new ThreadLocal<>();
    private static final ThreadLocal<JsonNode> NEW_VALUES = new ThreadLocal<>();
    private static final ThreadLocal<String> REASON = new ThreadLocal<>();

    public static void setOldValue(JsonNode v) {
        OLD_VALUES.set(v);
    }

    public static void setNewValues(JsonNode v) {
        NEW_VALUES.set(v);
    }

    public static void setReason(String r) {
        REASON.set(r);
    }

    public static JsonNode getOldValue() {
        return OLD_VALUES.get();
    }

    public static JsonNode getNewValue() {
        return NEW_VALUES.get();
    }

    public static String getReason() {
        return REASON.get();
    }

    public void clear() {
        OLD_VALUES.remove();
        NEW_VALUES.remove();
        REASON.remove();
    }


}
