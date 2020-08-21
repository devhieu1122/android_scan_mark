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

public class CreateRoomActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edt_maPhong, edt_tenPhong,edt_so_ChoNgoi;
    Button btn_tao_phong, btn_huy_tao_phong;
    String maPhong, tenPhong, soChoNgoi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create_room);

        initView();


    }
    void initView() {
        edt_maPhong = findViewById(R.id.edit_ma_phong_hoc);
        edt_tenPhong = findViewById(R.id.edit_ten_phong_hoc);
        edt_so_ChoNgoi = findViewById(R.id.edt_so_cho_ngoi);

        btn_tao_phong = findViewById(R.id.btn_tao_phongphoc);
        btn_tao_phong.setOnClickListener(this);

        btn_huy_tao_phong = findViewById(R.id.btn_huy_tao_phonghoc);
        btn_huy_tao_phong.setOnClickListener(this);
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
        maPhong = edt_maPhong.getText().toString();
        tenPhong = edt_tenPhong.getText().toString();
        soChoNgoi = edt_so_ChoNgoi.getText().toString();

        if (maPhong.equalsIgnoreCase("") || tenPhong.equalsIgnoreCase("") || soChoNgoi.equalsIgnoreCase("")) {

            showDialogResult(this, "Tạo Môn Học", "Vui lòng nhập đầy đủ thông tin");
            return false;
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tao_phongphoc:
//                showDialogResult(this, "Tạo Môn Học", "cc");
                if (verifyInputData()) {
                    try {
                        int soCho = Integer.parseInt(soChoNgoi);
                        maPhong.toUpperCase();
                        String url = RemoteService.createURL() + "quanly/themPhongHoc/" + maPhong + "/" + tenPhong+ "/" + soCho;
                        new HttpTaoPhongHoc().execute(url);
                    }
                    catch (Exception e){
                        showDialogResult(this, "Tạo Môn Học", "Số chỗ ngồi phải là số.");
                    }

                }

                break;
            case R.id.btn_huy_tao_phonghoc:
                startActivity(new Intent(CreateRoomActivity.this, MainActivity.class));
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
                Intent intent = new Intent(CreateRoomActivity.this, MainActivity.class);
                edt_maPhong.setText("");
                edt_tenPhong.setText("");
                edt_so_ChoNgoi.setText("");
                showDialogResult(CreateRoomActivity.this, "Thêm phòng học", "Thêm phòng học thành công.");
            } else {
                showDialogResult(CreateRoomActivity.this, "Thêm phòng học", "Lỗi! Không Thể thêm phòng học! Vui lòng thử lại");
            }
            super.onPostExecute(s);
        }
    }
}
