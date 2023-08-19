/*
 * $Id: Crypt.java 1101 2006-09-01 10:32:18Z nakayamas $
 * 
 * Copyright (C) 2004 SEGA Corporation. All rights reserved.
 */

package jp.co.sega_am2.crypt;

import java.io.Serializable;
import java.util.Random;

/**
 * 暗号処理（HTMLフォーム等のパラメタ／値に使うことを推奨）
 */
@SuppressWarnings("serial")
public class Crypt implements Serializable {
    /**
     * 公開定数
     */

    /**
     * 汎用暗号化の動作モード指定定数（バイナリ暗号モード）
     */
    public static final int mode_binary = 0;
    /**
     * 汎用暗号化の動作モード指定定数（ユーザー定義暗号モード）
     */
    public static final int mode_userdefine = 1;
    /**
     * 汎用暗号化の動作モード指定定数（英数字暗号モード）
     */
    public static final int mode_alphanumeric = 2;
    /**
     * 汎用暗号化の動作モード指定定数（URLパラメタ名暗号モード）
     */
    public static final int mode_parametername = 3;
    /**
     * このオブジェクトの名前
     */
    public static final String OBJNAME = "Crypt";
    /**
     * 開発時のデバッグ用、非暗号モード
     */
    private static CryptConf cryptConf = new CryptConf();

    // private String encoding = CarrierSetting.getCharacterEncoding();

    /**
     * 公開インターフェース
     */

    /**
     * コンストラクタ、乱数で鍵を初期化する。
     */
    public Crypt() {
        setKey(0);
    }

    /**
     * コンストラクタ、引数_keyは暗号鍵を指定。_keyが0の場合は乱数で暗号鍵を初期化する。
     */
    public Crypt(int _key) {
        setKey(_key);
    }

    /**
     * Cryptインスタンスを得る
     */
    public static Crypt newInstance() {
        return getCryptConf().isNoUrlEncode() ? new CryptNoUrlEncode()
                : new Crypt();
    }

    /**
     * @return cryptConf を戻します。
     */
    public static CryptConf getCryptConf() {
        return cryptConf;
    }

    /**
     * @param cryptConf
     *            設定する cryptConf。
     */
    public static void setCryptConf(CryptConf cryptConf) {
        Crypt.cryptConf = cryptConf;
    }

    /**
     * @return ignoreParameterEncription を戻します。
     */
    public static boolean isIgnoreParameterEncription() {
        return cryptConf.isIgnoreParameterEncription();
    }

    /**
     * @param ignoreParameterEncription
     *            設定する ignoreParameterEncription。
     */
    public static void setIgnoreParameterEncription(
            boolean ignoreParameterEncription) {
        cryptConf.setIgnoreParameterEncription(ignoreParameterEncription);
    }

    /**
     * 暗号化対象のnameかチェックするサブルーチン
     */
    public static final boolean isCriptParameterName(String _name) {
        return (string_parametername.indexOf(_name.charAt(0)) != -1);
    }

    /**
     * 暗号鍵を_keyに変更。_Keyが0の場合は乱数で鍵を初期化する。
     */
    public final void setKey(int _key) {
        this.initializeKey(_key);
        /* 暗号化対象文字のデフォルト設定（英数字） */
        setCryptChara(chara_alphanumeric);
    }

    /**
     * 暗号鍵を取得する。戻り値（暗号鍵）が0の場合は鍵が設定されていない。
     */
    public final int getKey() {
        return this.keyCode;
    }

    /**
     * ユーザー定義暗号モードで暗号処理する文字を指定。
     */
    public final void setCryptChara(char _cryptChara[]) {
        this.cryptChara = _cryptChara;
        this.codeBook = this.generateCodeBook(this.cryptChara);
    }

    /**
     * ユーザー定義暗号モードで暗号処理する文字を取得。
     */
    public final char[] getCryptChara() {
        return this.cryptChara;
    }

    /**
     * 数値パラメタの冗長Encode(LSB:2bitがCRC) 対応入力は0～1024
     */
    public static final int encodeInteger(int plain) throws CryptException {
        int encoded, crc_tmp;
        if ((plain < 0) || (plain >= 1024)) {
            /* ERROR */
            throw new CryptException();
        }
        crc_tmp = calcCrc_2bit(plain);
        encoded = (((plain << 2) & 0xffc) | (crc_tmp & 0x003));
        return encoded;
    }

