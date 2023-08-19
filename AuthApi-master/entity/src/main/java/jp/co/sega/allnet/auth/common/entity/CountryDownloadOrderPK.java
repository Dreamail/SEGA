package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the OVERSEAS_DOWNLOAD_ORDERS database table.
 * 
 */
@Embeddable
public class CountryDownloadOrderPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "GAME_ID", nullable = false)
    private String gameId;

    @Column(name = "GAME_VER", nullable = false)
    private String gameVer;

    @Column(name = "COUNTRY_CODE", nullable = false)
    private String countryCode;

    public CountryDownloadOrderPK() {
    }

    public CountryDownloadOrderPK(String gameId, String gameVer,
            String countryCode) {
        this.gameId = gameId;
        this.gameVer = gameVer;
        this.countryCode = countryCode;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CountryDownloadOrderPK)) {
            return false;
        }
        CountryDownloadOrderPK castOther = (CountryDownloadOrderPK) other;
        return this.gameId.equals(castOther.gameId)
                && this.gameVer.equals(castOther.gameVer)
                && this.countryCode.equals(castOther.countryCode);
    
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.gameId.hashCode();
        hash = hash * prime + this.gameVer.hashCode();
        hash = hash * prime + this.countryCode.hashCode();
    
        return hash;
    }

    @Override
    public String toString() {
        return "CountryDownloadOrderPK [gameId=" + gameId + ", gameVer="
                + gameVer + ", countryCode=" + countryCode + "]";
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

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}