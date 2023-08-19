/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.view;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import jp.co.sega.allnet.auth.common.entity.util.CsvWritable;
import jp.co.sega.allnet.auth.common.entity.util.XmlWritable;
import jp.co.sega.allnet.auth.csv.CsvUtils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author TsuboiY
 * 
 */
public class LogDetailOrderIpView implements Serializable, CsvWritable,
        XmlWritable {

    private static final long serialVersionUID = 1L;

    private String placeId;

    private String placeIp;

    private int count;

    @Override
    public Writer writeCsvLine(Writer writer) throws IOException {
        if (!StringUtils.isEmpty(placeId)) {
            writer.write(CsvUtils.escapeForCsv(placeId));
        }
        writer.write(",");
        if (!StringUtils.isEmpty(placeIp)) {
            writer.write(CsvUtils.escapeForCsv(placeIp));
        }
        writer.write(",");
        writer.write(String.valueOf(count));
        writer.write("\n");

        return writer;
    }

    @Override
    public Writer writeXmlLine(Writer writer) throws IOException {
        writer.write(XML_LINE_HEADER);
        writer.write("\n");
        if (!StringUtils.isEmpty(placeId)) {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", placeId));
        } else {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", ""));
        }
        writer.write("\n");
        if (!StringUtils.isEmpty(placeIp)) {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", placeIp));
        } else {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", ""));
        }
        writer.write("\n");
        writer.write(String.format(XML_COLUMN_TEMPLATE, "Number", count));
        writer.write("\n");
        writer.write(XML_LINE_FOTTER);
        writer.write("\n");

        return writer;
    }

    /**
     * @return the placeId
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * @param placeId
     *            the placeId to set
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * @return the placeIp
     */
    public String getPlaceIp() {
        return placeIp;
    }

    /**
     * @param placeIp
     *            the placeIp to set
     */
    public void setPlaceIp(String placeIp) {
        this.placeIp = placeIp;
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

}