    /**
     * 数値パラメタの冗長Decode(LSB:2bitがCRC) 対応出力は0～1024
     */
    public static final int decodeInteger(int encoded) throws CryptException {
        int plain, crc_tmp, crc_in;
        if ((encoded < 0) || (encoded >= 4096)) {
            /* ERROR */
            throw new CryptException();
        }
        plain = (char) ((encoded >> 2) & 0x3ff);
        crc_in = (encoded & 0x003);
        crc_tmp = (calcCrc_2bit(plain) & 0x003);
        if (crc_in != crc_tmp) {
            /* CRC ERROR */
            plain = 0;
            throw new CryptException();
        }
        return plain;
    }

    // /*********************/
    // /* URLパラメータ関連 */
    // /*********************/
    // /**
    // * 引数_inputに指定したURLパラメタ文字列の暗号化文字列を取得。
    // */
    // public String encodeUrlParameters(String _input) {
    // String _output = "";
    // boolean _not1st = false;
    // /* Token分解1:設定文毎に区切る */
    // StringTokenizer _str_tknz1 = new StringTokenizer(_input, "&");
    // try {
    // while (_str_tknz1.hasMoreTokens()) {
    // String _tkn1 = _str_tknz1.nextToken();
    // /* 区切り文字の除去 */
    // if (_tkn1.startsWith("&")) {
    // _tkn1 = _tkn1.substring(1, _tkn1.length());
    // }
    // {
    // /* Token分解2:名前と値に区切る */
    // StringTokenizer _str_tknz2 =
    // new StringTokenizer(_tkn1, "=");
    // String _tkn_name = _str_tknz2.nextToken();
    // /* 暗号化しない名前のチェック */
    // if (Crypt.isCriptParameterName(_tkn_name)) {
    // String _tkn_value = _str_tknz2.nextToken("");
    // /* 区切り文字の除去 */
    // if (_tkn_value.startsWith("=")) {
    // _tkn_value =
    // _tkn_value.substring(1, _tkn_value.length());
    // }
    // /* nameのエスケープデコード,暗号化,エスケープエンコード */
    // _tkn_name = escapeEncodeName(_tkn_name);
    // /* valueのエスケープデコード,暗号化,エスケープエンコード */
    // _tkn_value = escapeEncodeValue(_tkn_value);
    // /* Token結合1:名前と値を合体 */
    // _tkn1 = _tkn_name + "=" + _tkn_value;
    // }
    // }
    // /* Token結合2:設定文を合体 */
    // if (_not1st) {
    // _output = _output + "&";
    // }
    // _not1st = true;
    // _output = _output + _tkn1;
    // }
    // } catch (UnsupportedEncodingException ex) {
    // throw new NetRuntimeException("UnsupportedEncodingException");
    // }
    // /* 結果の出力 */
    // return _output;
    // }
    // /**
    // * 引数_inputに指定したURLパラメタ暗号文字列の復号化文字列を取得。
    // */
    // public final String decodeUrlParameters(String _input) {
    // String _output = "";
    // boolean _not1st = false;
    // /* Token分解1:設定文毎に区切る */
    // StringTokenizer _str_tknz1 = new StringTokenizer(_input, "&");
    // try {
    // while (_str_tknz1.hasMoreTokens()) {
    // String _tkn1 = _str_tknz1.nextToken();
    // /* 区切り文字の除去 */
    // if (_tkn1.startsWith("&")) {
    // _tkn1 = _tkn1.substring(1, _tkn1.length());
    // }
    // {
    // /* Token分解2:名前と値に区切る */
    // StringTokenizer _str_tknz2 =
    // new StringTokenizer(_tkn1, "=");
    // String _tkn_name = _str_tknz2.nextToken();
    // /* 暗号化しない名前のチェック */
    // if (Crypt.isCriptParameterName(_tkn_name)) {
    // String _tkn_value = _str_tknz2.nextToken("");
    // /* 区切り文字の除去 */
    // if (_tkn_value.startsWith("=")) {
    // _tkn_value =
    // _tkn_value.substring(1, _tkn_value.length());
    // }
    // /* nameのエスケープデコード,暗号化,エスケープエンコード */
    // _tkn_name = escapeDecodeName(_tkn_name);
    // /* valueのエスケープデコード,暗号化,エスケープエンコード */
    // _tkn_value = escapeDecodeValue(_tkn_value);
    // /* Token結合1:名前と値を合体 */
    // _tkn1 = _tkn_name + "=" + _tkn_value;
    // }
    // }
    // /* Token結合2:設定文を合体 */
    // if (_not1st) {
    // _output = _output + "&";
    // }
    // _not1st = true;
    // _output = _output + _tkn1;
    // }
    // } catch (UnsupportedEncodingException ex) {
    // throw new NetRuntimeException("UnsupportedEncodingException");
    // }
    // /* 結果の出力 */
    // return _output;
    // }
    // /**
    // * Requestパラメータ暗号の復号化文字列の取得（パラメタ名は平文で指定）一部の特殊nameは暗号対象外
    // */
    // public String getCryptedParameter(
    // HttpServletRequest _request,
    // String _input) {
    // if (Crypt.isCriptParameterName(_input)) { /* 暗号化対象のname */
    // return decodeValue(_request.getParameter(encodeName(_input)));
    // } else { /* 暗号化対象外のname */
    // return _request.getParameter(_input);
    // }
    // }
    // /**
    // * Requestパラメータnameの暗号化文字列の取得
    // *
    // * @throws UnsupportedEncodingException
    // */
    // protected String escapeEncodeName(String _input)
    // throws UnsupportedEncodingException {
    // return URLEncoder.encode(
    // encodeName(URLDecoder.decode(_input, encoding)), encoding);
    // }
    /**
     * Requestパラメータnameの暗号化文字列の取得
     */
    public String encodeName(String _input) {
        if (isIgnoreParameterEncription()) {
            return _input;
        }
        return encodeString_withCodeBook(this.getCodeBook(mode_parametername),
                _input);
    }

