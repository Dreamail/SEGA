package jp.co.sega.allnet.auth.common.entity;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.sega.allnet.auth.common.entity.util.CsvWritable;
import jp.co.sega.allnet.auth.common.entity.util.LongScalar;
import jp.co.sega.allnet.auth.common.entity.util.XmlWritable;
import jp.co.sega.allnet.auth.csv.CsvUtils;

import org.apache.commons.lang3.StringUtils;

/**
 * The persistent class for the LOGS database table.
 * 
 */
@Entity(name = "LOGS")
@NamedNativeQueries({
        @NamedNativeQuery(name = "countLogByGameId", query = "SELECT COUNT(1) as value "
                + "FROM logs " + "WHERE game_id = :gameId", resultClass = LongScalar.class),
        @NamedNativeQuery(name = "countLogByGameIdAndQuery", query = "SELECT COUNT(1) as value "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "AND debug_info||'##'||debug_info_place||response LIKE :searchQuery", resultClass = LongScalar.class),
        @NamedNativeQuery(name = "countLogByGameIdAndSerial", query = "SELECT COUNT(1) as value "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "AND serial = :serial", resultClass = LongScalar.class),
        @NamedNativeQuery(name = "countLogByGameIdSerialAndQuery", query = "SELECT COUNT(1) as value "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "AND serial = :serial "
                + "AND debug_info||'##'||debug_info_place||response LIKE :searchQuery", resultClass = LongScalar.class),
        @NamedNativeQuery(name = "countLogByGameIdAndAccessDate", query = "SELECT COUNT(1) as value "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "AND (access_date >= :startDate AND access_date < :endDate)", resultClass = LongScalar.class),
        @NamedNativeQuery(name = "countLogByGameIdAccessDateAndQuery", query = "SELECT COUNT(1) as value "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "AND (access_date >= :startDate AND access_date < :endDate) "
                + "AND debug_info||'##'||debug_info_place||response LIKE :searchQuery", resultClass = LongScalar.class),
        @NamedNativeQuery(name = "countLogByGameIdSerialAndAccessDate", query = "SELECT COUNT(1) as value "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "AND (access_date >= :startDate AND access_date < :endDate) "
                + "AND serial = :serial", resultClass = LongScalar.class),
        @NamedNativeQuery(name = "countLogByGameIdSerialAccessDateAndQuery", query = "SELECT COUNT(1) as value "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "AND (access_date >= :startDate AND access_date < :endDate) "
                + "AND serial = :serial "
                + "AND debug_info||'##'||debug_info_place||response LIKE :searchQuery", resultClass = LongScalar.class),
        @NamedNativeQuery(name = "findLogViewByGameId", query = "SELECT ROW_NUMBER() OVER(ORDER BY serial, access_date) AS dummyId, serial, access_date as accessDate, debug_info as debugInfo, debug_info_place as debugInfoPlace, response "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "ORDER BY serial, access_date", resultSetMapping = "logViewMapping"),
        @NamedNativeQuery(name = "findLogViewByGameIdAndQuery", query = "SELECT ROW_NUMBER() OVER(ORDER BY serial, access_date) AS dummyId, serial, access_date as accessDate, debug_info as debugInfo, debug_info_place as debugInfoPlace, response "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "AND debug_info||'##'||debug_info_place||response LIKE :searchQuery "
                + "ORDER BY serial, access_date", resultSetMapping = "logViewMapping"),
        @NamedNativeQuery(name = "findLogViewByGameIdAndSerial", query = "SELECT ROW_NUMBER() OVER(ORDER BY serial, access_date) AS dummyId, serial, access_date as accessDate, debug_info as debugInfo, debug_info_place as debugInfoPlace, response "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "AND serial = :serial " + "ORDER BY serial, access_date", resultSetMapping = "logViewMapping"),
        @NamedNativeQuery(name = "findLogViewByGameIdSerialAndQuery", query = "SELECT ROW_NUMBER() OVER(ORDER BY serial, access_date) AS dummyId, serial, access_date as accessDate, debug_info as debugInfo, debug_info_place as debugInfoPlace, response "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "AND serial = :serial "
                + "AND debug_info||'##'||debug_info_place||response LIKE :searchQuery "
                + "ORDER BY serial, access_date", resultSetMapping = "logViewMapping"),
        @NamedNativeQuery(name = "findLogViewByGameIdAndAccessDate", query = "SELECT ROW_NUMBER() OVER(ORDER BY serial, access_date) AS dummyId, serial, access_date as accessDate, debug_info as debugInfo, debug_info_place as debugInfoPlace, response "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "AND (access_date >= :startDate AND access_date < :endDate) "
                + "ORDER BY serial, access_date", resultSetMapping = "logViewMapping"),
        @NamedNativeQuery(name = "findLogViewByGameIdAccessDateAndQuery", query = "SELECT ROW_NUMBER() OVER(ORDER BY serial, access_date) AS dummyId, serial, access_date as accessDate, debug_info as debugInfo, debug_info_place as debugInfoPlace, response "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "AND (access_date >= :startDate AND access_date < :endDate) "
                + "AND debug_info||'##'||debug_info_place||response LIKE :searchQuery "
                + "ORDER BY serial, access_date", resultSetMapping = "logViewMapping"),
        @NamedNativeQuery(name = "findLogByGameIdSerialAndAccessDate", query = "SELECT ROW_NUMBER() OVER(ORDER BY serial, access_date) AS dummyId, serial, access_date as accessDate, debug_info as debugInfo, debug_info_place as debugInfoPlace, response "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "AND (access_date >= :startDate AND access_date < :endDate) "
                + "AND serial = :serial " + "ORDER BY serial, access_date", resultSetMapping = "logViewMapping"),
        @NamedNativeQuery(name = "findLogByGameIdSerialAccessDateAndQuery", query = "SELECT ROW_NUMBER() OVER(ORDER BY serial, access_date) AS dummyId, serial, access_date as accessDate, debug_info as debugInfo, debug_info_place as debugInfoPlace, response "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "AND (access_date >= :startDate AND access_date < :endDate) "
                + "AND serial = :serial "
                + "AND debug_info||'##'||debug_info_place||response LIKE :searchQuery "
                + "ORDER BY serial, access_date", resultSetMapping = "logViewMapping"),
        @NamedNativeQuery(name = "findLogsByGameId", query = "SELECT ROW_NUMBER() OVER(ORDER BY l.serial) AS rownum, l.* "
                + "FROM logs l "
                + "INNER JOIN ("
                + "SELECT serial, MAX(access_date) AS max_time "
                + "FROM logs "
                + "WHERE game_id = :gameId "
                + "GROUP BY serial) m "
                + "ON l.serial = m.serial AND l.access_date = m.max_time "
                + "ORDER BY l.serial", resultClass = Log.class),
        @NamedNativeQuery(name = "findLogsByGameIdAndAccessDate", query = "SELECT ROW_NUMBER() OVER(ORDER BY l.serial) AS rownum, l.* "
                + "FROM logs l "
                + "WHERE game_id = :gameId "
                + "AND l.access_date > :date ORDER BY l.serial,l.access_date", resultClass = Log.class) })
