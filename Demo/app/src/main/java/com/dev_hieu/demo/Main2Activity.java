package com.dev_hieu.demo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dev_hieu.demo.database.DatabaseSQLite;
import com.dev_hieu.demo.model.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    EditText edtMH, edtGV;
    DatabaseSQLite databaseSQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        edtMH = findViewById(R.id.edtMH);
        edtGV = findViewById(R.id.edtGV);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("SUBJECT_ID", edtMH.getText().toString());
                intent.putExtra("CLASS_ROOM", edtGV.getText().toString());
                startActivity(intent);

//                DatabaseSQLite.createTable(getApplicationContext());

                DatabaseSQLite.addStudent(getApplicationContext());
                DatabaseSQLite.addSubject(getApplicationContext());
//                sqLiteDatabase.execSQL(DatabaseSQLite.create_table_student);
//                DatabaseSQLite.addStudent(sqLiteDatabase);
//                Log.i("DatabaseLS", "onClick:  ddax toa");
//                List<Student> ls = DatabaseSQLite.getListStudent(sqLiteDatabase);
//                if(ls.size()==0){
//                    Log.i("ListStudent", "onClick: Do not data here");
//                }else{
//                    Log.i("ListStudent", "onClick: Data were created");
//                }
//                for (Student l: ls){
//                    Log.i("ListStudent", "onClick: " + l.toString());
//                }
            }
        });


    }

}
