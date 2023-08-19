package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the AUTH_DENIED_COMPS database table.
 * 
 */
@Entity
@Table(name = "AUTH_DENIED_COMPS")
@NamedQueries({ @NamedQuery(name = "findAuthDeniedCompAll", query = "SELECT a FROM AuthDeniedComp a JOIN FETCH a.comp c") })
public class AuthDeniedComp implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "COMP_CODE", nullable = false)
    private String compCode;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    // bi-directional one-to-many association to Comp
    // * this is workaround for hibernate can't fetch one-to-one lazily
    @ManyToOne
    @JoinColumn(name = "COMP_CODE", insertable = false, updatable = false)
    private Comp comp;

    public AuthDeniedComp() {
    }

    @Override
    public String toString() {
        return "AuthDeniedComp [compCode=" + compCode + ", createDate="
                + createDate + ", createUserId=" + createUserId + "]";
    }

    public String getCompCode() {
        return this.compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
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

}