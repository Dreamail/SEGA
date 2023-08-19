package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the AUTH_ALLOWED_PLACES database table.
 * 
 */
@Entity
@Table(name = "AUTH_ALLOWED_PLACES")
@NamedQueries({ @NamedQuery(name = "findAuthAllowedPlaceAll", query = "SELECT a FROM AuthAllowedPlace a JOIN FETCH a.place") })
public class AuthAllowedPlace implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AuthAllowedPlacePK pk;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    // bi-directional many-to-one association to Game
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GAME_ID", insertable = false, updatable = false)
    private Game game;

    // bi-directional many-to-one association to Place
    @ManyToOne
    @JoinColumn(name = "ALLNET_ID", insertable = false, updatable = false)
    private Place place;

    public AuthAllowedPlace() {
    }

    @Override
    public String toString() {
        return "AuthAllowedPlace [pk=" + pk + ", createDate=" + createDate
                + ", createUserId=" + createUserId + "]";
    }

    public AuthAllowedPlacePK getPk() {
        return pk;
    }

    public void setPk(AuthAllowedPlacePK pk) {
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

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Place getPlace() {
        return this.place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

}