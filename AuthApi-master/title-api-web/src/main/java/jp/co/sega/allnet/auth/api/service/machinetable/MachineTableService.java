/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.machinetable;

/**
 * @author TsuboiY
 * 
 */
public interface MachineTableService {

    /**
     * タイトルサーバ通信の基板情報取得処理を行う。
     * 
     * @param gameId
     * @param password
     * @param all
     * @return
     */
    String machineTable(String gameId, String password, boolean all);

    /**
     * タイトルサーバ通信の基板情報取得処理を行う。 authがtrueなら認証失敗時にnullを返す。
     * 
     * @param gameId
     * @param password
     * @param all
     * @param auth
     * @return
     */
    String machineTable(String gameId, String password, boolean all,
            boolean auth);

}
