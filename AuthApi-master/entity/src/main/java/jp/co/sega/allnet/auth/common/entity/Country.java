package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the COUNTRIES database table.
 * 
 */
@Entity
@Table(name = "COUNTRIES")
@NamedQueries({ @NamedQuery(name = "findAllCountries", query = "SELECT c FROM Country c ORDER BY c.countryCode") })
public class Country implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "COUNTRY_CODE", nullable = false)
    private String countryCode;

    @Column(name = "COUNTRY_NAME")
    private String countryName;

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

    // bi-directional many-to-one association to GameAttribute
    @OneToMany(mappedBy = "country")
    private List<GameAttribute> gameAttributes;

    // bi-directional many-to-one association to OverseasDownloadOrder
    @OneToMany(mappedBy = "country", cascade = CascadeType.REMOVE)
    private List<CountryDownloadOrder> countryDownloadOrders;

    // bi-directional many-to-one association to Place
    @OneToMany(mappedBy = "country")
    private List<Place> places;

    // bi-directional many-to-one association to Region0
    @OneToMany(mappedBy = "country")
    private List<Region0> region0s;

    public Country() {
    }

    @Override
    public String toString() {
        return "Country [countryCode=" + countryCode + ", countryName="
                + countryName + ", createDate=" + createDate
                + ", createUserId=" + createUserId + ", updateDate="
                + updateDate + ", updateUserId=" + updateUserId + "]";
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
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

    public List<GameAttribute> getGameAttributes() {
        return this.gameAttributes;
    }

    public void setGameAttributes(List<GameAttribute> gameAttributes) {
        this.gameAttributes = gameAttributes;
    }

    public List<CountryDownloadOrder> getCountryDownloadOrders() {
        return this.countryDownloadOrders;
    }

    public void setCountryDownloadOrders(
            List<CountryDownloadOrder> countryDownloadOrders) {
        this.countryDownloadOrders = countryDownloadOrders;
    }

    public List<Place> getPlaces() {
        return this.places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public List<Region0> getRegion0s() {
        return this.region0s;
    }

    public void setRegion0s(List<Region0> region0s) {
        this.region0s = region0s;
    }

}