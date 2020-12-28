package study.git2consul.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author thainq
 */
public enum SyncType {
    PUB("pup"),
    SECRET("secret"),
    ALL("all");

    private static final Map<String, SyncType> ENUM_MAP = new HashMap();

    static {
        for (SyncType e : SyncType.values()) {
            ENUM_MAP.put(e.value, e);
        }
    }

    private final String value;

    SyncType(String value) {
        this.value = value;
    }

    public static SyncType getEnum(String value) {
        return ENUM_MAP.get(value);
    }
}
