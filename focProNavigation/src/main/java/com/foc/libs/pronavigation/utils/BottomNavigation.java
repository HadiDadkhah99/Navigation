package com.foc.libs.pronavigation.utils;


import com.foc.libs.pronavigation.interfaces.ItemClicked;

public abstract class BottomNavigation {


    public BottomNavigation() {

    }

    /*
     * if bottom navigation item selected
     */
    public abstract void setOnItemSelected(ItemClicked itemClicked);

    /*
     * select bottom nav menu
     */
    public abstract void setItemActiveIndex(int pos);

}
