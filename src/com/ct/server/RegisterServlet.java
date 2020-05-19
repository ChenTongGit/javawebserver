package com.ct.server;

import com.ct.server.core.Request;
import com.ct.server.core.Response;
import com.ct.server.core.Servlet;

public class RegisterServlet implements Servlet {
    @Override
    public void service(Request request, Response response) {
        System.out.println("Register service");
        response.print("regiter success");
    }
}
