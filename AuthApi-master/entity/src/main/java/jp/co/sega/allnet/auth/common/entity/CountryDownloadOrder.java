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
import javax.persistence.Version;

/**
 * The persistent class for the COUNTRY_DOWNLOAD_ORDERS database table.
 * 
 */
@Entity
@Table(name = "COUNTRY_DOWNLOAD_ORDERS")
@NamedQueries({
        @NamedQuery(name = "deleteCountryDownloadOrder", query = "DELETE FROM CountryDownloadOrder d WHERE d.pk.gameId = :gameId AND d.pk.gameVer = :gameVer AND d.pk.countryCode = :countryCode"),
        @NamedQuery(name = "findCountryDownloadOrderByGameId", query = "SELECT c FROM CountryDownloadOrder c WHERE c.pk.gameId = :gameId ORDER BY c.pk.gameVer, c.pk.countryCode") })
public class CountryDownloadOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private CountryDownloadOrderPK pk;

    private String uri;

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

    public CountryDownloadOrder() {
    }

    @Override
    public String toString() {
        return "CountryDownloadOrder [pk=" + pk + ", uri=" + uri
                + ", createDate=" + createDate + ", createUserId="
                + createUserId + ", updateDate=" + updateDate
                + ", updateUserId=" + updateUserId + "]";
    }

    public CountryDownloadOrderPK getPk() {
        return pk;
    }

    public void setPk(CountryDownloadOrderPK pk) {
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

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUserId() {
        return this.updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}