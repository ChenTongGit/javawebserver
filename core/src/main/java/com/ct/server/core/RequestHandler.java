package com.ct.server.core;

import com.ct.server.core.exception.RequestParseException;
import com.ct.server.core.request.Request;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

@Slf4j
public class RequestHandler implements Runnable{
    private Socket client;

    public RequestHandler(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        Request request = null;
        try {
            request = new Request();
            request.build(client.getInputStream());
        } catch (IOException e) {

        } catch (RequestParseException e){

        }

    }
}
