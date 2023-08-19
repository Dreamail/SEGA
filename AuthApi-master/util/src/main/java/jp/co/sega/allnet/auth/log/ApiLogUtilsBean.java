/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.log;

/**
 * @author TsuboiY
 * 
 */
public class ApiLogUtilsBean extends AbstractLogUtils implements ApiLogUtils {

    private static final String LOG_TEMPLATE = "<<[%s]>> %s";

    private final long startTimeMillis = System.currentTimeMillis();

    @Override
    public String format(Object log, Object... args) {
        return String.format(LOG_TEMPLATE, getId(),
                String.format(log.toString(), args));
    }

    @Override
    public String formatStartLog(String functionName) {
        return format("%s start", functionName);
    }

    @Override
    public String formatStartLog(String functionName, String ip) {
        return format("%s start [requested from: %s]", functionName, ip);
    }

    @Override
    public String formatFinishLog(String functionName) {
        return formatFinishLog(functionName, false);
    }

    @Override
    public String formatFinishLog(String functionName, boolean interrupt) {
        return formatFinishLog(functionName, interrupt, null);
    }

    @Override
    public String formatFinishLog(String functionName, boolean interrupt,
            String response) {
        String stat;
        if (interrupt) {
            stat = "interrupted";
        } else {
            stat = "finished";
        }
        if (response == null) {
            return format("%s %s [%dmsec]", functionName, stat,
                    getElapsedTime());
        } else {
            return format("%s %s [%dmsec]: Response [%s]", functionName, stat,
                    getElapsedTime(), response);
        }
    }

    @Override
    public long getElapsedTime() {
        return System.currentTimeMillis() - startTimeMillis;
    }

}
