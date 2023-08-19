package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the PLACES database table.
 * 
 */
@Entity
@Table(name = "PLACES")
@NamedQueries({
        @NamedQuery(name = "findPlaceByBillCode", query = "SELECT p FROM Place p WHERE p.bill.billCode = :billCode ORDER BY p.allnetId"),
        @NamedQuery(name = "findMinimumAllnetId", query = "SELECT MIN(p.allnetId) - 1 FROM Place p"),
        @NamedQuery(name = "countOtherPlaceByName", query = "SELECT COUNT(p.allnetId) FROM Place p WHERE p.name = :name AND p.allnetId <> :allnetId") })
@NamedNativeQueries({
        @NamedNativeQuery(name = "findPlaceByRouterId", query = "SELECT p.* FROM places p INNER JOIN routers r ON p.allnet_id = r.allnet_id WHERE r.router_id = :routerId", resultClass = Place.class),
        @NamedNativeQuery(name = "findPlaceByPlaceIp", query = "SELECT p.* FROM places p INNER JOIN routers r ON p.allnet_id = r.allnet_id WHERE r.place_ip = :placeIp", resultClass = Place.class) })
public class Place implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ALLNET_ID", nullable = false)
    private long allnetId;

    @Column(name = "PLACE_ID", nullable = false)
    private String placeId;

    @Column(name = "NAME", nullable = false)
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

    @Column(name = "BILL_CODE")
    private String billCode;

    private String nickname;

    @Column(name = "KBPS", nullable = false)
    private BigDecimal kbps = new BigDecimal(100000);

    @Column(name = "COUNTRY_CODE", nullable = false)
    private String countryCode;

    @Column(name = "REGION0_ID")
    private BigDecimal region0Id;

    @Column(name = "REGION1_ID")
    private BigDecimal region1Id;

    @Column(name = "REGION2_ID")
    private BigDecimal region2Id;

    @Column(name = "REGION3_ID")
    private BigDecimal region3Id;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    @Column(name = "UPDATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Version
    private Date updateDate;

    @Column(name = "UPDATE_USER_ID", nullable = false)
    private String updateUserId;

    // bi-directional many-to-one association to Bill
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BILL_CODE", insertable = false, updatable = false)
    private Bill bill;

    // bi-directional many-to-one association to Country
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTRY_CODE", insertable = false, updatable = false)
    private Country country;

    // bi-directional many-to-one association to Region0
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "COUNTRY_CODE", referencedColumnName = "COUNTRY_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "REGION0_ID", referencedColumnName = "REGION_ID", insertable = false, updatable = false) })
    private Region0 region0;

    // bi-directional many-to-one association to Region1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "COUNTRY_CODE", referencedColumnName = "COUNTRY_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "REGION1_ID", referencedColumnName = "REGION_ID", insertable = false, updatable = false) })
    private Region1 region1;

    // bi-directional many-to-one association to Region2
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "COUNTRY_CODE", referencedColumnName = "COUNTRY_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "REGION2_ID", referencedColumnName = "REGION_ID", insertable = false, updatable = false) })
    private Region2 region2;

    // bi-directional many-to-one association to Region3
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "COUNTRY_CODE", referencedColumnName = "COUNTRY_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "REGION3_ID", referencedColumnName = "REGION_ID", insertable = false, updatable = false) })
    private Region3 region3;

    // bi-directional many-to-one association to AuthAllowedPlace
    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE)
    private List<AuthAllowedPlace> authAllowedPlaces;

    public Place() {
    }

    @Override
    public String toString() {
        return "Place [allnetId=" + allnetId + ", placeId=" + placeId
                + ", name=" + name + ", tel=" + tel + ", address=" + address
                + ", zipCode=" + zipCode + ", station=" + station
                + ", openTime=" + openTime + ", closeTime=" + closeTime
                + ", specialInfo=" + specialInfo + ", billCode=" + billCode
                + ", nickname=" + nickname + ", kbps=" + kbps
                + ", countryCode=" + countryCode + ", region0Id=" + region0Id
                + ", region1Id=" + region1Id + ", region2Id=" + region2Id
                + ", region3Id=" + region3Id + ", createDate=" + createDate
                + ", createUserId=" + createUserId + ", updateDate="
                + updateDate + ", updateUserId=" + updateUserId + "]";
    }

    public long getAllnetId() {
        return allnetId;
    }

    public void setAllnetId(long allnetId) {
        this.allnetId = allnetId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getSpecialInfo() {
        return specialInfo;
    }

    public void setSpecialInfo(String specialInfo) {
        this.specialInfo = specialInfo;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public BigDecimal getKbps() {
        return kbps;
    }

    public void setKbps(BigDecimal kbps) {
        this.kbps = kbps;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public BigDecimal getRegion0Id() {
        return region0Id;
    }

    public void setRegion0Id(BigDecimal region0Id) {
        this.region0Id = region0Id;
    }

    public BigDecimal getRegion1Id() {
        return region1Id;
    }

    public void setRegion1Id(BigDecimal region1Id) {
        this.region1Id = region1Id;
    }

    public BigDecimal getRegion2Id() {
        return region2Id;
    }

    public void setRegion2Id(BigDecimal region2Id) {
        this.region2Id = region2Id;
    }

    public BigDecimal getRegion3Id() {
        return region3Id;
    }

    public void setRegion3Id(BigDecimal region3Id) {
        this.region3Id = region3Id;
    }

    public Bill getBill() {
        return bill;
    }

    public Country getCountry() {
        return country;
    }

    public Region0 getRegion0() {
        return region0;
    }

    public Region1 getRegion1() {
        return region1;
    }

    public Region2 getRegion2() {
        return region2;
    }

    public Region3 getRegion3() {
        return region3;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public List<AuthAllowedPlace> getAuthAllowedPlaces() {
        return authAllowedPlaces;
    }

    public void setAuthAllowedPlaces(List<AuthAllowedPlace> authAllowedPlaces) {
        this.authAllowedPlaces = authAllowedPlaces;
    }

}