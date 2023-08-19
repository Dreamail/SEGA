/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.game;

import jp.co.sega_am2.crypt.CryptEx;

/**
 * @author TsuboiY
 * 
 */
public class TitleApiPasswordGenerator {

    private static final int CRYPT_KEY = 11312;

    /**
     * ゲームIDから認証APIアカウント用パスワードを生成する。
     * 
     * @param gameId
     * @return
     */
    static String generate(String gameId) {
        // TODO いつかam2libを切り離すときが来たら再実装する
        CryptEx crypt = new CryptEx(CRYPT_KEY);
        crypt.setCryptKey(CRYPT_KEY, 0, 0);
        return crypt.encodeValue(gameId);
    }

}
