package com.example.projectcuoiki.mark;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectcuoiki.R;
import com.example.projectcuoiki.adapter.StudentAdapter;
import com.example.projectcuoiki.controller.RemoteService;
import com.example.projectcuoiki.model.Student;

import org.apache.poi.ss.formula.functions.T;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CheckMarkActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    Spinner spn_ma_phong_hoc, spn_mon_hoc, spn_thu, spn_tiet, sp_dk;
    private ListView lv_check_DD;
    private Button btn_checkDD, btn_chonNgay, btn_chon_gio;
    private EditText edt_ngayDD, edt_gio_dd;
    private ListView lv_checkMark;
    private ProgressDialog progressDialog;

    StudentAdapter studentAdapter = null;

    private String selectedMaMH, selectedMaPH, selectedThu, selectedTiet, selectedDK;
    int maCaHoc = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mark);

        initView();
        spinnerSelectCondition();

        String url = RemoteService.createURL() + "quanly/getAllSubject";
        new HttpAsyncMaMH().execute(url);
    }

    private void initView() {
        spn_ma_phong_hoc = findViewById(R.id.spn_ma_phong_hoc);
        spn_mon_hoc = findViewById(R.id.spn_mon_hoc);
        spn_thu = findViewById(R.id.spn_thu);
        spn_tiet = findViewById(R.id.spn_tiet);
        sp_dk = findViewById(R.id.spinner_check_dkien);

        spn_ma_phong_hoc.setOnItemSelectedListener(this);
        spn_mon_hoc.setOnItemSelectedListener(this);
        spn_thu.setOnItemSelectedListener(this);
        spn_tiet.setOnItemSelectedListener(this);
        sp_dk.setOnItemSelectedListener(this);

        btn_checkDD = (Button) findViewById(R.id.btn_checkMark);
        btn_checkDD.setOnClickListener(this);
        btn_chonNgay = (Button) findViewById(R.id.btn_chonNgay);
        btn_chonNgay.setOnClickListener(this);

        edt_ngayDD = (EditText) findViewById(R.id.edt_ngaydd);

        lv_check_DD = (ListView) findViewById(R.id.lv_check_mark);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_chonNgay:
                showDatePickerDialog();
                break;
            case R.id.btn_checkMark:
                if (selectedMaMH.equals("") || selectedMaPH.equals("") || selectedThu.equals("") || selectedTiet.equals("") || edt_ngayDD.getText().toString().equals("")) {
                    showDialogResult(CheckMarkActivity.this, "Check Kiểm Danh", "Lỗi! Vui lòng chọn ngày điểm danh.");
                } else {
                    final String url = "sinhvien/getMaCaHoc/" + selectedMaPH + "/" + selectedMaMH + "/" + selectedThu + "/" + selectedTiet;
                    new AsyncGetMaCaHoc().execute(RemoteService.createURL() + url);
                }
                break;
        }
    }

    //Hiện thị dialog chọn ngày
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        // i: year
                        // i1:month
                        // i2:year
                        calendar.set(i, i1, i2);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        edt_ngayDD.setText(format.format(calendar.getTime()));
