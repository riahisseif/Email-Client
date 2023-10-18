package com.stageProject.model;

public class SizeInteger implements Comparable<SizeInteger> {
    //Comparable to trie les champ
    //we will replace the size in bite and kilobite
    private int size;

    public SizeInteger(int size) {
        this.size = size;
    }
    //we need to override the toString methode
    @Override
    public String toString(){
        if (size <= 0) {
            return "0";
        } else if (size < 1024) {
            return size + " B";//trasfer to bits
        } else if (size < 1048576) {
            return size / 1024 + " kB";//transfer to kilobite
        } else {
            return size / 1048576 + " MB";//transfer to megabyte
        }
    }

    @Override
    public int compareTo(SizeInteger o) {
        if(size > o.size) {
            return 1;
        } else if(o.size > size) {
            return -1;
        } else {//if equals
            return 0;
        }
    }
}
