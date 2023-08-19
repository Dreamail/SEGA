package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the GAME_GROUPS database table.
 * 
 */
@Entity
@Table(name = "GAME_GROUPS")
public class GameGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private GameGroupPK pk;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    // bi-directional many-to-one association to Game
    @ManyToOne
    @JoinColumn(name = "GAME_ID", insertable = false, updatable = false)
    private Game game;

    // bi-directional many-to-one association to Comp
    @ManyToOne
    @JoinColumn(name = "GROUP_ID", insertable = false, updatable = false)
    private Group group;

    public GameGroup() {
    }

    @Override
    public String toString() {
        return "GameGroup [pk=" + pk + ", createDate=" + createDate
                + ", createUserId=" + createUserId + "]";
    }

    public GameGroupPK getPk() {
        return pk;
    }

    public void setPk(GameGroupPK pk) {
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

}