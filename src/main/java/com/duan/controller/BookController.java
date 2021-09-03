package com.duan.controller;

import com.duan.annotation.CacheLock;
import com.duan.annotation.CacheParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName BookController
 * @Author DuanJinFei
 * @Date 2021/4/25 16:36
 * @Version 1.0
 */
@RestController
@RequestMapping("/books")
public class BookController {


    @CacheLock(prefix = "books")
    @GetMapping
    public String query(@CacheParam(name = "token") @RequestParam String token) {
        return "success - " + token;
    }
}
