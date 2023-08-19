package jp.co.sega.allnet.auth.common.entity;

import static jp.co.sega.allnet.auth.common.entity.MachineRewrite.REWRITE_UTC_TIME_FORMAT;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the MACHINE_REWRITE_LOGS database table.
 * 
 */
@Entity
@Table(name = "MACHINE_REWRITE_LOGS")
@NamedQueries({
        @NamedQuery(name = "findMachineRewriteLogsBySerial", query = "SELECT mrl FROM MachineRewriteLog mrl WHERE mrl.pk.serial = :serial ORDER BY mrl.pk.createDate DESC"),
        @NamedQuery(name = "countMachineRewriteLogsBySerial", query = "SELECT COUNT(*) FROM MachineRewriteLog mrl WHERE mrl.pk.serial = :serial") })
public class MachineRewriteLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MachineRewriteLogPK pk;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "MONTH")
    private String month;
    
    @Column(name = "DAY")
    private String day;
    
    @Column(name = "HOUR")
    private String hour;
    
    @Column(name = "MINUTE")
    private String minute;
    
    @Column(name = "SECOND")
    private String second;
    
    public MachineRewriteLog() {
    }

    @Override
    public String toString() {
        return "MachineRewriteLog [pk=" + pk + ", year=" + year + ", month=" + month +", day=" + day +
                ", hour=" + hour +", minute=" + minute +", second=" + second + "]";
    }

    /**
     * 設定されているUTC書き換え時刻を{@link Date}で返却する。
     * 
     * @return
     */
    public Date getUtcTimeByDate() {
        DateFormat df = new SimpleDateFormat(REWRITE_UTC_TIME_FORMAT + " z");
        try {
            return df.parse(String.format("%s-%s-%s %s:%s:%s GMT", year, month, day, hour, minute, second));
        } catch (ParseException e) {
            return null;
        }
    }

    public MachineRewriteLogPK getPk() {
        return pk;
    }

    public void setPk(MachineRewriteLogPK pk) {
        this.pk = pk;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

}