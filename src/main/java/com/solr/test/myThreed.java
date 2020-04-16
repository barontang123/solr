package com.solr.test;

public class myThreed implements Runnable {
    int i = 5;
    @Override
    public void run() {
        i--;
        System.out.println("线程名："+Thread.currentThread().getName()+"现在数据："+i);
    }

}
