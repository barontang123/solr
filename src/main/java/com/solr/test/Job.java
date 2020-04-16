package com.solr.test;

import java.util.concurrent.Callable;

public class Job implements Callable<String> {

    private String name;

    private String jobName;
    public Job(String name)
    {
        this.name=name;
    }


    @Override
    public String call() throws Exception {
        this.jobName = Thread.currentThread().getName();
        //使用传入的参数执行一些其他的操作，比如对数据库进行操作
        return "单钱线程为：" + jobName +",传入的参数为："+ name;
    }
}
