package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the FUNCTION_AUTH_SET_AUTHS database table.
 * 
 */
@Embeddable
public class FunctionAuthSetAuthPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "AUTH_SET_ID", nullable = false)
    private String authSetId;

    @Column(name = "AUTHORITY_ID", nullable = false)
    private String authorityId;

    public FunctionAuthSetAuthPK() {
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FunctionAuthSetAuthPK)) {
            return false;
        }
        FunctionAuthSetAuthPK castOther = (FunctionAuthSetAuthPK) other;
        return this.authSetId.equals(castOther.authSetId)
                && this.authorityId.equals(castOther.authorityId);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.authSetId.hashCode();
        hash = hash * prime + this.authorityId.hashCode();

        return hash;
    }

    @Override
    public String toString() {
        return "FunctionAuthSetAuthPK [authSetId=" + authSetId
                + ", authorityId="
                + authorityId + "]";
    }

    public String getAuthSetId() {
        return authSetId;
    }

    public void setAuthSetId(String authSetId) {
        this.authSetId = authSetId;
    }

    public String getAuthorityId() {
        return this.authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }
}