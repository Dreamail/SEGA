package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the GAME_GROUPS database table.
 * 
 */
@Embeddable
public class GameGroupPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "GAME_ID", nullable = false)
    private String gameId;

    @Column(name = "GROUP_ID", nullable = false)
    private String groupId;

    public GameGroupPK() {
    }

    public GameGroupPK(String gameId, String groupId) {
        this.gameId = gameId;
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "AuthAllowedCompPK [gameId=" + gameId + ", groupId=" + groupId
                + "]";
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GameGroupPK)) {
            return false;
        }
        GameGroupPK castOther = (GameGroupPK) other;
        return this.gameId.equals(castOther.gameId)
                && this.groupId.equals(castOther.groupId);

    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.gameId.hashCode();
        hash = hash * prime + this.groupId.hashCode();

        return hash;
    }
}