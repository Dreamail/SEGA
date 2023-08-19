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
 * The persistent class for the AUTH_ALLOWED_COMPS database table.
 * 
 */
@Entity
@Table(name = "AUTH_ALLOWED_COMPS")
@NamedQueries({ @NamedQuery(name = "findAuthAllowedCompAll", query = "SELECT a FROM AuthAllowedComp a JOIN FETCH a.comp c") })
public class AuthAllowedComp implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AuthAllowedCompPK pk;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    // bi-directional many-to-one association to Comp
    @ManyToOne
    @JoinColumn(name = "COMP_CODE", insertable = false, updatable = false)
    private Comp comp;

    // bi-directional many-to-one association to Game
    @ManyToOne
    @JoinColumn(name = "GAME_ID", insertable = false, updatable = false)
    private Game game;

    public AuthAllowedComp() {
    }

    @Override
    public String toString() {
        return "AuthAllowedComp [pk=" + pk + ", createDate=" + createDate
                + ", createUserId=" + createUserId + "]";
    }

    public AuthAllowedCompPK getPk() {
        return this.pk;
    }

    public void setPk(AuthAllowedCompPK pk) {
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

    public Comp getComp() {
        return this.comp;
    }

    public void setComp(Comp comp) {
        this.comp = comp;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}