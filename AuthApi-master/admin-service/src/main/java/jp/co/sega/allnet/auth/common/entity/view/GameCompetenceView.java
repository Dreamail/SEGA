/**
 * 
 */
package jp.co.sega.allnet.auth.common.entity.view;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;

/**
 * @author NakanoY
 * 
 */
@Entity
@SqlResultSetMappings({ @SqlResultSetMapping(name = "gameCompetenceMapping", entities = { @EntityResult(entityClass = GameCompetenceView.class) }) })
public class GameCompetenceView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "GAME_ID")
    private String gameId;

    @Column(name = "AUTHORITY_TYPE")
    private String authorityType;

    private String title;

    /**
     * @return gameId
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * @param gameId
     *            セットする gameId
     */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    /**
     * @return authorityType
     */
    public String getAuthorityType() {
        return authorityType;
    }

    /**
     * @param authorityType
     *            セットする authorityType
     */
    public void setAuthorityType(String authorityType) {
        this.authorityType = authorityType;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            セットする title
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
