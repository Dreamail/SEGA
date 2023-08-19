/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author NakanoY
 * 
 */
public class ResponseUtils {

    private static final Logger _log = LoggerFactory
            .getLogger(ResponseUtils.class);

    private ResponseUtils() {
        // do nothing
    }

    public static String createResponceString(List<ResponseData> list) {

        StringBuilder res = new StringBuilder();

        for (ResponseData rd : list) {
            if (res.length() > 0) {
                res.append("&");
            }
            res.append(rd.getKey()).append("=").append(rd.getValue());
        }

        res.append("\n");

        String resStr = res.toString();

        if (_log.isDebugEnabled()) {
            _log.debug("Built response string: {}", resStr);
        }

        return resStr;

    }

}
