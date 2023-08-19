package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the GAME_COMPETENCES database table.
 * 
 */
@Entity
@Table(name = "GAME_COMPETENCES")
@NamedQueries({
        @NamedQuery(name = "findReadCompetentGameIds", query = "SELECT gc.pk.gameId FROM GameCompetence gc WHERE gc.pk.userId = :userId AND gc.authorityType IN ('1','2')"),
        @NamedQuery(name = "findWriteCompetentGameIds", query = "SELECT gc.pk.gameId FROM GameCompetence gc WHERE gc.pk.userId = :userId AND gc.authorityType = '2'"),
        @NamedQuery(name = "findGameCompetenceByUserId", query = "SELECT gc FROM GameCompetence gc WHERE gc.pk.userId = :userId") })
@NamedNativeQueries({
        @NamedNativeQuery(name = "findGameCompetenceView", query = "SELECT g.game_id, gc.authority_type, g.title FROM games g LEFT OUTER JOIN game_competences gc ON g.game_id = gc.game_id AND gc.user_id = :userId ORDER BY g.title", resultSetMapping = "gameCompetenceMapping"),
        @NamedNativeQuery(name = "findGameCompetenceViewByGameIds", query = "SELECT g.game_id, gc.authority_type, g.title FROM games g LEFT OUTER JOIN game_competences gc ON g.game_id = gc.game_id AND gc.user_id = :userId WHERE g.game_id IN (:gameIds) ORDER BY g.title", resultSetMapping = "gameCompetenceMapping") })
public class GameCompetence implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String AUTHORITY_TYPE_NO_COMPETENCE = "0";

    public static final String AUTHORITY_TYPE_READ = "1";

    public static final String AUTHORITY_TYPE_READ_WRITE = "2";

    @EmbeddedId
    private GameCompetencePK pk;

    @Column(name = "AUTHORITY_TYPE", nullable = false)
    private String authorityType;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    @Column(name = "UPDATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Version
    private Date updateDate;

    @Column(name = "UPDATE_USER_ID", nullable = false)
    private String updateUserId;

    // bi-directional many-to-one association to Game
    @ManyToOne
    @JoinColumn(name = "GAME_ID", insertable = false, updatable = false)
    private Game game;

    // bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private User user;

    public GameCompetence() {
    }

    @Override
    public String toString() {
        return "GameCompetence [pk=" + pk + ", authorityType=" + authorityType
                + ", createDate=" + createDate + ", createUserId="
                + createUserId + ", updateDate=" + updateDate
                + ", updateUserId=" + updateUserId + "]";
    }

    public GameCompetencePK getPk() {
        return pk;
    }

    public void setPk(GameCompetencePK pk) {
        this.pk = pk;
    }

    public String getAuthorityType() {
        return authorityType;
    }

    public void setAuthorityType(String authorityType) {
        this.authorityType = authorityType;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}