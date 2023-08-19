package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the USER_FUNCTION_ROLES database table.
 * 
 */
@Embeddable
public class UserFunctionRolePK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "ROLE_ID", nullable = false)
    private String roleId;

    public UserFunctionRolePK() {
    }

    public UserFunctionRolePK(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UserFunctionRolePK)) {
            return false;
        }
        UserFunctionRolePK castOther = (UserFunctionRolePK) other;
        return this.userId.equals(castOther.userId)
                && this.roleId.equals(castOther.roleId);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.userId.hashCode();
        hash = hash * prime + this.roleId.hashCode();

        return hash;
    }

    @Override
    public String toString() {
        return "UserFunctionRolePK [userId=" + userId + ", roleId=" + roleId
                + "]";
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}