/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * @author TsuboiY
 * 
 */
public class GzipCompressor implements Compressor {

    private static final int BUFFER_SIZE = 1024;

    @Override
    public void compress(String val, String encoding, HttpServletResponse res)
            throws IOException {
        res.setHeader("Content-Encoding", "gzip");
        ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
        GZIPOutputStream gzos = new GZIPOutputStream(baos);
        Writer writer = new BufferedWriter(new OutputStreamWriter(gzos,
                encoding));
        writer.write(val);
        writer.flush();
        gzos.finish();
        gzos.close();
        baos.writeTo(res.getOutputStream());
    }

}
