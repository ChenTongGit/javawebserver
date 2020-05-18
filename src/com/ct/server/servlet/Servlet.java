package com.ct.server.servlet;

import com.ct.server.Request;
import com.ct.server.Response;

public interface Servlet {
    void service(Request request, Response response);

}
