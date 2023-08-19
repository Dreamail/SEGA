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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the BILLS database table.
 * 
 */
@Entity
@Table(name = "BILLS")
@NamedQueries({ @NamedQuery(name = "findBillWherePlaceExist", query = "SELECT DISTINCT b FROM Bill b JOIN b.places p WHERE b.billCode = :billCode") })
public class Bill implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BILL_CODE", nullable = false)
    private String billCode;

    @Column(name = "COMP_CODE", nullable = true)
    private String compCode;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    // bi-directional many-to-one association to AuthDeniedBill
    // * this is workaround for hibernate can't fetch one-to-one lazily
    @OneToMany(mappedBy = "bill", cascade = CascadeType.REMOVE)
    private List<AuthDeniedBill> authDeniedBills;

    // bi-directional many-to-one association to AuthDeniedGameBill
    @OneToMany(mappedBy = "bill", cascade = CascadeType.REMOVE)
    private List<AuthDeniedGameBill> authDeniedGameBills;

    // bi-directional many-to-one association to MoveDeniedBill
    // * this is workaround for hibernate can't fetch one-to-one lazily
    @OneToMany(mappedBy = "bill", cascade = CascadeType.REMOVE)
    private List<MoveDeniedBill> moveDeniedBills;

    // bi-directional many-to-one association to MoveDeniedGameBill
    @OneToMany(mappedBy = "bill", cascade = CascadeType.REMOVE)
    private List<MoveDeniedGameBill> moveDeniedGameBills;

    // bi-directional many-to-one association to Place
    @OneToMany(mappedBy = "bill")
    @OrderBy("allnetId")
    private List<Place> places;

    public Bill() {
    }

    @Override
    public String toString() {
        return "Bill [billCode=" + billCode + ", compCode=" + compCode
                + ", createDate=" + createDate + ", createUserId="
                + createUserId + "]";
    }

    public String getBillCode() {
        return this.billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getCompCode() {
        return compCode;
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

    public List<AuthDeniedBill> getAuthDeniedBills() {
        return authDeniedBills;
    }

    public void setAuthDeniedBills(List<AuthDeniedBill> authDeniedBills) {
        this.authDeniedBills = authDeniedBills;
    }

    public List<AuthDeniedGameBill> getAuthDeniedGameBills() {
        return this.authDeniedGameBills;
    }

    public void setAuthDeniedGameBills(
            List<AuthDeniedGameBill> authDeniedGameBills) {
        this.authDeniedGameBills = authDeniedGameBills;
    }

    public List<MoveDeniedBill> getMoveDeniedBills() {
        return moveDeniedBills;
    }

    public void setMoveDeniedBills(List<MoveDeniedBill> moveDeniedBills) {
        this.moveDeniedBills = moveDeniedBills;
    }

    public List<MoveDeniedGameBill> getMoveDeniedGameBills() {
        return this.moveDeniedGameBills;
    }

    public void setMoveDeniedGameBills(
            List<MoveDeniedGameBill> moveDeniedGameBills) {
        this.moveDeniedGameBills = moveDeniedGameBills;
    }

    public List<Place> getPlaces() {
        return this.places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

}