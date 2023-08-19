/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.util;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;

import jp.co.sega.allnet.auth.csv.CsvUtils;

/**
 * @author NakanoY
 * 
 */
@Entity
@SqlResultSetMappings({ @SqlResultSetMapping(name = "appDeliverReportMapping", entities = { @EntityResult(entityClass = AppDeliverReportView.class) }) })
public class AppDeliverReportView extends AbstractDeliverReportView implements
        Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "AP_VER_WORKING", nullable = false)
    private String apVerWorking;

    @Column(name = "OS_VER_WORKING")
    private String osVerWorking;

    @Override
    public List<String> createListAsRow() {
        List<String> cols = super.createListAsRow();

        // 作業中APバージョン（18番目に挿入）
        if (apVerWorking != null) {
            cols.add(17, CsvUtils.escapeForCsv(apVerWorking));
        }
        // 作業中OSバージョン（20番目に挿入）
        if (osVerWorking != null) {
            cols.add(19, CsvUtils.escapeForCsv(osVerWorking));
        }

        return cols;
    }

    /**
     * @return the apVerWorking
     */
    public String getApVerWorking() {
        return apVerWorking;
    }

    /**
     * @param apVerWorking
     *            the apVerWorking to set
     */
    public void setApVerWorking(String apVerWorking) {
        this.apVerWorking = apVerWorking;
    }

    /**
     * @return the osVerWorking
     */
    public String getOsVerWorking() {
        return osVerWorking;
    }

    /**
     * @param osVerWorking
     *            the osVerWorking to set
     */
    public void setOsVerWorking(String osVerWorking) {
        this.osVerWorking = osVerWorking;
    }
}
