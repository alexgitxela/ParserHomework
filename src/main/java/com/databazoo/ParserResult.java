package com.databazoo;

import java.util.LinkedHashMap;
import java.util.Map;

public class ParserResult {
    private final Map<String, Integer> badIp = new LinkedHashMap<>();

    public Map<String, Integer> getBadIp() {
        return badIp;
    }

    public void addBadIp(String ip, int callCount) {
        badIp.put(ip, callCount);
    }
}
