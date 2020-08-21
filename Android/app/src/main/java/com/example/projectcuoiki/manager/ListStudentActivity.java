package com.example.projectcuoiki.manager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.projectcuoiki.adapter.StudentAdapter;
import com.example.projectcuoiki.controller.RemoteService;
import com.example.projectcuoiki.mark.CheckMarkActivity;
import com.example.projectcuoiki.mark.MarkActivity;
import com.example.projectcuoiki.model.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.projectcuoiki.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListStudentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spn_ma_phong_hoc, spn_mon_hoc, spn_thu, spn_tiet;
    private ProgressDialog progressDialog;
    private String selectedMaMH = null;
    private String selectedMaPH = null;
    private String selectedThu = null;
    private String selectedTiet = null;
    private Button btn_xem;
    private ListView lv_listStudent;
    ArrayList<Student> listStu;
    StudentAdapter studentAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_student);

        listStu = new ArrayList<>();
        spn_ma_phong_hoc = findViewById(R.id.spn_ma_phong_hoc);
        spn_mon_hoc = findViewById(R.id.spn_mon_hoc);
        spn_thu = findViewById(R.id.spn_thu);
        spn_tiet = findViewById(R.id.spn_tiet);
        spn_ma_phong_hoc.setOnItemSelectedListener(this);
        spn_mon_hoc.setOnItemSelectedListener(this);
        spn_thu.setOnItemSelectedListener(this);
        spn_tiet.setOnItemSelectedListener(this);
        btn_xem = (Button) findViewById(R.id.btn_xem);
        lv_listStudent = findViewById(R.id.lv_student);
        String url = RemoteService.createURL() + "quanly/getAllSubject";
        new HttpAsyncMaMH().execute(url);

        viewListStudent();
    }

    private void viewListStudent() {
        btn_xem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = RemoteService.createURL() + "quanly/getSinhVienTheoMonHoc/" + selectedMaMH + "/" + selectedMaPH + "/" + selectedThu + "/" + selectedTiet;
                new HttpAsyncGetListStudent().execute(url);
//                showDataOnListView();
            }
        });
    }

    void setLayOutSpinner(Spinner spinner, ArrayList<String> listS) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListStudentActivity.this, android.R.layout.simple_spinner_item, listS);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectItem = parent.getItemAtPosition(position).toString();
        switch (parent.getId()) {
            case R.id.spn_mon_hoc:
                selectedMaMH = selectItem;
                if (selectItem != null) {
                    String url = RemoteService.createURL() + "quanly/getPhongHocByMaMH/" + selectedMaMH;
                    new HttpAsyncMaPH().execute(url);
                }
                Log.i("abc", "onItemSelected: " + selectedMaMH);
                break;
            case R.id.spn_ma_phong_hoc:
                selectedMaPH = selectItem;
                String url = RemoteService.createURL() + "quanly/getThuByMaMH/" + selectedMaMH + "/" + selectedMaPH;
                new HttpAsyncThu().execute(url);
                Log.i("abc", "onItemSelected: " + selectedMaPH);
                break;
            case R.id.spn_thu:
                selectedThu = selectItem;
                String urlTiet = RemoteService.createURL() + "quanly/getTietByMaMH/" + selectedMaMH + "/" + selectedMaPH + "/" + selectedThu;
                new HttpAsyncTiet().execute(urlTiet);
                Log.i("abc", "onItemSelected: " + selectedThu);
                break;
            case R.id.spn_tiet:
                selectedTiet = selectItem;
                Log.i("abc", "onItemSelected: " + selectedTiet);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getAnElementFromJson(List<String> objectList, String s, String target) {
        try {
            JSONArray jArray = new JSONArray(s);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                String element = jObject.getString(target);
                objectList.add(element);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class HttpAsyncMaMH extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RemoteService.GET(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<String> listMaMH = new ArrayList<String>();
            getAnElementFromJson(listMaMH, s, "maMH");
            setLayOutSpinner(spn_mon_hoc, listMaMH);
        }
    }

    class HttpAsyncMaPH extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RemoteService.GET(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                ArrayList<String> ls = new ArrayList<>();
                JSONArray jarr = new JSONArray(s);
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject obj = jarr.getJSONObject(i);
                    String maPH = obj.getString("maPhongHoc");
                    ls.add(maPH);
                }
                setLayOutSpinner(spn_ma_phong_hoc, ls);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class HttpAsyncThu extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RemoteService.GET(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<String> ls = xyLyChuoi(s);
            setLayOutSpinner(spn_thu, ls);
        }
    }

    class HttpAsyncTiet extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RemoteService.GET(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<String> ls = xyLyChuoi(s);
            setLayOutSpinner(spn_tiet, ls);
        }
    }

    public ArrayList<String> xyLyChuoi(String str) {
        ArrayList<String> ls = new ArrayList<>();
        String rpl = str.replaceAll("[^0-9,-\\.]", ",").trim();
        String arr[] = rpl.split(",");

        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].equalsIgnoreCase("")) {
                ls.add(arr[i]);
            }
        }
        return ls;
    }

    class HttpAsyncGetListStudent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RemoteService.GET(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                JSONArray jarr = new JSONArray(s);
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject obj = jarr.getJSONObject(i);
                    String maSV = obj.getString("maSV");
                    String tenSV = obj.getString("tenSV");
                    String maLop = obj.getString("maLopHoc");
                    Student stu = new Student(maSV, tenSV, maLop);
                    listStu.add(stu);
                }
                showResultListView(listStu);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void showResultListView(ArrayList<Student> arrayList) {
        ArrayList<Student> list1 = new ArrayList<>();

        if (arrayList.size() == 0) {
            studentAdapter = new StudentAdapter(ListStudentActivity.this, R.layout.my_student_layout, list1);
            studentAdapter.notifyDataSetChanged();
            lv_listStudent.setAdapter(studentAdapter);
            Toast.makeText(getApplicationContext(), "Không tìm thấy kết quả.", Toast.LENGTH_LONG).show();
        } else {
            list1.add(new Student("MSSV", "Họ Tên", "Mã lớp"));
            for (Student stu : arrayList) {
                list1.add(stu);
                Log.i("abc", "showResultListView: " + stu.toString());
            }
            studentAdapter = new StudentAdapter(ListStudentActivity.this, R.layout.my_student_layout, list1);
            studentAdapter.notifyDataSetChanged();
            lv_listStudent.setAdapter(studentAdapter);
        }
    }

    private void showDialogResult(Context context, String title, String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = alert.create();
        dialog.show();
    }
}
