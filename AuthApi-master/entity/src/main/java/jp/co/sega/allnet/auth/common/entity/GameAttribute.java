package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the GAME_ATTRIBUTES database table.
 * 
 */
@Entity
@Table(name = "GAME_ATTRIBUTES")
@NamedQueries({
        @NamedQuery(name = "findGameAttrByGameIdVer", query = "SELECT g FROM GameAttribute g WHERE g.pk.gameId = :gameId AND g.pk.gameVer = :gameVer ORDER BY g.pk.countryCode"),
        @NamedQuery(name = "findGameAttrByGameId", query = "SELECT g FROM GameAttribute g WHERE g.pk.gameId = :gameId ORDER BY g.pk.gameVer, g.pk.countryCode"),
        @NamedQuery(name = "findDistinctGameVerByGameId", query = "SELECT DISTINCT(g.pk.gameVer) FROM GameAttribute g WHERE g.pk.gameId = :gameId ORDER BY g.pk.gameVer"),
        @NamedQuery(name = "findCountryCodeByGameIdVer", query = "SELECT g.pk.countryCode FROM GameAttribute g WHERE g.pk.gameId = :gameId AND g.pk.gameVer = :gameVer ORDER BY g.pk.countryCode"),
        @NamedQuery(name = "findAllGameAttr", query = "SELECT g FROM GameAttribute g ORDER BY g.pk.gameId, g.pk.gameVer, g.pk.countryCode"),
        @NamedQuery(name = "countGameAttrByGameId", query = "SELECT COUNT(g.pk.gameId) FROM GameAttribute g WHERE g.pk.gameId = :gameId"),
        @NamedQuery(name = "countGameAttrByGameIdVer", query = "SELECT COUNT(g.pk.gameId) FROM GameAttribute g WHERE g.pk.gameId = :gameId AND g.pk.gameVer = :gameVer"),
        @NamedQuery(name = "findGameAttrByGameIdWithPass", query = "SELECT ga FROM GameAttribute ga JOIN FETCH ga.game g JOIN FETCH g.titleApiAccounts t WHERE ga.pk.gameId = :gameId ORDER BY ga.pk.gameVer, ga.pk.countryCode") })
@NamedNativeQueries({ @NamedNativeQuery(name = "findSameOrHigherGameAttrByPk", query = "SELECT * FROM game_attributes WHERE game_id = :gameId AND country_code = :countryCode AND TO_NUMBER(game_ver) >= TO_NUMBER(:gameVer) ORDER BY TO_NUMBER(game_ver)", resultClass = GameAttribute.class) })
public class GameAttribute implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private GameAttributePK pk;

    @Column(nullable = true)
    private String title;

    @Column(nullable = true)
    private String host;

    @Column(nullable = true)
    private String uri;

    @Column(name = "AUTH", nullable = false)
    private BigDecimal auth;

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

    // bi-directional many-to-one association to Country
    @ManyToOne
    @JoinColumn(name = "COUNTRY_CODE", insertable = false, updatable = false)
    private Country country;

    // bi-directional many-to-one association to Game
    @ManyToOne
    @JoinColumn(name = "GAME_ID", insertable = false, updatable = false)
    private Game game;

    public GameAttribute() {
    }

    @Override
    public String toString() {
        return "GameAttribute [pk=" + pk + ", title=" + title + ", host="
                + host + ", uri=" + uri + ", auth=" + auth + ", createDate="
                + createDate + ", createUserId=" + createUserId
                + ", updateDate=" + updateDate + ", updateUserId="
                + updateUserId + "]";
    }

    public GameAttributePK getPk() {
        return pk;
    }

    public void setPk(GameAttributePK pk) {
        this.pk = pk;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public BigDecimal getAuth() {
        return auth;
    }

    public void setAuth(BigDecimal auth) {
        this.auth = auth;
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}