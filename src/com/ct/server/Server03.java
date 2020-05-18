package com.ct.server;

import com.ct.server.servlet.LoginServlet;
import com.ct.server.servlet.Servlet;
import com.ct.server.servlet.WebApp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * 返回响应协议
 */
public class Server03 {
    private ServerSocket serverSocket;
    public static void main(String[] args) {
        Server03 server02 = new Server03();
        server02.start();
    }

    //启动服务
    public void start(){
        try{
            serverSocket = new ServerSocket(8888);//端口
            receive();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("服务端启动失败");
        }

    }

    //接受连接处理
    public void receive(){
        try {
            Socket client = serverSocket.accept();
            System.out.println("一个客户端建立了连接");
//            InputStream is = client.getInputStream();



//            StringBuilder content = new StringBuilder();

//            int size = content.toString().getBytes().length; //字节长度
//            StringBuilder responseInfo = new StringBuilder();
//            String blank = " ";
//            String CRLF = "\r\n";

            //返回
            //1 响应的状态行:HTTP/1.1 200 OK
//            responseInfo.append("HTTP/1.1").append(blank);
//            responseInfo.append("200").append(blank);
//            responseInfo.append("OK").append(CRLF);
            //2 响应头:
            /*

             */
//            responseInfo.append("Date:").append(new Date()).append(CRLF);
//            responseInfo.append("Server:").append("ct Server/0.0.1;charset=UTF-8").append(CRLF);
//            responseInfo.append("Content-type:text/html").append(CRLF);
//            responseInfo.append("Content-length:").append(size).append(CRLF);
//            responseInfo.append(CRLF);

            //3 正文
//            responseInfo.append(content.toString());

            //写到客户端

//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
//            bw.write(responseInfo.toString());
//            bw.flush();
            Request request = new Request(client);
            Response response = new Response(client);
            //只关注内容
            Servlet servlet = WebApp.getServletFromURL(request.getUrl());
//            if (request.getUrl().equals("login")){
//                servlet = new LoginServlet();
//            }else if (request.getUrl().equals("reg")){
//                servlet = new LoginServlet();
//            }
            if(null!=servlet){
                servlet.service(request,response);
                response.pushToBrowser(200);
            }else {
                //404
                response.pushToBrowser(404);
            }


        }catch (IOException e){
            e.printStackTrace();
            System.out.println("客户端错误");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //关闭服务
    public void stop(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
