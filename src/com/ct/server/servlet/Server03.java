package com.ct.server.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 返回响应协议
 */
public class Server03 {
    private ServerSocket serverSocket;
    private boolean isRunning;
    public static void main(String[] args) {
        Server03 server02 = new Server03();
        server02.start();
    }

    //启动服务
    public void start(){
        try{
            serverSocket = new ServerSocket(8888);//端口
            this.isRunning = true;
            receive();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("服务端启动失败");
        }

    }

    //接受连接处理
    public void receive(){
        while (isRunning){
            try {
                Socket client = serverSocket.accept();
                System.out.println("一个客户端建立了连接");
                new Thread(new Dispatcher(client)).start();

            }catch (IOException e){
                e.printStackTrace();
                System.out.println("客户端错误");
            }
        }

    }

    //关闭服务
    public void stop(){
        this.isRunning = false;
        try {
            serverSocket.close();
            System.out.println("Server Stop");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
