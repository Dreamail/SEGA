/*
 * $Id: CryptRuntimeException.java 430 2004-11-10 04:35:08Z imode $
 * 
 * Copyright (C) 2004 SEGA Corporation. All rights reserved.
 */

package jp.co.sega_am2.crypt;

import jp.co.sega.allnet.auth.am2lib.util.Log;

/**
 * Cryptで復元失敗時に発生したエラーを表す例外
 */
@SuppressWarnings("serial")
public final class CryptRuntimeException extends RuntimeException {

    public CryptRuntimeException() {
        super();
        Log.write("####### CryptRuntime Error #######", Log.Level.ERROR);
    }
}
