package com.example.projectcuoiki;

import android.content.Intent;
import android.os.Bundle;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnLogin, btnRegister;
    TextView tv_dangki;
    EditText edt_username,edt_pass;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        getView();

        if (mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

    }
    void getView() {
        btnLogin = findViewById(R.id.btn_login);
        tv_dangki = (TextView) findViewById(R.id.edt_register);
        edt_username=(EditText) findViewById(R.id.edt_username);
        edt_pass =(EditText) findViewById(R.id.edt_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBarlogin);
        progressBar.setVisibility(View.INVISIBLE);
        //Bat su kien khi nhan click
        tv_dangki.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btn_login){

            btnLogin.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

        String user = edt_username.getText().toString().trim();
        String pass = edt_pass.getText().toString().trim();

        if(TextUtils.isEmpty(user)){
            Toast.makeText(getApplication(),"Bạn chưa nhập địa chỉ email.",Toast.LENGTH_LONG).show();
              btnLogin.setVisibility(View.VISIBLE);
              progressBar.setVisibility(View.INVISIBLE);
            return;
        }
            if(TextUtils.isEmpty(pass)){
                Toast.makeText(getApplication(),"Bạn chưa nhập mật khẩu.",Toast.LENGTH_LONG).show();
            btnLogin.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
                return;
            }


        mAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
           if(task.isSuccessful()){
               finish();
               Intent intent = new Intent(LoginActivity.this,MainActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
               Toast.makeText(getApplication(),"Đăng nhập thành công.",Toast.LENGTH_LONG).show();
           }

           else {

               btnLogin.setVisibility(View.VISIBLE);
               progressBar.setVisibility(View.INVISIBLE);
               Toast.makeText(getApplication(),task.getException().getMessage(),Toast.LENGTH_LONG).show();

               Intent intent = new Intent(LoginActivity.this,MainActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
           }


            }
        });
        }

        else if(view.getId() == R.id.edt_register){
            sendIntent();
        }
    }
    void sendIntent() {

        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);

    }
}
