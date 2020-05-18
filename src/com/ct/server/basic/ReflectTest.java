package com.ct.server.basic;

import java.lang.reflect.InvocationTargetException;
//sax 获取解析工厂 获取解析器 加载文档注册处理器 编写处理器
// dom
public class ReflectTest {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //三种方法
        //1对象 getClass
        Iphone iphone = new Iphone();
        Class clz = iphone.getClass();

        //2 类class
        clz = Iphone.class;

        //3 forname
        clz = Class.forName("com.ct.server.basic.Iphone");

        //创建对象
        Iphone iphone1 = (Iphone) clz.getConstructor().newInstance();
        System.out.println(iphone1);
    }
}

class Iphone{
    public Iphone(){}
}
