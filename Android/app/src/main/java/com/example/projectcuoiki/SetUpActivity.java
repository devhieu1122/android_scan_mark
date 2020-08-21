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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import java.util.HashMap;

public class SetUpActivity extends AppCompatActivity implements View.OnClickListener{
    static  int PRepCode =1 ;
    static int REQUESTCODE = 1;
    Uri pickedImgUri ;
   private Button btnLuu;
    ImageView img_userPhotoSetUp;
   private EditText edt_hoten,edt_taikhoan,edt_quequan;
   private FirebaseAuth mAuth;
   private ProgressBar progressBar;
   private DatabaseReference userRef;
   String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        img_userPhotoSetUp = (ImageView) findViewById(R.id.img_userPhotoSetUp);
        img_userPhotoSetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= 22){

                    checkAndRequestForPermission();
                    Toast.makeText(SetUpActivity.this,"hoho",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(SetUpActivity.this,"kkk",Toast.LENGTH_LONG).show();
                    openGallery();
                }
            }
        });


        getView();
    }

    // kt quyền truy cập thư mục ảnh
    public void checkAndRequestForPermission(){
        if(ContextCompat.checkSelfPermission(SetUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ){
            if(ActivityCompat.shouldShowRequestPermissionRationale(SetUpActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){

                Toast.makeText(SetUpActivity.this,"Please accept for required permission",Toast.LENGTH_LONG).show();
            }
            else{
                ActivityCompat.requestPermissions(SetUpActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PRepCode );
            }

        }
        else{

            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null){
            pickedImgUri = data.getData();
            img_userPhotoSetUp.setImageURI(pickedImgUri);
        }
    }

    // mở thư viện ảnh của bạn
    public void openGallery(){
        Toast.makeText(SetUpActivity.this,"gallery",Toast.LENGTH_LONG).show();
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);

    }

    private void getView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar_Luu);
        progressBar.setVisibility(View.INVISIBLE);

        edt_hoten =(EditText) findViewById(R.id.edt_ten);
        edt_taikhoan =(EditText) findViewById(R.id.edt_tentaikhoan);
        edt_quequan =(EditText) findViewById(R.id.edt_quequan);
        btnLuu = (Button) findViewById(R.id.btn_luu);
        btnLuu.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_luu) {
            btnLuu.setVisibility(View.INVISIBLE);
            SaveAccountSetUpInformation();
        }
    }

    private void SaveAccountSetUpInformation() {
        String hoten = edt_hoten.getText().toString().trim();
        String taikhoan = edt_taikhoan.getText().toString().trim();
        String quequan = edt_quequan.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);

        if(hoten.isEmpty()){
            btnLuu.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            showMessage("Bạn chưa nhập họ tên.");
            return;
        }
        if(taikhoan.isEmpty()){
            btnLuu.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            showMessage("Bạn chưa nhập tên tài khoản.");
            return;
        }
        if(quequan.isEmpty()){
            btnLuu.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            showMessage("Bạn chưa nhập quê quán.");
            return;
        }
        else {
            HashMap userMap = new HashMap();
               userMap.put("tentaikhoan",taikhoan);
               userMap.put("hoten",hoten);
               userMap.put("quequan",quequan);
               userMap.put("trangthai","none");
               userMap.put("gioitinh","none");
               userMap.put("trangthaiquanhe","none");

               userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                   @Override
                   public void onComplete(@NonNull Task task) {
                          if(task.isSuccessful()){
                              upDateUserInfo(pickedImgUri,mAuth.getCurrentUser());
                          }
                          else{
                              btnLuu.setVisibility(View.VISIBLE);
                              progressBar.setVisibility(View.INVISIBLE);
                              showMessage(task.getException().toString());
                          }
                   }
               });
        }

    }

    // cập nhật ảnh và tên người dùng
    private void upDateUserInfo(Uri pickedimg_uri, final FirebaseUser currentUser) {
        // dầu tiên ta cần upload ảnh lên firebase storage và lấy uri
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = storageReference.child(pickedimg_uri.getLastPathSegment());
        imageFilePath.putFile(pickedimg_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // upload ảnh thành công
                // h ta có thễ lấy nó ra
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    // thông tin user đã update thành công
                                    showMessage("Tạo thành công.");

                                    updateUI();

                                }
                                else{
                                    btnLuu.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    showMessage(task.getException().getMessage());
                                }
                            }
                        });

                    }
                });

            }
        });

    }

    private void showMessage(String mess){
        Toast.makeText(SetUpActivity.this,mess,Toast.LENGTH_LONG).show();
    }
    private void updateUI() {
        Intent intent = new Intent(SetUpActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
