package com.bookcode.worksprotectingsystem.entity;

public class FingerPrint {
    private boolean fingerprintstatus; //指纹状态:是否添加
    private String fingerprintdate;    //指纹添加日期
    private String fingerprintvalue;   //指纹数据

    public boolean isFingerprintstatus() {
        return fingerprintstatus;
    }

    public void setFingerprintstatus(boolean fingerprintstatus) {
        this.fingerprintstatus = fingerprintstatus;
    }

    public String getFingerprintdate() {
        return fingerprintdate;
    }

    public void setFingerprintdate(String fingerprintdate) {
        this.fingerprintdate = fingerprintdate;
    }

    public String getFingerprintvalue() {
        return fingerprintvalue;
    }

    public void setFingerprintvalue(String fingerprintvalue) {
        this.fingerprintvalue = fingerprintvalue;
    }
}
