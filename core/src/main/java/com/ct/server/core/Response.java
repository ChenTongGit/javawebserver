package com.ct.server.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

/**
 * 封装响应信息
 * 1、内容可以动态添加
 * 2、关注状态码 404 拼接好响应信息
 */
public class Response {
    private BufferedWriter bw;
    //正文
    private StringBuilder content;
    //协议头信息
    private StringBuilder headInfo;
    //正文字节数
    private int len ;

    private final String BLANK = "";

    private final String CRLF = "\r\n";

    public Response(){
        content = new StringBuilder();
        headInfo = new StringBuilder();
        len = 0;
    }
    public Response(Socket client) {
        this();
        try {
            this.bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            headInfo = null;
        }

    }
    public Response(OutputStream os){
        this();
        bw = new BufferedWriter(new OutputStreamWriter(os));
    }

    //动态添加内容
    public Response print(String info){
        content.append(info);
        len += info.getBytes().length;
        return this;
    }

    public Response println(String info){
        content.append(info).append(CRLF);
        len += (info+CRLF).getBytes().length;
        return this;
    }

    //构建头部信息
    private void createHeadInfo(int code){
        //响应行：HTTP/1.1 200 OK
        headInfo.append("HTTP/1.1").append(BLANK);
        headInfo.append(code).append(BLANK);
        switch (code){
            case 200:
                headInfo.append("OK").append(CRLF);
                break;
            case 404:
                headInfo.append("NOT FOUND").append(CRLF);
                break;
            case 505:
                headInfo.append("SERVER ERROR").append(CRLF);
                break;
        }
        headInfo.append("Date:").append(new Date()).append(CRLF);
        headInfo.append("Server:").append("ct Server/0.0.1;charset=UTF-8").append(CRLF);
        headInfo.append("Content-type:text/html").append(CRLF);
        headInfo.append("Content-length:").append(len).append(CRLF);
        headInfo.append(CRLF);

    }

    //推送信息
    public void pushToBrowser(int code) throws IOException{
        if(null == headInfo){
            code = 505;
        }
        createHeadInfo(code);
        bw.append(headInfo);
        bw.append(content);
        bw.flush();
    }
}
