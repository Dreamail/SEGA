/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.bill;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * @author TsuboiY
 * 
 */
public interface MoveDeniedBillRegisterService {

    /**
     * 同一包括先内自動移設不許可請求先情報の登録を行います。
     * 
     * @param val
     * @return
     */
    String registerMoveDeniedBill(String val);

    /**
     * AllnetAdminから同一包括先内自動移設不許可請求先情報の登録を行います。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<MoveDeniedBillRegisterResult> registerMoveDeniedBillForAdmin(
            String val);

}
