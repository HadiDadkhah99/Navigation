package com.foc.libs.pronavigation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.foc.libs.pronavigation.interfaces.DestinationChangeListener;
import com.foc.libs.pronavigation.interfaces.ItemClicked;
import com.foc.libs.pronavigation.utils.BottomNavigation;
import com.foc.libs.pronavigation.utils.BottomNavigationClickController;
import com.foc.libs.pronavigation.utils.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class NavigationPro implements ItemClicked {

    private static NavigationPro nav;

    //destination listener
    private DestinationChangeListener listener;

    //current fragment
    private String currentFragment;

    //nav controller
    private BottomNavigationClickController bottomController;

    //fragment manager
    private final FragmentManager frm;

    //all fragments
    private final Map<String, Node<Fragment>> fragments;


    //back stack list
    private final LinkedList<Node<Fragment>> nodes;
    //menu items
    private final List<Node<Fragment>> menuItems;
    //last nodes for any item
    private final List<List<Node<Fragment>>> lastNodes;


    //last item selected
    private int lastPos = 0;

    //frame layout
    private final int frameLayoutID;

    private NavigationPro(Context context, int frameLayoutID) {
        this.frameLayoutID = frameLayoutID;
        this.frm = ((AppCompatActivity) context).getSupportFragmentManager();
        this.nodes = new LinkedList<>();
        this.menuItems = new ArrayList<>();
        this.lastNodes = new ArrayList<>();
        this.fragments = new HashMap<>();


    }


    /**
     * set destination change listener
     */
    public void setDestinationListener(DestinationChangeListener listener) {
        this.listener = listener;
    }


    /**
     * destroy navigation after activity destroyed
     * in onDestroy() activity
     */
    public void destroy() {
        nav = null;
    }


    /**
     * (create) navigation class in first time
     * init in onCreate() method activity
     */
    public static NavigationPro init(Context context, int frameLayoutID) {
        return nav = nav == null ? new NavigationPro(context, frameLayoutID) : nav;
    }


    /**
     * (get) navigation class after init() method called
     * for example in onCreateView() method of fragment call this method
     */
    public static NavigationPro get() {

        if (nav != null)
            return nav;
        else try {
            throw new Exception("You must first ini !");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nav;
    }


    /**
     * add the fragments that not menu item
     */
    public NavigationPro addFragment(Class<? extends Fragment> fragmentClass) {

        try {
            Fragment fragment = fragmentClass.newInstance();
            this.fragments.put(fragmentClass.getName(), new Node<>(fragment, fragmentClass.getName()));

        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return this;
    }


    /**
     * add fragments are menu item
     */
    public NavigationPro addFragment(Class<? extends Fragment> fragmentClass, boolean isMenu) {

        try {
            Fragment fragment = fragmentClass.newInstance();
            Node<Fragment> node = new Node<>(fragment, fragmentClass.getName());
            this.fragments.put(fragmentClass.getName(), node);
            if (isMenu) {
                this.menuItems.add(node);
                this.lastNodes.add(new ArrayList<>());
                this.lastNodes.get(menuItems.size() - 1).add(node);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }


        return this;
    }


    /**
     * show fragment (with Bundle)
     */
    public void showFragment(Class<? extends Fragment> fragmentClass, Bundle bundle, boolean addToBackStack) {

        Node<Fragment> node = fragments.get(fragmentClass.getName());
        node.fragment().setArguments(bundle);

        replace(node, fragmentClass.getName(), addToBackStack);

    }


    /**
     * show fragment (without Bundle)
     */
    public void showFragment(Class<? extends Fragment> fragmentClass, boolean addToBackStack) {

        Node<Fragment> node = fragments.get(fragmentClass.getName());
        node.fragment().setArguments(null);

        replace(node, fragmentClass.getName(), addToBackStack);

    }


    /**
     * restart fragment (without bundle)
     */
    public void restartFragment(Class<? extends Fragment> fragmentClass) {

        if (!currentFragment.equals(fragmentClass.getName()))
            return;

        //change destination
        changeDestination(currentFragment, fragmentClass.getName());

        //get fragment
        Node<Fragment> node = fragments.get(fragmentClass.getName());

        FragmentTransaction ft = frm.beginTransaction();
        //restart
        ft.detach(node.fragment());
        ft.attach(node.fragment());
        ft.commit();

    }


    /**
     * restart fragment (with bundle)
     */
    public void restartFragment(Class<? extends Fragment> fragmentClass, Bundle bundle) {

        if (!currentFragment.equals(fragmentClass.getName()))
            return;

        //change destination
        changeDestination(currentFragment, fragmentClass.getName());

        //get fragment
        Node<Fragment> node = fragments.get(fragmentClass.getName());

        //set bundle
        if (bundle != null)
            node.fragment().setArguments(bundle);

        FragmentTransaction ft = frm.beginTransaction();
        //restart
        ft.detach(node.fragment());
        ft.attach(node.fragment());
        ft.commit();
    }


    /**
     * get fragment
     */
    public Fragment getFragment(Class<? extends Fragment> fragmentClass) {

        return fragments.get(fragmentClass.getName()).fragment();

    }


    /**
     * mange activity on back press
     * call this method in onBackPress() method of activity
     */
    public void onBackPress(AppCompatActivity activity) {


        // if node size (back stack size) > 0
        if (nodes.size() > 0) {


            /*
             *last fragment is menu item and its first bottom navigation item
             *then finish activity
             */
            if (isMenuItem(nodes.getLast()) && nodes.getLast().nodeTag().equals(menuItems.get(0).nodeTag())) {
                destroy();
                activity.finish();
            }


            /*
             *last fragment is menu item
             */
            else if (isMenuItem(nodes.getLast())) {


                //*********************clear all back stack
                for (int i = 0; i < lastNodes.size(); i++) {
                    //clear all back stack of item
                    lastNodes.get(i).clear();
                    //set menu item as first back stack of any item
                    lastNodes.get(i).add(menuItems.get(i));
                }

                //clear back stack
                nodes.clear();
                //add first menu item to first back stack
                nodes.add(menuItems.get(0));
                //set first item as current item (0)
                lastPos = 0;
                //bottom navigation view select index 0
                if (bottomController != null)
                    bottomController.selectItem(lastPos);
                //show fragment
                showFragment(nodes.getLast().fragment().getClass(), nodes.getLast().fragment().getArguments(), false);


            }
            /*
             * show fragment in back stack
             */
            else {
                //remove last fragment
                lastNodes.get(lastPos).remove(lastNodes.get(lastPos).size() - 1);
                //remove from back stack
                nodes.remove(nodes.size() - 1);
                //show
                showFragment(nodes.getLast().fragment().getClass(), nodes.getLast().fragment().getArguments(), false);

            }

        }
        //if back stack size is 0
        else
            activity.finish();

    }


    /**
     * menu item clicked
     */
    @Override
    public void clicked(int pos) {


        /*
         *if last item and clicked item is equal and last fragment its not menu item
         * then clear item back stack
         */
        if (this.lastPos == pos && !isMenuItem(nodes.getLast())) {

            showFragment(menuItems.get(pos).fragment().getClass(), menuItems.get(pos).fragment().getArguments(), false);
            nodes.add(menuItems.get(pos));
            lastNodes.get(pos).clear();
            lastNodes.get(pos).add(menuItems.get(pos));

            return;

        }
        //if last item and clicked item is equal
        else if (this.lastPos == pos)
            return;


        //set last item
        this.lastPos = pos;

        //show fragment
        showFragment(lastNodes.get(pos).get(lastNodes.get(pos).size() - 1).fragment().getClass(), lastNodes.get(pos).get(lastNodes.get(pos).size() - 1).fragment().getArguments(), false);
        nodes.addAll(lastNodes.get(pos));


    }


    /**
     * attach bottom navigation
     * create class that extends of BottomNavigation (Required for BottomNavigationView integration)
     */
    public void attachBottomNavigation(BottomNavigation bottomNavigationView) {


        bottomController = new BottomNavigationClickController(this, bottomNavigationView);
        //default show
        clicked(0);

    }


    /**
     * if destination changed
     */
    private void changeDestination(String current, String destination) {

        if (listener == null)
            return;

        listener.change(current, destination);

    }

    /**
     * check fragment is menu item or not
     */
    private boolean isMenuItem(Node<Fragment> node) {

        for (Node<Fragment> menu : menuItems) {
            if (menu.nodeTag().equals(node.nodeTag())) {

                return true;
            }
        }

        return false;
    }


    /*
     * replace fragment
     */
    private void replace(Node<Fragment> node, String fragTag, boolean addToBackStack) {
        //change destination
        changeDestination(currentFragment, fragTag);

        //set current fragment
        this.currentFragment = fragTag;

        FragmentTransaction ft = frm.beginTransaction();
        ft.replace(frameLayoutID, node.fragment(), fragTag);

        if (addToBackStack) {
            addNode(node);
            this.lastNodes.get(lastPos).add(node);
        }

        //commit
        ft.commit();

    }


    /*
     * add Fragment to Back Stack (Navigation Pro Back Stack !!)
     */
    private void addNode(Node<Fragment> node) {
        this.nodes.add(node);
    }
}
		