    // /**
    // * Requestパラメータ暗号nameの復号化文字列の取得
    // *
    // * @throws UnsupportedEncodingException
    // */
    // protected String escapeDecodeName(String _input)
    // throws UnsupportedEncodingException {
    // return URLEncoder.encode(
    // decodeName(URLDecoder.decode(_input, encoding)), encoding);
    // }
    /**
     * Requestパラメータ暗号nameの復号化文字列の取得
     */
    public String decodeName(String _input) {
        if (isIgnoreParameterEncription()) {
            return _input;
        }
        return decodeString_withCodeBook(this.getCodeBook(mode_parametername),
                _input);
    }

    /**
     * Requestパラメータvalueの暗号化文字列の取得
     */
    public final String encodeValue(int _input) {
        return encodeValue(String.valueOf(_input));
    }

    // /**
    // * Requestパラメータvalueの暗号化文字列の取得
    // *
    // * @throws UnsupportedEncodingException
    // */
    // protected String escapeEncodeValue(String _input)
    // throws UnsupportedEncodingException {
    // return URLEncoder.encode(encodeValue(URLDecoder
    // .decode(_input, encoding)), encoding);
    // }
    /**
     * Requestパラメータvalueの暗号化文字列の取得
     */
    public String encodeValue(String _input) {
        if (isIgnoreParameterEncription()) {
            return _input;
        }
        return encodeString_withCodeBook(this.getCodeBook(mode_alphanumeric),
                _input);
    }

    // /**
    // * Requestパラメータ暗号valueの復号化文字列の取得
    // *
    // * @throws UnsupportedEncodingException
    // */
    // protected String escapeDecodeValue(String _input)
    // throws UnsupportedEncodingException {
    // return URLEncoder.encode(decodeValue(URLDecoder
    // .decode(_input, encoding)), encoding);
    // }
    /**
     * Requestパラメータ暗号valueの復号化文字列の取得
     */
    public String decodeValue(String _input) {
        if (isIgnoreParameterEncription()) {
            return _input;
        }
        return decodeString_withCodeBook(this.getCodeBook(mode_alphanumeric),
                _input);
    }

    /**
     * 暗号モードで指定した暗号化文字列の取得
     */
    public final String encodeString(int _mode, String _input) {
        return Crypt.encodeString_withCodeBook(this.getCodeBook(_mode), _input);
    }

    /**
     * 暗号モードで指定した復号化文字列の取得
     */
    public final String decodeString(int _mode, String _input) {
        return Crypt.decodeString_withCodeBook(this.getCodeBook(_mode), _input);
    }

    /**
     * 暗号モードで指定した暗号化Char配列の取得
     */
    public final char[] encodeChar(int _mode, char _input[]) {
        return Crypt.encode_withCodeBook(this.getCodeBook(_mode), _input);
    }

    /**
     * 暗号モードで指定した復号化Char配列の取得
     */
    public final char[] decodeChar(int _mode, char _input[]) {
        return Crypt.decode_withCodeBook(this.getCodeBook(_mode), _input);
    }

