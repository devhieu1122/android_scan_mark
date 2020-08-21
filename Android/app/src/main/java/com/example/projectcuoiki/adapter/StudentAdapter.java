package com.example.projectcuoiki.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectcuoiki.R;
import com.example.projectcuoiki.model.Student;
import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {
    private Activity context;
    private int layout;
    private List<Student> list;

    public StudentAdapter(Context context, int resource, List<Student> objects) {
        super(context, resource, objects);
        this.context= (Activity) context;
        this.layout=resource;
        this.list=objects;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater flater=context.getLayoutInflater();
        View row=flater.inflate(layout, parent,false);

        TextView txt1=(TextView) row.findViewById(R.id.txt_StuID);
        TextView txt2=(TextView) row.findViewById(R.id.txt_Name);
        TextView txt3=(TextView) row.findViewById(R.id.txt_classID);

         Student data=list.get(position);

        txt1.setText(data.getMaSV()==null?"":data.getMaSV());
        txt2.setText(data.getTenSV()==null?"":data.getTenSV());
        txt3.setText(data.getMaLopHoc()==null?"":data.getMaLopHoc());
        if(position==0)
        {
            row.setBackgroundColor(Color.RED);
        }

        return row;
    }
}
