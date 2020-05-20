package com.ct.server.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.*;

/**
 * 封装请求协议：获取method uri以及请求参数
 * 封装请求参数为map
 */
public class Request {
    //协议信息
    private String requestInfo;
    //请求方式
    private String method;
    //请求url
    private String url;
    //请求参数
    private String queryStr;
    //存储参数
    private Map<String, List<String>> parameterMap;

    private final String CRLF = "\r\n";

    public  Request(){

    }

    public  Request(Socket client) throws IOException {
        this(client.getInputStream());
    }

    public Request(InputStream is){
        this.parameterMap = new HashMap<String,List<String>>();
        byte[] data = new byte[1024*1024];
        int len;
        try {
            len = is.read(data);
            this.requestInfo = new String(data,0,len);
//            System.out.println(requestInfo);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //分解字符串
        parseRequestInfo();
    }

    //分解字符串
    private void parseRequestInfo(){
        System.out.println("----解析字符串-----");

        System.out.println("---- request method ---- head to st/");
        this.method = this.requestInfo.substring(0,this.requestInfo.indexOf("/")).trim();//第一个/的位置就是method
        System.out.println("method: " + method);

        System.out.println("---- request url ----- st/ to HTTP/----");
        int headIdx = this.requestInfo.indexOf("/")+1;
        int tailIdx = this.requestInfo.indexOf("HTTP/");
        this.url = this.requestInfo.substring(headIdx,tailIdx).trim();
        int queryIdx = this.url.indexOf("?");
        if (queryIdx>=0) {//存在请求参数
            String[] urlArray = this.url.split("\\?");
            this.url = urlArray[0].trim();
            queryStr = urlArray[1].trim();
        }


        System.out.println("url: "+this.url);

        System.out.println("---get request args----");

        if(method.equals("POST")||method.equals("post")){
            String qStr = this.requestInfo.substring(this.requestInfo.lastIndexOf(CRLF)).trim();
            if (null==queryStr){
                queryStr = qStr;
            }else {
                if (qStr.length()>0){
                    queryStr += "&" +qStr;
                }
            }
        }

        queryStr = queryStr == null ? "":queryStr;
        System.out.println("method: "+method+" ---> url: "+url+" ---> queryStr: "+queryStr);

        //
        convertMap();

    }

    private void convertMap(){
        String[] keyValues = this.queryStr.split("&");
        for (String query: keyValues){
            String[] kv = query.split("=");
            kv = Arrays.copyOf(kv,2);
            //获取key value
            String key = kv[0];
            String value = kv[1]==null?null:decode(kv[1],"utf-8");

            if (!parameterMap.containsKey(key)){
                parameterMap.put(key,new ArrayList<String>());
            }
            parameterMap.get(key).add(value);
        }
    }

    public String[] getParameterValues(String key){
        List<String> values = this.parameterMap.get(key);
        if (null==values||values.size()<1){
            return null;
        }
        return values.toArray(new String[0]);
    }

    public String getParameter(String key){
        String[] values = this.getParameterValues(key);
        return values == null?null:values[0];
    }

    /**
     * 处理中文
     * @return
     */
    private String decode(String value,String enc){
        try {
            return URLDecoder.decode(value,enc);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }
}
