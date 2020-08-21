package com.example.projectcuoiki;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

public class ProfileActivity extends AppCompatActivity {

    static  int PRepCode =1 ;
    static int REQUESTCODE = 1;
    Uri pickedImgUri ;
    ImageView img_userPhotoProfile;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private DatabaseReference usersRef;
    String currentUserId;
    FirebaseUser firebaseUser;
    EditText taikhoan;
    EditText quequan,name;

    ImageView imgPhotoUser;
    Button btn_updateAccountInfo,btn_changePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        taikhoan =(EditText) findViewById(R.id.edt_Pro_taiKhoan);
        quequan =(EditText) findViewById(R.id.edt_Pro_queQuan);
        name = (EditText) findViewById(R.id.edt_Pro_hoten);
        imgPhotoUser = (ImageView) findViewById(R.id.img_Pro_Photo);
        btn_updateAccountInfo =(Button) findViewById(R.id.btn_updateInfor);
        btn_changePass =(Button) findViewById(R.id.btn_changePassword);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        btn_changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        img_userPhotoProfile = (ImageView) findViewById(R.id.img_Pro_Photo);
        img_userPhotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= 22){

                    checkAndRequestForPermission();
                    Toast.makeText(ProfileActivity.this,"hoho",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ProfileActivity.this,"kkk",Toast.LENGTH_LONG).show();
                    openGallery();
                }
            }
        });

        if(currentUser == null){
            Intent intentLogin = new Intent(ProfileActivity.this,LoginActivity.class);
            startActivity(intentLogin);
        }
        else{
            getInfor();
            btn_updateAccountInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validateAcountInfo();
                }
            });


        }
    }

    private void validateAcountInfo() {


        String ten = name.getText().toString().trim();
        String tk =taikhoan.getText().toString().trim();
        String que =quequan.getText().toString().trim();

        if(ten.isEmpty()){
            showMessage("Họ tên không được để trống.");
            return;
        }
        if(tk.isEmpty()){
            showMessage("Họ tên không được để trống.");
        return;
        }
        if(que.isEmpty()){
            showMessage("Quê quán không được để trống.");
              return;
        }

            updateAccountInfo(ten,tk,que);


    }

    private void updateAccountInfo(String ten, String tk, String que) {
        HashMap map = new HashMap();
        map.put("hoten",ten);
        map.put("tentaikhoan",tk);
        map.put("quequan",que);

        usersRef.child(currentUserId).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                      if(task.isSuccessful()){
                          showMessage("Cập nhật thành công.");
                          upDateUserInfo(pickedImgUri,mAuth.getCurrentUser());

                      }
                      else {
                          showMessage(task.getException().toString());
                      }

            }
        });

    }


    private void getInfor() {
        firebaseUser = mAuth.getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgPhotoUser);

        usersRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("tentaikhoan")){
                        String tk = dataSnapshot.child("tentaikhoan").getValue().toString();
                        taikhoan.setText(tk);
                    }
                    if(dataSnapshot.hasChild("hoten")){
                        String ten = dataSnapshot.child("hoten").getValue().toString();
                        name.setText(ten);
                    }

                    if(dataSnapshot.hasChild("quequan")){
                        String que = dataSnapshot.child("quequan").getValue().toString();
                        quequan.setText(que);
                    }


                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void updateUI() {
        Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showMessage(String mess){
        Toast.makeText(ProfileActivity.this,mess,Toast.LENGTH_LONG).show();
    }

    // kt quyền truy cập thư mục ảnh
    public void checkAndRequestForPermission(){
        if(ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ){
            if(ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){

                Toast.makeText(ProfileActivity.this,"Please accept for required permission",Toast.LENGTH_LONG).show();
            }
            else{
                ActivityCompat.requestPermissions(ProfileActivity.this,
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
            img_userPhotoProfile.setImageURI(pickedImgUri);
        }
    }

    // mở thư viện ảnh của bạn
    public void openGallery(){
        Toast.makeText(ProfileActivity.this,"gallery",Toast.LENGTH_LONG).show();
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);

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

                                    showMessage("Cập nhật thành công.");

                                    updateUI();

                                }
                                else{
                                    showMessage(task.getException().getMessage());
                                }
                            }
                        });

                    }
                });

            }
        });

    }

}