public class Log implements Serializable, CsvWritable, XmlWritable {
    private static final long serialVersionUID = 1L;

    @Id
    private String rownum;

    @Column(name = "ACCESS_DATE", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date accessDate;

    @Column(name = "SERIAL", nullable = true)
    private String serial;

    @Column(name = "STAT", nullable = false)
    private BigDecimal stat;

    @Column(name = "CAUSE", nullable = false)
    private BigDecimal cause;

    @Column(name = "GAME_ID", nullable = true)
    private String gameId;

    @Column(name = "GAME_VER", nullable = true)
    private String gameVer;

    @Column(name = "PLACE_ID", nullable = true)
    private String placeId;

    @Column(name = "COUNTRY_CODE", nullable = true)
    private String countryCode;

    @Column(name = "PLACE_IP", nullable = true)
    private String placeIp;

    @Column(name = "GLOBAL_IP", nullable = true)
    private String globalIp;

    @Column(name = "ALLNET_ID", nullable = true)
    private BigDecimal allnetId;

    @Column(name = "REQUEST", nullable = true)
    private String request;

    @Column(name = "RESPONSE", nullable = true)
    private String response;

    @Column(name = "DEBUG_INFO", nullable = true)
    private String debugInfo;

    @Column(name = "DEBUG_INFO_PLACE", nullable = true)
    private String debugInfoPlace;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    public Log() {
    }

    @Override
    public String toString() {
        return "Log [rowId=" + rownum + ", accessDate=" + accessDate
                + ", serial=" + serial + ", stat=" + stat + ", cause=" + cause
                + ", gameId=" + gameId + ", gameVer=" + gameVer + ", placeId="
                + placeId + ", countryCode=" + countryCode + ", placeIp="
                + placeIp + ", globalIp=" + globalIp + ", allnetId=" + allnetId
                + ", request=" + request + ", response=" + response
                + ", debugInfo=" + debugInfo + ", debugInfoPlace="
                + debugInfoPlace + ", createDate=" + createDate
                + ", createUserId=" + createUserId + "]";
    }

    @Override
    public Writer writeCsvLine(Writer writer) throws IOException {
        writer.write(stat.toPlainString());
        writer.write(",");
        writer.write(cause.toPlainString());
        writer.write(",");
        if (!StringUtils.isEmpty(gameId)) {
            writer.write(CsvUtils.escapeForCsv(gameId));
        }
        writer.write(",");
        if (!StringUtils.isEmpty(gameVer)) {
            writer.write(CsvUtils.escapeForCsv(gameVer));
        }
        writer.write(",");
        if (!StringUtils.isEmpty(serial)) {
            writer.write(CsvUtils.escapeForCsv(serial));
        }
        writer.write(",");
        if (!StringUtils.isEmpty(placeId)) {
            writer.write(CsvUtils.escapeForCsv(placeId));
        }
        writer.write(",");
        if (!StringUtils.isEmpty(countryCode)) {
            writer.write(CsvUtils.escapeForCsv(countryCode));
        }
        writer.write(",");
        if (!StringUtils.isEmpty(placeIp)) {
            writer.write(CsvUtils.escapeForCsv(placeIp));
        }
        writer.write(",");
        if (!StringUtils.isEmpty(globalIp)) {
            writer.write(CsvUtils.escapeForCsv(globalIp));
        }
        writer.write(",");
        if (allnetId != null) {
            writer.write(allnetId.toPlainString());
        }
        writer.write(",");
        if (!StringUtils.isEmpty(request)) {
            writer.write(CsvUtils.escapeForCsv(request));
        }
        writer.write(",");

        if (!StringUtils.isEmpty(debugInfo)
                && !StringUtils.isEmpty(debugInfoPlace)
                && !StringUtils.isEmpty(response)) {
            writer.write(CsvUtils.escapeForCsv(debugInfo + "##"
                    + debugInfoPlace + this.response));
        }
        writer.write(",");

        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_YEAR_TO_MSEC);
        writer.write(df.format(accessDate));
        writer.write("\n");

        return writer;
    }

