/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.test;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author TsuboiY
 * 
 */
public class JulUtils {

    private JulUtils() {
        // do nothing
    }

    public static void configureForUnitTest() {
        for (Handler handler : Logger.getLogger("").getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                handler.setLevel(Level.ALL);
            }
        }
        Logger.getLogger("jp.co.sega.allnet.auth").setLevel(Level.ALL);
    }

}
