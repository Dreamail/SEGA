/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.util;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import jp.co.sega.allnet.auth.exception.ApplicationException;
import jp.co.sega.allnet.auth.test.ExtJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author TsuboiY
 * 
 */
@RunWith(ExtJUnit4ClassRunner.class)
public class RequestUtilsTest {

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.util.RequestUtils#mapParameters(java.lang.String)}
     * .
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public final void testMapParameters() throws UnsupportedEncodingException {
        String encoding = RequestUtils.DEFAULT_CHARACTER_ENCODING;
        String serial = "AZZZZZZZZZZ";
        String gameId = "SBXX";
        String gameVer = "";
        String text = "テキスト1＆＝";
        String s = String.format("serial=%s&game_id=%s&game_ver=%s&text=%s&&",
                URLEncoder.encode(serial, encoding),
                URLEncoder.encode(gameId, encoding),
                URLEncoder.encode(gameVer, encoding),
                URLEncoder.encode(text, encoding));

        Map<String, String> map = RequestUtils.mapParameters(s,
                RequestUtils.DEFAULT_CHARACTER_ENCODING);
        assertEquals(4, map.size());
        assertEquals(serial, map.get("serial"));
        assertEquals(gameId, map.get("game_id"));
        assertEquals(gameVer, map.get("game_ver"));
        assertEquals(text, map.get("text"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.util.RequestUtils#mapParameters(java.lang.String)}
     * .
     * 
     * @throws UnsupportedEncodingException
     */
    @Test(expected = ApplicationException.class)
    public final void testMapParametersInvalidFormat()
            throws UnsupportedEncodingException {
        String encoding = "Shift_JIS";
        String serial = "AZZZZZZZZZZ";
        String gameId = "SBXX";
        String gameVer = "";
        String text = "テキスト1＆＝";
        String s = String.format(
                "serial=%s&game_id=%s&game_ver=%s&text=%s=1&&",
                URLEncoder.encode(serial, encoding),
                URLEncoder.encode(gameId, encoding),
                URLEncoder.encode(gameVer, encoding),
                URLEncoder.encode(text, encoding));

        RequestUtils.mapParameters(s, RequestUtils.DEFAULT_CHARACTER_ENCODING);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.util.RequestUtils#mapParameters(java.lang.String)}
     * .
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public final void testMapParametersIsNull()
            throws UnsupportedEncodingException {
        Map<String, String> map = RequestUtils.mapParameters(null,
                RequestUtils.DEFAULT_CHARACTER_ENCODING);
        assertEquals(0, map.size());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.util.RequestUtils#mapParameters(java.lang.String)}
     * .
     * 
     * @throws UnsupportedEncodingException
     */
    @Test(expected = ApplicationException.class)
    public final void testMapParametersUnsupportedEncoding()
            throws UnsupportedEncodingException {
        String encoding = "Shift_JIS";
        String serial = "AZZZZZZZZZZ";
        String gameId = "SBXX";
        String gameVer = "";
        String text = "テキスト1＆＝";
        String s = String.format("serial=%s&game_id=%s&game_ver=%s&text=%s&&",
                URLEncoder.encode(serial, encoding),
                URLEncoder.encode(gameId, encoding),
                URLEncoder.encode(gameVer, encoding),
                URLEncoder.encode(text, encoding));

        RequestUtils.mapParameters(s, "TestEncoding");
    }
}
