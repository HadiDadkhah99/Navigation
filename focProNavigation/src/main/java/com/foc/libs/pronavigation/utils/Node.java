package com.foc.libs.pronavigation.utils;

public class Node<T> {

    private final String nodeTag;
    private final T t;


    public Node(T t, String nodeTag) {
        this.t = t;
        this.nodeTag = nodeTag;
    }



    public T fragment() {
        return this.t;
    }

    public String nodeTag() {
        return this.nodeTag;
    }


}//end node class
