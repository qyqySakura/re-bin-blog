package llf.llf.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EmailCodeCache {
    private static final Map<String, String> CODE_MAP = new ConcurrentHashMap<>();
    public static void save(String email, String code) { CODE_MAP.put(email, code); }
    public static String get(String email) { return CODE_MAP.get(email); }
    public static void remove(String email) { CODE_MAP.remove(email); }
} 