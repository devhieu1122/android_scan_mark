package com.dev_hieu.demo.model;

import androidx.annotation.NonNull;

public class StudentMark {
    //Mã sinh viên
    private String maSV;
    //Mã lớp học
    private String maMH;

    private int soTietDiHoc;

    private int soTietVang;


    public StudentMark(String maSV, String maMH, int soTietDiHoc, int soTietVang) {
        this.maSV = maSV;
        this.maMH = maMH;
        this.soTietDiHoc = soTietDiHoc;
        this.soTietVang = soTietVang;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public int getSoTietDiHoc() {
        return soTietDiHoc;
    }

    public void setSoTietDiHoc(int soTietDiHoc) {
        this.soTietDiHoc = soTietDiHoc;
    }

    public int getSoTietVang() {
        return soTietVang;
    }

    public void setSoTietVang(int soTietVang) {
        this.soTietVang = soTietVang;
    }

    @NonNull
    @Override
    public String toString() {
        return maSV + " => " + maMH + " => " + soTietDiHoc + " => " + soTietVang;
    }
}
