package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the BILL_OPEN_ALLOWED_GAMES database table.
 * 
 */
@Entity
@Table(name = "BILL_OPEN_ALLOWED_GAMES")
public class BillOpenAllowedGame implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "GAME_ID", nullable = false)
    private String gameId;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    // bi-directional one-to-many association to Game
    // * this is workaround for hibernate can't fetch one-to-one lazily
    @ManyToOne
    @JoinColumn(name = "GAME_ID", insertable = false, updatable = false)
    private Game game;

    public BillOpenAllowedGame() {
    }

    @Override
    public String toString() {
        return "BillOpenAllowedGame [gameId=" + gameId + ", createDate="
                + createDate + ", createUserId=" + createUserId + "]";
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
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

}