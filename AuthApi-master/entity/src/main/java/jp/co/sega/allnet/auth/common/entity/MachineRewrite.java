package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.StringUtils;

import jp.co.sega.allnet.auth.annotation.StringByteLength;

/**
 * The persistent class for the MACHINE_REWRITES database table.
 * 
 */
@Entity
@Table(name = "MACHINE_REWRITES")
@NamedQueries({
        @NamedQuery(name = "findMachineRewrites", query = "SELECT mr FROM MachineRewrite mr ORDER BY mr.serial"),
        @NamedQuery(name = "countMachineRewrites", query = "SELECT COUNT(mr) FROM MachineRewrite mr") })
public class MachineRewrite implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String REWRITE_UTC_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Id
    @Pattern(regexp = "(A|C)[0-9A-Z]{10}")
    @Column(name = "SERIAL", nullable = false)
    private String serial;

    @Pattern(regexp = "|[0-9]{4}")
    @Column(name = "YEAR")
    private String year;
    
    @Pattern(regexp = "|[0-9]{2}")
    @Column(name = "MONTH")
    private String month;
    
    @Pattern(regexp = "|[0-9]{2}")
    @Column(name = "DAY")
    private String day;
    
    @Pattern(regexp = "|[0-9]{2}")
    @Column(name = "HOUR")
    private String hour;
    
    @Pattern(regexp = "|[0-9]{2}Z")
    @Column(name = "MINUTE")
    private String minute;
    
    @Pattern(regexp = "|[0-9]{2}Z")
    @Column(name = "SECOND")
    private String second;
    
    @StringByteLength(max = 400)
    @Column(name = "REMARK")
    private String remark;

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

    public MachineRewrite() {
    }

    @Override
    public String toString() {
        return "MachineRewrite [serial=" + serial + ", year=" + year
                + ", month=" + month +", day=" + day + ", hour=" 
                + hour +", minute=" + minute +", second=" + second
                + ", remark=" + remark + ", createDate=" + createDate
                + ", createUserId=" + createUserId + ", updateDate="
                + updateDate + ", updateUserId=" + updateUserId + "]";
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

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
    
    public String getYear() {

        if(StringUtils.isEmpty(year)){
            return null;
        }
        
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {

        if(StringUtils.isEmpty(month)){
            return null;
        }
        
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {

        if(StringUtils.isEmpty(day)){
            return null;
        }
        
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {

        if(StringUtils.isEmpty(hour)){
            return null;
        }
        
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {

        if(StringUtils.isEmpty(minute)){
            return null;
        }
        
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {

        if(StringUtils.isEmpty(second)){
            return null;
        }
        
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

}