    @Override
    public Writer writeXmlLine(Writer writer) throws IOException {
        writer.write(XML_LINE_HEADER);
        writer.write("\n");

        writer.write(String.format(XML_COLUMN_TEMPLATE, "Number", stat));
        writer.write("\n");
        writer.write(String.format(XML_COLUMN_TEMPLATE, "Number", cause));
        writer.write("\n");

        if (!StringUtils.isEmpty(gameId)) {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", gameId));
        } else {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", ""));
        }

        writer.write("\n");
        if (!StringUtils.isEmpty(gameVer)) {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", gameVer));
        } else {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", ""));
        }
        writer.write("\n");
        if (!StringUtils.isEmpty(serial)) {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", serial));
        } else {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", ""));
        }
        writer.write("\n");
        if (!StringUtils.isEmpty(placeId)) {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", placeId));
        } else {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", ""));
        }
        writer.write("\n");
        if (!StringUtils.isEmpty(countryCode)) {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String",
                    countryCode));
        } else {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", ""));
        }
        writer.write("\n");
        if (!StringUtils.isEmpty(placeIp)) {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", placeIp));
        } else {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", ""));
        }
        writer.write("\n");
        if (!StringUtils.isEmpty(globalIp)) {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", globalIp));
        } else {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", ""));
        }
        writer.write("\n");
        if (allnetId != null) {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "Number", allnetId));
        } else {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "Number", ""));
        }
        writer.write("\n");
        if (!StringUtils.isEmpty(request)) {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", request));
        } else {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", ""));
        }
        writer.write("\n");

        if (!StringUtils.isEmpty(debugInfo)
                && !StringUtils.isEmpty(debugInfoPlace)
                && !StringUtils.isEmpty(response)) {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", debugInfo
                    + "##" + debugInfoPlace + this.response));
        } else {
            writer.write(String.format(XML_COLUMN_TEMPLATE, "String", ""));
        }
        writer.write("\n");

        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_YEAR_TO_MSEC);
        writer.write(String.format(XML_COLUMN_TEMPLATE, "String",
                df.format(accessDate)));
        writer.write("\n");
        writer.write(XML_LINE_FOTTER);
        writer.write("\n");

        return writer;
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Date getAccessDate() {
        return this.accessDate;
    }

    public void setAccessDate(Date accessDate) {
        this.accessDate = accessDate;
    }

    public BigDecimal getCause() {
        return cause;
    }

    public void setCause(BigDecimal cause) {
        this.cause = cause;
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

    public String getDebugInfo() {
        return debugInfo;
    }

    public void setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
    }

    public String getDebugInfoPlace() {
        return debugInfoPlace;
    }

    public void setDebugInfoPlace(String debugInfoPlace) {
        this.debugInfoPlace = debugInfoPlace;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameVer() {
        return gameVer;
    }

    public void setGameVer(String gameVer) {
        this.gameVer = gameVer;
    }

    public String getGlobalIp() {
        return globalIp;
    }

    public void setGlobalIp(String globalIp) {
        this.globalIp = globalIp;
    }

    public String getPlaceIp() {
        return placeIp;
    }

    public void setPlaceIp(String placeIp) {
        this.placeIp = placeIp;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public BigDecimal getStat() {
        return stat;
    }

    public void setStat(BigDecimal stat) {
        this.stat = stat;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public BigDecimal getAllnetId() {
        return allnetId;
    }

    public void setAllnetId(BigDecimal allnetId) {
        this.allnetId = allnetId;
    }

}