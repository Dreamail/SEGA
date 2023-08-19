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
import javax.persistence.Version;

/**
 * The persistent class for the USERS database table.
 * 
 */
@Entity
@Table(name = "USER_PREF_KEYS")
@NamedQueries({
        @NamedQuery(name = "findAllUserPrefKeys", query = "SELECT k FROM UserPrefKey k"),
        @NamedQuery(name = "findUndefinedUserPrefKeys", query = "SELECT k FROM UserPrefKey k WHERE k.prefKey not in (:definedKeys)") })
public class UserPrefKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PREF_KEY", nullable = false)
    private String prefKey;

    @Column(name = "DATA_TYPE", nullable = false)
    private String dataType;

    @Column(name = "DEFAULT_VALUE")
    private String defaultValue;

    @Column(name = "EXPLANATION", nullable = false)
    private String explanation;

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

    // bi-directional many-to-one association to UserPreference
    @OneToMany(mappedBy = "userPrefKey")
    private List<UserPreference> userPreferences;

    public UserPrefKey() {
    }

    @Override
    public String toString() {
        return "UserPrefKey [prefKey=" + prefKey + ", dataType=" + dataType
                + ", defaultValue=" + defaultValue + ", explanation="
                + explanation + ", createDate=" + createDate
                + ", createUserId=" + createUserId + ", updateDate="
                + updateDate + ", updateUserId=" + updateUserId + "]";
    }

    public String getPrefKey() {
        return prefKey;
    }

    public void setPrefKey(String prefKey) {
        this.prefKey = prefKey;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
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

    public List<UserPreference> getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(List<UserPreference> userPreferences) {
        this.userPreferences = userPreferences;
    }
}