    /**
     * 内部変数
     */

    /**
     * 暗号鍵番号:０は鍵がセットされていない
     */
    private int keyCode = 0;

    /**
     * ユーザー定義暗号化対象文字
     */
    private char cryptChara[] = null;

    /**
     * 暗号表（バイナリ）
     */
    private char codeBook_base[] = null;
    /**
     * 暗号表（ユーザー定義）
     */
    private char codeBook[] = null;
    /**
     * 暗号表（英数字）
     */
    private char codeBook_alphanumeric[] = null;
    /**
     * 暗号表（URLパラメータname用）
     */
    private char codeBook_parametername[] = null;

    /**
     * 内部定数
     */

    /**
     * 暗号表のサイズ（2^nとなる値のみ可）
     */
    private static final int codeBookSize = 0x100;

    /**
     * 暗号化文字コード定数（英数字）
     */
    private static final String string_alphanumeric = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String string_parametername = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final char chara_alphanumeric[] = string_alphanumeric
            .toCharArray();
    private static final char chara_parametername[] = string_parametername
            .toCharArray();

    /**
     * 内部ルーチン
     */

    /**
     * 暗号鍵初期化
     */
    private final void initializeKey(int _key) {
        { /* 鍵の決定 */
            this.keyCode = _key;
            if (this.keyCode == 0) { /* 指定されたキーが０の場合は乱数を用いて鍵を作る */
                Random _rnd = new Random();
                this.keyCode = _rnd.nextInt();
                if (this.keyCode == 0) { /* 0だったら固定値に変更 */
                    this.keyCode = 2002;
                }
            }
        }
        { /* 暗号表の作成 */
            int _i, _j, _k;
            char _tmp;
            /* 初期化 */
            this.codeBook_base = new char[codeBookSize];
            for (_i = 0; _i < codeBookSize; _i++) {
                this.codeBook_base[_i] = (char) ((codeBookSize - 1) - _i);
            }
            /* 複雑化 */{
                int _loop, _shift, _bias;
                _loop = ((this.keyCode >> 20) & 0x0f) + 2;
                for (_k = 0; _k <= _loop; _k++) {
                    for (_i = 0; _i < codeBookSize; _i++) {
                        _shift = ((_i + _k) & 0x1f);
                        _bias = (((this.keyCode >> (_shift)) | (this.keyCode << (32 - _shift))) & 0x0f);
                        _j = ((_i + _k + _bias) & (codeBookSize - 1));
                        _tmp = this.codeBook_base[_i];
                        this.codeBook_base[_i] = this.codeBook_base[_j];
                        this.codeBook_base[_j] = _tmp;
                    }
                }
            }
        }

        { /* 部分的暗号表の作成 */
            this.codeBook_alphanumeric = this
                    .generateCodeBook(Crypt.chara_alphanumeric);
            this.codeBook_parametername = this
                    .generateCodeBook(Crypt.chara_parametername);
        }
    }

    /**
     * 部分的暗号表の作成
     */
    private final char[] generateCodeBook(char _char_available[]) {
        char _table[] = null;
        if ((_char_available != null)
                && ((this.codeBook_base != null) && (this.codeBook_base.length == codeBookSize))) {
            _table = new char[codeBookSize];
            int _i, _j;

            /* 初期化 */
            boolean _available_flag[] = new boolean[codeBookSize];
            ;
            for (_i = 0; _i < codeBookSize; _i++) {
                _table[_i] = (char) (_i);
                _available_flag[_i] = false;
            }
            /* 許可された文字の整理 */
            for (_i = 0; _i < _char_available.length; _i++) {
                _available_flag[_char_available[_i]] = true;
            }

            /* 暗号表[base]から部分暗号表を作成 */
            _j = 0;
            for (_i = 0; _i < codeBookSize; _i++) {
                /* 対象文字検索 */
                if (_available_flag[this.codeBook_base[_i]]) {
                    /* 空きを探す */
                    for (; _j < codeBookSize; _j++) {
                        if (_available_flag[_j]) {
                            /* 更新 */
                            _table[_j] = this.codeBook_base[_i];
                            _j++;
                            break;
                        }
                    }
                }
            }
        }
        return _table;
    }

    /**
     * コードブックの取得
     */
    private final char[] getCodeBook(int _mode) {
        char[] _codeBook = null;
        switch (_mode) {
        case mode_binary:
            _codeBook = this.codeBook_base;
            break;
        case mode_userdefine:
            _codeBook = this.codeBook;
            break;
        case mode_alphanumeric:
            _codeBook = this.codeBook_alphanumeric;
            break;
        case mode_parametername:
            _codeBook = this.codeBook_parametername;
            break;
        default:
            _codeBook = null;
        }
        return _codeBook;
    }

