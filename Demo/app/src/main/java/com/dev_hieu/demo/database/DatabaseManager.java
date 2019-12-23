package com.dev_hieu.demo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String TAG = DatabaseManager.class.getSimpleName();
    private static final String DATABASE = Init_DB.DATABASE_NAME;
    public static String DATABASE_PATH;
    public Context mContext;
    private static SQLiteDatabase sqLiteDatabase;

    public DatabaseManager(Context context) {
        super(context, DATABASE, null, 1);
        if (Build.VERSION.SDK_INT >= 17) {
            DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
    }

    private boolean checkDatabase() {
        File db = new File(DATABASE_PATH + DATABASE);
        return db.exists();
    }

    private void copyDatabase() throws IOException {
        InputStream is = mContext.getAssets().open(DATABASE);
        String outFile = DATABASE_PATH + DATABASE;
        OutputStream os = new FileOutputStream(outFile);
        byte[] arr = new byte[1024];
        int length;
        while ((length = is.read(arr)) > 0) {
            os.write(arr, 0, length);

        }
        os.flush();
        os.close();
        is.close();
    }

    public void createDataBase() {
        boolean dbExist = checkDatabase();
        if (!dbExist) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDatabase();
                Log.i(TAG, "createDataBase: " + Init_DB.CREATE_SUCCESSFUL);
            } catch (IOException e) {
                throw new Error(Init_DB.CREATE_ERR);
            }
        }
    }

    public SQLiteDatabase openDatabase() {
        String path = DATABASE_PATH + DATABASE;
        sqLiteDatabase = sqLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return sqLiteDatabase;
    }

    public synchronized void close() {
        if (sqLiteDatabase != null)
            sqLiteDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
