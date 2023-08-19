/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import java.util.List;

import jp.co.sega.allnet.auth.api.domain.Machine;
import jp.co.sega.allnet.auth.api.domain.MachineTable;

/**
 * @author TsuboiY
 * 
 */
public interface MachineTableDao {

    int SETTING_COMM_OK = 1;

    /**
     * 該当のゲームIDに関するMachineTable用の基板情報を取得する。
     * 
     * @param gameId
     * @return
     */
    List<MachineTable> findMachineTables(String gameId);

    /**
     * 該当のゲームIDに関するMachineTable用の通信が許可された基板情報を取得する。
     * 
     * @param gameId
     * @return
     */
    List<MachineTable> findMachineTablesIncludeAuthDenied(String gameId);

    /**
     * 該当のゲームIDに関するGatMachineList用の基板情報を取得する。
     * 
     * @param gameId
     * @return
     */
    List<Machine> findMachines(String gameId);
    
    /**
     * 該当のゲームIDに関するGatMachineList用の通信が許可された基板情報を取得する。
     * 
     * @param gameId
     * @return
     */
    List<Machine> findMachinesIncludeAuthDenied(String gameId);

}
