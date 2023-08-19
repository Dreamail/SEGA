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
 * The persistent class for the MOVE_DENIED_GAMEVERS database table.
 * 
 */
@Entity
@Table(name = "MOVE_DENIED_GAMEVERS")
@NamedQueries({ @NamedQuery(name = "findMoveDeniedGameverAll", query = "SELECT m FROM MoveDeniedGamever m ") })
public class MoveDeniedGamever implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MoveDeniedGameverPK pk;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    // bi-directional many-to-one association to Game
    @ManyToOne
    @JoinColumn(name = "GAME_ID", insertable = false, updatable = false)
    private Game game;

    public MoveDeniedGamever() {
    }

    @Override
    public String toString() {
        return "MoveDeniedGamever [pk=" + pk + ", createDate=" + createDate
                + ", createUserId=" + createUserId + "]";
    }

    public MoveDeniedGameverPK getPk() {
        return pk;
    }

    public void setPk(MoveDeniedGameverPK pk) {
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}