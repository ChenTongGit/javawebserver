package com.ct.server.servlet;

import java.io.IOException;
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
            Servlet servlet = WebApp.getServletFromURL(request.getUrl());
            if (null!=servlet){
                servlet.service(request,response);
                response.pushToBrowser(200);
            }else{
                response.pushToBrowser(404);
            }

        }catch (Exception e){
            try {
                response.pushToBrowser(500);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    private void release(){
        try {
            this.client.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
