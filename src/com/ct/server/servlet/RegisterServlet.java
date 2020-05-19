package com.ct.server.servlet;

public class RegisterServlet implements Servlet {
    @Override
    public void service(Request request, Response response) {
        System.out.println("Register service");
        response.print("regiter success");
    }
}
