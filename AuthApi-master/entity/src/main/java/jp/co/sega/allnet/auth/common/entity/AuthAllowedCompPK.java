package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the AUTH_ALLOWED_COMPS database table.
 * 
 */
@Embeddable
public class AuthAllowedCompPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "GAME_ID", nullable = false)
    private String gameId;

    @Column(name = "COMP_CODE", nullable = false)
    private String compCode;

    public AuthAllowedCompPK() {
    }

    public AuthAllowedCompPK(String gameId, String compCode) {
        this.gameId = gameId;
        this.compCode = compCode;
    }

    @Override
    public String toString() {
        return "AuthAllowedCompPK [gameId=" + gameId + ", compCode=" + compCode
                + "]";
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getCompCode() {
        return this.compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AuthAllowedCompPK)) {
            return false;
        }
        AuthAllowedCompPK castOther = (AuthAllowedCompPK) other;
        return this.gameId.equals(castOther.gameId)
                && this.compCode.equals(castOther.compCode);

    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.gameId.hashCode();
        hash = hash * prime + this.compCode.hashCode();

        return hash;
    }
}