package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Pattern;

/**
 * The persistent class for the GAMES database table.
 * 
 */
@Entity
@Table(name = "GAMES")
@NamedQueries({
        @NamedQuery(name = "findGames", query = "SELECT g FROM Game g WHERE g.gameId IN (:gameIds)"),
        @NamedQuery(name = "findAllGameIds", query = "SELECT g.gameId FROM Game g"),
        @NamedQuery(name = "findGroupGameIds", query = "SELECT g.gameId FROM Game g JOIN g.gameGroups gg JOIN gg.group gr JOIN gr.userGroups ug WHERE ug.userId = :userId ORDER BY g.gameId") })
@NamedNativeQueries({
        @NamedNativeQuery(name = "findReadCompetentGamesForSelector", query = "SELECT key, value "
                + "FROM ( "
                + "    SELECT g.game_id as key, g.game_id||':'||g.title as value, g.title "
                + "    FROM games g "
                + "    INNER JOIN game_competences gc ON g.game_id = gc.game_id "
                + "    WHERE gc.user_id = :userId "
                + "    AND gc.authority_type IN ('1','2') "
                + ") t "
                + "ORDER BY title", resultSetMapping = "stringKeyValuePairMapping"),
        @NamedNativeQuery(name = "findWriteCompetentGamesForSelector", query = "SELECT key, value "
                + "FROM ( "
                + "    SELECT g.game_id as key, g.game_id||':'||g.title as value, g.title "
                + "    FROM games g "
                + "    INNER JOIN game_competences gc ON g.game_id = gc.game_id "
                + "    WHERE gc.user_id = :userId "
                + "    AND gc.authority_type = '2' "
                + ") t "
                + "ORDER BY title", resultSetMapping = "stringKeyValuePairMapping"),
        @NamedNativeQuery(name = "findAllGamesForSelector", query = "SELECT key, value "
                + "FROM ( "
                + "    SELECT game_id as key, game_id||':'||title as value, title "
                + "    FROM games "
                + ") t "
                + "ORDER BY title", resultSetMapping = "stringKeyValuePairMapping"),
        @NamedNativeQuery(name = "findGroupGamesForSelector", query = "SELECT key, value "
                + "FROM ( "
                + "    SELECT g.game_id as key, g.game_id||':'||g.title as value, g.title "
                + "    FROM games g "
                + "    INNER JOIN game_groups gg ON g.game_id = gg.game_id "
                + "    INNER JOIN user_groups ug ON gg.group_id = ug.group_id "
                + "    WHERE ug.user_id = :userId "
                + ") t "
                + "ORDER BY title", resultSetMapping = "stringKeyValuePairMapping"),
        @NamedNativeQuery(name = "findReadDownloadOrderGamesForSelector", query = "SELECT key, value "
                + "FROM ( "
                + "    SELECT DISTINCT g.game_id as key, g.game_id||':'||g.title as value, g.title "
                + "    FROM games g "
                + "    INNER JOIN game_competences gc ON g.game_id = gc.game_id "
                + "    WHERE ( "
                + "        EXISTS ( "
                + "            SELECT * "
                + "            FROM download_orders d "
                + "            WHERE g.game_id = d.game_id"
                + "        ) OR EXISTS ( "
                + "            SELECT * "
                + "            FROM country_download_orders cdo "
                + "            WHERE g.game_id = cdo.game_id "
                + "        ) OR EXISTS ( "
                + "            SELECT * "
                + "            FROM machine_download_orders mdo "
                + "            INNER JOIN machines m ON m.serial = mdo.serial "
                + "            WHERE g.game_id = m.game_id "
                + "            AND mdo.uri IS NOT NULL "
                + "         ) "
                + "    ) "
                + "    AND gc.user_id = :userId "
                + "    AND gc.authority_type IN ('1','2') "
                + ") t "
                + "ORDER BY title", resultSetMapping = "stringKeyValuePairMapping"),
        @NamedNativeQuery(name = "findWriteDownloadOrderGamesForSelector", query = "SELECT key, value "
                + "FROM ( "
                + "    SELECT DISTINCT g.game_id as key, g.game_id||':'||g.title as value, g.title "
                + "    FROM games g "
                + "    INNER JOIN game_competences gc ON g.game_id = gc.game_id "
                + "    WHERE ( "
                + "        EXISTS ( "
                + "            SELECT *"
                + "            FROM download_orders d "
                + "            WHERE g.game_id = d.game_id "
                + "        ) OR EXISTS ( "
                + "            SELECT * "
                + "            FROM country_download_orders cdo "
                + "            WHERE g.game_id = cdo.game_id "
                + "        ) OR EXISTS ( "
                + "            SELECT * "
                + "            FROM machine_download_orders mdo "
                + "            INNER JOIN machines m ON m.serial = mdo.serial "
                + "            WHERE g.game_id = m.game_id "
                + "            AND mdo.uri IS NOT NULL "
                + "        ) "
                + "    ) "
                + "    AND gc.user_id = :userId "
                + "    AND gc.authority_type ='2' "
                + ") t "
                + "ORDER BY title", resultSetMapping = "stringKeyValuePairMapping"),
        @NamedNativeQuery(name = "findAllDownloadOrderGamesForSelector", query = "SELECT key, value "
                + "FROM ( "
                + "    SELECT DISTINCT g.game_id as key, g.game_id||':'||g.title as value, g.title "
                + "    FROM games g "
                + "    WHERE EXISTS ( "
                + "        SELECT * "
                + "        FROM download_orders d "
                + "        WHERE g.game_id = d.game_id "
                + "    ) "
                + "    OR EXISTS ( "
                + "        SELECT * "
                + "        FROM country_download_orders cdo "
                + "        WHERE g.game_id = cdo.game_id "
                + "    ) "
                + "    OR EXISTS ( "
                + "        SELECT * "
                + "        FROM machine_download_orders mdo "
                + "        INNER JOIN machines m ON m.serial = mdo.serial "
                + "        WHERE g.game_id = m.game_id "
                + "        AND mdo.uri IS NOT NULL"
                + "    ) "
                + ") t "
                + "ORDER BY title", resultSetMapping = "stringKeyValuePairMapping"),
        @NamedNativeQuery(name = "findGropuDownloadOrderGamesForSelector", query = "SELECT key, value "
                + "FROM ( "
                + "    SELECT DISTINCT g.game_id as key, g.game_id||':'||g.title as value, g.title "
                + "    FROM games g "
                + "    INNER JOIN game_groups gg ON g.game_id = gg.game_id "
                + "    INNER JOIN user_groups ug ON gg.group_id = ug.group_id "
                + "    WHERE ( "
                + "        EXISTS ( "
                + "            SELECT * "
                + "            FROM download_orders d "
                + "            WHERE g.game_id = d.game_id "
                + "        ) OR EXISTS ( "
                + "            SELECT * "
                + "            FROM country_download_orders cdo "
                + "            WHERE g.game_id = cdo.game_id "
                + "        ) OR EXISTS ( "
                + "            SELECT * "
                + "            FROM machine_download_orders mdo "
                + "            INNER JOIN machines m ON m.serial = mdo.serial "
                + "            WHERE g.game_id = m.game_id "
                + "            AND mdo.uri IS NOT NULL "
                + "        ) "
                + "    ) "
                + "    AND ug.user_id = :userId "
                + ") t "
                + "ORDER BY title", resultSetMapping = "stringKeyValuePairMapping"),
        @NamedNativeQuery(name = "findReadDeliverReportGamesForSelector", query = "SELECT key, value "
                + "FROM ( "
                + "    SELECT DISTINCT g.game_id as key, g.game_id||':'||g.title as value, g.title "
                + "    FROM games g "
                + "    INNER JOIN game_competences gc ON g.game_id = gc.game_id "
                + "    WHERE ( "
                + "        EXISTS ( "
                + "            SELECT * "
                + "            FROM app_deliver_reports ad "
                + "            INNER JOIN machines m ON m.serial = ad.serial "
                + "            WHERE g.game_id = m.game_id "
                + "        ) "
                + "        OR EXISTS ( "
                + "            SELECT * "
                + "            FROM opt_deliver_reports od "
                + "            INNER JOIN machines m ON m.serial = od.serial "
                + "            WHERE g.game_id = m.game_id "
                + "        ) "
                + "    ) "
                + "    AND gc.user_id = :userId "
                + "    AND gc.authority_type IN ('1','2') "
                + ") t "
                + "ORDER BY title", resultSetMapping = "stringKeyValuePairMapping"),
        @NamedNativeQuery(name = "findWriteDeliverReportGamesForSelector", query = "SELECT key, value "
                + "FROM ( "
                + "    SELECT DISTINCT g.game_id as key, g.game_id||':'||g.title as value, g.title "
                + "    FROM games g "
                + "    INNER JOIN game_competences gc ON g.game_id = gc.game_id "
                + "    WHERE ( "
                + "        EXISTS ( "
                + "            SELECT * "
                + "            FROM app_deliver_reports ad "
                + "            INNER JOIN machines m ON m.serial = ad.serial "
                + "            WHERE g.game_id = m.game_id "
                + "        ) "
                + "        OR EXISTS ( "
                + "            SELECT * "
                + "            FROM opt_deliver_reports od "
                + "            INNER JOIN machines m ON m.serial = od.serial "
                + "            WHERE g.game_id = m.game_id "
                + "        ) "
                + "    ) "
                + "    AND gc.user_id = :userId "
                + "    AND gc.authority_type = '2' "
                + ") t "
                + "ORDER BY title", resultSetMapping = "stringKeyValuePairMapping"),
        @NamedNativeQuery(name = "findAllDeliverReportGamesForSelector", query = "SELECT key, value "
                + "FROM ( "
                + "    SELECT DISTINCT g.game_id as key, g.game_id||':'||g.title as value, g.title "
                + "    FROM games g "
                + "    WHERE EXISTS ( "
                + "        SELECT * "
                + "        FROM app_deliver_reports ad "
                + "        INNER JOIN machines m ON m.serial = ad.serial "
                + "        WHERE g.game_id = m.game_id "
                + "    ) "
                + "    OR EXISTS ( "
                + "        SELECT * "
                + "        FROM opt_deliver_reports od "
                + "        INNER JOIN machines m ON m.serial = od.serial "
                + "        WHERE g.game_id = m.game_id "
                + "    ) "
                + ") t "
                + "ORDER BY title", resultSetMapping = "stringKeyValuePairMapping"),
        @NamedNativeQuery(name = "findGroupDeliverReportGamesForSelector", query = "SELECT key, value "
                + "FROM ( "
                + "    SELECT DISTINCT g.game_id as key, g.game_id||':'||g.title as value, g.title "
                + "    FROM games g "
                + "    INNER JOIN game_groups gg ON g.game_id = gg.game_id "
                + "    INNER JOIN user_groups ug ON gg.group_id = ug.group_id "
                + "    WHERE ( "
                + "        EXISTS ( "
                + "            SELECT * "
                + "            FROM app_deliver_reports ad "
                + "            INNER JOIN machines m ON m.serial = ad.serial "
                + "            WHERE g.game_id = m.game_id "
                + "        ) "
                + "        OR EXISTS ( "
                + "            SELECT * "
                + "            FROM opt_deliver_reports od "
                + "            INNER JOIN machines m ON m.serial = od.serial "
                + "            WHERE g.game_id = m.game_id "
                + "        ) "
                + "    ) "
                + "    AND ug.user_id = :userId "
                + ") t "
                + "ORDER BY title", resultSetMapping = "stringKeyValuePairMapping") })
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "GAME_ID", nullable = false)
    @Pattern(regexp = "[0-9A-Z]{1,5}", message = "{entity.game.gameId.pattern}")
    private String gameId;

    @Column(name = "TITLE", nullable = true)
    private String title;

    @Column(name = "INVALIDED", nullable = false)
    private char invalided = '0';

    @Column(name = "CREATE_DATE", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false, updatable = false)
    private String createUserId;

    @Column(name = "UPDATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Version
    private Date updateDate;

    @Column(name = "UPDATE_USER_ID", nullable = false)
    private String updateUserId;

    // bi-directional many-to-one association to AuthAllowedComp
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<AuthAllowedComp> authAllowedComps;

    // bi-directional many-to-one association to AuthAllowedPlace
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<AuthAllowedPlace> authAllowedPlaces;

    // bi-directional many-to-one association to TitleApiAccount
    // * this is workaround for hibernate can't fetch one-to-one lazily
    @OneToMany(mappedBy = "game")
    private List<TitleApiAccount> titleApiAccounts;

    // bi-directional many-to-one association to AuthDeniedGameBill
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<AuthDeniedGameBill> authDeniedGameBills;

    // bi-directional many-to-one association to AuthDeniedGameComp
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<AuthDeniedGameComp> authDeniedGameComps;

    // bi-directional many-to-one association to BillOpenAllowedGame
    // * this is workaround for hibernate can't fetch one-to-one lazily
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<BillOpenAllowedGame> billOpenAllowedGames;

    // bi-directional many-to-one association to DownloadOrder
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<DownloadOrder> downloadOrders;

    // bi-directional many-to-one association to GameAttribute
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<GameAttribute> gameAttributes;

    // bi-directional many-to-one association to GameCompetence
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<GameCompetence> gameCompetences;

    // bi-directional many-to-one association to MoveDeniedGame
    // * this is workaround for hibernate can't fetch one-to-one lazily
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<MoveDeniedGame> moveDeniedGames;

    // bi-directional many-to-one association to MoveDeniedGamever
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<MoveDeniedGamever> moveDeniedGamevers;

    // bi-directional many-to-one association to MoveDeniedGameBill
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<MoveDeniedGameBill> moveDeniedGameBills;

    // bi-directional many-to-one association to MoveDeniedGameComp
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<MoveDeniedGameComp> moveDeniedGameComps;

    // bi-directional many-to-one association to OverseasDownloadOrder
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<CountryDownloadOrder> countryDownloadOrders;

    // bi-directional many-to-one association to GameGroup
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<GameGroup> gameGroups;

    public Game() {
    }

    @Override
    public String toString() {
        return "Game [gameId=" + gameId + ", title=" + title + ", invalided="
                + invalided + ", createDate=" + createDate + ", createUserId="
                + createUserId + ", updateDate=" + updateDate
                + ", updateUserId=" + updateUserId + "]";
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isInvalided() {
        return invalided == '1' ? true : false;
    }

    public void setInvalided(char invalided) {
        this.invalided = invalided;
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

    public List<AuthAllowedComp> getAuthAllowedComps() {
        return this.authAllowedComps;
    }

    public void setAuthAllowedComps(List<AuthAllowedComp> authAllowedComps) {
        this.authAllowedComps = authAllowedComps;
    }

    public List<AuthAllowedPlace> getAuthAllowedPlaces() {
        return this.authAllowedPlaces;
    }

    public void setAuthAllowedPlaces(List<AuthAllowedPlace> authAllowedPlaces) {
        this.authAllowedPlaces = authAllowedPlaces;
    }

    public List<AuthDeniedGameBill> getAuthDeniedGameBills() {
        return this.authDeniedGameBills;
    }

    public List<TitleApiAccount> getTitleApiAccounts() {
        return titleApiAccounts;
    }

    public void setTitleApiAccounts(List<TitleApiAccount> titleApiAccounts) {
        this.titleApiAccounts = titleApiAccounts;
    }

    public void setAuthDeniedGameBills(
            List<AuthDeniedGameBill> authDeniedGameBills) {
        this.authDeniedGameBills = authDeniedGameBills;
    }

    public List<AuthDeniedGameComp> getAuthDeniedGameComps() {
        return this.authDeniedGameComps;
    }

    public void setAuthDeniedGameComps(
            List<AuthDeniedGameComp> authDeniedGameComps) {
        this.authDeniedGameComps = authDeniedGameComps;
    }

    public List<BillOpenAllowedGame> getBillOpenAllowedGames() {
        return billOpenAllowedGames;
    }

    public void setBillOpenAllowedGames(
            List<BillOpenAllowedGame> billOpenAllowedGames) {
        this.billOpenAllowedGames = billOpenAllowedGames;
    }

    public List<DownloadOrder> getDownloadOrders() {
        return this.downloadOrders;
    }

    public void setDownloadOrders(List<DownloadOrder> downloadOrders) {
        this.downloadOrders = downloadOrders;
    }

    public List<GameAttribute> getGameAttributes() {
        return this.gameAttributes;
    }

    public void setGameAttributes(List<GameAttribute> gameAttributes) {
        this.gameAttributes = gameAttributes;
    }

    public List<GameCompetence> getGameCompetences() {
        return this.gameCompetences;
    }

    public void setGameCompetences(List<GameCompetence> gameCompetences) {
        this.gameCompetences = gameCompetences;
    }

    public List<MoveDeniedGame> getMoveDeniedGames() {
        return moveDeniedGames;
    }

    public void setMoveDeniedGames(List<MoveDeniedGame> moveDeniedGames) {
        this.moveDeniedGames = moveDeniedGames;
    }

    public List<MoveDeniedGamever> getMoveDeniedGamevers() {
        return this.moveDeniedGamevers;
    }

    public void setMoveDeniedGamevers(
            List<MoveDeniedGamever> moveDeniedGamevers) {
        this.moveDeniedGamevers = moveDeniedGamevers;
    }

    public List<MoveDeniedGameBill> getMoveDeniedGameBills() {
        return this.moveDeniedGameBills;
    }

    public void setMoveDeniedGameBills(
            List<MoveDeniedGameBill> moveDeniedGameBills) {
        this.moveDeniedGameBills = moveDeniedGameBills;
    }

    public List<MoveDeniedGameComp> getMoveDeniedGameComps() {
        return this.moveDeniedGameComps;
    }

    public void setMoveDeniedGameComps(
            List<MoveDeniedGameComp> moveDeniedGameComps) {
        this.moveDeniedGameComps = moveDeniedGameComps;
    }

    public List<CountryDownloadOrder> getCountryDownloadOrders() {
        return countryDownloadOrders;
    }

    public void setCountryDownloadOrders(
            List<CountryDownloadOrder> countryDownloadOrders) {
        this.countryDownloadOrders = countryDownloadOrders;
    }

    public List<GameGroup> getGameGroups() {
        return gameGroups;
    }

    public void setGameGroups(List<GameGroup> gameGroups) {
        this.gameGroups = gameGroups;
    }

}