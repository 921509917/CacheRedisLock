package com.duan.controller;

import org.springframework.beans.factory.annotation.Value;

/**
 * @ClassName TestValue
 * @Author DuanJinFei
 * @Date 2021/8/17 15:36
 * @Version 1.0
 */
public class Valuexx {
    @Value("xxx")
    private String cc;

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }
}
