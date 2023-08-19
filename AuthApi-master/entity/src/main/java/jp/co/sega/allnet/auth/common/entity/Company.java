package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the COMPANIES database table.
 * 
 */
@Entity
@Table(name = "COMPANIES")
@NamedQueries({ @NamedQuery(name = "findAllCompanies", query = "SELECT c FROM Company c ORDER BY c.companyId DESC") })
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "COMPANY_ID", nullable = false)
    private String companyId;

    @Column(name = "COMPANY_NAME", nullable = false)
    private String companyName;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    @Column(name = "UPDATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Column(name = "UPDATE_USER_ID", nullable = false)
    private String updateUserId;

    // bi-directional many-to-one association to User
    @OneToMany(mappedBy = "company")
    private List<User> users;

    public Company() {
    }

    @Override
    public String toString() {
        return "Company [companyId=" + companyId + ", companyName="
                + companyName + ", createDate=" + createDate
                + ", createUserId=" + createUserId + ", updateDate="
                + updateDate + ", updateUserId=" + updateUserId + "]";
    }

    public String getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}