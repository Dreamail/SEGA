/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authtype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;

import com.opencsv.CSVReader;

/**
 * @author TsuboiY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AuthTypeRegisterServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(AuthTypeRegisterServiceBeanTest.class);

    @Resource(name = "authTypeRegisterService")
    private AuthTypeRegisterService _service;

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.authtype.AuthTypeRegisterServiceBean#registerAuthType(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterAuthType() throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("authType", "1");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBYY");
        map2.put("authType", "2");
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("gameId", "");
        map3.put("authType", "3");
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("gameId", "SBZZ");
        map4.put("authType", "");
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("gameId", "");
        map5.put("authType", "");
        Map<String, String> map6 = new HashMap<String, String>();
        map6.put("gameId", "SBAA");

        String val = String.format("%s,%s", map1.get("gameId"),
                map1.get("authType"));
        val += "\n"
                + String.format("%s,%s", map2.get("gameId"),
                        map2.get("authType"));
        val += "\n"
                + String.format("%s,%s", map3.get("gameId"),
                        map3.get("authType"));
        val += "\n"
                + String.format("%s,%s", map4.get("gameId"),
                        map4.get("authType"));
        val += "\n"
                + String.format("%s,%s", map5.get("gameId"),
                        map5.get("authType"));
        val += "\n" + String.format("%s", map6.get("gameId"));

        String ret = _service.registerAuthType(val);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			String[] line = reader.readNext();
			// 1行目
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals("1", line[0]);
			assertEquals(map1.get("gameId"), line[1]);
			assertEquals(map1.get("authType"), line[2]);
			assertEquals("この機能は実装されていません", line[3]);
			assertEquals("0", line[4]);
			// 3行目
			line = reader.readNext();
			assertEquals("2", line[0]);
			assertEquals(map2.get("gameId"), line[1]);
			assertEquals(map2.get("authType"), line[2]);
			assertEquals("この機能は実装されていません", line[3]);
			assertEquals("0", line[4]);
			// 4行目
			line = reader.readNext();
			assertEquals("3", line[0]);
			assertEquals(map3.get("gameId"), line[1]);
			assertEquals(map3.get("authType"), line[2]);
			assertEquals("この機能は実装されていません", line[3]);
			assertEquals("0", line[4]);
			// 5行目
			line = reader.readNext();
			assertEquals("4", line[0]);
			assertEquals(map4.get("gameId"), line[1]);
			assertEquals(map4.get("authType"), line[2]);
			assertEquals("この機能は実装されていません", line[3]);
			assertEquals("0", line[4]);
			// 6行目
			line = reader.readNext();
			assertEquals("5", line[0]);
			assertEquals(map5.get("gameId"), line[1]);
			assertEquals(map5.get("authType"), line[2]);
			assertEquals("この機能は実装されていません", line[3]);
			assertEquals("0", line[4]);
			// 7行目
			line = reader.readNext();
			assertEquals("6", line[0]);
			assertEquals(map6.get("gameId"), line[1]);
			assertEquals("", line[2]);
			assertEquals("この機能は実装されていません", line[3]);
			assertEquals("0", line[4]);
			// 8行目
			line = reader.readNext();
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }
}
