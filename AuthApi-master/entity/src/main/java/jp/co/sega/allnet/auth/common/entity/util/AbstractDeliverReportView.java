/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.sega.allnet.auth.csv.CsvUtils;

/**
 * @author NakanoY
 * 
 */
@MappedSuperclass
public abstract class AbstractDeliverReportView {

    public static final String DL_RATIO_UNKNOWN = "-1";

    protected static final String DATE_FORMAT_YEAR_TO_MSEC1 = "yyyy-MM-dd HH:mm:ss.S";

    @Id
    private String serial;

    @Column(name = "ALLNET_ID")
    private int allnetId;

    @Column(name = "PLACE_ID")
    private String placeId;

    @Column(name = "NAME")
    private String placeName;

    @Column(name = "GAME_ID")
    private String gameId;

    @Column(name = "FILES_RELEASED")
    private String filesReleased;

    @Column(name = "FILES_WORKING")
    private String filesWorking;

    @Column(name = "SEGS_TOTAL", nullable = false)
    private int segsTotal;

    @Column(name = "SEGS_DOWNLOADED", nullable = false)
    private int segsDownloaded;

    @Column(name = "AUTH_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date authTime;

    @Column(name = "ORDER_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderTime;

    @Column(name = "RELEASE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseTime;

    @Column(name = "AUTH_STATE", nullable = false)
    private int authState;

    @Column(name = "DOWNLOAD_STATE", nullable = false)
    private int downloadState;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "AP_VER_RELEASED")
    private String apVerReleased;

    @Column(name = "OS_VER_RELEASED")
    private String osVerReleased;

    @Column(name = "download_ratio")
    private BigDecimal downloadRatio;

    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 共通の順序で1行分のリストを作成する。
     * 
     * @return
     */
    public List<String> createListAsRow() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_YEAR_TO_MSEC1);

        List<String> cols = new ArrayList<String>();
        // シリアル
        cols.add(escapeForCsv(serial));
        // ゲームID
        cols.add(escapeForCsv(gameId));
        // ALL.Net ID
        cols.add(String.valueOf(allnetId));
        // 店舗ID
        cols.add(escapeForCsv(placeId));
        // 店舗名
        cols.add(escapeForCsv(placeName));
        // DL率
        if (downloadRatio == null || downloadRatio.intValue() < 0) {
            cols.add(DL_RATIO_UNKNOWN);
        } else {
            cols.add(String.valueOf(downloadRatio));
        }
        // 公開済みファイルリスト
        cols.add(escapeForCsv(filesReleased));
        // 作業中ファイルリスト
        cols.add(escapeForCsv(filesWorking));
        // 総セグメント数
        cols.add(String.valueOf(segsTotal));
        // DL済みセグメント数
        cols.add(String.valueOf(segsDownloaded));
        // 認証時刻
        if (authTime != null) {
            cols.add(df.format(authTime));
        } else {
            cols.add("");
        }
        // DL開始日時
        if (orderTime != null) {
            cols.add(df.format(orderTime));
        } else {
            cols.add("");
        }
        // 公開日時
        if (releaseTime != null) {
            cols.add(df.format(releaseTime));
        } else {
            cols.add("");
        }
        // 認証状態
        cols.add(String.valueOf(authState));
        // DL状態
        cols.add(String.valueOf(downloadState));
        // 指示書説明
        cols.add(escapeForCsv(description));
        // 公開済みAPバージョン
        cols.add(escapeForCsv(apVerReleased));
        // 公開済みOSバージョン
        cols.add(escapeForCsv(osVerReleased));
        // アクセス時刻
        cols.add(df.format(updateDate));

        return cols;
    }

