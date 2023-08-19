package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the FUNCTION_ROLE_AUTHORITIES database table.
 * 
 */
@Embeddable
public class FunctionRoleAuthorityPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "ROLE_ID", nullable = false)
    private String roleId;

    @Column(name = "AUTHORITY_ID", nullable = false)
    private String authorityId;

    public FunctionRoleAuthorityPK() {
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FunctionRoleAuthorityPK)) {
            return false;
        }
        FunctionRoleAuthorityPK castOther = (FunctionRoleAuthorityPK) other;
        return this.roleId.equals(castOther.roleId)
                && this.authorityId.equals(castOther.authorityId);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.roleId.hashCode();
        hash = hash * prime + this.authorityId.hashCode();

        return hash;
    }

    @Override
    public String toString() {
        return "FunctionRoleAuthorityPK [roleId=" + roleId + ", authorityId="
                + authorityId + "]";
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getAuthorityId() {
        return this.authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }
}