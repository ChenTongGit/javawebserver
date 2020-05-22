package com.ct.server.core.request;

import com.ct.server.core.constant.CharConstant;
import com.ct.server.core.constant.CharsetProperties;
import com.ct.server.core.enumeration.RequestMethod;
import com.ct.server.core.exception.RequestParseException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

/**
 * GET /login?username=%E9%99%88%E7%AB%A5&haha=324 HTTP/1.1
 * Host: localhost:8888
 * Connection: keep-alive
 * User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36
 * Sec-Fetch-Dest: empty
 * Accept: * / *
 * Sec-Fetch-Site:none
 * Sec-Fetch-Mode:cors
 * Accept-Encoding:gzip,deflate,br,Accept-Language:zh-CN,zh;q=0.9,zh-TW;q=0.8,en-US;q=0.7,en;q=0.6
 *
 * //
 *
 * POST /login HTTP/1.1
 * Host: localhost:8888
 * Connection: keep-alive
 * Content-Length: 46
 * Sec-Fetch-Dest: empty
 * User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36
 * Content-Type: text/plain;charset=UTF-8
 * Accept: * / *
 * Origin:chrome-extension://eejfoncpjfgmeleakejdcanedmefagga
 * Sec-Fetch-Site: none
 * Sec-Fetch-Mode: cors
 * Accept-Encoding: gzip, deflate, br
 * Accept-Language: zh-CN,zh;q=0.9,zh-TW;q=0.8,en-US;q=0.7,en;q=0.6
 *
 * "data":
 */

@Data
@Slf4j
public class Request {
    private RequestMethod method;
    private String url;
    private Map<String,List<String>> params;//(1 to many) multiple choice
    private Map<String,List<String>> headers;

    public void build(InputStream is) throws RequestParseException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, CharsetProperties.charset_cn));//UTF-8
        String line;
        List<String> lines = new ArrayList<>();
        try {
            while((line = br.readLine())!=null)
                lines.add(line);
        }catch (IOException e){
            e.printStackTrace();
        }

        if (lines.size()==0)
            throw new RequestParseException();
        //解析request报文
        parse(lines);
    }

    /**
     * parse the request
     * @param lines
     *
     */
    private void parse(List<String> lines){
        String firstLine = lines.get(0);//method parameter path

        //parse firstLine => Method URL Parameters HTTP/1.1
        String[] firstLineSlices = firstLine.split(CharConstant.BLANK);
        //method
        this.method = RequestMethod.valueOf(firstLineSlices[0].trim());
        log.info("method:{}",this.method);

        //url
        //split => urlSlices[]:[0]url?[1]parameter
        /** /login?username=Tommy&password=psw123&mul_choice=12,32,21 **/
        String[] urlSlices = firstLineSlices[1].split("\\?");
        this.url = urlSlices[0].trim();
        log.info("url:{}",this.url);

        //params
        // case1 method:GET
        if(urlSlices.length>1)
            parseParams(urlSlices[1]);
        log.info("params:{}",this.params);

        //headers
        String header;
        this.headers = new HashMap<>(); //initial
        Integer bodyIndex = null;//record the index of request body
        for (int i = 1; i < lines.size(); i++) {
            header = lines.get(i);
            if (header.equals("")){
                bodyIndex = i + 1;
                break;
            }
            // Content-Type: text/plain;charset=UTF-8
            // Accept-Encoding: gzip, deflate, br
            int colonIndex = header.indexOf(":");
            String key = header.substring(0,colonIndex);
            String[] values = header.substring(colonIndex+2)
                    .trim()/*blank */.split(",");
            headers.put(key,Arrays.asList(values));
        }
        log.info("headers:{}",this.headers);

        // method
        // case2 method:POST request body
        // only text (? JSON)
        if(method != RequestMethod.GET)
            parseParams(lines.get(bodyIndex));
        log.info("params:{}",this.params);

    }

    /**
     * load the params to hashmap
     * @param params String (urlSlices[1])
     * eg. params :
     *     username=Tommy&password=psw123&mul_choice=12,32,21
     */
    private void parseParams(String params){
        String[] urlParams = params.split("&");
        if (this.params == null)
            this.params = new HashMap<>();//initial
        for(String param : urlParams){
            String [] key_value = param.split("=");
            String key = key_value[0];
            //decode enc:utf-8
            String values = key_value[1]==null?null:decode(key_value[1],"utf-8");//utf-8
            String[] valueList = values.split(",");
            //load the params
            this.params.put(key, Arrays.asList(valueList));
        }
    }

    /**
     * deal with chinese (params)
     * @param value
     * @param encode
     * @return String
     */
    private String decode(String value,String encode){
        try {
            return URLDecoder.decode(value,encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;

    }
}
