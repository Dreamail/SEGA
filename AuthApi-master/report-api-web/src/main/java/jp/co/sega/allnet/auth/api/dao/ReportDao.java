/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import jp.co.sega.allnet.auth.api.domain.ReportImage;

/**
 * @author NakanoY
 * 
 */
public interface ReportDao {

    String USER_ID = "Report";

    /**
     * アプリケーション用配信レポートの登録更新を行う
     * 
     * @param image
     */
    void updateAppDeliverReport(ReportImage image);

    /**
     * オプション用配信レポートの登録更新を行う
     * 
     * @param image
     */
    void updateOptDeliverReport(ReportImage image);

}
