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
 * The persistent class for the USER_PREFERENCES database table.
 * 
 */
@Entity
@Table(name = "USER_PREFERENCES")
@NamedQueries({ @NamedQuery(name = "findUserPreferences", query = "SELECT u FROM UserPreference u JOIN u.userPrefKey WHERE u.pk.userId = :userId") })
public class UserPreference implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UserPreferencePK pk;

    @Column(name = "VALUE", nullable = false)
    private String value;

    @Column(name = "REMARKS", nullable = true)
    private String remarks;

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

    // bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false, nullable = false)
    private User user;

    // bi-directional many-to-one association to UserPrefKey
    @ManyToOne
    @JoinColumn(name = "PREF_KEY", insertable = false, updatable = false, nullable = false)
    private UserPrefKey userPrefKey;

    public UserPreference() {
    }

    @Override
    public String toString() {
        return "UserPreference [pk=" + pk + ", value=" + value + ", remarks="
                + remarks + ", createDate=" + createDate + ", createUserId="
                + createUserId + ", updateDate=" + updateDate
                + ", updateUserId=" + updateUserId + "]";
    }

    public UserPreferencePK getPk() {
        return pk;
    }

    public void setPk(UserPreferencePK pk) {
        this.pk = pk;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserPrefKey getUserPrefKey() {
        return userPrefKey;
    }

    public void setUserPrefKey(UserPrefKey userPrefKey) {
        this.userPrefKey = userPrefKey;
    }

}