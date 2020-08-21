package com.example.projectcuoiki.model;

public class StudentMarkDetail {
    //Mã chi tiết điểm danh
    private String STT;
    private String maSV;
    private String lyDoNghi;
    private String ngayDiemDanh;
    private String tietDiemDanh;

    public StudentMarkDetail(String STT, String maSV, String lyDoNghi, String ngayDiemDanh, String tietDiemDanh) {
        this.STT = STT;
        this.maSV = maSV;
        this.lyDoNghi = lyDoNghi;
        this.ngayDiemDanh = ngayDiemDanh;
        this.tietDiemDanh = tietDiemDanh;
    }

    public String getSTT() {
        return STT;
    }

    public void setSTT(String STT) {
        this.STT = STT;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getLyDoNghi() {
        return lyDoNghi;
    }

    public void setLyDoNghi(String lyDoNghi) {
        this.lyDoNghi = lyDoNghi;
    }

    public String getNgayDiemDanh() {
        return ngayDiemDanh;
    }

    public void setNgayDiemDanh(String ngayDiemDanh) {
        this.ngayDiemDanh = ngayDiemDanh;
    }

    public String getTietDiemDanh() {
        return tietDiemDanh;
    }

    public void setTietDiemDanh(String tietDiemDanh) {
        this.tietDiemDanh = tietDiemDanh;
    }
}
