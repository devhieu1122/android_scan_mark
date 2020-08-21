package com.example.projectcuoiki.manager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.projectcuoiki.MainActivity;
import com.example.projectcuoiki.controller.RemoteService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.projectcuoiki.R;

public class CreateLectureActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edt_maCaHoc, edt_maMonHoc, edt_maPhongHoc, edt_thu, edt_tietHoc;
    Button btn_tao_caHoc, btn_huy_tao_caHoc;
    String maCaHoc, maMonHoc, maPhongHoc, thu, tiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create_lecture);
        initView();
    }

    void initView() {
        edt_maCaHoc = findViewById(R.id.edt_ma_ca_hoc_ch);
        edt_maMonHoc = findViewById(R.id.edt_ma_mon_hoc_ch);
        edt_maPhongHoc = findViewById(R.id.edt_ma_phong_hoc_ch);
        edt_thu = findViewById(R.id.edt_thu_ch);
        edt_tietHoc = findViewById(R.id.edt_tiet_ch);

        btn_tao_caHoc = findViewById(R.id.btn_taoCaHoc);
        btn_tao_caHoc.setOnClickListener(this);

        btn_huy_tao_caHoc = findViewById(R.id.btn_huyTaoCaHoc);
        btn_huy_tao_caHoc.setOnClickListener(this);
    }

    private void showDialogResult(Context context, String title, String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setCancelable(false);
        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = alert.create();
        dialog.show();
    }

    boolean verifyInputData() {
        maCaHoc = edt_maCaHoc.getText().toString();
        maMonHoc = edt_maMonHoc.getText().toString();
        maPhongHoc = edt_maPhongHoc.getText().toString();
        thu = edt_thu.getText().toString();
        tiet = edt_tietHoc.getText().toString();


        if (maCaHoc.equalsIgnoreCase("") || maMonHoc.equalsIgnoreCase("")
                || maPhongHoc.equalsIgnoreCase("") || thu.isEmpty() || tiet.isEmpty()) {
            showDialogResult(this, "Tạo Môn Học", "Vui lòng nhập đầy đủ thông tin");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_taoCaHoc:

                if (verifyInputData()) {
                    String url = RemoteService.createURL() + "quanly/themCaHoc/" + maCaHoc + "/" + maPhongHoc + "/" + maMonHoc +"/" +thu +"/" + tiet;
                    new HttpTaoPhongHoc().execute(url);

                }

                break;
            case R.id.btn_huyTaoCaHoc:
                startActivity(new Intent(CreateLectureActivity.this, MainActivity.class));
                break;
        }
    }
    class HttpTaoPhongHoc extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RemoteService.GET(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("true")) {
                Intent intent = new Intent(CreateLectureActivity.this, MainActivity.class);
                edt_maCaHoc.setText("");
                edt_maPhongHoc.setText("");
                edt_maMonHoc.setText("");
                edt_thu.setText("");
                edt_tietHoc.setText("");


                showDialogResult(CreateLectureActivity.this, "Thêm ca học", "Thêm ca học thành công.");
            } else {
                showDialogResult(CreateLectureActivity.this, "Thêm ca học", "Lỗi! Không Thể thêm ca học! Vui lòng thử lại");
            }
            super.onPostExecute(s);
        }
    }
}
