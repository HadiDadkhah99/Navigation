package com.foc.navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.foc.libs.pronavigation.NavigationPro;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavigationPro navigationPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.bottom_view);

        //init nav
        this.navigationPro = NavigationPro.init(this, R.id.main_frame);

    }
}