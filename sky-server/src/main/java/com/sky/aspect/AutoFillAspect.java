package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Aspect     //用于定义一个切面类, 切面类是实现切面编程的基本单元，用于在程序运行时动态地修改程序行为。
@Component  //将这个类交给Bean管理
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")    //进行粗拦截，告诉程序需要拦截的方法要满足这个条件
    public void autoFillPointCut() {}

    /**
     * 通知: 前置通知：在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        // joinPoint为拦截时传入的参数
        log.info("开始进行公共字段的自动填充...");

        //获取当前被拦截方法的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();     //方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);        //获得方法上的注解对象
        OperationType operationType = autoFill.value();     //获得数据库操作类型

        //获取当前被拦截的方法参数（实体对象）
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0) {      //如果没有参数，返回
            return;
        }
        Object entity = args[0];    //得到第一个参数

        //准备赋值的数据
        LocalDateTime localDateTime = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 根据当前不同操作类型，为对应的属性通过反射来赋值
        if(operationType == OperationType.INSERT) {
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);

                setCreateTime.invoke(entity, localDateTime);
                setUpdateTime.invoke(entity, localDateTime);
                setCreateUser.invoke(entity, currentId);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if(operationType == OperationType.UPDATE) {
            try {
                //Method setCreateTime = entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                //Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);

                //setCreateTime.invoke(entity, localDateTime);
                setUpdateTime.invoke(entity, localDateTime);
                //setCreateUser.invoke(entity, currentId);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
