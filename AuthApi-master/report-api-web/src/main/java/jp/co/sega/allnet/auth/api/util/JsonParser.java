/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.sega.allnet.auth.log.ApiLogUtils;

/**
 * @author NakanoY
 * 
 */
public class JsonParser {

    private static final Logger _log = LoggerFactory
            .getLogger(JsonParser.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * オブジェクトからJSON文字列に変換する
     * 
     * @param o
     * @return
     */
    public static String writeToJson(Map<String, Object> o) {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;

        try {
            jsonString = objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    /**
     * JSON文字列をパースしてオブジェクトに変換する。
     * 
     * @param is
     * @param klass
     * @return
     * @throws IOException
     */
    public static <T> T readJsonStr(ApiLogUtils logUtils, InputStream is,
            Class<T> klass) throws IOException {
        StringBuilder sb = null;

        try {
            if (_log.isDebugEnabled()) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));
                sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }
                    sb.append(line);
                }
                return MAPPER.readValue(sb.toString(), klass);
            } else {
                return MAPPER.readValue(is, klass);
            }
        } finally {
            if (_log.isDebugEnabled()) {
                _log.debug(logUtils.format("Request json : %s", sb.toString()));
            }
        }
    }

}
