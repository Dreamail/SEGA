/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.view;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;

/**
 * @author TsuboiY
 * 
 */
@Entity
@NamedNativeQueries({
        @NamedNativeQuery(name = "findPlaceDownloadView", query = "SELECT DISTINCT p.*, r0.name AS region0, r1.name AS region1, r2.name AS region2, r3.name AS region3 FROM places p INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON (p.country_code = r0.country_code AND p.region0_id = r0.region_id) AND c.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON (p.country_code = r1.country_code AND p.region1_id = r1.region_id) "
                + "AND (r0.country_code = r1.country_code AND r0.region_id = r1.parent_region_id) LEFT OUTER JOIN region2 r2 ON (p.country_code = r2.country_code AND p.region2_id = r2.region_id) AND (r1.country_code = r2.country_code AND "
                + "r1.region_id = r2.parent_region_id) LEFT OUTER JOIN region3 r3 ON (p.country_code = r3.country_code AND p.region3_id = r3.region_id) AND (r2.country_code = r3.country_code AND r2.region_id = r3.parent_region_id) "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code ORDER BY p.place_id", resultSetMapping = "placeDownloadMapping"),
        @NamedNativeQuery(name = "findMachinePlaceDownloadView", query = "SELECT DISTINCT p.*, r0.name AS region0, r1.name AS region1, r2.name AS region2, r3.name AS region3 FROM places p INNER JOIN machines m ON p.allnet_id = m.allnet_id INNER JOIN countries c ON p.country_code = c.country_code LEFT OUTER JOIN region0 r0 ON (p.country_code = r0.country_code AND p.region0_id = r0.region_id) AND c.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON (p.country_code = r1.country_code AND p.region1_id = r1.region_id) AND (r0.country_code = r1.country_code AND r0.region_id = r1.parent_region_id) LEFT OUTER JOIN region2 r2 ON (p.country_code = r2.country_code AND p.region2_id = r2.region_id) AND (r1.country_code = r2.country_code AND r1.region_id = r2.parent_region_id) LEFT OUTER JOIN region3 r3 ON (p.country_code = r3.country_code AND p.region3_id = r3.region_id) AND (r2.country_code = r3.country_code AND r2.region_id = r3.parent_region_id) LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds) ORDER BY p.place_id", resultSetMapping = "placeDownloadMapping"),

        @NamedNativeQuery(name = "findPlaceDownloadViewActive", query = "SELECT DISTINCT p.*, r0.name AS region0, r1.name AS region1, r2.name AS region2, r3.name AS region3 FROM places p INNER JOIN machines m ON p.allnet_id = m.allnet_id INNER JOIN machine_statuses ms ON m.serial = ms.serial INNER JOIN countries c ON p.country_code = c.country_code LEFT OUTER JOIN region0 r0 ON (p.country_code = r0.country_code AND p.region0_id = r0.region_id) AND c.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON (p.country_code = r1.country_code AND p.region1_id = r1.region_id) AND (r0.country_code = r1.country_code AND r0.region_id = r1.parent_region_id) LEFT OUTER JOIN region2 r2 ON (p.country_code = r2.country_code AND p.region2_id = r2.region_id) AND (r1.country_code = r2.country_code AND r1.region_id = r2.parent_region_id) LEFT OUTER JOIN region3 r3 ON (p.country_code = r3.country_code AND p.region3_id = r3.region_id) AND (r2.country_code = r3.country_code AND r2.region_id = r3.parent_region_id) LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE ms.last_access > :limit ORDER BY p.place_id", resultSetMapping = "placeDownloadMapping"),
        @NamedNativeQuery(name = "findMachinePlaceDownloadViewActive", query = "SELECT DISTINCT p.*, r0.name AS region0, r1.name AS region1, r2.name AS region2, r3.name AS region3 FROM places p INNER JOIN machines m ON p.allnet_id = m.allnet_id INNER JOIN machine_statuses ms ON m.serial = ms.serial INNER JOIN countries c ON p.country_code = c.country_code LEFT OUTER JOIN region0 r0 ON (p.country_code = r0.country_code AND p.region0_id = r0.region_id) AND c.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON (p.country_code = r1.country_code AND p.region1_id = r1.region_id) AND (r0.country_code = r1.country_code AND r0.region_id = r1.parent_region_id) LEFT OUTER JOIN region2 r2 ON (p.country_code = r2.country_code AND p.region2_id = r2.region_id) AND (r1.country_code = r2.country_code AND r1.region_id = r2.parent_region_id) LEFT OUTER JOIN region3 r3 ON (p.country_code = r3.country_code AND p.region3_id = r3.region_id) AND (r2.country_code = r3.country_code AND r2.region_id = r3.parent_region_id) LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) AND ms.last_access > :limit ORDER BY p.place_id", resultSetMapping = "placeDownloadMapping"),

        @NamedNativeQuery(name = "findPlaceDownloadViewNotActive", query = "SELECT DISTINCT p.*, r0.name AS region0, r1.name AS region1, r2.name AS region2, r3.name AS region3 FROM places p INNER JOIN machines m ON p.allnet_id = m.allnet_id INNER JOIN machine_statuses ms ON m.serial = ms.serial INNER JOIN countries c ON p.country_code = c.country_code LEFT OUTER JOIN region0 r0 ON (p.country_code = r0.country_code AND p.region0_id = r0.region_id) AND c.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON (p.country_code = r1.country_code AND p.region1_id = r1.region_id) AND (r0.country_code = r1.country_code AND r0.region_id = r1.parent_region_id) LEFT OUTER JOIN region2 r2 ON (p.country_code = r2.country_code AND p.region2_id = r2.region_id) AND (r1.country_code = r2.country_code AND r1.region_id = r2.parent_region_id) LEFT OUTER JOIN region3 r3 ON (p.country_code = r3.country_code AND p.region3_id = r3.region_id) AND (r2.country_code = r3.country_code AND r2.region_id = r3.parent_region_id) LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE ms.last_access <= :limit ORDER BY p.place_id", resultSetMapping = "placeDownloadMapping"),
        @NamedNativeQuery(name = "findMachinePlaceDownloadViewNotActive", query = "SELECT DISTINCT p.*, r0.name AS region0, r1.name AS region1, r2.name AS region2, r3.name AS region3 FROM places p INNER JOIN machines m ON p.allnet_id = m.allnet_id INNER JOIN machine_statuses ms ON m.serial = ms.serial INNER JOIN countries c ON p.country_code = c.country_code LEFT OUTER JOIN region0 r0 ON (p.country_code = r0.country_code AND p.region0_id = r0.region_id) AND c.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON (p.country_code = r1.country_code AND p.region1_id = r1.region_id) AND (r0.country_code = r1.country_code AND r0.region_id = r1.parent_region_id) LEFT OUTER JOIN region2 r2 ON (p.country_code = r2.country_code AND p.region2_id = r2.region_id) AND (r1.country_code = r2.country_code AND r1.region_id = r2.parent_region_id) LEFT OUTER JOIN region3 r3 ON (p.country_code = r3.country_code AND p.region3_id = r3.region_id) AND (r2.country_code = r3.country_code AND r2.region_id = r3.parent_region_id) LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) AND ms.last_access <= :limit ORDER BY p.place_id", resultSetMapping = "placeDownloadMapping") })
@SqlResultSetMappings({ @SqlResultSetMapping(name = "placeDownloadMapping", entities = { @EntityResult(entityClass = PlaceDownloadView.class) }) })
public class PlaceDownloadView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ALLNET_ID")
    private long allnetId;

    private String name;

    private String tel;

    private String address;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    private String station;

    @Column(name = "OPEN_TIME")
    private String openTime;

    @Column(name = "CLOSE_TIME")
    private String closeTime;

    @Column(name = "SPECIAL_INFO")
    private String specialInfo;

    private String region0;

    private String region1;

    private String region2;

    private String region3;

    private String nickname;

    @Column(name = "BILL_CODE")
    private String billCode;

    @Column(name = "COUNTRY_CODE")
    private String countryCode;

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
}
