/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import jp.co.sega.allnet.auth.test.ExtJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author TsuboiY
 * 
 */
@RunWith(ExtJUnit4ClassRunner.class)
public class ResponseUtilsTest {

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.util.ResponseUtils#createResponceString(java.util.List)}
     * .
     */
    @Test
    public final void testCreateResponceString() {
        List<ResponseData> list = new ArrayList<ResponseData>();
        list.add(new ResponseData("serial", "AZZZZZZZZZZ"));
        list.add(new ResponseData("game_id", "SBXX"));
        list.add(new ResponseData("game_ver", ""));
        list.add(new ResponseData("country_code", null));
        list.add(new ResponseData("allnet_id", 1));

        String s = ResponseUtils.createResponceString(list);

        String[] ampSep = s.split("&");

        assertEquals(2, ampSep[0].split("=").length);
        assertEquals(list.get(0).getKey(), ampSep[0].split("=")[0]);
        assertEquals(list.get(0).getValue().toString(), ampSep[0].split("=")[1]);

        assertEquals(2, ampSep[1].split("=").length);
        assertEquals(list.get(1).getKey(), ampSep[1].split("=")[0]);
        assertEquals(list.get(1).getValue().toString(), ampSep[1].split("=")[1]);

        assertEquals(1, ampSep[2].split("=").length);
        assertEquals(list.get(2).getKey(), ampSep[2].split("=")[0]);

        assertEquals(2, ampSep[3].split("=").length);
        assertEquals(list.get(3).getKey(), ampSep[3].split("=")[0]);
        assertEquals("null", ampSep[3].split("=")[1]);

        assertEquals(2, ampSep[4].split("=").length);
        assertEquals(list.get(4).getKey(), ampSep[4].split("=")[0]);
        assertEquals(list.get(4).getValue().toString() + "\n",
                ampSep[4].split("=")[1]);

    }
}
