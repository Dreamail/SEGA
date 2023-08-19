/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.place;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import jp.co.sega.allnet.auth.annotation.StringByteLength;
import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterParameter;

import org.apache.commons.lang3.StringUtils;

/**
 * AdminRegister?cmd=place のvalパラメータを保持するクラス。
 * 
 * @author NakanoY
 * 
 */
public class PlaceRegisterParameter extends AbstractRegisterParameter implements
        Serializable {

    private static final long serialVersionUID = 1L;

    private static final int CSV_COLUMN_SIZE = 25;

    private static final String DEFAULT_COUNTRY_CODE = "JPN";

    @NotNull
    @Pattern(regexp = "\\-?[0-9]{1,10}")
    private String allnetId;

    @NotNull
    @StringByteLength(min = 1, max = 40, encoding = "EUC-JP")
    private String name;

    @Pattern(regexp = "[0-9]{0,16}")
    private String tel;

    @StringByteLength(max = 100, encoding = "EUC-JP")
    private String address;

    @Pattern(regexp = "[0-9\\-]{0,9}")
    private String zipCode;

    @StringByteLength(max = 80, encoding = "EUC-JP")
    private String station;

    @Pattern(regexp = "[0-9]{0,2}")
    private String openTimeHour;

    @Pattern(regexp = "|[0-9]|[0-5][0-9]")
    private String openTimeMinute;

    @Pattern(regexp = "[0-9]{0,2}")
    private String closeTimeHour;

    @Pattern(regexp = "|[0-9]|[0-5][0-9]")
    private String closeTimeMinute;

    @StringByteLength(max = 128, encoding = "EUC-JP")
    private String specialInfo;

    @StringByteLength(max = 27, encoding = "EUC-JP")
    private String nickname;

    @Pattern(regexp = "[0-9A-Z]{0,9}")
    private String billCode;

    @NotNull
    @Pattern(regexp = "[A-Z]{3}")
    private String countryCode;

    @StringByteLength(max = 32, encoding = "EUC-JP")
    private String region0;

    @StringByteLength(max = 32, encoding = "EUC-JP")
    private String region1;

    @StringByteLength(max = 32, encoding = "EUC-JP")
    private String region2;

    @StringByteLength(max = 32, encoding = "EUC-JP")
    private String region3;

    /**
     * コンストラクタ
     * 
     * @param csv
     */
    public PlaceRegisterParameter(String[] csv) {
        super(csv, CSV_COLUMN_SIZE);
    }

    @Override
    protected void assign(String[] inputCsv) {
        allnetId = inputCsv[23];
        name = inputCsv[0];
        tel = inputCsv[1];
        address = inputCsv[2];
        zipCode = inputCsv[6];
        station = inputCsv[11];
        if (inputCsv[12] != null) {
            openTimeHour = inputCsv[12];
        }
        if (inputCsv[13] != null) {
            openTimeMinute = inputCsv[13];
        }
        if (inputCsv[14] != null) {
            closeTimeHour = inputCsv[14];
        }
        if (inputCsv[15] != null) {
            closeTimeMinute = inputCsv[15];
        }
        specialInfo = inputCsv[16];
        nickname = inputCsv[21];
        billCode = inputCsv[22];
        countryCode = inputCsv[24];
        region0 = inputCsv[17];
        region1 = inputCsv[18];
        region2 = inputCsv[19];
        region3 = inputCsv[20];

        if (StringUtils.isEmpty(countryCode)) {
            countryCode = DEFAULT_COUNTRY_CODE;
        }
    }

    /**
     * @return the allnetId
     */
    public String getAllnetId() {
        return allnetId;
    }

    /**
     * @param allnetId
     *            the allnetId to set
     */
    public void setAllnetId(String allnetId) {
        this.allnetId = allnetId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel
     *            the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * @param zipCode
     *            the zipCode to set
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * @return the station
     */
    public String getStation() {
        return station;
    }

    /**
     * @param station
     *            the station to set
     */
    public void setStation(String station) {
        this.station = station;
    }

    /**
     * @return the openTimeHour
     */
    public String getOpenTimeHour() {
        return openTimeHour;
    }

    /**
     * @param openTimeHour
     *            the openTimeHour to set
     */
    public void setOpenTimeHour(String openTimeHour) {
        this.openTimeHour = openTimeHour;
    }

    /**
     * @return the openTimeMinute
     */
    public String getOpenTimeMinute() {
        return openTimeMinute;
    }

    /**
     * @param openTimeMinute
     *            the openTimeMinute to set
     */
    public void setOpenTimeMinute(String openTimeMinute) {
        this.openTimeMinute = openTimeMinute;
    }

    /**
     * @return the closeTimeHour
     */
    public String getCloseTimeHour() {
        return closeTimeHour;
    }

    /**
     * @param closeTimeHour
     *            the closeTimeHour to set
     */
    public void setCloseTimeHour(String closeTimeHour) {
        this.closeTimeHour = closeTimeHour;
    }

    /**
     * @return the closeTimeMinute
     */
    public String getCloseTimeMinute() {
        return closeTimeMinute;
    }

    /**
     * @param closeTimeMinute
     *            the closeTimeMinute to set
     */
    public void setCloseTimeMinute(String closeTimeMinute) {
        this.closeTimeMinute = closeTimeMinute;
    }

    /**
     * @return the specialInfo
     */
    public String getSpecialInfo() {
        return specialInfo;
    }

    /**
     * @param specialInfo
     *            the specialInfo to set
     */
    public void setSpecialInfo(String specialInfo) {
        this.specialInfo = specialInfo;
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     *            the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the billCode
     */
    public String getBillCode() {
        return billCode;
    }

    /**
     * @param billCode
     *            the billCode to set
     */
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    /**
     * @return the countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode
     *            the countryCode to set
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return the region0
     */
    public String getRegion0() {
        return region0;
    }

    /**
     * @param region0
     *            the region0 to set
     */
    public void setRegion0(String region0) {
        this.region0 = region0;
    }

    /**
     * @return the region1
     */
    public String getRegion1() {
        return region1;
    }

    /**
     * @param region1
     *            the region1 to set
     */
    public void setRegion1(String region1) {
        this.region1 = region1;
    }

    /**
     * @return the region2
     */
    public String getRegion2() {
        return region2;
    }

    /**
     * @param region2
     *            the region2 to set
     */
    public void setRegion2(String region2) {
        this.region2 = region2;
    }

    /**
     * @return the region3
     */
    public String getRegion3() {
        return region3;
    }

    /**
     * @param region3
     *            the region3 to set
     */
    public void setRegion3(String region3) {
        this.region3 = region3;
    }

}
