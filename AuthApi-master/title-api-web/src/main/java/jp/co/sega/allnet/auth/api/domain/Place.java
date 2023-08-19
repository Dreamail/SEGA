package jp.co.sega.allnet.auth.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Place implements Serializable {
    private static final long serialVersionUID = 1L;

    private long allnetId;

    private String address;

    private String closeTime;

    private Timestamp createDate;

    private String createUserId;

    private BigDecimal kbps;

    private String name;

    private String nickname;

    private String openTime;

    private String placeId;

    private String specialInfo;

    private String station;

    private String tel;

    private Timestamp updateDate;

    private String updateUserId;

    private String zipCode;

    private String billCode;

    private String countryCode;

    private BigDecimal region0Id;

    private BigDecimal region1Id;

    private BigDecimal region2Id;

    private BigDecimal region3Id;

    public Place() {
    }

    /**
     * @return the allnetId
     */
    public long getAllnetId() {
        return allnetId;
    }

    /**
     * @param allnetId
     *            the allnetId to set
     */
    public void setAllnetId(long allnetId) {
        this.allnetId = allnetId;
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
     * @return the closeTime
     */
    public String getCloseTime() {
        return closeTime;
    }

    /**
     * @param closeTime
     *            the closeTime to set
     */
    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    /**
     * @return the createDate
     */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     *            the createDate to set
     */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the createUserId
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * @param createUserId
     *            the createUserId to set
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * @return the kbps
     */
    public BigDecimal getKbps() {
        return kbps;
    }

    /**
     * @param kbps
     *            the kbps to set
     */
    public void setKbps(BigDecimal kbps) {
        this.kbps = kbps;
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
     * @return the openTime
     */
    public String getOpenTime() {
        return openTime;
    }

    /**
     * @param openTime
     *            the openTime to set
     */
    public void setOpenTime(String openTime) {
        this.openTime = openTime;
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
     * @return the updateDate
     */
    public Timestamp getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     *            the updateDate to set
     */
    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the updateUserId
     */
    public String getUpdateUserId() {
        return updateUserId;
    }

    /**
     * @param updateUserId
     *            the updateUserId to set
     */
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
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
     * @return the region0Id
     */
    public BigDecimal getRegion0Id() {
        return region0Id;
    }

    /**
     * @param region0Id
     *            the region0Id to set
     */
    public void setRegion0Id(BigDecimal region0Id) {
        this.region0Id = region0Id;
    }

    /**
     * @return the region1Id
     */
    public BigDecimal getRegion1Id() {
        return region1Id;
    }

    /**
     * @param region1Id
     *            the region1Id to set
     */
    public void setRegion1Id(BigDecimal region1Id) {
        this.region1Id = region1Id;
    }

    /**
     * @return the region2Id
     */
    public BigDecimal getRegion2Id() {
        return region2Id;
    }

    /**
     * @param region2Id
     *            the region2Id to set
     */
    public void setRegion2Id(BigDecimal region2Id) {
        this.region2Id = region2Id;
    }

    /**
     * @return the region3Id
     */
    public BigDecimal getRegion3Id() {
        return region3Id;
    }

    /**
     * @param region3Id
     *            the region3Id to set
     */
    public void setRegion3Id(BigDecimal region3Id) {
        this.region3Id = region3Id;
    }

}