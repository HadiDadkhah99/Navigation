package com.foc.navigation;




import com.foc.libs.pronavigation.interfaces.ItemClicked;
import com.foc.libs.pronavigation.utils.BottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class BottomNavigationV extends BottomNavigation {

    private final BottomNavigationView bottomNavigationView;

    public BottomNavigationV(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }

    @Override
    public void setOnItemSelected(ItemClicked itemClicked) {


    }

    @Override
    public void setItemActiveIndex(int pos) {


    }
}
