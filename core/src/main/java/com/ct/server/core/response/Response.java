package com.ct.server.core.response;

import com.ct.server.core.constant.CharConstant;
import com.ct.server.core.constant.CharsetProperties;
import com.ct.server.core.enumeration.ResponseStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * HTTP/1.1 200 OK
 * Date: Sat, 31 Dec 2005 23:59:59 GMT
 * Content-Type: text/html;constant=ISO-8859-1
 * Content-Length: 122  //(important)
 *
 * <html>
 *     <head>
 *         <title>Home</title>
 *     </head>
 *     <body>
 *         <h1>Welcome<h1/>
 *     </body>
 * </html>
 */

@Data
@Slf4j
public class Response {
    private String response;

    public Response(String response){
        this.response = response;
    }

    public static class ResponseBuilder{

        private StringBuilder header;
        private StringBuilder body;

        public ResponseBuilder header(ResponseStatus status){
            header = new StringBuilder();
            //HTTP/1.1 200 OK
            header.append("HTTP/1.1").append(CharConstant.BLANK).append(CharConstant.CRLF);
            //Date
            header.append("Date:").append(CharConstant.BLANK).append(new Date()).append(CharConstant.CRLF);
            //Content-Type: text/html;constant=ISO-8859-1
            header.append("Content-Type:").append(CharConstant.BLANK).append("text/html;constant=UTF-8").append(CharConstant.CRLF);
            //Content-Length: 122
            header.append("Content-Length:").append(CharConstant.BLANK);//.append(CharConstant.CRLF);
            //?
            return this;
        }

        public ResponseBuilder body(String body){
            this.header.append(body.getBytes(CharsetProperties.charset_cn).length).append(CharConstant.CRLF);
            this.body = new StringBuilder(body);
            return this;
        }

        public Response build(){
            return new Response(new StringBuilder()
                    .append(header)
                    .append(CharConstant.CRLF)
                    .append(body).toString());
        }
    }
}
