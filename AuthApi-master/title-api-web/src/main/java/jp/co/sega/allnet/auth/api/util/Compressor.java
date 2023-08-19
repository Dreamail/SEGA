/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * @author TsuboiY
 * 
 */
public interface Compressor {

    /**
     * 圧縮してレスポンスに書き出す処理を行う。
     * 
     * @param val
     * @param encoding
     * @param res
     */
    public void compress(String val, String encoding, HttpServletResponse res)
            throws IOException;

}
