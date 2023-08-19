/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.log;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/**
 * @author TsuboiY
 * 
 */
public class AdminLogUtilsBean implements AdminLogUtils, Serializable {

    private static final long serialVersionUID = 1L;

    private static final String LOG_TEMPLATE = "<<[%s]>> %s";

    private String _id;

    private String _currentRequestPath;

    private long _currentRequestStartTimeMillis = -1;

    @Override
    public String format(Object log, Object... args) {
        return String.format(LOG_TEMPLATE, _id,
                String.format(log.toString(), args));
    }

    @Override
    public void prepare(String id) {
        if (_id == null) {
            _id = id;
        }
    }

    @Override
    public String formatRequestStartLog(String path, Map<?, ?> parameterMap) {
        _currentRequestStartTimeMillis = System.currentTimeMillis();
        _currentRequestPath = path;
        String paramStr = buildParameter(parameterMap);
        return format("%s%s requested.", _currentRequestPath, paramStr);
    }

    @Override
    public String formatRequestEndLog() {
        String path = _currentRequestPath;
        long elapsed = getRequestElapsedTime();
        _currentRequestPath = null;
        _currentRequestStartTimeMillis = -1;
        return format("%s done. (%dmsec)", path, elapsed);
    }

    @Override
    public long getRequestElapsedTime() {
        return System.currentTimeMillis() - _currentRequestStartTimeMillis;
    }

    private String buildParameter(Map<?, ?> parameterMap) {
        StringBuilder sb = new StringBuilder();
        Iterator<?> ite = parameterMap.keySet().iterator();
        while (ite.hasNext()) {
            if (sb.length() > 0) {
                sb.append(" ");
            } else {
                sb.append(" {");
            }
            String key = (String) ite.next();
            sb.append(key);
            sb.append(":");
            for (String s : (String[]) parameterMap.get(key)) {
                sb.append("[");
                sb.append(s);
                sb.append("]");
            }
        }
        if (sb.length() > 0) {
            sb.append("}");
        }
        return sb.toString();
    }
}
