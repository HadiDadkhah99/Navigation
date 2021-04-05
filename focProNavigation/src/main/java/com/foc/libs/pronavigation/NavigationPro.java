package com.foc.libs.pronavigation;

import android.content.Context;
import android.os.Bundle;

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
    private int lastPos = -1;

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

    /*
     *destination change listener
     */

    public void setDestinationListener(DestinationChangeListener listener) {
        this.listener = listener;
    }

    /*
     *destination change
     */
    private void changeDestination(String current, String destination) {

        if (listener == null)
            return;

        listener.change(current, destination);

    }

    /*
     *destroy
     */
    public void destroy() {
        nav = null;
    }

    /*
     * singleTone class
     */
    public static NavigationPro init(Context context, int frameLayoutID) {
        return nav = nav == null ? new NavigationPro(context, frameLayoutID) : nav;
    }

    /*
     * singleTone class
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

    /*
     * add fragments in hashMap (just replace fragment and don't create instance in any replacement)
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

    /*
     * if added fragment is menu item
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


    /*
     * add Fragment to Back Stack (Navigation Pro Back Stack !!)
     */
    private void addNode(Node<Fragment> node) {
        this.nodes.add(node);
    }


    /*
     * show fragment in frame layout (with Bundle)
     */
    public void showFragment(Class<? extends Fragment> fragmentClass, Bundle bundle, boolean addToBackStack) {

        Node<Fragment> node = fragments.get(fragmentClass.getName());
        node.fragment().setArguments(bundle);

        replace(node, fragmentClass.getName(), addToBackStack);

    }

    /*
     * show fragment in frame layout (without Bundle)
     */
    public void showFragment(Class<? extends Fragment> fragmentClass, boolean addToBackStack) {

        Node<Fragment> node = fragments.get(fragmentClass.getName());
        node.fragment().setArguments(null);

        replace(node, fragmentClass.getName(), addToBackStack);

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

        ft.commit();

    }

    /*
     * restart fragment
     */
    public void restartFragment(Class<? extends Fragment> fragmentClass) {

        if (!currentFragment.equals(fragmentClass.getName()))
            return;

        //change destination
        changeDestination(currentFragment, fragmentClass.getName());

        FragmentTransaction ft = frm.beginTransaction();

        //get fragment
        Node<Fragment> node = fragments.get(fragmentClass.getName());

        //restart
        ft.detach(node.fragment());
        ft.attach(node.fragment());
        ft.commit();

    }

    public Fragment getFragment(String tag) {
        return fragments.get(tag).fragment();
    }


    /*
     * mange on back press
     */
    public void onBackPress(AppCompatActivity activity) {


        if (nodes.size() > 0) {

            if (isMenuItem(nodes.getLast()) && nodes.getLast().nodeTag().equals(menuItems.get(0).nodeTag())) {
                destroy();
                activity.finish();
            }


            //check last node is menu item
            else if (isMenuItem(nodes.getLast())) {

                for (int i = 0; i < lastNodes.size(); i++) {
                    lastNodes.get(i).clear();
                    lastNodes.get(i).add(menuItems.get(i));
                }

                nodes.clear();
                nodes.add(menuItems.get(0));
                lastPos = 0;
                bottomController.selectItem(lastPos);
                showFragment(nodes.getLast().fragment().getClass(), nodes.getLast().fragment().getArguments(), false);


            } else {
                lastNodes.get(lastPos).remove(lastNodes.get(lastPos).size() - 1);
                nodes.remove(nodes.size() - 1);
                showFragment(nodes.getLast().fragment().getClass(), nodes.getLast().fragment().getArguments(), false);

            }

        } else
            activity.finish();

    }

    public boolean isMenuItem(Node<Fragment> node) {

        for (Node<Fragment> menu : menuItems) {
            if (menu.nodeTag().equals(node.nodeTag())) {

                return true;
            }
        }

        return false;
    }


    @Override
    public void clicked(int pos) {


        if (this.lastPos == pos && !isMenuItem(nodes.getLast())) {

            showFragment(menuItems.get(pos).fragment().getClass(), menuItems.get(pos).fragment().getArguments(), false);
            nodes.add(menuItems.get(pos));
            lastNodes.get(pos).clear();
            lastNodes.get(pos).add(menuItems.get(pos));


            return;

        } else if (this.lastPos == pos) {

            return;
        }

        this.lastPos = pos;

        showFragment(lastNodes.get(pos).get(lastNodes.get(pos).size() - 1).fragment().getClass(), lastNodes.get(pos).get(lastNodes.get(pos).size() - 1).fragment().getArguments(), false);
        nodes.addAll(lastNodes.get(pos));


    }


    public void attachBottomNavigation(BottomNavigation bottomNavigationView) {


        bottomController = new BottomNavigationClickController(this, bottomNavigationView);
        //default show
        clicked(0);

    }


}
		
