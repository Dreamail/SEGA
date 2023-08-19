/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.util;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.sql.DataSource;

import jp.co.sega.allnet.auth.exception.ApplicationException;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author NakanoY
 * 
 */
@Component("sequenceGenerator")
@Scope("singleton")
public class SequenceGeneratorBean implements SequenceGenerator {

    @Resource(name = "transactionTemplate")
    private TransactionTemplate _txTemplate;

    @Resource(name = "dataSource")
    private DataSource _dataSource;

    @Override
    public String generatePlaceId() {
        final String key = "PLACE_SEQ";

        int seq = _txTemplate.execute(new TransactionCallback<Integer>() {

            @Override
            public Integer doInTransaction(TransactionStatus status) {
                NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);
                BigDecimal seq;

                try {
                    seq = jdbc
                            .queryForObject(
                                    "SELECT value FROM sequences WHERE seq_name = :key ",
                                    new MapSqlParameterSource("key", key),
                                    BigDecimal.class);
                } catch (EmptyResultDataAccessException e) {
                    throw new ApplicationException("シーケンスが見つかりません ： " + key);
                }

                int intValue = seq.intValue();
                intValue++;

                jdbc.update(
                        "UPDATE sequences SET value = :value, update_date = CURRENT_TIMESTAMP WHERE seq_name = :key",
                        new MapSqlParameterSource("value", intValue).addValue(
                                "key", key));

                return intValue;
            }
        });

        return String.format("%04X", seq);
    }

    @Override
    public int generateRegionId(String countryCode, int level) {

        final String key = "REGION" + level + "_" + countryCode + "_SEQ";

        return _txTemplate.execute(new TransactionCallback<Integer>() {

            @Override
            public Integer doInTransaction(TransactionStatus status) {
            	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

                BigDecimal seq;
                try {
                    seq = jdbc
                            .queryForObject(
                                    "SELECT value FROM sequences WHERE seq_name = :key ",
                                    new MapSqlParameterSource("key", key),
                                    BigDecimal.class);

                } catch (EmptyResultDataAccessException e) {
                    throw new ApplicationException("シーケンスが見つかりません ： " + key);
                }

                int intValue = seq.intValue();
                intValue++;

                jdbc.update(
                        "UPDATE sequences SET value = :value, update_date = CURRENT_TIMESTAMP WHERE seq_name = :key",
                        new MapSqlParameterSource("value", intValue).addValue(
                                "key", key));

                return intValue;
            }
        });
    }
}
