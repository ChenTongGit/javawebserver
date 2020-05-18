package com.ct.server.servlet;

import com.ct.server.Request;
import com.ct.server.Response;

import java.io.IOException;

public class LoginServlet implements Servlet {
    @Override
    public void service(Request request, Response response) {
        System.out.println("Login service");

        response.print("<html>");
            response.print("<head>");
            response.print("<meta charset=\"UTF-8\">");
            response.print("<title>");
                response.print("服务器响应成功");
            response.print("</title>");
            response.print("</head>");
            response.print("<body>");
                response.print("登录成功");
                response.print(request.getParameter("username"));
            response.print("</body>");
        response.print("</html>");
        System.out.println("param:   ---"+request.getParameter("password")+"     "+request.getParameter("username")+"    "+request.getParameter("faa"));


    }
}
