package com.example.projectcuoiki.model;

public class Subject {

    //Mã môn học
    private String maMH;
    //Tên môn học
    private String tenMH;

    private String maGV;

    private int soTiet;

    public Subject(String maMH, String tenMH, String maGV, int soTiet) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.maGV = maGV;
        this.soTiet = soTiet;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getTenMH() {
        return tenMH;
    }

    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public int getSoTiet() {
        return soTiet;
    }

    public void setSoTiet(int soTiet) {
        this.soTiet = soTiet;
    }
}
