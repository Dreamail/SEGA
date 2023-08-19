/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.codec;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

import jp.co.sega.allnet.auth.exception.ApplicationException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author NakanoY
 * 
 */
public class CodecUtils {

    private static final Logger _log = LoggerFactory
            .getLogger(CodecUtils.class);

    // デコードの際に使用する最大バッファサイズ
    private static final int UPLOAD_BUFFER_SIZE = 4096;

    // バッファオーバーフロー時のエラーメッセージ
    private static final String ERR_MESSAGE = "ERROR! Buffer Overflow. Please Set UPLOAD_BUFFER_SIZE.";

    // エンコードの際に使用する標準バッファサイズ
    private static final int DEFAULT_ENCODE_BUF = 1024;

    private CodecUtils() {
        // do nothing
    }

    /**
     * リクエストを復号化
     * 
     * @param is
     * @return
     */
    public static String decodeStream(InputStream is) {

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String s;
        StringBuilder sb = new StringBuilder();

        try {
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
        } catch (IOException e) {
            throw new ApplicationException(e);
        }

        Base64 base = new Base64();
        byte[] decoded = base.decode(sb.toString());

        Inflater in = new Inflater();
        in.setInput(decoded, 0, decoded.length);
        byte[] outbuff = new byte[UPLOAD_BUFFER_SIZE + 1];

        // オーバフローが起こったら例外とする
        try {
            int num = in.inflate(outbuff);
            if (num > UPLOAD_BUFFER_SIZE) {
                throw new ApplicationException(ERR_MESSAGE + " size:" + num);
            }
        } catch (DataFormatException e) {
            throw new ApplicationException(e);
        }

        String decodedStr = new String(outbuff).trim();

        if (_log.isDebugEnabled()) {
            _log.debug("Uncrypted string: " + decodedStr);
        }

        return decodedStr;

    }

    /**
     * レスポンスを暗号化
     * 
     * @param plain
     * @param encode
     * @return
     */
    public static String encodeString(String plain, String encode) {
        return encodeString(plain, encode, DEFAULT_ENCODE_BUF);
    }

    /**
     * レスポンスを暗号化
     * 
     * @param plain
     * @param encode
     * @param bufferSize
     * @return
     */
    public static String encodeString(String plain, String encode,
            int bufferSize) {

        if (_log.isDebugEnabled()) {
            _log.debug("暗号化前の文字列 : " + plain);
        }

        try {
            byte[] output = new byte[bufferSize];
            Deflater df = new Deflater();

            df.setInput(plain.getBytes(encode), 0,
                    plain.getBytes(encode).length);

            df.finish();
            int n = df.deflate(output, 0, bufferSize);

            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            OutputStream os = MimeUtility.encode(oStream, "base64");
            os.write(output, 0, n);
            os.flush();
            String s = oStream.toString();
            os.close();
            oStream.close();

            if (_log.isDebugEnabled()) {
                _log.debug("暗号化後の文字列 : " + s);
            }

            return s + "\n";

        } catch (UnsupportedEncodingException e) {
            throw new ApplicationException(e);
        } catch (IOException e) {
            throw new ApplicationException(e);
        } catch (MessagingException e) {
            throw new ApplicationException(e);
        }

    }
}
