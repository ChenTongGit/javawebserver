package com.ct.server.core;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

@Data
@Slf4j
public class Listener extends Thread{
//    private Logger log = LoggerFactory.getLogger(Listener.class);
    private ServerSocket server;
    private ThreadPoolExecutor pool;
    public Listener(ServerSocket server, ThreadPoolExecutor pool){
        this.server = server;
        this.pool = pool;
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()){
            try {
                Socket client = server.accept();
                pool.execute(new RequestHandler(client));

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
