package com.nervous.common;



/**
 * 基于ThreadLocal封装的工具类，获取当前登录用户的id
 */
public class BaseContext {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static long getCurrentId(){
       return threadLocal.get();
    }
}
