package com.example.projectcuoiki.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.projectcuoiki.mark.MarkActivity;
import com.example.projectcuoiki.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
   ImageView img_scan;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        img_scan =(ImageView) view.findViewById(R.id.img_scan);
        img_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MarkActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}