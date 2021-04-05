package com.foc.navigation;


import android.view.MenuItem;

import com.foc.libs.pronavigation.interfaces.ItemClicked;
import com.foc.libs.pronavigation.utils.BottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;


public class BVNavigation extends BottomNavigation {

    private final BottomNavigationView bottomNavigationView;

    public BVNavigation(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }

    @Override
    public void setOnItemSelected(ItemClicked itemClicked) {

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id._home)
                itemClicked.clicked(0);
            else if (item.getItemId() == R.id._news)
                itemClicked.clicked(1);
            else if (item.getItemId() == R.id._profile)
                itemClicked.clicked(2);

            return true;
        });

    }

    @Override
    public void setItemActiveIndex(int pos) {

        switch (pos) {
            case 0:
                bottomNavigationView.setSelectedItemId(R.id._home);
                break;
            case 1:
                bottomNavigationView.setSelectedItemId(R.id._news);
                break;
            case 2:
                bottomNavigationView.setSelectedItemId(R.id._profile);
                break;
        }

    }

}
