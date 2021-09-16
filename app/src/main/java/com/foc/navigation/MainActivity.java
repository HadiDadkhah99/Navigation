package com.foc.navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.foc.libs.pronavigation.NavigationPro;
import com.foc.navigation.fragment.HomeFragment;
import com.foc.navigation.fragment.ProductDetailFragment;
import com.foc.navigation.fragment.ProductFragment;
import com.foc.navigation.fragment.ProfileFragment;
import com.foc.navigation.fragment.ProfileSettingsFragment;
import com.foc.navigation.fragment.NewsFragment;
import com.foc.navigation.fragment.TopNewsFragment;
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
                addFragment(HomeFragment.class, true);
        this.navigationPro.showFragment(HomeFragment.class, null, false);


    }

    /*
     *add this code for own activity
     * mange back stack in pro navigation
     */
    @Override
    public void onBackPressed() {
        navigationPro.onBackPress(this);
    }

    /*
     * destroy navigation (importance)
     */
    @Override
    protected void onDestroy() {
        navigationPro.destroy();
        super.onDestroy();
    }
}