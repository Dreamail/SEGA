/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.util;

import java.io.IOException;
import java.io.Writer;

/**
 * @author TsuboiY
 * 
 */
public interface XmlWritable {

    String DATE_FORMAT_YEAR_TO_MSEC = "yyyy-MM-dd HH:mm:ss.SSS";

    String DATE_FORMAT_YEAR_TO_MSEC1 = "yyyy-MM-dd HH:mm:ss.S";

    String XML_LINE_HEADER = "<Row>";

    String XML_COLUMN_TEMPLATE = "<Cell><Data ss:Type=\"%s\">%s</Data></Cell>";

    String XML_LINE_FOTTER = "</Row>";

    /**
     * 1行分のダウンロード用XML文字列を{@link Writer}に書き込む。
     * 
     * @param writer
     * @return
     * @throws IOException
     */
    Writer writeXmlLine(Writer writer) throws IOException;

}
