package com.dev_hieu.demo.database;

public class Init_DB {

    /*
     * Author: Dev Hieu
     * Date: 17/12/2019
     */
    public static final String DATABASE_NAME = "students.db";

    //Bảng Sinh viên
    public static final String TABLE_STUDENT = "student";
    //Columns
    public static final String ST_MA_SV = "maSV";
    public static final String ST_TEN_SV = "tenSV";
    public static final String ST_MA_LOP_HOC = "maLopHoc";

    // Bảng Môn học
    public static final String TABLE_SUBJECT = "subject";
    //Columns
    public static final String SUB_MA_MH = "maMH";
    public static final String SUB_TEN_MH = "tenMH";
    public static final String SUB_MA_GV = "maGV";
    public static final String SUB_PHONG_HOC = "phongHoc";
    public static final String SUB_SO_TIET = "soTiet";

    public static final String TABLE_STUDENT_MARK = "student_mark";
    //Bảng Điểm danh
    //Columns
    public static final String ST_MAR_MA_SV = "maSV";
    public static final String ST_MAR_MA_MH = "maMH";
    public static final String ST_MAR_ST_DI_HOC = "soTietDiHoc";
    public static final String ST_MAR_ST_Vang = "soTietVang";

    //Bảng Chi tiết điểm danh
    public static final String TABLE_STUDENT_MARK_DETAIL = "student_mark_detail";
    //Columns
    public static final String ST_MAR_DETAIL_STT = "STT";
    public static final String ST_MAR_DETAIL_MA_SV = "maSV";
    public static final String ST_MAR_DETAIL_LY_DO = "lyDoNghi";
    public static final String ST_MAR_DETAIL_NGAY = "ngayDiemDanh";
    public static final String ST_MAR_DETAIL_TIET = "tietDiemDanh";

    //INFORM
    public static final String SUCCESSFUL = "Successful";
    public static final String CREATE_SUCCESSFUL = "Update Successful";
    public static final String INSERT_SUCCESSFUL = "Insert Successful";
    public static final String DELETE_SUCCESSFUL = "Delete Successful";
    public static final String ADD_SUCCESSFUL = "Add Successful";
    public static final String LOAD_SUCCESSFUL = "Load Successful";
    public static final String UPDATE_SUCCESSFUL = "Update Successful";

    public static final String UPDATE_ERR = "Update Error";
    public static final String CREATE_ERR = "Create Error";
    public static final String INSERT_ERR = "Insert Error";
    public static final String DELETE_ERR = "Delete Error";
    public static final String UPLOAD_ERR = "Upload Error";
    public static final String ADD_ERR = "Add Error";
}
