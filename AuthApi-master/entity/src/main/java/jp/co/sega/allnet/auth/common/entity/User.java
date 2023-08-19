package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "USERS")
@NamedQueries({
        @NamedQuery(name = "findAllUsers", query = "SELECT u FROM User u ORDER BY u.userId"),
        @NamedQuery(name = "countUsersWithFilter", query = "SELECT COUNT(u) "
                + "FROM User u "
                + "WHERE (userId like :userId OR :userId IS NULL) "
                + "AND (u.company.companyId = :companyId OR :companyId IS NULL) "
                + "AND (invalided = :invalided OR :invalided IS NULL)"),
        @NamedQuery(name = "findUsersWithFilter", query = "SELECT u "
                + "FROM User u LEFT JOIN FETCH u.userFunctionRoles ufr LEFT JOIN FETCH ufr.functionRole fr "
                + "WHERE (u.userId like :userId OR :userId IS NULL) "
                + "AND (u.company.companyId = :companyId OR :companyId IS NULL) "
                + "AND (invalided = :invalided OR :invalided IS NULL) "
                + "ORDER BY u.userId"),
        @NamedQuery(name = "deleteUser", query = "DELETE FROM User u WHERE u.userId = :userId") })
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "E_MAIL")
    private String emailAddress;

    // bi-directional many-to-one association to Company
    @ManyToOne
    @JoinColumn(name = "COMPANY_ID", nullable = false)
    private Company company;

    @Column(name = "DEFAULT_GAME_OPEN", nullable = false)
    private int defaultGameOpen;

    @Column(name = "INVALIDED", nullable = false)
    private char invalided = '0';

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

    // bi-directional many-to-one association to GameCompetence
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<GameCompetence> gameCompetences;

    // bi-directional many-to-one association to UserFunctionRole
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserFunctionRole> userFunctionRoles;

    // bi-directional many-to-one association to UserGroup
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserGroup> userGroups;

    public User() {
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password
                + ", emailAddress=" + emailAddress + ", company=" + company
                + ", defaultGameOpen=" + defaultGameOpen + ", invalided="
                + invalided + ", createDate=" + createDate + ", createUserId="
                + createUserId + ", updateDate=" + updateDate
                + ", updateUserId=" + updateUserId + "]";
    }

    public void markInvalid() {
        setInvalided('1');
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public int getDefaultGameOpen() {
        return defaultGameOpen;
    }

    public void setDefaultGameOpen(int defaultGameOpen) {
        this.defaultGameOpen = defaultGameOpen;
    }

    public boolean isInvalided() {
        return invalided == '1' ? true : false;
    }

    public void setInvalided(char invalided) {
        this.invalided = invalided;
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

    public List<GameCompetence> getGameCompetences() {
        return gameCompetences;
    }

    public void setGameCompetences(List<GameCompetence> gameCompetences) {
        this.gameCompetences = gameCompetences;
    }

    public List<UserFunctionRole> getUserFunctionRoles() {
        return userFunctionRoles;
    }

    public void setUserFunctionRoles(List<UserFunctionRole> userFunctionRoles) {
        this.userFunctionRoles = userFunctionRoles;
    }

    public enum GameOpenStatus {
        NOT_OPEN(0),
        OPEN(1),
        GROUP_OPEN(2);

        private int value;

        private GameOpenStatus(int v) {
            value = v;
        }

        public int value() {
            return value;
        }

        public static GameOpenStatus fromValue(int v) {
            for (GameOpenStatus g : GameOpenStatus.values()) {
                if (g.value == v) {
                    return g;
                }
            }
            throw new IllegalArgumentException(String.valueOf(v));
        }
    }
}