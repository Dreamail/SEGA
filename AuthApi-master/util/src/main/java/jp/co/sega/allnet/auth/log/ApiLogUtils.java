/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.log;

/**
 * @author TsuboiY
 * 
 */
public interface ApiLogUtils extends LogUtils {

    String formatStartLog(String apiName);

    String formatStartLog(String apiName, String ip);

    String formatFinishLog(String apiName);

    String formatFinishLog(String apiName, boolean interrupt);

    String formatFinishLog(String apiName, boolean interrupt, String response);

    long getElapsedTime();

}
