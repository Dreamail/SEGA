/*
 * $Id: CryptEx.java 1139 2006-10-05 09:47:10Z nakayamas $
 * 
 * Copyright (C) 2004 SEGA Corporation. All rights reserved.
 */

package jp.co.sega_am2.crypt;

import java.util.Random;

import jp.co.sega.allnet.auth.am2lib.util.Log;

/**
 * 拡張暗号(Crypt.java の暗号にさらに別暗号をかける） 目的は、PC無料版提供用に暗号方式を変える
 */
@SuppressWarnings("serial")
public class CryptEx extends Crypt {

    /* 変換テーブル数 */
    static private final int CONV_TBL_NUM = 4;

    static private final char DEF_KEY = 'A';

    static private final int CHECK_SUM_LEN = 4;

    // value 変換用
    /* 元テーブル */
    static char lw_src_tbl[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', '_', '.', '@' };

    /* 変換テーブル */
    static char lw_conv_tbl[][] = {
            { 'A', 'Z', 'm', '@', 'z', 'F', 'P', 'H', 'I', 'J', '.', 'L', 'M',
                    'v', '5', '0', 'Q', 'R', 'r', 'T', 'U', 'V', 'W', 'X', 'Y',
                    'B', 'K', 'b', 'c', 'o', 'e', 'd', 'g', 'h', 'i', 'j', 'k',
                    '8', 'C', 'n', 'f', 'p', 'q', 'S', 's', 't', 'u', 'y', '2',
                    'x', 'N', 'O', 'G', '1', 'w', '3', '4', '6', '7', 'l', '9',
                    'E', '_', 'a', 'D', },
            { '3', 'Z', 'm', 'e', 'z', 'I', 'J', 'Q', 'Y', 'B', 'G', '1', '2',
                    'A', '4', 'E', '6', 't', 'l', '9', 'U', 'V', '_', 'X', 'F',
                    'P', 'k', '8', 'C', 'n', 'f', 'p', 'q', 'S', 's', 'i', 'K',
                    'b', 'c', 'o', '@', 'd', 'g', 'h', '7', 'j', 'u', 'v', 'O',
                    'x', 'y', '5', '.', 'W', 'M', 'a', 'w', '0', 'H', 'R', 'r',
                    'T', 'L', 'N', 'D', },
            { 'p', 'q', 'S', 's', '7', 'k', '8', 'C', 'n', 'f', 'G', '3', '4',
                    '.', 'L', 'j', '6', 't', 'l', '9', 'u', 'v', 'O', 'x', 'g',
                    '5', '1', 'Z', 'm', 'e', 'z', 'F', 'P', 'Q', 'Y', 'B', 'K',
                    'b', 'c', 'o', '@', 'd', 'i', 'h', 'D', 'y', 'U', 'V', '_',
                    'N', 'I', 'J', '2', 'E', 'M', 'X', 'w', '0', 'H', 'R', 'r',
                    'T', 'W', 'a', 'A', },
            { 'Q', 'q', 'S', 's', '7', 'l', '@', 'B', '2', 'E', 'p', 'Y', 'M',
                    'I', 'w', '0', 'H', 'R', 'r', 'T', 'u', 'x', '.', 'L', 'v',
                    '3', '1', 'O', 'm', 'e', 'z', 'F', 'P', 'k', '8', 'C', 'K',
                    'A', 'c', 'o', 'X', 'g', 'h', 'i', 'j', 'D', 'G', 'Z', 'a',
                    'd', 'n', 'f', 'U', 'W', '_', 'J', 'y', '5', '4', '9', 'N',
                    't', 'V', '6', 'b', }, };

    // value 変換用
    /* 元テーブル */
    static char lw_name_src_tbl[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', };

    /* 変換テーブル */
    static char lw_name_conv_tbl[][] = {
            { 'S', 'W', 'R', 'E', 'F', 'O', '3', 'L', '0', 'Z', '5', 'C', 'Q',
                    'X', 'P', 'N', '2', 'M', 'Y', 'K', 'T', 'B', 'D', '6', 'G',
                    '4', 'A', 'U', 'V', '7', 'H', '8', '9', '1', 'J', 'I', },
            { '3', 'A', 'V', 'P', 'G', 'O', 'N', '8', '2', '1', 'T', '4', 'B',
                    'S', 'H', '5', 'M', '7', 'Y', '0', 'I', 'U', '9', 'R', 'Q',
                    '6', 'J', 'K', 'C', 'W', 'D', 'L', 'E', 'X', 'Z', 'F', },
            { 'G', '9', '4', '6', 'W', '5', 'Q', '3', '2', '1', '7', 'F', 'E',
                    'V', 'X', 'R', 'P', 'Y', 'Z', '0', '8', 'J', 'I', 'D', 'S',
                    'O', 'H', 'K', 'U', 'C', 'T', 'N', 'B', 'M', 'L', 'A', },
            { '8', 'K', 'Z', '5', '6', 'E', 'W', '2', 'T', 'U', 'I', '7', 'L',
                    'A', '4', 'D', '3', '1', 'F', 'V', '9', 'J', 'M', 'O', 'P',
                    'X', 'H', 'Y', 'N', 'B', 'C', 'Q', '0', 'S', 'G', 'R', }, };

    private char _key;
    private int _chkSumBaseVal;

    /**
     * コンストラクタ
     */
    public CryptEx() {
        this(0);
    }

    public CryptEx(int key) {
        super(key);

        Random rand = new Random();

        _key = (char) (DEF_KEY + (rand.nextInt() % 4));
        _chkSumBaseVal = rand.nextInt();

        Log.write("_key=" + _key + ",chkSumBaseVal=" + _chkSumBaseVal);
    }

    /**
     * 暗号化のキーを設定する
     * 
     * @param key
     *            : キー(0だと乱数になる)
     * @param keyEx
     *            : 拡張用キー(0-3)
     * @param chkSumBaseVal
     *            : チェックサムにプラスする値
     */
    public void setCryptKey(int key, int keyEx, int chkSumBaseVal) {
        _key = (char) (DEF_KEY + keyEx);
        _chkSumBaseVal = chkSumBaseVal;
        setKey(key);
    }

    /**
     * 入力された文字が元テーブルの何番目か取得する
     * 
     * @param ch
     *            : 入力文字
     * @return 番号（見つからない場合は-1）
     */
    static private int getSrcTblNo(char ch, char[] src_tbl) {
        int i;

        for (i = 0; i < src_tbl.length; i++) {
            if (src_tbl[i] == ch) {
                /* テーブルの文字と一致した場合 */
                return (i);
            }
        }

        return (-1);
    }

    /**
     * 入力された文字が変換テーブルの何番目か取得する
     * 
     * @param ch
     *            : 入力文字
     * @param tbl_no
     *            : テーブル番号
     * @return 番号（見つからない場合は-1）
     */
    static private int getConvTblNo(char ch, int tbl_no, char[][] conv_tbl,
            int tblSize) {
        int i;

        for (i = 0; i < tblSize; i++) {
            if (conv_tbl[tbl_no][i] == ch) {
                /* テーブルの文字と一致した場合 */
                return (i);
            }
        }

        return (-1);
    }

    private String cnvChkSumStr(char key, int val, char[] src_tbl,
            char[][] conv_tbl) {
        char[] charSum = new char[CHECK_SUM_LEN];

        Log.write("val=" + Integer.toHexString(val));
        for (int i = 0; i < CHECK_SUM_LEN; i++) {
            int val2 = (val & 0xff);
            Log.write("val2=" + Integer.toHexString(val2));
            val >>= 8;
            charSum[i] = (char) ('A' + (val2 % 26));
        }
        String strSum = new String(charSum);

        String angoStr = ciphMakeCipherSub(key, strSum, src_tbl, conv_tbl);

        return angoStr;
    }

    /**
     * 入力された文字列を暗号化する
     * 
     * @param key
     *            : キー
     * @param in_str
     *            : 入力文字列
     * @return 出力文字列
     */
    private String ciphMakeCipherSub(char key, String in_str, char[] src_tbl,
            char[][] conv_tbl) {
        int tbl_no, no;
        char ch, nch;

        char[] inCh, outCh;
        inCh = in_str.toCharArray();
        outCh = new char[inCh.length];

        for (int i = 0; i < inCh.length; i++) {
            ch = inCh[i];
            /* 元テーブルから番号を取得 */
            no = getSrcTblNo(ch, src_tbl);
            if (no == -1) {
                /* 元テーブルにない文字の場合 */
                nch = ch;
            } else {
                /* 元テーブルにあった場合 */
                tbl_no = (i + key) % CONV_TBL_NUM;
                nch = conv_tbl[tbl_no][no];
            }
            outCh[i] = nch;
        }
        String outStr = new String(outCh);
        return outStr;
    }

    /**
     * 入力された文字列からチェックサム値を求める
     * 
     * @param in_str
     *            : 入力文字列
     * @return チェックサム
     */
    private int calcChkSum(String in_str) {
        char[] inCh = in_str.toCharArray();

        int chkSum = _chkSumBaseVal;
        for (int i = 0; i < inCh.length; i++) {
            int val = (int) inCh[i];
            int val2 = val << 24;
            chkSum += (val + val2);
            Log.write("chkSum=" + Integer.toHexString(chkSum) + ",val=" + val
                    + ",val2=" + val2);
        }
        return chkSum;
    }

    /**
     * 入力された文字列を暗号化する
     * 
     * @param key
     *            : キー
     * @param in_str
     *            : 入力文字列
     * @return 出力文字列
     */
    private String ciphMakeCipher(char key, String in_str, char[] src_tbl,
            char[][] conv_tbl) {
        if (in_str == null) {
            return null;
        }
        String angoStr = ciphMakeCipherSub(key, in_str, src_tbl, conv_tbl);
        int chkSum = calcChkSum(in_str);
        String chkSumStr = cnvChkSumStr(key, chkSum, src_tbl, conv_tbl);
        Log.write("chkSumStr:" + chkSumStr + "," + chkSum);
        return angoStr + chkSumStr;
    }

    private String ciphMakeCipherValue(char key, String in_str) {
        return ciphMakeCipher(key, in_str, lw_src_tbl, lw_conv_tbl);
    }

    private String divCryptStr(String in_str) {
        int len = in_str.length();
        if (len < CHECK_SUM_LEN) {
            return null;
        }
        return in_str.substring(0, len - CHECK_SUM_LEN);
    }

    private String divChkSumStr(String in_str) {
        int len = in_str.length();
        return in_str.substring(len - CHECK_SUM_LEN);
    }

    /**
     * 入力された暗号化された文字列を元に戻す
     * 
     * @param key
     *            : キー
     * @param in_str
     *            : 入力文字列
     * @return 復元された文字列
     */
    private String ciphRestoreCipherSub(char key, String in_str,
            char[] src_tbl, char[][] conv_tbl) {
        int tbl_no, no;
        char ch, nch;
        char[] inCh, outCh;

        inCh = in_str.toCharArray();
        outCh = new char[inCh.length];

        for (int i = 0; i < inCh.length; i++) {
            ch = inCh[i];
            /* 変換テーブルから番号を取得 */
            tbl_no = (i + key) % CONV_TBL_NUM;
            no = getConvTblNo(ch, tbl_no, conv_tbl, src_tbl.length);
            if (no == -1) {
                /* 元テーブルにない文字の場合 */
                nch = ch;
            } else {
                /* 元テーブルにあった場合 */
                nch = src_tbl[no];
            }
            outCh[i] = nch;
        }

        return new String(outCh);
    }

    /**
     * 入力された暗号化された文字列を元に戻す
     * 
     * @param key
     *            : キー
     * @param in_str
     *            : 入力文字列
     * @return 復元された文字列
     */
    private String ciphRestoreCipher(char key, String in_str, char[] src_tbl,
            char[][] conv_tbl) throws CryptRuntimeException {
        if (in_str == null) {
            return null;
        }

        // なぜか落合さんだと後ろに改行がつくことがあったので、削除しています。
        in_str = ("a" + in_str).trim().substring(1);

        String angoStr = divCryptStr(in_str);
        if (angoStr == null) {
            throw new CryptRuntimeException();
        }
        String chkSumStr = divChkSumStr(in_str);

        String motoStr = ciphRestoreCipherSub(key, angoStr, src_tbl, conv_tbl);

        int chkSum = calcChkSum(motoStr);
        String chkSumStr2 = cnvChkSumStr(key, chkSum, src_tbl, conv_tbl);
        if (!chkSumStr.equals(chkSumStr2)) {
            // チェックサムが違う
            Log.write("crypt error : checkSumStr=" + chkSumStr + ",chkSumStr2="
                    + chkSumStr2 + ",chkSum=" + chkSum + ",motoStr=" + motoStr
                    + ",key=" + key + ",in_str=" + in_str, Log.Level.ERROR);
            throw new CryptRuntimeException();
        }
        return motoStr;
    }

    private String ciphRestoreCipherValue(char key, String in_str)
            throws CryptRuntimeException {
        return ciphRestoreCipher(key, in_str, lw_src_tbl, lw_conv_tbl);
    }

    /**
     * Requestパラメータvalueの暗号化文字列の取得
     */
    public String encodeValue(String input) {
        if (CryptSetting.getIgnoreParameterEncription()) {
            return input;
        }
        String input2 = super.encodeValue(input);
        String retStr = ciphMakeCipherValue(_key, input2);

        Log.write("encodeValue=" + input + "," + retStr);
        return retStr;
    }

    /**
     * Requestパラメータ暗号valueの復号化文字列の取得
     */
    public String decodeValue(String input) {
        if (CryptSetting.getIgnoreParameterEncription()) {
            return input;
        }
        Log.write("decodeValue input=" + input);
        String input2 = ciphRestoreCipherValue(_key, input);
        String retStr = super.decodeValue(input2);

        Log.write("decodeValue output=" + retStr);
        return retStr;
    }

}
