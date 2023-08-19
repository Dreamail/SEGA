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
 * The persistent class for the COMPS database table.
 * 
 */
@Entity
@Table(name = "COMPS")
@NamedQueries({ @NamedQuery(name = "findAllComps", query = "SELECT c FROM Comp c") })
public class Comp implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "COMP_CODE", nullable = false)
    private String compCode;

    @Column(name = "name", nullable = false)
    private String name;

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

    // bi-directional many-to-one association to AuthAllowedComp
    @OneToMany(mappedBy = "comp", cascade = CascadeType.REMOVE)
    private List<AuthAllowedComp> authAllowedComps;

    // bi-directional one-to-one association to AuthDeniedComp
    // * this is workaround for hibernate can't fetch one-to-one lazily
    @OneToMany(mappedBy = "comp", cascade = CascadeType.REMOVE)
    private List<AuthDeniedComp> authDeniedComps;

    // bi-directional many-to-one association to AuthDeniedGameComp
    @OneToMany(mappedBy = "comp", cascade = CascadeType.REMOVE)
    private List<AuthDeniedGameComp> authDeniedGameComps;

    // bi-directional one-to-one association to MoveDeniedComp
    // * this is workaround for hibernate can't fetch one-to-one lazily
    @OneToMany(mappedBy = "comp", cascade = CascadeType.REMOVE)
    private List<MoveDeniedComp> moveDeniedComps;

    // bi-directional many-to-one association to MoveDeniedGameComp
    @OneToMany(mappedBy = "comp", cascade = CascadeType.REMOVE)
    private List<MoveDeniedGameComp> moveDeniedGameComps;

    public Comp() {
    }

    @Override
    public String toString() {
        return "Comp [compCode=" + compCode + ", name=" + name
                + ", createDate=" + createDate + ", createUserId="
                + createUserId + ", updateDate=" + updateDate
                + ", updateUserId=" + updateUserId + "]";
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<AuthAllowedComp> getAuthAllowedComps() {
        return this.authAllowedComps;
    }

    public void setAuthAllowedComps(List<AuthAllowedComp> authAllowedComps) {
        this.authAllowedComps = authAllowedComps;
    }

    public List<AuthDeniedComp> getAuthDeniedComps() {
        return authDeniedComps;
    }

    public void setAuthDeniedComps(List<AuthDeniedComp> authDeniedComps) {
        this.authDeniedComps = authDeniedComps;
    }

    public List<AuthDeniedGameComp> getAuthDeniedGameComps() {
        return this.authDeniedGameComps;
    }

    public void setAuthDeniedGameComps(
            List<AuthDeniedGameComp> authDeniedGameComps) {
        this.authDeniedGameComps = authDeniedGameComps;
    }

    public List<MoveDeniedComp> getMoveDeniedComps() {
        return moveDeniedComps;
    }

    public void setMoveDeniedComps(List<MoveDeniedComp> moveDeniedComps) {
        this.moveDeniedComps = moveDeniedComps;
    }

    public List<MoveDeniedGameComp> getMoveDeniedGameComps() {
        return this.moveDeniedGameComps;
    }

    public void setMoveDeniedGameComps(
            List<MoveDeniedGameComp> moveDeniedGameComps) {
        this.moveDeniedGameComps = moveDeniedGameComps;
    }

}