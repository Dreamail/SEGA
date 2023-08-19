package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the GROUPS database table.
 * 
 */
@Entity
@Table(name = "GROUPS")
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "GROUP_ID", nullable = false)
    private String groupId;

    @Column(name = "EXPLANATION", nullable = true)
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

    // bi-directional many-to-one association to UserGroup
    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE)
    private List<UserGroup> userGroups;

    // bi-directional many-to-one association to GameGroup
    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE)
    private List<GameGroup> gameGroups;

    public Group() {
    }

    @Override
    public String toString() {
        return "Group [groupId=" + groupId + ", explanation=" + explanation
                + ", createDate=" + createDate + ", createUserId="
                + createUserId + ", updateDate=" + updateDate
                + ", updateUserId=" + updateUserId + "]";
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public List<GameGroup> getGameGroups() {
        return gameGroups;
    }

    public void setGameGroups(List<GameGroup> gameGroups) {
        this.gameGroups = gameGroups;
    }

}