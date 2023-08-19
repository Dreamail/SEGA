package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the INFORMATIONS database table.
 * 
 */
@Entity
@Table(name = "INFORMATIONS")
@NamedQueries({
        @NamedQuery(name = "findAllInformations", query = "SELECT i FROM Information i ORDER BY i.publishDate DESC"),
        @NamedQuery(name = "countAllInformations", query = "SELECT COUNT(i) FROM Information i") })
public class Information implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ARTICLE_ID", nullable = false)
    private long articleId;

    @Column(name = "PUBLISH_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "BODY", nullable = false)
    private String body;

    @Column(name = "CATEGORY")
    private BigDecimal category;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

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

    public Information() {
    }

    @Override
    public String toString() {
        return "Information [articleId=" + articleId + ", publishDate="
                + publishDate + ", title=" + title + ", body=" + body
                + ", category=" + category + ", userId=" + userId
                + ", createDate=" + createDate + ", createUserId="
                + createUserId + ", updateDate=" + updateDate
                + ", updateUserId=" + updateUserId + "]";
    }

    /**
     * @return the articleId
     */
    public long getArticleId() {
        return articleId;
    }

    /**
     * @param articleId
     *            the articleId to set
     */
    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    /**
     * @return the publishDate
     */
    public Date getPublishDate() {
        return publishDate;
    }

    /**
     * @param publishDate
     *            the publishDate to set
     */
    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body
     *            the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the category
     */
    public BigDecimal getCategory() {
        return category;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(BigDecimal category) {
        this.category = category;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     *            the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the createUserId
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * @param createUserId
     *            the createUserId to set
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     *            the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the updateUserId
     */
    public String getUpdateUserId() {
        return updateUserId;
    }

    /**
     * @param updateUserId
     *            the updateUserId to set
     */
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

}