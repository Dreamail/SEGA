/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.log;

import java.util.Map;

/**
 * @author TsuboiY
 * 
 */
public interface AdminLogUtils extends LogUtils {

    void prepare(String id);

    String formatRequestStartLog(String path, Map<?, ?> parameterMap);

    String formatRequestEndLog();

    long getRequestElapsedTime();

}
