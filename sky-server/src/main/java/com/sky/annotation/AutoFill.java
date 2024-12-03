package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识某个方法需要进行功能字段自动填充处理
 */
@Target(ElementType.METHOD)     //指定当前注解只能加在方法上
@Retention(RetentionPolicy.RUNTIME)     //指定当前注解的生命周期，这里表示注解在运行时会被 JVM 保留
public @interface AutoFill {
    //指定数据库操作类型: UPDATE, INSERT
    //这里的value是注解的一个属性，它的作用是接收一个 OperationType 类型的值，该值用来表示操作的类型（例如 INSERT 或 UPDATE）。
    OperationType value();
}
