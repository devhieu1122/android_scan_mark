package com.dev_hieu.demo.model;

import androidx.annotation.NonNull;

public class Student {
    //Mã sinh viên
    String maSV;
    //Họ Tên
    String tenSV;
    //Mã lớp
    String maLopHoc;

    public Student(String maSV, String tenSV, String maLopHoc) {
        this.maSV = maSV;
        this.tenSV = tenSV;
        this.maLopHoc = maLopHoc;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getTenSV() {
        return tenSV;
    }

    public void setTenSV(String tenSV) {
        this.tenSV = tenSV;
    }

    public String getMaLopHoc() {
        return maLopHoc;
    }

    public void setMaLopHoc(String maLopHoc) {
        this.maLopHoc = maLopHoc;
    }

    @NonNull
    @Override
    public String toString() {
        String str = "Student [ id: " + maSV + "\n"
                + "name:    " + tenSV + "\n"
                + "classID: " + maLopHoc + "\n";
        return str;
    }
}
