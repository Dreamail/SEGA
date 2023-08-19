package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * The persistent class for the ROUTERS database table.
 * 
 */
@Entity
@Table(name = "ROUTERS")
@NamedQueries({
        @NamedQuery(name = "deleteRouter", query = "DELETE FROM Router r WHERE r.routerId = :routerId"),
        @NamedQuery(name = "findRouterCountByIp", query = "SELECT COUNT(r.routerId) FROM Router r  WHERE r.placeIp = :placeIp AND r.routerId <> :routerId"),
        @NamedQuery(name = "findRouterByIpWithoutRouterId", query = "SELECT r FROM Router r  WHERE r.placeIp = :placeIp"),
        @NamedQuery(name = "findRouterByIpLike", query = "SELECT r FROM Router r WHERE r.placeIp like :placeIp") })
@NamedNativeQueries({
        @NamedNativeQuery(name = "findAllRouter", query = "SELECT DISTINCT r.*, p.* FROM routers r INNER JOIN places p ON r.allnet_id = p.allnet_id ORDER BY r.router_id", resultSetMapping = "routerWithPlaceMapping"),
        @NamedNativeQuery(name = "findAllRouterAndGameIds", query = "SELECT DISTINCT r.*, p.* FROM routers r INNER JOIN places p ON r.allnet_id = p.allnet_id INNER JOIN machines m ON p.allnet_id = m.allnet_id WHERE m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds) ORDER BY r.router_id", resultSetMapping = "routerWithPlaceMapping") })
@SqlResultSetMappings({ @SqlResultSetMapping(name = "routerWithPlaceMapping", entities = {
        @EntityResult(entityClass = Router.class),
        @EntityResult(entityClass = Place.class) }) })
public class Router implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ROUTER_ID", nullable = false)
    private String routerId;

    @Column(name = "ALLNET_ID", nullable = false)
    private BigDecimal allnetId;

    @Column(name = "ROUTER_TYPE_ID", nullable = false)
    private BigDecimal routerTypeId;

    @Column(name = "LC_TYPE_ID")
    private BigDecimal lcTypeId;

    @Column(name = "PLACE_IP", nullable = false)
    private String placeIp;

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

    @Transient
    private RouterType routerType;

    @Transient
    private LcType lcType;

    public Router() {
    }

    @Override
    public String toString() {
        return "Router [routerId=" + routerId + ", allnetId=" + allnetId
                + ", routerTypeId=" + routerTypeId + ", lcTypeId=" + lcTypeId
                + ", placeIp=" + placeIp + ", createDate=" + createDate
                + ", createUserId=" + createUserId + ", updateDate="
                + updateDate + ", updateUserId=" + updateUserId + "]";
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public BigDecimal getAllnetId() {
        return allnetId;
    }

    public void setAllnetId(BigDecimal allnetId) {
        this.allnetId = allnetId;
    }

    public BigDecimal getRouterTypeId() {
        return routerTypeId;
    }

    public void setRouterTypeId(BigDecimal routerTypeId) {
        this.routerTypeId = routerTypeId;
    }

    public BigDecimal getLcTypeId() {
        return lcTypeId;
    }

    public void setLcTypeId(BigDecimal lcTypeId) {
        this.lcTypeId = lcTypeId;
    }

    public String getPlaceIp() {
        return placeIp;
    }

    public void setPlaceIp(String placeIp) {
        this.placeIp = placeIp;
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

    public RouterType getRouterType() {
        return routerType;
    }

    public void setRouterType(RouterType routerType) {
        this.routerType = routerType;
    }

    public LcType getLcType() {
        return lcType;
    }

    public void setLcType(LcType lcType) {
        this.lcType = lcType;
    }

}