package com.dev_hieu.demo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.dev_hieu.demo.MainActivity;
import com.dev_hieu.demo.model.Student;
import com.dev_hieu.demo.model.StudentMark;
import com.dev_hieu.demo.model.Subject;


import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class DatabaseSQLite extends SQLiteOpenHelper {

    private static SQLiteDatabase sqlite;
    private static final String DB_NAME = "student.db";
    private static final String TABLE_STUDENT = "student";

    private final static int DB_VERSION = 1;
    static List<Student> listStudent;

    final static String COL_ID = "id";
    final static String COL_FIRST_NAME = "firstName";
    final static String COL_LAST_NAME = "lastName";
    final static String COL_CLASS_ID = "classID";

    private static final String TABLE_SUBJECT = "subject";
    final static String SUBJECT_ID = "subjectID";
    final static String SUBJECT_NAME = "subjectName";

    private static final String TABLE_STUDENT_MARK = "studentMark";
    final static String STUDENT_MARK_ID = "studentID";
    final static String SUBJECT_MARK_ID = "subjectID";
    final static String CLASS_ROOM = "classRoom";
    final static String MARK_DATE = "markDate";
    final static String STATUS = "status";


    public static String create_table_student = "create table " + TABLE_STUDENT + "("
            + COL_ID + " int primary key, "
            + COL_FIRST_NAME + " text, "
            + COL_LAST_NAME + " text, "
            + COL_CLASS_ID + " text);";

    public static String create_table_subject = "CREATE TABLE `" + TABLE_SUBJECT + "`(\n" +
            "  `" + SUBJECT_ID + "` varchar(45) NOT NULL,\n" +
            "  `" + SUBJECT_NAME + "` text NOT NULL\n" +
            ");\n";

    public static String create_table_studentMark = "CREATE TABLE `" + TABLE_STUDENT_MARK + "` (\n" +
            "  `" + STUDENT_MARK_ID + "` int(11) NOT NULL,\n" +
            "  `" + SUBJECT_MARK_ID + "` varchar(45),\n" +
            "  `" + CLASS_ROOM + "` text NOT NULL,\n" +
            "  `" + MARK_DATE + "` text NOT NULL,\n" +
            "  `" + STATUS + "` text NOT NULL\n" +
            ")";

    public DatabaseSQLite(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(create_table_student);
//        db.execSQL(create_table_match);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_STUDENT);
        db.execSQL("delete from TABLE_STUDENT_MARK where STUDENT_MARK_ID='16130392'");

        onCreate(db);
    }

    public static void createTable(Context context) {
        sqlite = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);
        sqlite.execSQL(create_table_student);
        sqlite.execSQL(create_table_subject);
        sqlite.execSQL(create_table_studentMark);
        sqlite.close();
    }

    public static void addStudent(Context context) {
        sqlite = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);
        List<Student> list = initDataStudent();
        for (int i = 0; i < list.size(); i++) {
            int code = list.get(i).getId();
            String fName = list.get(i).getFirstName();
            String lName = list.get(i).getLastName();
            String clCode = list.get(i).getClassID();

            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_ID, code);
            contentValues.put(COL_FIRST_NAME, fName);
            contentValues.put(COL_LAST_NAME, lName);
            contentValues.put(COL_CLASS_ID, clCode);

            sqlite.insert(TABLE_STUDENT, null, contentValues);
        }
        sqlite.close();
    }

    public static void addSubject(Context context) {
        sqlite = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);
        List<Subject> list = initSubject();
        for (int i = 0; i < list.size(); i++) {
            String subjectID = list.get(i).getSubjectID();
            String subjectName = list.get(i).getSubjectName();

            ContentValues contentValues = new ContentValues();
            contentValues.put(SUBJECT_ID, subjectID);
            contentValues.put(SUBJECT_NAME, subjectName);

            sqlite.insert(TABLE_SUBJECT, null, contentValues);
        }
        sqlite.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean addStudentMark(Context context, StudentMark studentMark) {
        sqlite = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);

        LocalDate now = LocalDate.now();
        Date date = Date.valueOf(String.valueOf(now));
