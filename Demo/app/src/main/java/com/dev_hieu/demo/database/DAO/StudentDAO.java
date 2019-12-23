package com.dev_hieu.demo.database.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dev_hieu.demo.database.DatabaseManager;
import com.dev_hieu.demo.database.Init_DB;
import com.dev_hieu.demo.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    private String TAG = StudentDAO.class.getSimpleName();
    ArrayList<Student> studentList;
    static DatabaseManager dbManager;
    static SQLiteDatabase sqLite;


    boolean insertStudent(Student st) {
        String maSV = st.getMaSV();
        String tenSV = st.getTenSV();
        String maLop = st.getMaLopHoc();
        ContentValues values = new ContentValues();
        values.put(Init_DB.ST_MA_SV, maSV);
        values.put(Init_DB.ST_TEN_SV, tenSV);
        values.put(Init_DB.ST_MA_LOP_HOC, maLop);
        if (checkOpenDB(sqLite)) {
            if (sqLite.insert(Init_DB.TABLE_STUDENT, null, values) != -1) {
                Log.i(TAG, "insertStudent: " + Init_DB.INSERT_SUCCESSFUL);
                return true;
            }
        }
        return false;
    }

    public static Student getStudentByID(String id) {
        Student st = null;
        sqLite = dbManager.openDatabase();
        String sql = "select * from " + Init_DB.TABLE_STUDENT + " where maSV = ?";

        if (sqLite.rawQuery(sql, new String[]{id}) != null) {
            Cursor cursor = sqLite.rawQuery(sql, new String[]{id});
            String maSV = cursor.getString(0);
            String tenSV = cursor.getString(1);
            String maLopHoc = cursor.getString(2);
            st = new Student(maSV, tenSV, maLopHoc);
        }
        return st;
    }

    ArrayList<Student> getAllStudent() {
        Student st = null;
        studentList = new ArrayList<>();
        String sql = "select * from " + Init_DB.TABLE_STUDENT;
        if (checkOpenDB(sqLite)) {
            Cursor cursor = sqLite.rawQuery(sql, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                String maSV = cursor.getString(0);
                String tenSV = cursor.getString(1);
                String maLopHoc = cursor.getString(2);
                st = new Student(maSV, tenSV, maLopHoc);
                studentList.add(st);
                cursor.moveToNext();
            }
        }
        return studentList;
    }

    boolean checkOpenDB(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase = dbManager.openDatabase();
        if (sqLiteDatabase != null) {
            Log.i("DatabaseManager", "openDatabase: " + Init_DB.SUCCESSFUL);
            return true;
        }
        return false;
    }

}
