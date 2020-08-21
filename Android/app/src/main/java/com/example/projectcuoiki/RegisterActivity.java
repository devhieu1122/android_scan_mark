package com.example.projectcuoiki;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView edt_dangki;
    Button btnLogin, btnRegister;
    EditText edt_username,edt_pass,edt_repass,edt_hoten;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        getView();

    }
    void getView() {

        btnRegister = findViewById(R.id.btn_dangki);
        edt_username=(EditText) findViewById(R.id.edt_taikhoan);
        edt_pass =(EditText) findViewById(R.id.edt_matkhau);
        edt_repass =(EditText) findViewById(R.id.edt_nhaplaimaikhau);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_Register);

        progressBar.setVisibility(View.INVISIBLE);
        //Bat su kien khi nhan click
        btnRegister.setOnClickListener(this);
    }




    @Override
    public void onClick(View view) {

        btnRegister.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        String user = edt_username.getText().toString().trim();
        String pass = edt_pass.getText().toString().trim();
        String repass = edt_repass.getText().toString().trim();

        if(user.isEmpty()){
            showMessage("Bạn chưa nhập tài khoản.");
            btnRegister.setVisibility(view.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        return;
        }
        if(pass.isEmpty()){
            showMessage("Bạn chưa nhập mật khẩu.");
            btnRegister.setVisibility(view.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        return;
        }
        if(!pass.equals(repass)){
            showMessage("Mật khẩu không trùng khớp.");
            btnRegister.setVisibility(view.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
       return;
        }
        // đăng kí
      createUserAccout(user,pass);

    }
   private void createUserAccout(String username, String password){

       mAuth.createUserWithEmailAndPassword(username, password)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                           // nếu như tạo thành công
                       if (task.isSuccessful()) {
                          updateUI();

                       }
                           // thất bại
                       else {
                           btnRegister.setVisibility(View.VISIBLE);
                           progressBar.setVisibility(View.INVISIBLE);
                           Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                       }
                   }
               });
   }


    private void updateUI() {
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showMessage(String mess){
        Toast.makeText(RegisterActivity.this,mess,Toast.LENGTH_LONG).show();
    }
}
