package com.highright.highcare.util;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
public class LogColoring extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        switch (event.getLevel().levelInt) {
            case ch.qos.logback.classic.Level.ERROR_INT:
                return "\u001B[31mERROR\u001B[0m"; // Red color for ERROR
            case ch.qos.logback.classic.Level.WARN_INT:
                return "\u001B[33mWARN\u001B[0m"; // Yellow color for WARN
            case ch.qos.logback.classic.Level.INFO_INT:
                return "\u001B[32mINFO\u001B[0m"; // reset color for INFO
            case ch.qos.logback.classic.Level.DEBUG_INT:
                return "\u001B[34mDEBUG\u001B[0m "; // Blue color for DEBUG
            case ch.qos.logback.classic.Level.TRACE_INT:
                return "\u001B[36mTRACE\u001B[0m"; // Cyan color for TRACE
            default:
                return "\u001B[0m"; // Reset color for other levels
        }



    }
}