/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.view;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;

/**
 * @author TsuboiY
 * 
 */
@Entity
@NamedNativeQueries({

        @NamedNativeQuery(name = "findPlaceByAllnetId", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE p.allnet_id = :allnetId ORDER BY p.place_id", resultClass = PlaceView.class),
        @NamedNativeQuery(name = "findMachinePlaceByAllnetId", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN machines m ON p.allnet_id = m.allnet_id INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE p.allnet_id = :allnetId AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY p.place_id", resultClass = PlaceView.class),

        @NamedNativeQuery(name = "findPlaceByPlaceId", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE p.place_id = :placeId ORDER BY p.place_id", resultClass = PlaceView.class),
        @NamedNativeQuery(name = "findMachinePlaceByPlaceId", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN machines m ON p.allnet_id = m.allnet_id INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE p.place_id = :placeId AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY p.place_id", resultClass = PlaceView.class),

        @NamedNativeQuery(name = "findPlaceByTel", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE p.tel LIKE :tel ORDER BY p.place_id", resultClass = PlaceView.class),
        @NamedNativeQuery(name = "findMachinePlaceByTel", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN machines m ON p.allnet_id = m.allnet_id INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE p.tel LIKE :tel AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY p.place_id", resultClass = PlaceView.class),

        @NamedNativeQuery(name = "findPlaceByName", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE p.name LIKE :name ORDER BY p.place_id", resultClass = PlaceView.class),
        @NamedNativeQuery(name = "findMachinePlaceByName", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN machines m ON p.allnet_id = m.allnet_id INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE p.name LIKE :name AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY p.place_id", resultClass = PlaceView.class),

        @NamedNativeQuery(name = "findMachinePlaceByRouterId", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN routers r ON p.allnet_id = r.allnet_id INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE r.router_id = :routerId ORDER BY p.place_id", resultClass = PlaceView.class),
        @NamedNativeQuery(name = "findMachinePlaceByRouterIdAndGameId", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN machines m ON p.allnet_id = m.allnet_id INNER JOIN routers r ON p.allnet_id = r.allnet_id INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE r.router_id = :routerId AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY p.place_id", resultClass = PlaceView.class),

        @NamedNativeQuery(name = "findPlaceByCountryCode", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE c.country_code = :countryCode ORDER BY p.place_id", resultClass = PlaceView.class),
        @NamedNativeQuery(name = "findMachinePlaceByCountryCode", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN machines m ON p.allnet_id = m.allnet_id INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE c.country_code = :countryCode AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY p.place_id", resultClass = PlaceView.class),
        @NamedNativeQuery(name = "findPlaceByRegion0Id", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE c.country_code = :countryCode AND p.region0_id = :region0Id ORDER BY p.place_id", resultClass = PlaceView.class),
        @NamedNativeQuery(name = "findMachinePlaceByRegion0Id", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN machines m ON p.allnet_id = m.allnet_id INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE c.country_code = :countryCode AND p.region0_id = :region0Id AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY p.place_id", resultClass = PlaceView.class),
        @NamedNativeQuery(name = "findPlaceByRegion0IdAndRegion1Id", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE c.country_code = :countryCode AND p.region0_id = :region0Id AND p.region1_id = :region1Id ORDER BY p.place_id", resultClass = PlaceView.class),
        @NamedNativeQuery(name = "findMachinePlaceByRegion0IdAndRegion1Id", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN machines m ON p.allnet_id = m.allnet_id INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE c.country_code = :countryCode AND p.region0_id = :region0Id AND p.region1_id = :region1Id AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY p.place_id", resultClass = PlaceView.class),

        @NamedNativeQuery(name = "findAuthAllowedPlaceByGameId", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN auth_allowed_places pa ON p.allnet_id = pa.allnet_id INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE pa.game_id = :gameId ORDER BY p.place_id", resultClass = PlaceView.class),
        @NamedNativeQuery(name = "findMachineAuthAllowedPlaceByGameId", query = "SELECT DISTINCT p.*, r0.name AS region0_name, r1.name AS region1_name, r2.name AS region2_name, r3.name AS region3_name FROM places p INNER JOIN auth_allowed_places pa ON p.allnet_id = pa.allnet_id INNER JOIN machines m ON p.allnet_id = m.allnet_id "
                + "INNER JOIN routers r ON p.allnet_id = r.allnet_id INNER JOIN countries c ON p.country_code = c.country_code "
                + "LEFT OUTER JOIN region0 r0 ON p.region0_id = r0.region_id AND p.country_code = r0.country_code LEFT OUTER JOIN region1 r1 ON p.region1_id = r1.region_id AND p.country_code = r1.country_code LEFT OUTER JOIN region2 r2 ON p.region2_id = r2.region_id AND p.country_code = r2.country_code LEFT OUTER JOIN region3 r3 ON p.region3_id = r3.region_id  AND p.country_code = r3.country_code "
                + "LEFT OUTER JOIN bills b ON p.bill_code = b.bill_code WHERE pa.game_id = :gameId AND (m.game_id = :gameId OR m.reserved_game_id = :gameId) ORDER BY p.place_id", resultClass = PlaceView.class) })
public class PlaceView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ALLNET_ID")
    private long allnetId;

    @Column(name = "PLACE_ID")
    private String placeId;

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

    @Column(name = "REGION0_ID")
    private BigDecimal region0Id;

    @Column(name = "REGION0_NAME")
    private String region0Name;

    @Column(name = "REGION1_ID")
    private BigDecimal region1Id;

    @Column(name = "REGION1_NAME")
    private String region1Name;

    @Column(name = "REGION2_ID")
    private BigDecimal region2Id;

    @Column(name = "REGION2_NAME")
    private String region2Name;

    @Column(name = "REGION3_ID")
    private BigDecimal region3Id;

    @Column(name = "REGION3_NAME")
    private String region3Name;

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
     * @return placeId
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * @param placeId
     *            セットする placeId
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
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
     * @return region0Id
     */
    public BigDecimal getRegion0Id() {
        return region0Id;
    }

    /**
     * @param region0Id
     *            セットする region0Id
     */
    public void setRegion0Id(BigDecimal region0Id) {
        this.region0Id = region0Id;
    }

    /**
     * @return region0Name
     */
    public String getRegion0Name() {
        return region0Name;
    }

    /**
     * @param region0Name
     *            セットする region0Name
     */
    public void setRegion0Name(String region0Name) {
        this.region0Name = region0Name;
    }

    /**
     * @return region1Id
     */
    public BigDecimal getRegion1Id() {
        return region1Id;
    }

    /**
     * @param region1Id
     *            セットする region1Id
     */
    public void setRegion1Id(BigDecimal region1Id) {
        this.region1Id = region1Id;
    }

    /**
     * @return region1Name
     */
    public String getRegion1Name() {
        return region1Name;
    }

    /**
     * @param region1Name
     *            セットする region1Name
     */
    public void setRegion1Name(String region1Name) {
        this.region1Name = region1Name;
    }

    /**
     * @return region2Id
     */
    public BigDecimal getRegion2Id() {
        return region2Id;
    }

    /**
     * @param region2Id
     *            セットする region2Id
     */
    public void setRegion2Id(BigDecimal region2Id) {
        this.region2Id = region2Id;
    }

    /**
     * @return region2Name
     */
    public String getRegion2Name() {
        return region2Name;
    }

    /**
     * @param region2Name
     *            セットする region2Name
     */
    public void setRegion2Name(String region2Name) {
        this.region2Name = region2Name;
    }

    /**
     * @return region3Id
     */
    public BigDecimal getRegion3Id() {
        return region3Id;
    }

    /**
     * @param region3Id
     *            セットする region3Id
     */
    public void setRegion3Id(BigDecimal region3Id) {
        this.region3Id = region3Id;
    }

    /**
     * @return region3Name
     */
    public String getRegion3Name() {
        return region3Name;
    }

    /**
     * @param region3Name
     *            セットする region3Name
     */
    public void setRegion3Name(String region3Name) {
        this.region3Name = region3Name;
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
