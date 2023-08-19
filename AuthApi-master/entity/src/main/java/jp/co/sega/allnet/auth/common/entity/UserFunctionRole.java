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
 * The persistent class for the USER_FUNCTION_ROLES database table.
 * 
 */
@Entity
@Table(name = "USER_FUNCTION_ROLES")
@NamedQueries({
        @NamedQuery(name = "findUserFunctionRolesByRoles", query = "SELECT u FROM UserFunctionRole u WHERE u.pk.userId = :userId AND u.pk.roleId IN (:roleIds)"),
        @NamedQuery(name = "findUserFunctionRolesByRoleId", query = "SELECT u FROM UserFunctionRole u WHERE u.pk.roleId = :roleId") })
public class UserFunctionRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UserFunctionRolePK pk;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    // bi-directional many-to-one association to FunctionRole
    @ManyToOne
    @JoinColumn(name = "ROLE_ID", insertable = false, updatable = false, nullable = false)
    private FunctionRole functionRole;

    // bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false, nullable = false)
    private User user;

    public UserFunctionRole() {
    }

    @Override
    public String toString() {
        return "UserFunctionRole [pk=" + pk + ", createDate=" + createDate
                + ", createUserId=" + createUserId + "]";
    }

    public UserFunctionRolePK getPk() {
        return pk;
    }

    public void setPk(UserFunctionRolePK pk) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}