/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import jp.co.sega.allnet.auth.codec.CodecUtils;

/**
 * @author NakanoY
 * 
 */
public class ResponseUtils {

    private static final String STR_ENCODED = "DFI";

    private static final int DEFLATER_BUFFER_SIZE = 2 * 1024 * 1024;

    private static final String RESPONSE_ENCODING = "EUC-JP";

    private ResponseUtils() {
        // do nothing
    }

    /**
     * レスポンスを書き出す。
     * 
     * @param ret
     * @param res
     * @param compress
     * @throws IOException
     */
    public static void writeResponse(String ret, HttpServletResponse res,
            boolean compress) throws IOException {

        res.setContentType(String.format("%s; charset=%s", "text/plain",
                RESPONSE_ENCODING));

        if (compress) {
            res.setHeader("Pragma", STR_ENCODED);
            PrintWriter pw = res.getWriter();
            pw.write(CodecUtils.encodeString(ret, RESPONSE_ENCODING,
                    DEFLATER_BUFFER_SIZE));
        } else {
            Writer writer = res.getWriter();
            writer.write(ret);
        }
    }

    /**
     * 旧API用のレスポンスを書き出す。
     * 
     * @param ret
     * @param res
     * @throws IOException
     */
    public static void writeResponseForLegacyApi(String ret,
            HttpServletResponse res) throws IOException {
        res.setHeader("Pragma", STR_ENCODED);
        res.setContentType("text/plain; charset=" + RESPONSE_ENCODING);
        PrintWriter pw = res.getWriter();
        pw.write(CodecUtils.encodeString(ret, RESPONSE_ENCODING,
                DEFLATER_BUFFER_SIZE));
    }
}
