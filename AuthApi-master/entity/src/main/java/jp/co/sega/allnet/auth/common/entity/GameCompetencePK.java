package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the GAME_COMPETENCES database table.
 * 
 */
@Embeddable
public class GameCompetencePK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "GAME_ID", nullable = false)
    private String gameId;

    public GameCompetencePK() {
    }

    public GameCompetencePK(String gameId, String userId) {
        this.gameId = gameId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GameCompetencePK)) {
            return false;
        }
        GameCompetencePK castOther = (GameCompetencePK) other;
        return this.userId.equals(castOther.userId)
                && this.gameId.equals(castOther.gameId);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.userId.hashCode();
        hash = hash * prime + this.gameId.hashCode();

        return hash;
    }

    @Override
    public String toString() {
        return "GameCompetencePK [userId=" + userId + ", gameId=" + gameId
                + "]";
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}