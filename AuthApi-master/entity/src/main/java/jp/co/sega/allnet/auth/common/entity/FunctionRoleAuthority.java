package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the FUNCTION_ROLE_AUTHORITIES database table.
 * 
 */
@Entity
@Table(name = "FUNCTION_ROLE_AUTHORITIES")
public class FunctionRoleAuthority implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private FunctionRoleAuthorityPK pk;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    // bi-directional many-to-one association to FunctionAuthority
    @ManyToOne
    @JoinColumn(name = "AUTHORITY_ID", insertable = false, updatable = false)
    private FunctionAuthority functionAuthority;

    // bi-directional many-to-one association to FunctionRole
    @ManyToOne
    @JoinColumn(name = "ROLE_ID", insertable = false, updatable = false)
    private FunctionRole functionRole;

    public FunctionRoleAuthority() {
    }

    @Override
    public String toString() {
        return "FunctionRoleAuthority [id=" + pk + ", createDate=" + createDate
                + ", createUserId=" + createUserId + "]";
    }

    public FunctionRoleAuthorityPK getPk() {
        return pk;
    }

    public void setPk(FunctionRoleAuthorityPK pk) {
        this.pk = pk;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public FunctionAuthority getFunctionAuthority() {
        return this.functionAuthority;
    }

    public void setFunctionAuthority(FunctionAuthority functionAuthority) {
        this.functionAuthority = functionAuthority;
    }

    public FunctionRole getFunctionRole() {
        return this.functionRole;
    }

    public void setFunctionRole(FunctionRole functionRole) {
        this.functionRole = functionRole;
    }

}