    public boolean isCheckAuth() {

        if (authTime == null) {
            return false;
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Calendar lastAuthCal = Calendar.getInstance();
        lastAuthCal.setTime(authTime);
        if (cal.after(lastAuthCal)) {
            return true;
        }
        return false;
    }

    /**
     * CSV出力するために文字エスケープを行う。
     * 
     * @param s
     * @return
     */
    private String escapeForCsv(String s) {
        if (s == null) {
            return "";
        }
        return CsvUtils.escapeForCsv(s);
    }

    /**
     * @return the serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial
     *            the serial to set
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * @return the allnetId
     */
    public int getAllnetId() {
        return allnetId;
    }

    /**
     * @param allnetId
     *            the allnetId to set
     */
    public void setAllnetId(int allnetId) {
        this.allnetId = allnetId;
    }

    /**
     * @return the placeId
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * @param placeId
     *            the placeId to set
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * @return the placeName
     */
    public String getPlaceName() {
        return placeName;
    }

    /**
     * @param placeName
     *            the placeName to set
     */
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    /**
     * @return the gameId
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * @param gameId
     *            the gameId to set
     */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    /**
     * @return filesReleased
     */
    public String getFilesReleased() {
        return filesReleased;
    }

    /**
     * @param filesReleased
     *            セットする filesReleased
     */
    public void setFilesReleased(String filesReleased) {
        this.filesReleased = filesReleased;
    }

    /**
     * @return filesWorking
     */
    public String getFilesWorking() {
        return filesWorking;
    }

    /**
     * @param filesWorking
     *            セットする filesWorking
     */
    public void setFilesWorking(String filesWorking) {
        this.filesWorking = filesWorking;
    }

    /**
     * @return segsTotal
     */
    public int getSegsTotal() {
        return segsTotal;
    }

    /**
     * @param segsTotal
     *            セットする segsTotal
     */
    public void setSegsTotal(int segsTotal) {
        this.segsTotal = segsTotal;
    }

    /**
     * @return segsDownloaded
     */
    public int getSegsDownloaded() {
        return segsDownloaded;
    }

    /**
     * @param segsDownloaded
     *            セットする segsDownloaded
     */
    public void setSegsDownloaded(int segsDownloaded) {
        this.segsDownloaded = segsDownloaded;
    }

    /**
     * @return authTime
     */
    public Date getAuthTime() {
        return authTime;
    }

    /**
     * @param authTime
     *            セットする authTime
     */
    public void setAuthTime(Date authTime) {
        this.authTime = authTime;
    }

    /**
     * @return orderTime
     */
    public Date getOrderTime() {
        return orderTime;
    }

    /**
     * @param orderTime
     *            セットする orderTime
     */
    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * @return releaseTime
     */
    public Date getReleaseTime() {
        return releaseTime;
    }

    /**
     * @param releaseTime
     *            セットする releaseTime
     */
    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    /**
     * @return authState
     */
    public int getAuthState() {
        return authState;
    }

    /**
     * @param authState
     *            セットする authState
     */
    public void setAuthState(int authState) {
        this.authState = authState;
    }

    /**
     * @return downloadState
     */
    public int getDownloadState() {
        return downloadState;
    }

    /**
     * @param downloadState
     *            セットする downloadState
     */
    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            セットする description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return apVerReleased
     */
    public String getApVerReleased() {
        return apVerReleased;
    }

    /**
     * @param apVerReleased
     *            セットする apVerReleased
     */
    public void setApVerReleased(String apVerReleased) {
        this.apVerReleased = apVerReleased;
    }

    /**
     * @return osVerReleased
     */
    public String getOsVerReleased() {
        return osVerReleased;
    }

    /**
     * @param osVerReleased
     *            セットする osVerReleased
     */
    public void setOsVerReleased(String osVerReleased) {
        this.osVerReleased = osVerReleased;
    }

    /**
     * @return downloadRatio
     */
    public BigDecimal getDownloadRatio() {
        return downloadRatio;
    }

    /**
     * @param downloadRatio
     *            セットする downloadRatio
     */
    public void setDownloadRatio(BigDecimal downloadRatio) {
        this.downloadRatio = downloadRatio;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     *            the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
