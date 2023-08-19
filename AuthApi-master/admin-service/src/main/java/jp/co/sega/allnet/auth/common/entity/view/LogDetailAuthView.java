/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.view;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import jp.co.sega.allnet.auth.common.entity.util.CsvWritable;
import jp.co.sega.allnet.auth.common.entity.util.XmlWritable;
import jp.co.sega.allnet.auth.csv.CsvUtils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author TsuboiY
 * 
 */
public class LogDetailAuthView implements Serializable, CsvWritable,
        XmlWritable {

    private static final long serialVersionUID = 1L;

    private static final String PROBABILITY_FORMAT = "0.00";

    private String val;

    private int count;

    private BigDecimal probability;

    @Override
    public Writer writeCsvLine(Writer writer) throws IOException {
        if (!StringUtils.isEmpty(val)) {
            writer.write(CsvUtils.escapeForCsv(val));
        }
        writer.write(",");
        writer.write(String.valueOf(count));
        writer.write(",");
        writer.write(new DecimalFormat(PROBABILITY_FORMAT).format(probability));
        writer.write("\n");

        return writer;
    }

    @Override
    public Writer writeXmlLine(Writer writer) throws IOException {
        writer.write(XML_LINE_HEADER);
        writer.write("\n");
        if (!StringUtils.isEmpty(val)) {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", val));
        } else {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", ""));
        }
        writer.write("\n");
        writer.write(String.format(XML_COLUMN_TEMPLATE, "Number", count));
        writer.write("\n");
        writer.write(String.format(XML_COLUMN_TEMPLATE, "Number",
                new DecimalFormat(PROBABILITY_FORMAT).format(probability)));
        writer.write("\n");
        writer.write(XML_LINE_FOTTER);
        writer.write("\n");

        return writer;
    }

    /**
     * @return the val
     */
    public String getVal() {
        return val;
    }

    /**
     * @param val
     *            the val to set
     */
    public void setVal(String val) {
        this.val = val;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count
     *            the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the probability
     */
    public BigDecimal getProbability() {
        return probability;
    }

    /**
     * @param probability
     *            the probability to set
     */
    public void setProbability(BigDecimal probability) {
        this.probability = probability;
    }

}
