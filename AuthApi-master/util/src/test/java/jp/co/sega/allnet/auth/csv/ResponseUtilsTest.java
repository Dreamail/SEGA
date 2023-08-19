/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.csv;

import static org.junit.Assert.assertEquals;
import jp.co.sega.allnet.auth.csv.CsvUtils;
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
     * {@link jp.co.sega.allnet.auth.api.util.ResponseUtils#escapeForCsv(java.lang.String)}
     * .
     */
    @Test
    public final void testEscapeForCsv() {
        String test = "\"a,あ\nおs\rd";
        String expected = "\"\"\"a,あおsd\"";

        assertEquals(expected, CsvUtils.escapeForCsv(test));

    }
}
