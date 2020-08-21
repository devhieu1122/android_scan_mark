package com.example.projectcuoiki;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.projectcuoiki.manager.CreateLectureActivity;
import com.example.projectcuoiki.manager.CreateRoomActivity;
import com.example.projectcuoiki.manager.ListStudentActivity;
import com.example.projectcuoiki.mark.CheckMarkActivity;
import com.example.projectcuoiki.create_student.CreateStudentActivity;
import com.example.projectcuoiki.manager.CreateSubjectActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    FirebaseUser firebaseUser;
    String currentUserId;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser currentUser = mAuth.getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        if (currentUser == null) {
            Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intentLogin);
        } else {
            CheckUserExistence();
        }


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateLectureActivity.class);
                startActivity(intent);
            }
        });

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_dssv, R.id.nav_profile,
                R.id.nav_themdssv, R.id.nav_taomh, R.id.nav_logout, R.id.nav_checkDD, R.id.nav_taophong, R.id.nav_taoCa)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //ăn sự kiện click
        navigationView.bringToFront();

        // cap nhat lai email
        if (mAuth.getCurrentUser() != null) {
            updateNavHeader();
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_dssv:
                        Intent intentListStu = new Intent(MainActivity.this, ListStudentActivity.class);
                        startActivity(intentListStu);
                        break;

                    case R.id.nav_themdssv:
                        Intent intent = new Intent(MainActivity.this, CreateStudentActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_taomh:
                        Intent intentCreateMH = new Intent(MainActivity.this, CreateSubjectActivity.class);
                        startActivity(intentCreateMH);
                        break;
                    case R.id.nav_checkDD:
                        Intent intentCheckMark = new Intent(MainActivity.this, CheckMarkActivity.class);
                        startActivity(intentCheckMark);
                        break;

                    case R.id.nav_taophong:
                        Intent intentTaoPhong = new Intent(MainActivity.this, CreateRoomActivity.class);
                        startActivity(intentTaoPhong);
                        break;
                    case R.id.nav_taoCa:
                        Intent intentTaoCaHoc = new Intent(MainActivity.this, CreateLectureActivity.class);
                        startActivity(intentTaoCaHoc);
                        break;

                    case R.id.nav_logout:
                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        break;

                    case R.id.nav_profile:
                        Toast.makeText(getApplicationContext(), "bạn chọn profile", Toast.LENGTH_LONG).show();
                        Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intentProfile);
                        break;


                }
                drawer.closeDrawers();// kích vô thì nó sẽ đóng lại,hiện Activity muốn tới


                return false;
            }

        });


    }

    // kt user này đã được setup thông tin chưa
    private void CheckUserExistence() {
        final String current_user_id = mAuth.getCurrentUser().getUid();

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // nếu chưa
                if (!dataSnapshot.hasChild(current_user_id)) {
                    Intent setUpIntent = new Intent(MainActivity.this, SetUpActivity.class);
                    setUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(setUpIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    // load thông tin (hình ảnh,username) vào nav header
    public void updateNavHeader() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        TextView nav_Email = headerView.findViewById(R.id.nav_emailUser);
        final TextView nav_NameU = headerView.findViewById(R.id.nav_nameUser);
        ImageView imgPhotoUser = headerView.findViewById(R.id.imgUserPhoto);

        firebaseUser = mAuth.getCurrentUser();

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        usersRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("tentaikhoan")) {
                        String Username = dataSnapshot.child("tentaikhoan").getValue().toString();
                        //user name
                        nav_NameU.setText(Username);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //email
        nav_Email.setText(firebaseUser.getEmail());
        //hình ảnh
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgPhotoUser);
    }
}
