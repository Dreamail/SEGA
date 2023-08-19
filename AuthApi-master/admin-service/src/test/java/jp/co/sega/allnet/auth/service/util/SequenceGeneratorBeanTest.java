/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.annotation.Resource;
import javax.sql.DataSource;

import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author TsuboiY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SequenceGeneratorBeanTest {

    @Resource(name = "dataSource")
    private DataSource _ds;

    @Resource(name = "sequenceGenerator")
    private SequenceGenerator _sg;

    @Resource(name = "txTemplateForTest")
    private TransactionTemplate _txTemplate;

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.util.SequenceGeneratorBean#generatePlaceId()}
     * .
     */
    @Ignore
    @Test
    public final void testGeneratePlaceId() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.util.SequenceGeneratorBean#generateRegionId(java.lang.String, int)}
     * .
     */
    @Ignore
    @Test
    public final void testGenerateRegionId() {
        String countryCode = "JPN";
        int level = 1;

        final String key = String.format("REGION%s_%s_SEQ", countryCode, level);

        final NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);

        _txTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                jdbc.update("MERGE INTO sequences s USING DUAL "
                        + "ON ( s.seq_name = :key ) WHEN MATCHED THEN "
                        + "UPDATE SET value = 0 WHEN NOT MATCHED THEN "
                        + "INSERT ( seq_name ) VALUES ( :key )",
                        new MapSqlParameterSource("key", key));
            }
        });

        assertEquals(1, _sg.generateRegionId(countryCode, level));

        assertEquals(1, jdbc.queryForObject(
                "SELECT value FROM sequences WHERE seq_name = :seq_name",
                new MapSqlParameterSource("seq_name", key), Integer.class).intValue());

        assertEquals(2, _sg.generateRegionId(countryCode, level));

        assertEquals(2, jdbc.queryForObject(
                "SELECT value FROM sequences WHERE seq_name = :seq_name",
                new MapSqlParameterSource("seq_name", key), Integer.class).intValue());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.util.SequenceGeneratorBean#generateRegionId(java.lang.String, int)}
     * .
     */
    @Ignore
    @Test
    public final void testGenerateRegionIdNoSeq() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.util.SequenceGeneratorBean#generateRegionId(java.lang.String, int)}
     * .
     */
    @Ignore
    @Test
    public final void testGenerateRegionIdArgCountryCodeIsNull() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.util.SequenceGeneratorBean#generateRegionId(java.lang.String, int)}
     * .
     */
    @Ignore
    @Test
    public final void testGenerateRegionIdAnotherTransactionFail() {
        fail("Not yet implemented"); // TODO
    }

}
