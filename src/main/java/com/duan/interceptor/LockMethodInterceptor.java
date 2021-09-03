package com.duan.interceptor;

import com.duan.annotation.CacheLock;
import com.duan.util.CacheKeyGenerator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @ClassName LockMethodInterceptor
 * @Author DuanJinFei
 * @Date 2021/4/25 16:24
 * @Version 1.0
 */
@Aspect // AOP 横切
@Configuration
public class LockMethodInterceptor {

    @Autowired
    public LockMethodInterceptor(StringRedisTemplate stringRedisTemplate, CacheKeyGenerator cacheKeyGenerator) {
        this.lockRedisTemplate = stringRedisTemplate;
        this.cacheKeyGenerator = cacheKeyGenerator;
    }
    private final StringRedisTemplate lockRedisTemplate;
    private final CacheKeyGenerator cacheKeyGenerator;

    // 环绕通知 添加了CacheLock注解的方法都执行拦截器里面的代码
    @Around("execution(public * *(..)) && @annotation(com.duan.annotation.CacheLock)")
    public Object interceptor(ProceedingJoinPoint pjb){
        MethodSignature signature = (MethodSignature) pjb.getSignature();
        Method method = signature.getMethod();
        CacheLock lock = method.getAnnotation(CacheLock.class);
        if (StringUtils.isEmpty(lock.prefix())){
            throw new RuntimeException("lock key don't null...");
        }
        final String lockKey = cacheKeyGenerator.getLockKey(pjb);
        final Boolean success =
                lockRedisTemplate.execute((RedisCallback<Boolean>) connection -> connection.set(lockKey.getBytes(),
                        new byte[0], Expiration.from(lock.expire(), lock.timeUnit()),
                        RedisStringCommands.SetOption.SET_IF_ABSENT));
        if (!success){
            throw new RuntimeException("请勿重复请求");
        }
        try {
            return pjb.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException("系统异常");
        }finally {
            // TODO 如果演示的话需要注释该代码;实际应该放开
            // lockRedisTemplate.delete(lockKey);
        }
    }

}
