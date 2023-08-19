package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the REGION3 database table.
 * 
 */
@Entity
@NamedNativeQueries({ @NamedNativeQuery(name = "findRegion3ByName", query = "SELECT * "
        + "FROM region3 WHERE name = :name AND country_code = :countryCode AND parent_region_id = :region2Id", resultClass = Region3.class) })
public class Region3 implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Region3PK pk;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PARENT_REGION_ID", nullable = false)
    private BigDecimal parentRegionId;

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

    // bi-directional many-to-one association to Region2
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "COUNTRY_CODE", referencedColumnName = "COUNTRY_CODE", insertable = false, updatable = false, nullable = false),
            @JoinColumn(name = "PARENT_REGION_ID", referencedColumnName = "REGION_ID", insertable = false, updatable = false, nullable = false) })
    private Region2 parentRegion;

    // bi-directional many-to-one association to Place
    @OneToMany(mappedBy = "region3")
    private List<Place> places;

    public Region3() {
    }

    @Override
    public String toString() {
        return "Region3 [pk=" + pk + ", name=" + name + ", parentRegionId="
                + parentRegionId + ", createDate=" + createDate
                + ", createUserId=" + createUserId + ", updateDate="
                + updateDate + ", updateUserId=" + updateUserId + "]";
    }

    public Region3PK getPk() {
        return pk;
    }

    public void setPk(Region3PK pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getParentRegionId() {
        return parentRegionId;
    }

    public void setParentRegionId(BigDecimal parentRegionId) {
        this.parentRegionId = parentRegionId;
    }

    public Region2 getParentRegion() {
        return parentRegion;
    }

    public void setParentRegion(Region2 parentRegion) {
        this.parentRegion = parentRegion;
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

}