//                        ngaydd = format.format(calendar.getTime());
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectItem = parent.getItemAtPosition(position).toString();
        switch (parent.getId()) {
            case R.id.spn_mon_hoc:
                selectedMaMH = selectItem;
                if (selectedMaMH != null) {
                    String url = RemoteService.createURL() + "quanly/getPhongHocByMaMH/" + selectedMaMH;
                    new HttpAsyncMaPH().execute(url);
                }
                Log.i("abc", "onItemSelected: " + selectedMaMH);
                break;
            case R.id.spn_ma_phong_hoc:
                selectedMaPH = selectItem;
                if (selectedMaMH != null) {
                    String url = RemoteService.createURL() + "quanly/getThuByMaMH/"
                            + selectedMaMH + "/" + selectedMaPH;
                    new HttpAsyncThu().execute(url);
                    Log.i("abc", "onItemSelected: " + selectedMaPH);
                }
                break;
            case R.id.spn_thu:
                if (selectedMaMH != null) {
                    selectedThu = selectItem;
                    String urlTiet = RemoteService.createURL() + "quanly/getTietByMaMH/"
                            + selectedMaMH + "/" + selectedMaPH + "/" + selectedThu;
                    new HttpAsyncTiet().execute(urlTiet);
                    Log.i("abc", "onItemSelected: " + selectedThu);
                }
                break;
            case R.id.spn_tiet:
                if (selectedMaMH != null) {
                    selectedTiet = selectItem;
                    Log.i("abc", "onItemSelected: " + selectedTiet);
                }
                break;

            case R.id.spinner_check_dkien:
                selectedDK = selectItem;
                Log.i("abc", "onItemSelected: " + selectedDK);
                break;
        }
    }

    //Hiện thị adapter của spinner
    void setLayOutSpinner(Spinner spinner, ArrayList<String> listS) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckMarkActivity.this, android.R.layout.simple_spinner_item, listS);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
    }

    public void spinnerSelectCondition() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Tất cả");
        list.add("Sinh viên đã điểm danh");
        list.add("Sinh viên chưa điểm danh");
        setLayOutSpinner(sp_dk, list);
    }

    // Hiện thị dialog thông báo lỗi
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Hiển thị listview
    void showResultListView(ArrayList<Student> arrayList) {
        ArrayList<Student> list1 = new ArrayList<>();
        list1.add(new Student("MSSV", "Họ Tên", "Mã lớp"));

        if (arrayList.size() == 0) {
            studentAdapter = new StudentAdapter(CheckMarkActivity.this, R.layout.my_student_layout, list1);
            studentAdapter.notifyDataSetChanged();
            lv_check_DD.setAdapter(studentAdapter);
            Toast.makeText(getApplicationContext(), "Không tìm thấy kết quả.", Toast.LENGTH_LONG).show();
        } else {
            for (Student stu : arrayList) {
                list1.add(stu);
                Log.i("abc", "showResultListView: " + stu.toString());
            }
            studentAdapter = new StudentAdapter(CheckMarkActivity.this, R.layout.my_student_layout, list1);
            studentAdapter.notifyDataSetChanged();
            lv_check_DD.setAdapter(studentAdapter);
        }
    }


    //xu ly lay danh sach ma Mon Hoc
    class HttpAsyncMaMH extends AsyncTask<String, Void, String> {

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
                    String maMonHoc = obj.getString("maMH");
                    ls.add(maMonHoc);
                }
                setLayOutSpinner(spn_mon_hoc, ls);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Lay danh sach ma phong hoc tu ma mon hoc
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

    //lay danh sach thu tu maMH va maPH
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


    //lay danh sach thu tu maMH, maPH va thu
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

    //Xử lý cắt chuỗi của server gửi về
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


    class AsyncGetMaCaHoc extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            return RemoteService.GET(strings[0]);
        }

        @Override
        protected void onPostExecute(final String s) {
            // Hiện progress bar
            progressDialog = new ProgressDialog(CheckMarkActivity.this);
            progressDialog.setMessage("Đang chạy ..."); // Setting Message
            progressDialog.setTitle("Đang kiểm tra !"); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
            new Thread(new Runnable() {
                public void run() {
                }
            }).start();

            //Delay 1s
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Tắt progress bar
                    progressDialog.dismiss();
//                    Chuyển layout nếu get đc mã ca học
                    if (!s.equals(0)) {
                        String url = RemoteService.createURL() + "sinhvien/";
                        maCaHoc = Integer.parseInt(s);
                        if (selectedDK.equalsIgnoreCase("Tất cả")) {
                            new HttpAsycnGetStudentMark().execute(url + "getStudentAllMarked/"
                                    + maCaHoc + "/" + edt_ngayDD.getText().toString());
                        } else if (selectedDK.equalsIgnoreCase("Sinh viên đã điểm danh")) {
                            new HttpAsycnGetStudentMark().execute(url + "getStudentIsMarked/"
                                    + maCaHoc + "/" + edt_ngayDD.getText().toString());
                        } else {
                            new HttpAsycnGetStudentMark().execute(url + "getStudentIsNotMarked/"
                                    + maCaHoc + "/" + edt_ngayDD.getText().toString());
                        }
                    } else {
                        Toast.makeText(CheckMarkActivity.this, "Lỗi! Vui lòng kiểm tra mạng và thử lại"
                                , Toast.LENGTH_SHORT).show();
                    }

                }
            }, 1000);
        }
    }

    private class HttpAsycnGetStudentMark extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return RemoteService.GET(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                ArrayList<Student> ls = new ArrayList<>();
                JSONArray jarr = new JSONArray(s);
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject obj = jarr.getJSONObject(i);
                    String maSV = obj.getString("maSV");
                    String tenSV = obj.getString("tenSV");
                    String maLopHoc = obj.getString("maLopHoc");
                    Student st = new Student(maSV, tenSV, maLopHoc);
                    ls.add(st);
                }
                showResultListView(ls);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
