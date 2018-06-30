package com.databazoo.util;

public enum Duration {
    daily(24),
    hourly(1);

    private final int hours;

    Duration(int hours) {
        this.hours = hours;
    }

    public int getHours() {
        return hours;
    }
}
