package com.foc.navigation.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.foc.libs.pronavigation.NavigationPro;
import com.foc.navigation.R;

public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //get button
        Button button = view.findViewById(R.id.button_product);

        //button click
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //show product fragment (add to back stack)
                NavigationPro.get().showFragment(ProductFragment.class, true);

            }
        });

        return view;
    }
}