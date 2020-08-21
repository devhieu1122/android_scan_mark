package com.example.projectcuoiki.create_student.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectcuoiki.MainActivity;
import com.example.projectcuoiki.R;
import com.example.projectcuoiki.controller.RemoteService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class Class_insert_a_student extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Button btn_huy, btn_them;

    Spinner spn_ma_phong_hoc, spn_mon_hoc, spn_thu, spn_tiet;

    private EditText edt_masv, edt_ten, edt_maLop;

    private String selectedMaMH, selectedMaPH, selectedThu, selectedTiet;

    private String masv, hoten, maLop;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_insert_a_student);

        spn_ma_phong_hoc = findViewById(R.id.spn_ma_phong_hoc);
        spn_mon_hoc = findViewById(R.id.spn_mon_hoc);
        spn_thu = findViewById(R.id.spn_thu);
        spn_tiet = findViewById(R.id.spn_tiet);

        spn_ma_phong_hoc.setOnItemSelectedListener(this);
        spn_mon_hoc.setOnItemSelectedListener(this);
        spn_thu.setOnItemSelectedListener(this);
        spn_tiet.setOnItemSelectedListener(this);

        edt_masv = findViewById(R.id.edt_masv);
        edt_ten = findViewById(R.id.edt_tenSV);
        edt_maLop = findViewById(R.id.edt_lop);

        btn_huy = findViewById(R.id.btn_huythem);
        btn_huy.setOnClickListener(this);
        btn_them = findViewById(R.id.btn_them);
        btn_them.setOnClickListener(this);

        String url = RemoteService.createURL() + "quanly/getAllSubject";
        new HttpAsyncMaMH().execute(url);

    }

    boolean verifyInputData() {
        maLop = edt_maLop.getText().toString().trim().toUpperCase();
        hoten = edt_ten.getText().toString().trim();
        masv = edt_masv.getText().toString().trim();

        if (maLop.equals("") || hoten.equals("") || masv.equals("")) {
            showDialogResult(this, "Tạo Môn Học", "Vui lòng nhập đầy đủ thông tin");
            return false;
        }
        return true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_huythem:
                startActivity(new Intent(Class_insert_a_student.this, MainActivity.class));
                break;
            case R.id.btn_them:
                if (verifyInputData()) {
                    String url = RemoteService.createURL() + "sinhvien/getMaCaHoc/" + selectedMaPH + "/" + selectedMaMH + "/" + selectedThu + "/" + selectedTiet;
                    new HttpAsyncGetMaCaHoc().execute(url);
                }
                break;
        }

    }

    void setLayOutSpinner(Spinner spinner, ArrayList<String> listS) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Class_insert_a_student.this, android.R.layout.simple_spinner_item, listS);
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
                if (!selectedMaMH.equals("")) {
                    String url = RemoteService.createURL() + "quanly/getPhongHocByMaMH/" + selectedMaMH;
                    new HttpAsyncMaPH().execute(url);
                }
                Log.i("abc", "onItemSelected: " + selectedMaMH);
                break;
            case R.id.spn_ma_phong_hoc:
                selectedMaPH = selectItem;
                if (!selectedMaPH.equals("")) {
                    String url = RemoteService.createURL() + "quanly/getThuByMaMH/" + selectedMaMH + "/" + selectedMaPH;
                    new HttpAsyncThu().execute(url);
                    Log.i("abc", "onItemSelected: " + selectedMaPH);
                }
                break;
            case R.id.spn_thu:

                selectedThu = selectItem;
                if (!selectedThu.equals("")) {
                    String urlTiet = RemoteService.createURL() + "quanly/getTietByMaMH/" + selectedMaMH + "/" + selectedMaPH + "/" + selectedThu;
                    new HttpAsyncTiet().execute(urlTiet);
                    Log.i("abc", "onItemSelected: " + selectedThu);
                }
                break;
            case R.id.spn_tiet:
                selectedTiet = selectItem;
                Log.i("abc", "onItemSelected: " + selectedTiet);
                break;
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


    // lay ma ca hoc
    class HttpAsyncGetMaCaHoc extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RemoteService.GET(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int maCaHoc = Integer.parseInt(s);

            if (maCaHoc > 0) {
                try {
                    URI uri = new URI(
                            "http",
                            null,
                            "192.168.1.96",
                            8899,
                            "/quanly/themSinhVien/" + masv.trim() + "/" + URLEncoder.encode(hoten.trim(), "UTF-8") + "/" + maLop.trim() + "/" + maCaHoc,
                            null,
                            null);
                    String request = uri.toString();
                    new HttpAsyncAddAStudent().execute(request);
                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            } else {
                showDialogResult(Class_insert_a_student.this, "Thêm Sinh Viên", "Lỗi! Vui lòng thử lại.");
            }

        }
    }

    class HttpAsyncAddAStudent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RemoteService.GET(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("true")) {
                showDialogResult(Class_insert_a_student.this, "Thông báo", "Thêm sinh viên thành công.");
            } else {
                showDialogResult(Class_insert_a_student.this, "Thông báo", "Lỗi! Sinh viên đã thêm trước đó! Hoặc có lỗi xảy ra! Vui lòng thử lại");
            }

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
