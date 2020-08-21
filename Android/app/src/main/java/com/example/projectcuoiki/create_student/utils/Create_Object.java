package com.example.projectcuoiki.create_student.utils;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectcuoiki.R;


public class Create_Object  extends AppCompatActivity implements View.OnClickListener {
    private Button btn_Tao, btn_Huy;
    private TextView tv_error;
    private EditText edt_MaMH,edt_TenMH;
    private  String maMH;
    private String tenMH;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create_subject);
        init();
    }
    public void init(){
//        btn_Huy = findViewById(R.id.btn_huy);
//        btn_Tao =findViewById(R.id.btn_tao);

        tv_error = findViewById(R.id.tv_error);

        edt_MaMH =findViewById(R.id.edit_ma_mon_hoc);
        edt_TenMH =findViewById(R.id.edit_ten_mon_hoc);
        event();
    }

    public void event(){
        btn_Tao.setOnClickListener(this);
        btn_Huy.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        maMH =edt_MaMH.getText().toString().trim();
        tenMH = edt_TenMH.getText().toString().trim();
        if (btn_Huy ==v){
            finish();
        }else{
            if (maMH.length()>0&& tenMH.length()>0){

                   // them mon hoc
                   //   db.insertMonHoc(maMH,tenMH);
                      tv_error.setText("");
                      tv_error.setError(null);
                      tv_error.clearFocus();
                      String err = "";
                      if (!err.equals("Môn đã có trong dữ liệu")){
                          finish();
                      }else{

                      }
//
            }else{
                tv_error.setText("Bạn vui lòng chọn đầy đủ thông tin");
                tv_error.setError("");
                tv_error.setTextColor(Color.parseColor("#c4001d"));
            }
        }
    }
}
