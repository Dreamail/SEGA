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

/**
 * The persistent class for the FUNCTION_AUTH_SETS database table.
 * 
 */
@Entity
@Table(name = "FUNCTION_AUTH_SETS")
@NamedQueries({ @NamedQuery(name = "findAllAuthSets", query = "SELECT f FROM FunctionAuthSet f ORDER BY f.authSetId") })
public class FunctionAuthSet implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "AUTH_SET_ID", nullable = false)
    private String authSetId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EXPLANATION", nullable = false)
    private String explanation;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    // bi-directional many-to-one association to FunctionAuthSetAuth
    @OneToMany(mappedBy = "functionAuthSet", cascade = CascadeType.REMOVE)
    private List<FunctionAuthSetAuth> functionAuthSetAuths;

    // bi-directional many-to-one association to FunctionRoleAuthSet
    @OneToMany(mappedBy = "functionAuthSet", cascade = CascadeType.REMOVE)
    private List<FunctionRoleAuthSet> functionRoleAuthSets;

    public FunctionAuthSet() {
    }

    @Override
    public String toString() {
        return "FunctionRoleAuthSet [authSetId=" + authSetId + ", name=" + name
                + ", explanation="
                + explanation + ", createDate=" + createDate
                + ", createUserId=" + createUserId + "]";
    }

    public String getAuthSetId() {
        return authSetId;
    }

    public void setAuthSetId(String authSetId) {
        this.authSetId = authSetId;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<FunctionAuthSetAuth> getFunctionAuthSetAuths() {
        return functionAuthSetAuths;
    }

    public void setFunctionAuthSetAuths(
            List<FunctionAuthSetAuth> functionAuthSetAuths) {
        this.functionAuthSetAuths = functionAuthSetAuths;
    }

    public List<FunctionRoleAuthSet> getFunctionRoleAuthSets() {
        return functionRoleAuthSets;
    }

    public void setFunctionRoleAuthSets(
            List<FunctionRoleAuthSet> functionRoleAuthSets) {
        this.functionRoleAuthSets = functionRoleAuthSets;
    }


}