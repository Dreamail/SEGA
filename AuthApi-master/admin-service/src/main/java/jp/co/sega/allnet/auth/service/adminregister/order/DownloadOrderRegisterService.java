/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.order;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * @author NakanoY
 * 
 */
public interface DownloadOrderRegisterService {

    RegisterServiceResult<DownloadOrderRegisterResult> registerDownloadOrder(
            String val, String gameId);

}
