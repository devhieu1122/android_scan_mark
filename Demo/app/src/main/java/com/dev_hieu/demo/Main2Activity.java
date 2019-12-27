package com.dev_hieu.demo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dev_hieu.demo.controller.RemoteService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spn_ma_phong_hoc, spn_mon_hoc, spn_thu, spn_tiet;
    private ProgressDialog progressDialog;
    LinearLayout layout;
    static String maGV;
    private static String maMH, maPH, thu, tiet;
    private String selectedMaMH = null;
    private String selectedMaPH = null;
    private String selectedThu = null;
    private String selectedTiet = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        spn_ma_phong_hoc = findViewById(R.id.spn_ma_phong_hoc);
        spn_mon_hoc = findViewById(R.id.spn_mon_hoc);
        spn_thu = findViewById(R.id.spn_thu);
        spn_tiet = findViewById(R.id.spn_tiet);
        spn_ma_phong_hoc.setOnItemSelectedListener(this);
        spn_mon_hoc.setOnItemSelectedListener(this);
        spn_thu.setOnItemSelectedListener(this);
        spn_tiet.setOnItemSelectedListener(this);

        String url = RemoteService.createURL() + "quanly/getAllSubject";
        new HttpAsyncMaMH().execute(url);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String maPH = edtPH.getText().toString();
//                String maMH = edtMH.getText().toString();
//                int thu = Integer.parseInt(edtThu.getText().toString());
//                int tiet = Integer.parseInt(edtTiet.getText().toString());

                final String url = "quanly/checkGioDiemDanh/" + selectedThu + "/" + selectedTiet;
                new AsyncCheckGioDiemDanh().execute(RemoteService.createURL() + url);
//                final String url = "sinhvien/getMaCaHoc/" + selectedMaPH + "/" + selectedMaMH + "/" + selectedThu + "/" + selectedTiet;
//                new AsyncGetMaCaHoc().execute(RemoteService.createURL() + url);

            }
        });
    }

    void setLayOutSpinner(Spinner spinner, ArrayList<String> listS) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main2Activity.this, android.R.layout.simple_spinner_item, listS);
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

    /* String getMaGiaoVien() {
         maGV = "";
         Context context;
         final EditText txtMaGV = new EditText(this);
         txtMaGV.setHint("VD: 1125215");
         final AlertDialog.Builder aler = new AlertDialog.Builder(this);
         aler.setView(txtMaGV);
         aler.setTitle("Mã Giáo Viên");
         aler.setMessage("Vui lòng nhập mã Giáo Viên của bạn!");
         aler.setCancelable(false);
         aler.setPositiveButton("Tiếp Tục", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 maGV = txtMaGV.getText().toString();
                 if (txtMaGV.equals("")) {
                     aler.setMessage("Vui long nhap vao ma GV");
                 }
                 if (!maGV.equalsIgnoreCase("")) {
 //                    layout.setVisibility(View.VISIBLE);
                     dialog.dismiss();
                 }
             }
         });

         Dialog dialog = aler.create();
         dialog.show();
         return maGV;
     }*/
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

    class AsyncCheckGioDiemDanh extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RemoteService.GET(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equalsIgnoreCase("true")) {
                final String url = "sinhvien/getMaCaHoc/" + selectedMaPH + "/" + selectedMaMH + "/" + selectedThu + "/" + selectedTiet;
                new AsyncGetMaCaHoc().execute(RemoteService.createURL() + url);
            } else {
                androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Main2Activity.this);
                alert.setTitle("Kiểm tra giờ điểm danh");
                alert.setMessage("Chưa đến giờ điểm danh! Vui lòng thử lại sau.");
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
    }

    class AsyncGetMaCaHoc extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            return RemoteService.GET(strings[0]);
        }

        @Override
        protected void onPostExecute(final String s) {
            // Hiện progress bar
            progressDialog = new ProgressDialog(Main2Activity.this);
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
                    if (!s.equalsIgnoreCase("0")) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("maCaHoc", s);
                        startActivity((intent));
                    } else if (s.equalsIgnoreCase("0")) {
                        Toast.makeText(Main2Activity.this, "Lich hoc ko ton tai !", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(Main2Activity.this, "Lỗi vui lòng thử lại sau !", Toast.LENGTH_LONG).show();

                }
            }, 1000);
        }
    }
}
