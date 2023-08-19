/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.report;

import jp.co.sega.allnet.auth.api.domain.ReportData;
import jp.co.sega.allnet.auth.api.exception.ImageKeyInvalidException;
import jp.co.sega.allnet.auth.api.exception.InvalidParameterException;

/**
 * @author NakanoY
 * 
 */
public interface ReportService {

    String KEY_NAME_APP_REPORT = "appimage";

    String KEY_NAME_OPT_REPORT = "optimage";

    int AUTH_STATE_FAILED = 1;

    int AUTH_STATE_SUCCESS = 2;

    /**
     * 配信レポートの登録・更新を行う。
     * 
     * @param imageMap
     * @return
     */
    void report(ReportData data) throws ImageKeyInvalidException,
            InvalidParameterException;

}
