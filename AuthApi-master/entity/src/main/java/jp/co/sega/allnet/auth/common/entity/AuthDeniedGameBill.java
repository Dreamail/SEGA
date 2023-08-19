package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the AUTH_DENIED_GAME_BILLS database table.
 * 
 */
@Entity
@Table(name = "AUTH_DENIED_GAME_BILLS")
@NamedQueries({ @NamedQuery(name = "findAuthDeniedGameBillAll", query = "SELECT distinct a FROM AuthDeniedGameBill a JOIN FETCH a.bill b JOIN FETCH b.places p") })
public class AuthDeniedGameBill implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AuthDeniedGameBillPK pk;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    // bi-directional many-to-one association to Bill
    @ManyToOne
    @JoinColumn(name = "BILL_CODE", insertable = false, updatable = false)
    private Bill bill;

    // bi-directional many-to-one association to Game
    @ManyToOne
    @JoinColumn(name = "GAME_ID", insertable = false, updatable = false)
    private Game game;

    public AuthDeniedGameBill() {
    }

    @Override
    public String toString() {
        return "AuthDeniedGameBill [pk=" + pk + ", createDate=" + createDate
                + ", createUserId=" + createUserId + "]";
    }

    public AuthDeniedGameBillPK getPk() {
        return pk;
    }

    public void setPk(AuthDeniedGameBillPK pk) {
        this.pk = pk;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Bill getBill() {
        return this.bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}