package com.duan.util;

import com.duan.annotation.CacheLock;
import com.duan.annotation.CacheParam;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @ClassName LockKeyGenerator
 * @Author DuanJinFei
 * @Date 2021/4/25 16:12
 * @Version 1.0
 */
public class LockKeyGenerator implements CacheKeyGenerator {
    @Override
    public String getLockKey(ProceedingJoinPoint pjp) {

        MethodSignature signature = (MethodSignature) pjp.getSignature(); // 获取切入点的签名
        Method method = signature.getMethod(); // 拿到切入方法

        CacheLock lockAnnotation = method.getAnnotation(CacheLock.class);

        final Object[] args = pjp.getArgs();
        final Parameter[] parameters = method.getParameters(); // 获取方法参数

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            final CacheParam annotation = parameters[i].getAnnotation(CacheParam.class);
            if (annotation == null){
                continue;
            }
            stringBuilder.append(lockAnnotation.delimiter()).append(args[i]);
        }

        if (StringUtils.isEmpty(stringBuilder.toString())){
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                final Object object = args[i];
                final Field[] declaredFields = object.getClass().getDeclaredFields(); // 获取方法全部声明的字段
                for (Field declaredField : declaredFields) {
                    final CacheParam annotation = declaredField.getAnnotation(CacheParam.class);
                    if (annotation == null) continue;
                    declaredField.setAccessible(true);
                    stringBuilder.append(lockAnnotation.delimiter()).append(ReflectionUtils.getField(declaredField,object));
                }
            }
        }
        return lockAnnotation.prefix() + stringBuilder.toString();
    }
}
