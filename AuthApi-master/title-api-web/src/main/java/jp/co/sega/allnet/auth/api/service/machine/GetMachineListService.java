/**
 * Copyright (C) 2014 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.machine;

import jp.co.sega.allnet.auth.api.service.AuthenticationException;
import jp.co.sega.allnet.auth.api.service.machinetable.MachineTableService;

/**
 * @author NakanoY
 * 
 */
public interface GetMachineListService extends MachineTableService {

    /**
     * 基板情報取得処理を行う。
     * 
     * @param gameId
     * @param password
     * @param allMachine
     * @return
     * @throws AuthenticationException
     */
    String getMachineList(String gameId, String password, boolean allMachine)
            throws AuthenticationException;

}