//
        int id = studentMark.getStudentID();
        String subjectID = studentMark.getSubjectID();
        String classRoom = studentMark.getClassRoom();
        String markDate = studentMark.getMarkDate();
        String status = studentMark.getStatus();

        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_MARK_ID, id);
        contentValues.put(SUBJECT_MARK_ID, subjectID);
        contentValues.put(CLASS_ROOM, classRoom);
        contentValues.put(MARK_DATE, markDate);
        contentValues.put(STATUS, status);
        if (sqlite.insert(TABLE_STUDENT_MARK, null, contentValues) != -1) {
            return true;
        }
        sqlite.close();
        return false;
    }

    public static StudentMark getStudentMark(Context context, int studentID) {
        StudentMark studentMark = null;
        sqlite = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);
        String sql = "select * from " + TABLE_STUDENT_MARK + " where studentID = ?";

        Cursor cursor = sqlite.query(TABLE_STUDENT_MARK, null, null, new String[]{String.valueOf(studentID)}, null, null, null);
        if (cursor != null) {
            int id = cursor.getInt(0);
            String subjectID = cursor.getString(1);
            String classRoom = cursor.getString(2);
            String markDate = cursor.getString(3);
            String status = cursor.getString(4);
            studentMark = new StudentMark(
                    id, subjectID, classRoom, markDate, status
            );
        }
        return studentMark;
    }

    public static Student getStudent(Context context, String id) {
        Student student = null;
        sqlite = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);
        String query = "select * from " + TABLE_STUDENT + " where code =?";
        Cursor cursor = sqlite.rawQuery(query, new String[]{id});
        if (cursor != null) {
            int code = Integer.parseInt(cursor.getString(0));
            String firstName = cursor.getString(1);
            String lastName = cursor.getString(2);
            String classID = cursor.getString(3);
            student = new Student(code, firstName, lastName, classID);
        }
        return student;
    }

    public static List<StudentMark> getListStudentMark(Context context) {
        List<StudentMark> listST = new ArrayList<>();
        StudentMark studentMark;
        sqlite = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);

        String query = "select * from " + TABLE_STUDENT_MARK;
        Cursor cursor = sqlite.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String subjectID = cursor.getString(1);
            String classRoom = cursor.getString(2);
            String markDate = cursor.getString(3);
            String status = cursor.getString(4);
            studentMark = new StudentMark(
                    id, subjectID, classRoom, markDate, status);
            listST.add(studentMark);
            cursor.moveToNext();

        }

        return listST;
    }

    public static List<Student> getListStudent(SQLiteDatabase database) {
        List<Student> listST = new ArrayList<>();
        String query = "select * from " + TABLE_STUDENT;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            int code = Integer.parseInt(cursor.getString(0));
            String firstName = cursor.getString(1);
            String lastName = cursor.getString(2);
            String classID = cursor.getString(3);
            Student student = new Student(code, firstName, lastName, classID);
            listST.add(student);
            cursor.moveToNext();
        }

        return listST;
    }

    public static List<Student> initDataStudent() {
        listStudent = new ArrayList<>();
        listStudent.add(new Student(16130379, "Hiếu", "Nguyễn", "DH16DTA"));
        listStudent.add(new Student(16130603, "Thuận", "Lê Văn", "DH16DTA"));
        listStudent.add(new Student(16130380, "Hiếu", "Nguyễn Trung", "DH16DTC"));
        listStudent.add(new Student(16130460, "Mẫn", "Trương Công", "DH16DTC"));
        return listStudent;
    }

    public static List<Subject> initSubject() {
        List<Subject> lsSubject = new ArrayList<>();
        lsSubject.add(new Subject("TTNT", "Trí tuệ nhân tạo"));
        lsSubject.add(new Subject("LTTBDD", "Lập trình thiết bị di động"));
        lsSubject.add(new Subject("LTNC", "Lập trình nâng cao"));
        lsSubject.add(new Subject("CSDL", "Cơ sở dữ liệu"));
        return lsSubject;
    }
}