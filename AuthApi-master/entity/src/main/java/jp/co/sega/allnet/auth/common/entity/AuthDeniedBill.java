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
 * The persistent class for the AUTH_DENIED_BILLS database table.
 * 
 */
@Entity
@Table(name = "AUTH_DENIED_BILLS")
@NamedQueries({ @NamedQuery(name = "findAuthDeniedBillAll", query = "SELECT DISTINCT a FROM AuthDeniedBill a JOIN FETCH a.bill b JOIN FETCH b.places p") })
public class AuthDeniedBill implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BILL_CODE", nullable = false)
    private String billCode;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    // bi-directional one-to-many association to Bill
    // * this is workaround for hibernate can't fetch one-to-one lazily
    @ManyToOne
    @JoinColumn(name = "BILL_CODE", insertable = false, updatable = false)
    private Bill bill;

    public AuthDeniedBill() {
    }

    @Override
    public String toString() {
        return "AuthDeniedBill [billCode=" + billCode + ", createDate="
                + createDate + ", createUserId=" + createUserId + "]";
    }

    public String getBillCode() {
        return this.billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
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

    public Bill getBill() {
        return this.bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

}