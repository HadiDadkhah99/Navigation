package com.foc.libs.pronavigation.utils;

import androidx.annotation.AnimatorRes;

public class Animation {

    @AnimatorRes
    public int enter;
    @AnimatorRes
    public int exit;
    @AnimatorRes
    public int popEnter=-1;
    @AnimatorRes
    public int popExit=-1;

    public Animation() {
    }

    public Animation(int enter, int exit, int popEnter, int popExit) {
        this.enter = enter;
        this.exit = exit;
        this.popEnter = popEnter;
        this.popExit = popExit;
    }


}
