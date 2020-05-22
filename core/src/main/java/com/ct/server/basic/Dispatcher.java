package com.ct.server.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Dispatcher implements Runnable{
    private Socket client;
    private Request request;
    private Response response;

    public Dispatcher(Socket client){
        this.client = client;
        try {
            this.request = new Request(client);
            this.response = new Response(client);
        } catch (IOException e) {
            e.printStackTrace();
            release();
        }

    }
    @Override
    public void run() {
        try {
            //首页
            System.out.println("url"+request.getUrl()+request.getUrl().equals(""));
            if (null == request.getUrl()||request.getUrl().equals("")){
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("com\\ct\\server\\index.html");
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while ((line = br.readLine())!=null){
                    response.print(line);
                }
                br.close();
                response.pushToBrowser(200);
                return;
            }
            Servlet servlet = WebApp.getServletFromURL(request.getUrl());
            if (null!=servlet){
                servlet.service(request,response);
                response.pushToBrowser(200);
            }else{
                //错误
                //404 NOT FOUND
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("com\\ct\\server\\404.html");
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String line = null;
                while ((line = br.readLine())!=null){
                    response.print(line);
                }
                br.close();
                response.pushToBrowser(404);

            }

        }catch (Exception e){
            e.printStackTrace();
            try {
                response.pushToBrowser(500);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        release();//短连接

    }

    private void release(){
        try {
            this.client.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
