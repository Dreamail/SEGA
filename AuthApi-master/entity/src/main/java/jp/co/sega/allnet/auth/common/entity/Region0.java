package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the REGION0 database table.
 * 
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "findRegion0LikeName", query = "SELECT r FROM Region0 r WHERE r.pk.countryCode = :countryCode AND r.name LIKE :regionName ORDER BY r.pk.regionId"),
        @NamedQuery(name = "findRegion0ByCountryCode", query = "SELECT r FROM Region0 r WHERE r.pk.countryCode = :countryCode ORDER BY r.pk.regionId") })
@NamedNativeQueries({ @NamedNativeQuery(name = "findRegion0ByName", query = "SELECT * "
        + "FROM region0 WHERE name = :name AND country_code = :countryCode", resultClass = Region0.class) })
public class Region0 implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Region0PK pk;

    @Column(name = "NAME", nullable = false)
    private String name;

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

    // bi-directional many-to-one association to Place
    @OneToMany(mappedBy = "region0")
    private List<Place> places;

    // bi-directional many-to-one association to Country
    @ManyToOne
    @JoinColumn(name = "COUNTRY_CODE", insertable = false, updatable = false)
    private Country country;

    // bi-directional many-to-one association to Region1
    @OneToMany(mappedBy = "parentRegion")
    private List<Region1> region1s;

    public Region0() {
    }

    @Override
    public String toString() {
        return "Region0 [pk=" + pk + ", name=" + name + ", createDate="
                + createDate + ", createUserId=" + createUserId
                + ", updateDate=" + updateDate + ", updateUserId="
                + updateUserId + "]";
    }

    public Region0PK getPk() {
        return pk;
    }

    public void setPk(Region0PK pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Region1> getRegion1s() {
        return region1s;
    }

    public void setRegion1s(List<Region1> region1s) {
        this.region1s = region1s;
    }

}