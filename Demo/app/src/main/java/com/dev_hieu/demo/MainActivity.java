package com.dev_hieu.demo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dev_hieu.demo.camera.CameraSourcePreview;
import com.dev_hieu.demo.database.DatabaseSQLite;
import com.dev_hieu.demo.model.Student;
import com.dev_hieu.demo.model.StudentMark;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;

import java.time.Instant;

import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import static com.dev_hieu.demo.R.layout.activity_main;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BarcodeReader.BarcodeReaderListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static Button btnSave, btn_flash_mode;
    static List<Student> listStudent;
    static List<StudentMark> listMark;
    CameraSourcePreview mPreview;

    private static BarcodeReader barcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        // getting barcode instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
        barcodeReader.getFragmentManager();
        barcodeReader.onCreate(savedInstanceState);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                Context context;
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Information");
                alert.setView(R.layout.dialog_match_information);
                alert.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("Quay lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                Dialog dialog = alert.create();
                dialog.show();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onScanned(final Barcode barcode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new MarkAsyncTask().execute(barcode.displayValue);
            }
        });

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {

    }

    private void showDialogResult(Context context, String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Information");
        alert.setMessage(msg);
        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = alert.create();
        dialog.show();
    }

    public void playVibrator(Context context) {
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }

    public void playSound(Context context, boolean success) {
        try {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            MediaPlayer mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(context, soundUri);
            final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {

                // Uncomment the following line if you aim to play it repeatedly
                // mMediaPlayer.setLooping(true);
                if (success) {
                    mMediaPlayer.setAudioStreamType(audioManager.STREAM_ALARM);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } else {
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                }

            }
            playVibrator(context);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initiateData() {
        listStudent = new ArrayList<>();
        listStudent.add(new Student(16130379, "Hiếu", "Nguyễn", "DH16DTA"));
        listStudent.add(new Student(16130603, "Thuận", "Lê Văn", "DH16DTA"));
        listStudent.add(new Student(16130380, "Hiếu", "Nguyễn Trung", "DH16DTC"));
    }

    public String getIntentSubject(String type) {
        String subject = "";
        Intent intent = this.getIntent();
        if (type == "SUBJECT_ID") {
            subject = intent.getStringExtra("SUBJECT_ID");
        } else {
            subject = intent.getStringExtra("CLASS_ROOM");
        }
        return subject;
    }

    private class MarkAsyncTask extends AsyncTask<String, Void, String> {
        List<StudentMark> list = null;
        StudentMark studentMark;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strings) {
            String msg = "";
            String id = strings[0];
            LocalDate now = LocalDate.now();
            Date date = Date.valueOf(String.valueOf(now));

            list = DatabaseSQLite.getListStudentMark(getApplicationContext());
            for(int i=0;i<list.size();i++) {
                if (list != null) {
                    if ( list.get(i).getStudentID() == Integer.parseInt(id)) {
                        msg = "Sinh viên đã được điểm danh trước do";
                    } else {
                        int studentID = Integer.parseInt(id);
                        String subjectID = getIntentSubject("SUBJECT_ID");
                        String classRoom = getIntentSubject("CLASS_ROOM");
                        String d = date.toString();
                        String status = "True";
                        studentMark = new StudentMark(studentID, subjectID, classRoom, d, status);
                        DatabaseSQLite.addStudentMark(getApplicationContext(), studentMark);
                        msg = "Sinh viên chưa điểm danh";
                    }
                } else {
                    int studentID = Integer.parseInt(id);
                    String subjectID = getIntentSubject("SUBJECT_ID");
                    String classRoom = getIntentSubject("CLASS_ROOM");
                    String d = date.toString();
                    String status = "True";
                    studentMark = new StudentMark(studentID, subjectID, classRoom, d, status);
                    DatabaseSQLite.addStudentMark(getApplicationContext(), studentMark);
                    msg = "List null";
                }
            }
            Log.i(TAG, "doInBackground: " +msg);

            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            showDialogResult(getApplicationContext(), s);
        }
    }

    public void getDate() {

    }

}
