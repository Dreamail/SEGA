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
public interface CsvWritable {

    /**
     * 1行分のダウンロード用CSV文字列を{@link Writer}に書き込む。
     * 
     * @param writer
     * @return
     * @throws IOException
     */
    Writer writeCsvLine(Writer writer) throws IOException;

}
