package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the FUNCTION_ROLE_AUTH_SETS database table.
 * 
 */
@Embeddable
public class FunctionRoleAuthSetPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "ROLE_ID", nullable = false)
    private String roleId;

    @Column(name = "AUTH_SET_ID", nullable = false)
    private String authSetId;

    public FunctionRoleAuthSetPK() {
    }

    public FunctionRoleAuthSetPK(String roleId, String authSetId) {
        this.setRoleId(roleId);
        this.setAuthSetId(authSetId);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FunctionRoleAuthSetPK)) {
            return false;
        }
        FunctionRoleAuthSetPK castOther = (FunctionRoleAuthSetPK) other;
        return this.roleId.equals(castOther.roleId)
                && this.authSetId.equals(castOther.authSetId);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.roleId.hashCode();
        hash = hash * prime + this.authSetId.hashCode();

        return hash;
    }

    @Override
    public String toString() {
        return "FunctionRoleAuthSetPK [roleId=" + roleId + ", authSetId="
                + authSetId + "]";
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getAuthSetId() {
        return authSetId;
    }

    public void setAuthSetId(String authSetId) {
        this.authSetId = authSetId;
    }

}