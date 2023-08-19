/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import jp.co.sega.allnet.auth.exception.ApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author NakanoY
 * 
 */
public class RequestUtils {

    private static final Logger _log = LoggerFactory
            .getLogger(RequestUtils.class);

    // デフォルトの文字コード
    public static final String DEFAULT_CHARACTER_ENCODING = "EUC-JP";

    private RequestUtils() {
        // do nothing
    }

    /**
     * レスポンスを変数ごとに分割
     * 
     * @param req
     * @return
     */
    public static Map<String, String> mapParameters(String req, String encoding) {

        Map<String, String> map = new HashMap<String, String>();

        if (req == null) {
            return map;
        }

        if (_log.isDebugEnabled()) {
            _log.debug("paramter: {}", req);
        }

        // リクエストを&で分割
        String[] st = req.split("&");

        for (String s : st) {
            // リクエストを=で分割
            String[] sp = s.split("=");
            try {
                String key = null;
                String value = null;
                switch (sp.length) {
                case 1:
                    key = sp[0];
                    value = "";
                    break;
                case 2:
                    key = sp[0];
                    value = sp[1];
                    break;
                default:
                    throw new ApplicationException(
                            new IllegalArgumentException(
                                    "More than twice of \"=\" are found in paramter set"));
                }
                map.put(key, URLDecoder.decode(value, encoding));
            } catch (UnsupportedEncodingException e) {
                throw new ApplicationException(e);
            }
        }

        return map;

    }
}
