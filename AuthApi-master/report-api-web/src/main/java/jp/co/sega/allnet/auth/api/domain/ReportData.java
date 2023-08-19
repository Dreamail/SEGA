/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author NakanoY
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportData implements Serializable {

    private static final long serialVersionUID = 1L;

    private ReportImage appimage;

    private ReportImage optimage;

    /**
     * @return the appimage
     */
    public ReportImage getAppimage() {
        return appimage;
    }

    /**
     * @param appimage
     *            the appimage to set
     */
    public void setAppimage(ReportImage appimage) {
        this.appimage = appimage;
    }

    /**
     * @return the optimage
     */
    public ReportImage getOptimage() {
        return optimage;
    }

    /**
     * @param optimage
     *            the optimage to set
     */
    public void setOptimage(ReportImage optimage) {
        this.optimage = optimage;
    }

}
