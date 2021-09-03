package com.duan;

import com.duan.util.CacheKeyGenerator;
import com.duan.util.LockKeyGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName RedisLockApplication
 * @Author DuanJinFei
 * @Date 2021/4/25 16:08
 * @Version 1.0
 */
@SpringBootApplication
public class RedisLockApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisLockApplication.class);
    }

    @Bean
    public CacheKeyGenerator cacheKeyGenerator(){
        return new LockKeyGenerator();
    }
}
