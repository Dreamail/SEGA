package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the REGION0 database table.
 * 
 */
@Embeddable
public class Region0PK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "COUNTRY_CODE", nullable = false)
    private String countryCode;

    @Column(name = "REGION_ID", nullable = false)
    private BigDecimal regionId;

    public Region0PK() {
    }

    public Region0PK(String countryCode, BigDecimal regionId) {
        this.countryCode = countryCode;
        this.regionId = regionId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Region0PK)) {
            return false;
        }
        Region0PK castOther = (Region0PK) other;
        return this.countryCode.equals(castOther.countryCode)
                && (this.regionId == castOther.regionId);
    
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.countryCode.hashCode();
    
        if (this.regionId == null) {
            return hash;
        }
    
        hash = hash
                * prime
                + ((int) (this.regionId.longValue() ^ (this.regionId
                        .longValue() >>> 32)));
    
        return hash;
    }

    @Override
    public String toString() {
        return "Region0PK [countryCode=" + countryCode + ", regionId="
                + regionId + "]";
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public BigDecimal getRegionId() {
        return regionId;
    }

    public void setRegionId(BigDecimal regionId) {
        this.regionId = regionId;
    }

}