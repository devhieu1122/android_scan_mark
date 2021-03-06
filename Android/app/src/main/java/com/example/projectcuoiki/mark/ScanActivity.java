package com.example.projectcuoiki.mark;

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

import com.example.projectcuoiki.camera.CameraSourcePreview;
import com.example.projectcuoiki.controller.RemoteService;
import com.example.projectcuoiki.model.Student;
import com.example.projectcuoiki.model.StudentMark;
import com.google.android.gms.vision.barcode.Barcode;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Vibrator;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import com.example.projectcuoiki.R;

import java.io.IOException;
import java.util.List;

public class ScanActivity extends AppCompatActivity implements View.OnClickListener, BarcodeReader.BarcodeReaderListener {

    private static final String TAG = ScanActivity.class.getSimpleName();
    private static Button btnSave, btn_flash_mode;
    static List<Student> listStudent;
    static List<StudentMark> listMark;
    CameraSourcePreview mPreview;

    private static BarcodeReader barcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                Intent intent = new Intent(ScanActivity.this, CheckMarkActivity.class);
                startActivity(intent);
               /* Context context;
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
                dialog.show();*/
                break;
        }
    }

    @Override
    public void onScanned(final Barcode barcode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int maCaHoc = Integer.parseInt(getIntentString());
                String maSV = barcode.displayValue.trim();
                final String url = "sinhvien/" + "diemDanhSV/" + maSV + "/" + maCaHoc;
                new MarkAsyncTask().execute(RemoteService.createURL() + url);
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

    private void showDialogResult(Context context, String title, String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
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
//        vibrator.vibrate(500);
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

    public String getIntentString() {
        String maCH = "";
        Intent intent = this.getIntent();
        maCH = intent.getStringExtra("maCaHoc");
        return maCH;
    }


    private class MarkAsyncTask extends AsyncTask<String, Void, String> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strings) {
            return RemoteService.GET(strings[0]);
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!s.equalsIgnoreCase("")) {
                        showDialogResult(ScanActivity.this, "Điểm danh sinh viên", s);
                    }
                }
            });


        }
    }
}
