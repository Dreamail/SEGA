/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.util;

/**
 * @author NakanoY
 * 
 */
public interface CsvXmlExportable {

    String DATE_FORMAT_YEAR_TO_MSEC = "yyyy-MM-dd HH:mm:ss.SSSSSS";

    String DATE_FORMAT_YEAR_TO_MSEC1 = "yyyy-MM-dd HH:mm:ss.S";

    String XML_LINE_HEADER = "<Row>";

    String XML_COLUMN_TEMPLATE = "<Cell><Data ss:Type=\"%s\">%s</Data></Cell>";

    String XML_LINE_FOTTER = "</Row>";

    /**
     * 1行分のCSVダウンロード文字列を作成する。
     * 
     * @return
     */
    String createCsvLine();

    /**
     * 1行分のXMLダウンロード文字列を作成する。
     * 
     * @return
     */
    String createXmlLine();

}
