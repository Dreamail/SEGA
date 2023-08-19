package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the AUTH_DENIED_GAME_BILLS database table.
 * 
 */
@Embeddable
public class AuthDeniedGameBillPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "GAME_ID", nullable = false)
    private String gameId;

    @Column(name = "BILL_CODE", nullable = false)
    private String billCode;

    public AuthDeniedGameBillPK() {
    }

    public AuthDeniedGameBillPK(String gameId, String billCode) {
        this.gameId = gameId;
        this.billCode = billCode;
    }

    @Override
    public String toString() {
        return "AuthDeniedGameBillPK [gameId=" + gameId + ", billCode="
                + billCode + "]";
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getBillCode() {
        return this.billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AuthDeniedGameBillPK)) {
            return false;
        }
        AuthDeniedGameBillPK castOther = (AuthDeniedGameBillPK) other;
        return this.gameId.equals(castOther.gameId)
                && this.billCode.equals(castOther.billCode);

    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.gameId.hashCode();
        hash = hash * prime + this.billCode.hashCode();

        return hash;
    }
}