    /**
     * String暗号化サブルーチン
     */
    private static final String encodeString_withCodeBook(char _codeBook[],
            String _input) {
        if (_input != null) {
            return new String(encode_withCodeBook(_codeBook,
                    _input.toCharArray()));
        } else {
            return null;
        }
    }

    /**
     * String復号化サブルーチン
     */
    private static final String decodeString_withCodeBook(char _codeBook[],
            String _input) {
        if (_input != null) {
            return new String(decode_withCodeBook(_codeBook,
                    _input.toCharArray()));
        } else {
            return null;
        }
    }

    /**
     * 暗号化:本体
     */
    private static char[] encode_withCodeBook(char _codeBook[],
            char _inputCode[]) {
        char _outputCode[];
        if ((_codeBook != null) && (_codeBook.length == codeBookSize)) {
            _outputCode = new char[_inputCode.length];
            int _i;
            for (_i = 0; _i < _inputCode.length; _i++) {
                if (_inputCode[_i] < codeBookSize) {
                    _outputCode[_i] = _codeBook[_inputCode[_i]];
                } else {
                    /* 未知のコードが来た！ */
                    _outputCode[_i] = _inputCode[_i];
                }
            }
        } else {
            /* コードブック異常！ */
            _outputCode = null;
        }
        return _outputCode;
    }

    /**
     * 復号化:本体
     */
    private static char[] decode_withCodeBook(char _codeBook[],
            char _inputCode[]) {
        char _outputCode[];
        if ((_codeBook != null) && (_codeBook.length == codeBookSize)) {
            _outputCode = new char[_inputCode.length];
            int _i, _j;
            for (_i = 0; _i < _inputCode.length; _i++) {
                if (_inputCode[_i] < codeBookSize) {
                    for (_j = 0; _j < codeBookSize; _j++) {
                        if (_codeBook[_j] == _inputCode[_i])
                            break;
                    }
                    if (_j < codeBookSize) {
                        _outputCode[_i] = (char) _j;
                    } else {
                        /* コードブック異常！ */
                        _outputCode = null;
                        break;
                    }
                } else {
                    /* 未知のコードが来た！ */
                    _outputCode[_i] = _inputCode[_i];
                }
            }
        } else {
            /* コードブック異常！ */
            _outputCode = null;
        }
        return _outputCode;
    }

    /**
     * 数値パラメタの冗長化用2bitCRC生成
     */
    private static final int calcCrc_2bit(int imp) {
        int ret;
        ret = (((((imp << 1)) ^ (~(imp >> 1)) ^ ((imp >> 3)) ^ (~(imp >> 5))
                ^ ((imp >> 7)) ^ ((imp >> 9))) + (((imp >> 0)) + ((imp >> 2))
                + ((imp >> 4)) + ((imp >> 6)) + ((imp >> 8)))) & 0x03);
        return ret;
    }

    /**
     * URLエンコードを行なわないCryptクラス
     */
    private static class CryptNoUrlEncode extends Crypt {
        /**
         * Requestパラメータ暗号nameの復号化文字列の取得
         */
        @SuppressWarnings("unused")
		protected String escapeDecodeName(String _input) {
            return decodeName(_input);
        }

        /**
         * Requestパラメータ暗号valueの復号化文字列の取得
         */
        @SuppressWarnings("unused")
		protected String escapeDecodeValue(String _input) {
            return decodeValue(_input);
        }

        /**
         * Requestパラメータnameの暗号化文字列の取得
         */
        @SuppressWarnings("unused")
		protected String escapeEncodeName(String _input) {
            return encodeName(_input);
        }

        /**
         * Requestパラメータvalueの暗号化文字列の取得
         */
        @SuppressWarnings("unused")
		protected String escapeEncodeValue(String _input) {
            return actionUrlEncode(encodeValue(_input));
        }

        /**
         * スラッシュ"/"を"%2F"に置換するだけなので通常のURLEncodeより高速。
         */
        private String actionUrlEncode(String actionStr) {
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < actionStr.length(); i++) {
                if (actionStr.charAt(i) == '/') {
                    buf.append("%2F");
                } else {
                    buf.append(actionStr.charAt(i));
                }
            }

            return buf.toString();
        }

    }
}
