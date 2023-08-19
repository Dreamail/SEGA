package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the AUTH_ALLOWED_PLACES database table.
 * 
 */
@Embeddable
public class AuthAllowedPlacePK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "GAME_ID", nullable = false)
    private String gameId;

    @Column(name = "ALLNET_ID", nullable = false)
    private long allnetId;

    public AuthAllowedPlacePK() {
    }

    public AuthAllowedPlacePK(String gameId, long allnetId) {
        this.gameId = gameId;
        this.allnetId = allnetId;
    }

    @Override
    public String toString() {
        return "AuthAllowedPlacePK [gameId=" + gameId + ", allnetId="
                + allnetId + "]";
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public long getAllnetId() {
        return this.allnetId;
    }

    public void setAllnetId(long allnetId) {
        this.allnetId = allnetId;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AuthAllowedPlacePK)) {
            return false;
        }
        AuthAllowedPlacePK castOther = (AuthAllowedPlacePK) other;
        return this.gameId.equals(castOther.gameId)
                && (this.allnetId == castOther.allnetId);

    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.gameId.hashCode();
        hash = hash * prime + ((int) (this.allnetId ^ (this.allnetId >>> 32)));

        return hash;
    }
}