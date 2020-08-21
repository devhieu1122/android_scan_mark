package com.example.projectcuoiki.create_student.utils;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.projectcuoiki.R;
import com.example.projectcuoiki.controller.RemoteService;
import com.example.projectcuoiki.model.Student;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Class_insert_import_student extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ArrayList<String> pathHistory;
    private int count;
    private String lastDirectory;
    String path = null;
    public final String TAG = "MainActivity";
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    private int maCaHoc;
    File file;

    TextView tv_Path;

    String getPath = "";

    Spinner spn_ma_phong_hoc, spn_mon_hoc, spn_thu, spn_tiet;
    private ProgressDialog progressDialog;
    Button btn_import, btn_tao, btn_huy, btn_back;
    LinearLayout layout;
    static String maGV;
    private String selectedMaMH = null;
    private String selectedMaPH = null;
    private String selectedThu = null;
    private String selectedTiet = null;

    int size = 0;

    private int kq = -1;
    private int maCa;
    private ArrayList<Student> listStudent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_import_student);

        pathHistory = new ArrayList<String>();

        String url = RemoteService.createURL() + "quanly/getAllSubject";
        new HttpAsyncMaMH().execute(url);


        init();
    }

    public void init() {
        listStudent = new ArrayList<>();
        btn_import = (Button) findViewById(R.id.btn_import);

        btn_tao = (Button) findViewById(R.id.btn_tao);
        btn_huy = (Button) findViewById(R.id.btn_huy);


        spn_ma_phong_hoc = findViewById(R.id.spn_ma_phong_hoc);
        spn_mon_hoc = findViewById(R.id.spn_mon_hoc);
        spn_thu = findViewById(R.id.spn_thu);
        spn_tiet = findViewById(R.id.spn_tiet);
        spn_ma_phong_hoc.setOnItemSelectedListener(this);
        spn_mon_hoc.setOnItemSelectedListener(this);
        spn_thu.setOnItemSelectedListener(this);
        spn_tiet.setOnItemSelectedListener(this);


        tv_Path = findViewById(R.id.tv_Path);

        btn_import.setOnClickListener(this);
        btn_huy.setOnClickListener(this);
        btn_tao.setOnClickListener(this);

    }

    void setLayOutSpinner(Spinner spinner, ArrayList<String> listS) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Class_insert_import_student.this, android.R.layout.simple_spinner_item, listS);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);

    }

    String selectItem = "";

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectItem = parent.getItemAtPosition(position).toString();
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


    ///////////////////////////////////////////////////////////import////////////////////////////////


    ListView listView;
    Dialog dialog;

    public void eventDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_choosefile);
        dialog.setTitle("Choose file");
        listView = dialog.findViewById(R.id.list_item);
