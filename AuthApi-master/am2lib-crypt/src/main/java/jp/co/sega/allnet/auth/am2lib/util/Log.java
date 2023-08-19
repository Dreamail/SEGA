/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.am2lib.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author TsuboiY
 * 
 */
public class Log {

    private static final Logger _log = LoggerFactory.getLogger(Log.class);

    public static void write(String msg, Level level) {
        switch (level) {
        case ERROR:
            _log.error(msg);
            break;
        case WARN:
            _log.warn(msg);
            break;
        case INFO:
            _log.info(msg);
            break;
        case TRACE:
            _log.trace(msg);
            break;
        default:
            _log.debug(msg);
        }
    }

    public static void write(String msg) {
        write(msg, Level.DEBUG);
    }

    public enum Level {
        ERROR, WARN, INFO, DEBUG, TRACE;
    }

}
