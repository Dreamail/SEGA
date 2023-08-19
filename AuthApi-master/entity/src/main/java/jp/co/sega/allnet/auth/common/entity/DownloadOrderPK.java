package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the DOWNLOAD_ORDERS database table.
 * 
 */
@Embeddable
public class DownloadOrderPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "GAME_ID", nullable = false)
    private String gameId;

    @Column(name = "GAME_VER", nullable = false)
    private String gameVer;

    public DownloadOrderPK() {
    }

    public DownloadOrderPK(String gameId, String gameVer) {
        this.gameId = gameId;
        this.gameVer = gameVer;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DownloadOrderPK)) {
            return false;
        }
        DownloadOrderPK castOther = (DownloadOrderPK) other;
        return this.gameId.equals(castOther.gameId)
                && this.gameVer.equals(castOther.gameVer);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.gameId.hashCode();
        hash = hash * prime + this.gameVer.hashCode();

        return hash;
    }

    @Override
    public String toString() {
        return "DownloadOrderPK [gameId=" + gameId + ", gameVer=" + gameVer
                + "]";
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameVer() {
        return this.gameVer;
    }

    public void setGameVer(String gameVer) {
        this.gameVer = gameVer;
    }
}