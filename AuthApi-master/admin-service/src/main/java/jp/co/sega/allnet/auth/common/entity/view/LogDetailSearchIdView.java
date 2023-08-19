/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.view;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import jp.co.sega.allnet.auth.common.entity.util.CsvWritable;
import jp.co.sega.allnet.auth.common.entity.util.XmlWritable;
import jp.co.sega.allnet.auth.csv.CsvUtils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author TsuboiY
 * 
 */
public class LogDetailSearchIdView implements Serializable, CsvWritable,
        XmlWritable {

    private static final long serialVersionUID = 1L;

    private String serial;

    private String placeId;

    private String placeIp;

    private String globalIp;

    private Timestamp accessDate;

    @Override
    public Writer writeCsvLine(Writer writer) throws IOException {
        if (!StringUtils.isEmpty(serial)) {
            writer.write(CsvUtils.escapeForCsv(serial));
        }
        writer.write(",");
        if (!StringUtils.isEmpty(placeId)) {
            writer.write(CsvUtils.escapeForCsv(placeId));
        }
        writer.write(",");
        if (!StringUtils.isEmpty(placeIp)) {
            writer.write(CsvUtils.escapeForCsv(placeIp));
        }
        writer.write(",");
        if (!StringUtils.isEmpty(globalIp)) {
            writer.write(CsvUtils.escapeForCsv(globalIp));
        }
        writer.write(",");

        if (accessDate != null) {
            writer.write(accessDate.toString());
        }
        writer.write("\n");

        return writer;
    }

    @Override
    public Writer writeXmlLine(Writer writer) throws IOException {
        writer.write(XML_LINE_HEADER);
        writer.write("\n");
        if (!StringUtils.isEmpty(serial)) {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", serial));
        } else {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", ""));
        }
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
        if (!StringUtils.isEmpty(globalIp)) {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", globalIp));
        } else {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", ""));
        }
        writer.write("\n");
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_YEAR_TO_MSEC);
        writer.write(String.format(XML_COLUMN_TEMPLATE, "String",
                df.format(accessDate)));
        writer.write("\n");
        writer.write(XML_LINE_FOTTER);
        writer.write("\n");

        return writer;
    }

    /**
     * @return the serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial
     *            the serial to set
     */
    public void setSerial(String serial) {
        this.serial = serial;
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
     * @return the globalIp
     */
    public String getGlobalIp() {
        return globalIp;
    }

    /**
     * @param globalIp
     *            the globalIp to set
     */
    public void setGlobalIp(String globalIp) {
        this.globalIp = globalIp;
    }

    /**
     * @return the accessDate
     */
    public Timestamp getAccessDate() {
        return accessDate;
    }

    /**
     * @param accessDate
     *            the accessDate to set
     */
    public void setAccessDate(Timestamp accessDate) {
        this.accessDate = accessDate;
    }

}
