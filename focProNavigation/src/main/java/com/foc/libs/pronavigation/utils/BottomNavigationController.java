package com.foc.libs.pronavigation.utils;


import com.foc.libs.pronavigation.NavigationPro;
import com.foc.libs.pronavigation.interfaces.ItemClicked;

public class BottomNavigationController implements ItemClicked {

    private final BottomNavigation btnNav;
    private final NavigationPro navigationPro;


    public BottomNavigationController(NavigationPro navigationPro, BottomNavigation btnNav) {

        this.btnNav = btnNav;
        this.navigationPro = navigationPro;
        btnNav.setOnItemSelected(this);


    }


    public void selectItem(int pos) {

        btnNav.setItemActiveIndex(pos);

    }


    @Override
    public void clicked(int pos) {

        navigationPro.clicked(pos);

    }

    public BottomNavigation getBtnNav() {
        return btnNav;
    }
}
