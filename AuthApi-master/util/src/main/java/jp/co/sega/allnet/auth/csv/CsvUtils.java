/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.csv;

/**
 * @author TsuboiY
 * 
 */
public class CsvUtils {

    private CsvUtils() {
        // do nothing
    }

    /**
     * CSV文字列に対してエスケープ処理を行う。
     * 
     * @param s
     * @return
     */
    public static String escapeForCsv(String s) {
        return escapeForCsv(s, true);
    }

    /**
     * CSV文字列に対してエスケープ処理を行う。
     * 
     * @param s
     * @oaran removeCrLf
     * @return
     */
    public static String escapeForCsv(String s, boolean removeCrLf) {
        // ダブルクォート(") があれば二重にする
        s = s.replaceAll("\"", "\"\"");
        // sにカンマ (,) 、ダブルクオート (") があれば前後をダブルクオート (") で囲む
        if (s.indexOf(',') >= 0 || s.indexOf('\"') >= 0) {
            s = "\"" + s + "\"";
        }
        if (removeCrLf) {
            // sに、改行(\r\n)があれば削除する
            s = s.replaceAll("\r|\n", "");
        }
        return s;
    }

}
