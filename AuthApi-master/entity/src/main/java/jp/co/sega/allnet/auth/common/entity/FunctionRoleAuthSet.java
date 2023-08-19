package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the FUNCTION_AUTH_SET_AUTHS database table.
 * 
 */
@Entity
@Table(name = "FUNCTION_ROLE_AUTH_SETS")
@NamedQueries({
        @NamedQuery(name = "findFunctionRoleAuthSetByRoleId", query = "SELECT f FROM FunctionRoleAuthSet f WHERE f.pk.roleId = :roleId"),
        @NamedQuery(name = "deleteRoleAuth", query = "DELETE FROM FunctionRoleAuthSet f WHERE f.pk.roleId = :roleId") })
public class FunctionRoleAuthSet implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private FunctionRoleAuthSetPK pk;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    // bi-directional many-to-one association to FunctionRole
    @ManyToOne
    @JoinColumn(name = "ROLE_ID", insertable = false, updatable = false)
    private FunctionRole functionRole;

    // bi-directional many-to-one association to FunctionAuthSet
    @ManyToOne
    @JoinColumn(name = "AUTH_SET_ID", insertable = false, updatable = false)
    private FunctionAuthSet functionAuthSet;

    public FunctionRoleAuthSet() {
    }

    @Override
    public String toString() {
        return "FunctionRoleAuthSet [id=" + pk + ", createDate=" + createDate
                + ", createUserId=" + createUserId + "]";
    }

    public FunctionRoleAuthSetPK getPk() {
        return pk;
    }

    public void setPk(FunctionRoleAuthSetPK pk) {
        this.pk = pk;
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

    public FunctionRole getFunctionRole() {
        return functionRole;
    }

    public void setFunctionRole(FunctionRole functionRole) {
        this.functionRole = functionRole;
    }

    public FunctionAuthSet getFunctionAuthSet() {
        return functionAuthSet;
    }

    public void setFunctionAuthSet(FunctionAuthSet functionAuthSet) {
        this.functionAuthSet = functionAuthSet;
    }


}