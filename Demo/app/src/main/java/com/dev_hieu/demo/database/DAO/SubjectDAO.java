package com.dev_hieu.demo.database.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dev_hieu.demo.database.DatabaseManager;
import com.dev_hieu.demo.database.Init_DB;
import com.dev_hieu.demo.model.Subject;

import java.util.List;

public class SubjectDAO {
    private final String TAG = SubjectDAO.class.getSimpleName();
    List<Subject> subjectList;
    SQLiteDatabase sqlite;
    DatabaseManager dbManager;

    public boolean insertSubject(Subject sub) {
        sqlite = dbManager.openDatabase();
        ContentValues values = new ContentValues();
        values.put(Init_DB.SUB_MA_MH, sub.getMaMH());
        values.put(Init_DB.SUB_TEN_MH, sub.getTenMH());
        values.put(Init_DB.SUB_MA_GV, sub.getMaGV());
        values.put(Init_DB.SUB_PHONG_HOC, sub.getPhongHoc());
        values.put(Init_DB.SUB_SO_TIET, sub.getSoTiet());

        if (sqlite.insert(Init_DB.TABLE_SUBJECT, null, values) != -1) {
            return true;
        } else return false;
    }

    public Subject getSubjectByID(String subjectID) {
        sqlite = dbManager.openDatabase();
        Subject sub = null;
        String sql = "select * from " + Init_DB.TABLE_SUBJECT + " where subjectID =?";
        if (sqlite.rawQuery(sql, new String[]{subjectID}) != null) {
            Cursor cursor = sqlite.rawQuery(sql, new String[]{subjectID});
            cursor.moveToFirst();
            sub = new Subject(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4)
            );
        }
        return sub;
    }
}
