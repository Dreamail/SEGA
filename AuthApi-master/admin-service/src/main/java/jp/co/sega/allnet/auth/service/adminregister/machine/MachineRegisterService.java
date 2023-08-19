/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.machine;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * 基板情報の登録処理を行う。
 * 
 * @author TsuboiY
 * 
 */
public interface MachineRegisterService {

    int STATUS_REGISTER = 1;

    int STATUS_MODIFY = 2;

    int MIN_DELETION_REASON_NO = 0;

    /**
     * 基板情報の登録処理を行う。
     * 
     * @param val
     * @return
     */
    String registerMachine(String val);

    /**
     * AllnetAdminからの基板情報の登録処理を行う。
     * 
     * @param val
     * @param gameId
     * @param reserve
     * @return
     */
    RegisterServiceResult<MachineRegisterResult> registerMachineForAdmin(
            String val, String gameId, boolean reserve);

}
