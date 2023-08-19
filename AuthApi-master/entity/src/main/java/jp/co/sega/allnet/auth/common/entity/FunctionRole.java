package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the FUNCTION_ROLES database table.
 * 
 */
@Entity
@Table(name = "FUNCTION_ROLES")
@NamedQueries({ @NamedQuery(name = "findAllRoles", query = "SELECT f FROM FunctionRole f ORDER BY f.roleId") })
public class FunctionRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ROLE_ID", nullable = false)
    private String roleId;

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
    @OneToMany(mappedBy = "functionRole", cascade = CascadeType.REMOVE)
    private List<FunctionRoleAuthority> functionRoleAuthorities;

    // bi-directional many-to-one association to UserFunctionRole
    @OneToMany(mappedBy = "functionRole", cascade = CascadeType.REMOVE)
    private List<UserFunctionRole> userFunctionRoles;

    // bi-directional many-to-one association to UserFunctionRole
    @OneToMany(mappedBy = "functionRole", cascade = CascadeType.REMOVE)
    private List<FunctionRoleAuthSet> functionRoleAuthSets;

    public FunctionRole() {
    }

    @Override
    public String toString() {
        return "FunctionRole [roleId=" + roleId + ", explanation="
                + explanation + ", createDate=" + createDate
                + ", createUserId=" + createUserId + ", updateDate="
                + updateDate + ", updateUserId=" + updateUserId + "]";
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
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

    public List<UserFunctionRole> getUserFunctionRoles() {
        return this.userFunctionRoles;
    }

    public void setUserFunctionRoles(List<UserFunctionRole> userFunctionRoles) {
        this.userFunctionRoles = userFunctionRoles;
    }

    public List<FunctionRoleAuthSet> getFunctionRoleAuthSets() {
        return this.functionRoleAuthSets;
    }

    public void setFunctionRoleAuthSets(
            List<FunctionRoleAuthSet> functionRoleAuthSets) {
        this.functionRoleAuthSets = functionRoleAuthSets;
    }

}