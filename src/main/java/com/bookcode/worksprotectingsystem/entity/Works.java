package com.bookcode.worksprotectingsystem.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="work")
public class Works implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long workid;               //作品ID
    private String workname;           //作品名字
    private boolean fingerprintstatus; //指纹状态:是否添加
    private String fingerprintdate;    //指纹添加日期
    private String fingerprintvalue;   //指纹数据
    private boolean watermarkstatus;   //水印状态:是否添加
    private String watermarkdate;      //水印添加日期
    private String watermarkvalue;     //水印数据
    private String ownername;          //作者名字
    private long ownerid;              //作者id
    private String uploadtime;          //上传时间
    private String lastchecktime;       //最后一次审查时间
    private int intervaltime;            //审查周期
    private String[] keywords;            //关键词
    private String workabstract;          //摘要
    private String workfile;         //下载地址

    public String[] getUrllist() {
        return urllist;
    }

    public void setUrllist(String[] urlList) {
        this.urllist = urlList;
    }

    private String[] urllist;
    private String worktype;            //作品类型

    public String getWorktype() {
        return worktype;
    }
    public void setWorktype(String workType) {
        this.worktype = workType;
    }

    public Works() {}
    public void setWorkid(Long workId) {
        this.workid = workId;
    }

    public void setUploadtime(String uploadTime) {
        this.uploadtime = uploadTime;
    }

    public void setWorkname(String workName) {
        this.workname = workName;
    }

    public void setFingerprintstatus(boolean fingerPrintStatus) {
        this.fingerprintstatus = fingerPrintStatus;
    }

    public void setFingerprintdate(String fingerPrintDate) {
        this.fingerprintdate = fingerPrintDate;
    }

    public void setFingerprintvalue(String fingerPrintValue) {
        this.fingerprintvalue = fingerPrintValue;
    }

    public void setWatermarkstatus(boolean waterMarkStatus) {
        this.watermarkstatus = waterMarkStatus;
    }

    public void setWatermarkdate(String waterMarkDate) {
        this.watermarkdate = waterMarkDate;
    }

    public void setWatermarkvalue(String waterMarkValue) {
        this.watermarkvalue = waterMarkValue;
    }

    public void setOwnername(String ownerName) {
        this.ownername = ownerName;
    }

    public void setOwnerid(Long ownerId) {
        this.ownerid = ownerId;
    }

    public void setLastchecktime(String lastCheckTime) {
        this.lastchecktime = lastCheckTime;
    }

    public void setIntervaltime(int intervalTime) {
        this.intervaltime = intervalTime;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public void setWorkabstract(String workAbstract) {
        this.workabstract = workAbstract;
    }

    public void setWorkfile(String workFile) {
        this.workfile = workFile;
    }

    public Long getWorkid() {
        return workid;
    }

    public String getWorkname() {
        return workname;
    }

    public boolean isFingerprintstatus() {
        return fingerprintstatus;
    }

    public String getFingerprintdate() {
        return fingerprintdate;
    }

    public String getFingerprintvalue() {
        return fingerprintvalue;
    }

    public boolean isWatermarkstatus() {
        return watermarkstatus;
    }

    public String getWatermarkdate() {
        return watermarkdate;
    }

    public String getWatermarkvalue() {
        return watermarkvalue;
    }

    public String getOwnername() {
        return ownername;
    }

    public Long getOwnerid() {
        return ownerid;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public String getLastchecktime() {
        return lastchecktime;
    }

    public int getIntervaltime() {
        return intervaltime;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public String getWorkabstract() {
        return workabstract;
    }

    public String getWorkfile() {
        return workfile;
    }

    public void update(Works work) {
        setWorkabstract(work.workabstract);
        setKeywords(work.keywords);
    }
}
