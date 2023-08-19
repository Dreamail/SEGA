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
 * The persistent class for the MOVE_DENIED_GAME_BILLS database table.
 * 
 */
@Entity
@Table(name = "MOVE_DENIED_GAME_BILLS")
@NamedQueries({ @NamedQuery(name = "findMoveDeniedGameBillAll", query = "SELECT distinct m FROM MoveDeniedGameBill m JOIN FETCH m.bill b JOIN FETCH b.places p") })
public class MoveDeniedGameBill implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MoveDeniedGameBillPK pk;

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

    public MoveDeniedGameBill() {
    }

    @Override
    public String toString() {
        return "MoveDeniedGameBill [pk=" + pk + ", createDate=" + createDate
                + ", createUserId=" + createUserId + "]";
    }

    public MoveDeniedGameBillPK getPk() {
        return pk;
    }

    public void setPk(MoveDeniedGameBillPK pk) {
        this.pk = pk;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}