//        btn_back = findViewById(R.id.btn_back);
//        btn_back.setOnClickListener(this);

        // lay path cua file
        count = 0;
        pathHistory = new ArrayList<String>();
        pathHistory.add(count, System.getenv("EXTERNAL_STORAGE"));
        Log.d(TAG, "btnSDCard: " + pathHistory.get(count));
        checkInternalStorage();
    }


    public void onClick(View v) {
        if (btn_import == v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    eventDialog();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                            lastDirectory = pathHistory.get(count);
                            if (lastDirectory.equals(adapterView.getItemAtPosition(i))) {
                                Log.d("MainActivity", "lvInternalStorage: Selected a file for upload: " + lastDirectory);

                                //Execute method for reading the excel data.
                                //                    readExcelData(lastDirectory);
                                TextView textView = dialog.findViewById(R.id.tv_path);
                                textView.setText(lastDirectory);
                                path = lastDirectory;
                            } else {
                                count++;
                                pathHistory.add(count, (String) adapterView.getItemAtPosition(i));
                                checkInternalStorage();
                                Log.d(TAG, "lvInternalStorage: " + pathHistory.get(count));
                            }
                        }
                    });
                    //
                    Button btn_memory = dialog.findViewById(R.id.btn_memory);
                    btn_memory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                            tv_Path.setText(path);
                            tv_Path.setGravity(Gravity.CENTER);

                        }
                    });


                    Button btn_cannel = dialog.findViewById(R.id.btn_back);
                    btn_cannel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (count == 0) {
                                Log.d("MainActivity", "btnUpDirectory: You have reached the highest level directory.");
                            } else {
                                if (count == 0) {
                                    Log.d(TAG, "btnUpDirectory: You have reached the highest level directory.");
                                } else {
                                    pathHistory.remove(count);
                                    count--;
                                    checkInternalStorage();
                                    Log.d(TAG, "btnUpDirectory: " + pathHistory.get(count));
                                }
                            }
                        }
                    });

                    dialog.show();
                } else {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(Class_insert_import_student.this);
                    builder.setTitle("Truy cập bộ nhớ");
                    builder.setMessage("Vui lòng cấp quyền truy cập bộ nhớ để tiếp tục");
                    builder.setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Class_insert_import_student.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }


        } else if (btn_tao == v) {
            if (path == null || selectedMaMH == null || selectedMaPH == null || selectedThu == null || selectedTiet == null) {
                showDialogResult(Class_insert_import_student.this, "Lỗi", "Bạn phải chọn đầy đủ thông tin.");
            } else {
                readExcelData(path);

                String url1 = RemoteService.createURL() + "sinhvien/getMaCaHoc/" + selectedMaPH + "/" + selectedMaMH + "/" + selectedThu + "/" + selectedTiet;
                new HttpAsyncGetMaCaHoc().execute(url1);

//                Log.i(TAG, "onClick: " + kq);
//
                if (kq == 1) {
                    progressBar.setVisibility(View.VISIBLE);
                    setProgressBar(progress);
//                    showDialogResult(Class_insert_import_student.this, "Lỗi", "ok.");
                }
                if (kq == -1) {
                    showDialogResult(Class_insert_import_student.this, "Lỗi", "Sinh viên đã được thêm trước đó.");
                }

            }
//        } else if (btn_back == v) {
//            onBackPressed();
        } else {
            finish();
        }

    }

    // read excel
    public void readExcelData(String filePath) {
        Log.d(TAG, "readExcelData: Reading Excel File.");

        //decarle input file
        File inputFile = new File(filePath);

        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb = new StringBuilder();

            //outter loop, loops through rows
            for (int r = 1; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                //inner loop, loops through columns
                for (int c = 0; c < cellsCount; c++) {
                    //handles if there are to many columns on the excel sheet.
                    if (c > 4) {
                        Log.e(TAG, "readExcelData: ERROR. Excel File Format is incorrect! ");
                        toastMessage("ERROR: Excel File Format is incorrect!");
                        break;
                    } else {
                        String value = getCellAsString(row, c, formulaEvaluator);
                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                        Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
                        sb.append(value + ", ");
                    }
                }
                sb.append(":");
            }
            Log.d(TAG, "readExcelData: STRINGBUILDER: " + sb.toString());

            parseStringBuilder(sb);

        } catch (FileNotFoundException e) {
            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "readExcelData: Error reading inputstream. " + e.getMessage());
        }
    }


    // lay du lieu len
    String errr = "";

    private void parseStringBuilder(StringBuilder mStringBuilder) {

        Log.d(TAG, "parseStringBuilder: Started parsing.");


        // splits the sb into rows.
        String[] rows = mStringBuilder.toString().split(":");

        //Add to the ArrayList<XYValue> row by row
        for (int i = 0; i < rows.length; i++) {
            //Split the columns of the rows
            String[] columns = rows[i].split(",");

            //use try catch to make sure there are no "" that try to parse into doubles.
            try {

                String mssv = columns[1];
                String lastName = columns[2];
                String firtName = columns[3];
                String classs = columns[4];

                String cellInfo = "(mssv, lastName, firtName, class): (" + mssv + "," + lastName + "," + firtName + "," + classs + ")";
//                String tensv = new String(firtName.getBytes(), StandardCharsets.UTF_8);
                String tensv = lastName.concat(firtName);
                String maPhongHoc = selectItem;

                listStudent.add(new Student(mssv, tensv, classs));

                //Kich thuoc cua mangs
                size = rows.length;


            } catch (NumberFormatException e) {

                Log.e(TAG, "parseStringBuilder: NumberFormatException: " + e.getMessage());

            }
        }


        // thêm sinh viên vào lớp học


    }

    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {

            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage());
        }
        return value;
    }

    public void checkInternalStorage() {
        Log.d(TAG, "checkInternalStorage: Started.");
        try {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                toastMessage("No SD card found.");
            } else {
                // Locate the image folder in your SD Car;d
                file = new File(pathHistory.get(count));
//                Log.d(TAG, "checkInternalStorage: directory path: " + pathHistory.get(count));
            }

            listFile = file.listFiles();

            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];

            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();

            }

            for (int i = 0; i < listFile.length; i++) {
//                Log.d("Files", "FileName:" + listFile[i].getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FilePathStrings);
            listView.setAdapter(adapter);

        } catch (NullPointerException e) {
            Log.e(TAG, "checkInternalStorage: NULLPOINTEREXCEPTION " + e.getMessage());
        }
//        return FilePathStrings;
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // progressbar
    AtomicBoolean isrunning = new AtomicBoolean(false);
    private ProgressBar progressBar;
    int progress = 0;

    public void eventProgressBar() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

    }

    Thread thread;

    public void setProgressBar(final int progress) {
        progressBar.setProgress(0);
        // thread
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    // lay message  tư main thread
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setProgressBar(progress + 5);

                Message msg = handler.obtainMessage();
                msg.arg1 = progress;
                handler.sendMessage(msg);
                if (progress > 100 && thread != null) {
                    stopThread(thread);
                }
            }
        });
        thread.start();

    }

    private synchronized void stopThread(Thread theThread) {
        if (theThread != null) {
            theThread.interrupt();
            Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            finish();
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

            int ma = Integer.parseInt(s);
//            ma = maCa;
            if (ma > 0) {

                for (Student stu : listStudent) {
                    try {
                        URI uri = new URI(
                                "http",
                                null,
                                "192.168.1.96",
                                8899,
                                "/quanly/themSinhVien/" + stu.getMaSV().trim() + "/" + URLEncoder.encode(stu.getTenSV().trim(), "UTF-8") + "/" + stu.getMaLopHoc().trim() + "/" + ma,
                                null,
                                null);
                        String request = uri.toString();
                        new HttpAsyncAddAStudent().execute(request);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
            super.onPostExecute(s);
        }
    }

    class HttpAsyncAddAStudent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RemoteService.GET(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);
//            setProgressBar(progress);


        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);

            if (s.equals("true")) {
                kq = 1;
            }
        }
    }


    private void showDialogResult(Context context, String title, String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setCancelable(false);
        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = alert.create();
        dialog.show();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int count = msg.arg1;
            progressBar.setProgress(count);
            btn_huy.setEnabled(false);
            btn_import.setEnabled(false);
            btn_tao.setEnabled(false);
            Log.d(TAG, "" + count);
        }
    };


}
