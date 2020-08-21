package com.example.projectcuoiki.manager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectcuoiki.MainActivity;
import com.example.projectcuoiki.R;
import com.example.projectcuoiki.controller.RemoteService;

public class CreateSubjectActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edt_maMH, edt_tenMH, edt_maGV, edt_so_tiet;
    Button btn_tao, btn_huy;
    String maMH, tenMH, maGV, soTiet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create_subject);
        initView();
    }

    void initView() {
        edt_maMH = findViewById(R.id.edit_ma_mon_hoc);
        edt_tenMH = findViewById(R.id.edit_ten_mon_hoc);
        edt_maGV = findViewById(R.id.edt_ma_gv);
        edt_so_tiet = findViewById(R.id.edt_so_tiet);

        btn_tao = findViewById(R.id.btn_tao);
        btn_tao.setOnClickListener(this);

        btn_huy = findViewById(R.id.btn_huy);
        btn_tao.setOnClickListener(this);
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
        maMH = edt_maMH.getText().toString();
        tenMH = edt_maGV.getText().toString();
        maGV = edt_tenMH.getText().toString();
        soTiet = edt_so_tiet.getText().toString();
        if (maMH.equalsIgnoreCase("") || tenMH.equalsIgnoreCase("")
                || maGV.equalsIgnoreCase("") || soTiet.equalsIgnoreCase("")) {
            showDialogResult(this, "Tạo Môn Học", "Vui lòng nhập đầy đủ thông tin");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tao:
                if (verifyInputData()) {
                    maMH.toUpperCase();

                    String url = RemoteService.createURL() + "quanly/themMonHoc/" + maMH + "/"
                            + tenMH + "/" + maGV + "/" + Integer.parseInt(soTiet);
                    new HttpTaoMH().execute(url);
                }


                break;
            case R.id.btn_huy:
                startActivity(new Intent(CreateSubjectActivity.this, MainActivity.class));
                break;
        }
    }

    class HttpTaoMH extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RemoteService.GET(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("true")) {
                Intent intent = new Intent(CreateSubjectActivity.this, MainActivity.class);
                showDialogResult(CreateSubjectActivity.this, "Thêm Môn Học", "Thêm môn học thành công.");
            } else {
                showDialogResult(CreateSubjectActivity.this, "Thêm Môn Học"
                        , "Lỗi! Không Thể thêm môn học! Vui lòng thử lại");
            }
            super.onPostExecute(s);
        }
    }
}
