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
 * The persistent class for the FUNCTION_AUTH_SET_AUTHS database table.
 * 
 */
@Entity
@Table(name = "FUNCTION_AUTH_SET_AUTHS")
public class FunctionAuthSetAuth implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private FunctionAuthSetAuthPK pk;

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
    @JoinColumn(name = "AUTH_SET_ID", insertable = false, updatable = false)
    private FunctionAuthSet functionAuthSet;

    public FunctionAuthSetAuth() {
    }

    @Override
    public String toString() {
        return "FunctionAuthSet [id=" + pk + ", createDate=" + createDate
                + ", createUserId=" + createUserId + "]";
    }

    public FunctionAuthSetAuthPK getPk() {
        return pk;
    }

    public void setPk(FunctionAuthSetAuthPK pk) {
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

    public FunctionAuthSet getFunctionAuthSet() {
        return this.functionAuthSet;
    }

    public void setFunctionAuthSet(FunctionAuthSet functionAuthSet) {
        this.functionAuthSet = functionAuthSet;
    }


}