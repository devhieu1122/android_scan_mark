package com.example.projectcuoiki.create_student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectcuoiki.R;
import com.example.projectcuoiki.create_student.utils.Class_insert_a_student;
import com.example.projectcuoiki.create_student.utils.Class_insert_import_student;

public class CreateStudentActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_import;
    private Button btn_create;
//    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create_student);

//        toolbar =findViewById(R.id.toolbar);
        btn_create =findViewById(R.id.btn_create);
        btn_import =findViewById(R.id.btn_import);

//        setSupportActionBar(toolbar);
//        if (getSupportActionBar()!=null) {
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
        btn_import.setOnClickListener(this);
        btn_create.setOnClickListener(this);
}

    @Override
    public void onClick(View v) {
        if ( btn_create ==v){
            Intent intent = new Intent(CreateStudentActivity.this, Class_insert_a_student.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(CreateStudentActivity.this, Class_insert_import_student.class);
            startActivity(intent); }
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId()==android.R.id.home){
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}

