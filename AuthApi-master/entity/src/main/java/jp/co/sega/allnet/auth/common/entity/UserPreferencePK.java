package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the UserPreference database table.
 * 
 */
@Embeddable
public class UserPreferencePK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "PREF_KEY", nullable = false)
    private String prefKey;

    public UserPreferencePK() {
    }

    public UserPreferencePK(String userId, String prefKey) {
        this.userId = userId;
        this.prefKey = prefKey;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UserPreferencePK)) {
            return false;
        }
        UserPreferencePK castOther = (UserPreferencePK) other;
        return this.userId.equals(castOther.userId)
                && this.prefKey.equals(castOther.prefKey);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.userId.hashCode();
        hash = hash * prime + this.prefKey.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return "UserPreferencePK [userId=" + userId + ", prefKey=" + prefKey
                + "]";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrefKey() {
        return prefKey;
    }

    public void setPrefKey(String prefKey) {
        this.prefKey = prefKey;
    }

}