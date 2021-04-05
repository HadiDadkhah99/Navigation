package com.foc.navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.foc.libs.pronavigation.NavigationPro;
import com.foc.navigation.fragment.HomeFragment;
import com.foc.navigation.fragment.ProfileFragment;
import com.foc.navigation.fragment.ShopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavigationPro navigationPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_view);

        //init nav
        this.navigationPro = NavigationPro.init(this, R.id.main_frame);
        this.navigationPro.
                addFragment(HomeFragment.class, true).
                addFragment(ShopFragment.class, true).
                addFragment(ProfileFragment.class, true);


        this.navigationPro.attachBottomNavigation(new BVNavigation(bottomNavigationView));
    }
}