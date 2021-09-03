package com.duan.util;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @ClassName CacheKeyGenerator
 * @Author DuanJinFei
 * key生成器
 * @Date 2021/4/25 16:10
 * @Version 1.0
 */
public interface CacheKeyGenerator {

    /**
     * 获取AOP参数，生成指定缓存Key
      * @param pjp
     * @return
     */
    String getLockKey(ProceedingJoinPoint pjp);
}
