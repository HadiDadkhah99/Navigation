package com.foc.navigation.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foc.libs.pronavigation.NavigationPro;
import com.foc.navigation.R;

public class NewsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        view.findViewById(R.id.button_news).setOnClickListener(v -> NavigationPro.get().showFragment(TopNewsFragment.class, true));

        return view;
    }
}