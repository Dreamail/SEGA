/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.dlorder;

/**
 * @author NakanoY
 * 
 */
public interface DownloadOrderService {

    int SETTING_COMM_OK = 1;

    String downloadOrder(DownloadOrderParameter param);

}
