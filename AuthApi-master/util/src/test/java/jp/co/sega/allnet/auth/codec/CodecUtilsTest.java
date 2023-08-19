/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.codec;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import jp.co.sega.allnet.auth.codec.CodecUtils;
import jp.co.sega.allnet.auth.exception.ApplicationException;
import jp.co.sega.allnet.auth.test.ExtJUnit4ClassRunner;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author NakanoY
 * 
 */
@RunWith(ExtJUnit4ClassRunner.class)
public class CodecUtilsTest {

    /**
     * {@link jp.co.sega.allnet.auth.codec.CodecUtils#decodeStream(java.io.InputStream)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testDecodeStreamSuccess() {

        StringBuilder sb = new StringBuilder();

        int j = 0;
        for (int i = 0; i < 4096; i++) {
            if (j > 9) {
                j = 0;
            }
            sb.append(j);
            j++;
        }

        byte[] output = new byte[1024];
        Deflater df = new Deflater();
        df.setInput(sb.toString().getBytes());
        df.finish();
        df.deflate(output);

        Base64 base = new Base64();
        byte[] encoded = base.encode(output);

        ByteArrayInputStream bais = new ByteArrayInputStream(encoded);

        String res = CodecUtils.decodeStream(bais);

        assertEquals(sb.toString(), res);
    }

    /**
     * {@link jp.co.sega.allnet.auth.codec.CodecUtils#decodeStream(java.io.InputStream)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testDecodeStreamBlank() {
        StringBuilder sb = new StringBuilder();

        byte[] output = new byte[1024];
        Deflater df = new Deflater();
        df.setInput(sb.toString().getBytes());
        df.finish();
        df.deflate(output);

        Base64 base = new Base64();
        byte[] encoded = base.encode(output);

        ByteArrayInputStream bais = new ByteArrayInputStream(encoded);

        String res = CodecUtils.decodeStream(bais);

        assertEquals(sb.toString(), res);
    }

    /**
     * {@link jp.co.sega.allnet.auth.codec.CodecUtils#decodeStream(java.io.InputStream)}
     * のためのテスト・メソッド。
     */
    @Test(expected = ApplicationException.class)
    public final void testDecodeStreamIsNull() {
        InputStream is = new InputStream() {

            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };

        CodecUtils.decodeStream(is);

    }

    /**
     * {@link jp.co.sega.allnet.auth.codec.CodecUtils#decodeStream(java.io.InputStream)}
     * のためのテスト・メソッド。
     */

    @Test(expected = ApplicationException.class)
    public final void testDecodeStreamOverFlow() {

        StringBuilder sb = new StringBuilder();

        int j = 0;
        for (int i = 0; i < 4097; i++) {
            if (j > 9) {
                j = 0;
            }
            sb.append(j);
            j++;
        }

        byte[] output = new byte[1024];
        Deflater df = new Deflater();
        df.setInput(sb.toString().getBytes());
        df.finish();
        df.deflate(output);

        Base64 base = new Base64();
        byte[] encoded = base.encode(output);

        ByteArrayInputStream bais = new ByteArrayInputStream(encoded);

        CodecUtils.decodeStream(bais);

    }

    /**
     * {@link jp.co.sega.allnet.auth.codec.CodecUtils#decodeStream(java.io.InputStream)}
     * のためのテスト・メソッド。
     */

    @Test(expected = ApplicationException.class)
    public final void testDecodeStreamFormatError() {

        String s = "test";

        Base64 base = new Base64();
        byte[] encoded = base.encode(s.getBytes());

        ByteArrayInputStream bais = new ByteArrayInputStream(encoded);

        CodecUtils.decodeStream(bais);

    }

    /**
     * {@link jp.co.sega.allnet.auth.codec.CodecUtils#encodeString(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public final void testEncodeStringSJIS() throws DataFormatException,
            UnsupportedEncodingException {

        String plain = "エンコードのテスト(s-jis)";
        String encode = "Shift_JIS";

        String res = CodecUtils.encodeString(plain, encode);

        assertEquals(new String(plain.getBytes("Shift_JIS"), encode),
                decode(res, encode));

    }

    /**
     * {@link jp.co.sega.allnet.auth.codec.CodecUtils#encodeString(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws DataFormatException
     * @throws UnsupportedEncodingException
     */
    @Test
    public final void testEncodeStringUTF8() throws DataFormatException,
            UnsupportedEncodingException {
        String plain = "エンコードのテスト(utf-8)";
        String encode = "UTF-8";

        String res = CodecUtils.encodeString(plain, encode);

        assertEquals(plain, decode(res, encode));

    }

    /**
     * {@link jp.co.sega.allnet.auth.codec.CodecUtils#encodeString(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws DataFormatException
     * @throws UnsupportedEncodingException
     */
    @Test
    public final void testEncodeStringBlank() throws DataFormatException,
            UnsupportedEncodingException {
        String plain = "";
        String encode = "UTF-8";

        String res = CodecUtils.encodeString(plain, encode);

        assertEquals(plain, decode(res, encode));
    }

    /**
     * {@link jp.co.sega.allnet.auth.codec.CodecUtils#encodeString(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test(expected = ApplicationException.class)
    public final void testEncodeStringInvalidEncode() {
        String plain = "エンコードのテスト(test-encode)";
        String encode = "TEST-ENCODE";

        CodecUtils.encodeString(plain, encode);
    }

    /**
     * 復号化用メソッド
     * 
     * @param str
     * @return
     * @throws DataFormatException
     * @throws UnsupportedEncodingException
     */
    private String decode(String str, String encoding)
            throws DataFormatException, UnsupportedEncodingException {
        Base64 base = new Base64();
        byte[] decoded = base.decode(str);

        Inflater in = new Inflater();
        in.setInput(decoded, 0, decoded.length);
        byte[] outbuff = new byte[4096];

        in.inflate(outbuff);

        return new String(outbuff, encoding).trim();
    }

}
