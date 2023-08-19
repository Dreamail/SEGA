package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the FUNCTION_AUTHORITIES database table.
 * 
 */
@Entity
@Table(name = "FUNCTION_AUTHORITIES")
public class FunctionAuthority implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "AUTHORITY_ID", nullable = false)
    private String authorityId;

    private String explanation;

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

    // bi-directional many-to-one association to FunctionRoleAuthority
    @OneToMany(mappedBy = "functionAuthority", cascade = CascadeType.REMOVE)
    private List<FunctionRoleAuthority> functionRoleAuthorities;

    public FunctionAuthority() {
    }

    @Override
    public String toString() {
        return "FunctionAuthority [authorityId=" + authorityId
                + ", explanation=" + explanation + ", createDate=" + createDate
                + ", createUserId=" + createUserId + ", updateDate="
                + updateDate + ", updateUserId=" + updateUserId + "]";
    }

    public String getAuthorityId() {
        return this.authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
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

    public String getExplanation() {
        return this.explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUserId() {
        return this.updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public List<FunctionRoleAuthority> getFunctionRoleAuthorities() {
        return this.functionRoleAuthorities;
    }

    public void setFunctionRoleAuthorities(
            List<FunctionRoleAuthority> functionRoleAuthorities) {
        this.functionRoleAuthorities = functionRoleAuthorities;
    }

}