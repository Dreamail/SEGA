package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the ROUTER_TYPES database table.
 * 
 */
@Entity
@Table(name = "ROUTER_TYPES")
@NamedQueries({
        @NamedQuery(name = "findAllRouterTypes", query = "SELECT rt FROM RouterType rt"),
        @NamedQuery(name = "findMininumRouterTypeId", query = "SELECT MIN(rt.routerTypeId) FROM RouterType rt") })
public class RouterType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ROUTER_TYPE_ID", nullable = false)
    private int routerTypeId;

    @Column(name = "NAME")
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

    public RouterType() {
    }

    @Override
    public String toString() {
        return "RouterType [routerTypeId=" + routerTypeId + ", name=" + name
                + ", createDate=" + createDate + ", createUserId="
                + createUserId + ", updateDate=" + updateDate
                + ", updateUserId=" + updateUserId + "]";
    }

    public int getRouterTypeId() {
        return routerTypeId;
    }

    public void setRouterTypeId(int routerTypeId) {
        this.routerTypeId = routerTypeId;
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

}