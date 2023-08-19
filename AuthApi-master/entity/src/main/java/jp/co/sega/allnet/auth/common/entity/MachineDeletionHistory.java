package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the MACHINE_DELETION_HISTORIES database table.
 * 
 */
@Entity
@Table(name = "MACHINE_DELETION_HISTORIES")
public class MachineDeletionHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MachineDeletionHistoryPK pk;

    @Column(name = "GAME_ID", nullable = false)
    private String gameId;

    @Column(name = "RESERVED_GAME_ID")
    private String reservedGameId;

    @Column(name = "PLACE_NAME")
    private String placeName;

    @Column(name = "ALLNET_ID")
    private BigDecimal allnetId;

    @Column(name = "REASON_ID", nullable = false)
    private BigDecimal reasonId;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    // bi-directional many-to-one association to Game
    @ManyToOne
    @JoinColumn(name = "GAME_ID", nullable = true, insertable = false, updatable = false)
    private Game game;

    // bi-directional many-to-one association to Game
    @ManyToOne
    @JoinColumn(name = "RESERVED_GAME_ID", referencedColumnName = "GAME_ID", nullable = true, insertable = false, updatable = false)
    private Game reservedGame;

    public MachineDeletionHistory() {
    }

    @Override
    public String toString() {
        return "MachineDeletionHistory [pk=" + pk + ", gameId=" + gameId
                + ", reservedGameId=" + reservedGameId + ", placeName="
                + placeName + ", allnetId=" + allnetId + ", resonId="
                + reasonId + ", createUserId=" + createUserId + "]";
    }

    public MachineDeletionHistoryPK getPk() {
        return pk;
    }

    public void setPk(MachineDeletionHistoryPK pk) {
        this.pk = pk;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getReservedGameId() {
        return reservedGameId;
    }

    public void setReservedGameId(String reservedGameId) {
        this.reservedGameId = reservedGameId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public BigDecimal getAllnetId() {
        return allnetId;
    }

    public void setAllnetId(BigDecimal allnetId) {
        this.allnetId = allnetId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public BigDecimal getReasonId() {
        return reasonId;
    }

    public void setReasonId(BigDecimal reasonId) {
        this.reasonId = reasonId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getReservedGame() {
        return reservedGame;
    }

    public void setReservedGame(Game reservedGame) {
        this.reservedGame = reservedGame;
    }

}