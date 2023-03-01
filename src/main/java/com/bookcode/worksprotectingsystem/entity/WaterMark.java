package com.bookcode.worksprotectingsystem.entity;

public class WaterMark {
    private boolean watermarkstatus;
    private String watermarkdate;      //水印添加日期
    private String watermarkvalue;     //水印数据

    public boolean isWatermarkstatus() {
        return watermarkstatus;
    }

    public void setWatermarkstatus(boolean watermarkstatus) {
        this.watermarkstatus = watermarkstatus;
    }

    public String getWatermarkdate() {
        return watermarkdate;
    }

    public void setWatermarkdate(String watermarkdate) {
        this.watermarkdate = watermarkdate;
    }

    public String getWatermarkvalue() {
        return watermarkvalue;
    }

    public void setWatermarkvalue(String watermarkvalue) {
        this.watermarkvalue = watermarkvalue;
    }
}
