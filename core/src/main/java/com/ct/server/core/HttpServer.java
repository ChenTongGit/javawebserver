package com.ct.server.core;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpServer {
    private static final int PORT = 8888;//服务器端口号
    private ThreadPoolExecutor pool;
    private ServerSocket server;
    private Listener listener;

    public HttpServer(){
        try {

            log.info("fa");
            server = new ServerSocket(PORT);
            pool = new ThreadPoolExecutor(
                    5,
                    8,
                    1,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(10),
                    new ThreadPoolExecutor.CallerRunsPolicy());

            listener = new Listener(server,pool);


        }catch (IOException e){
            e.printStackTrace();
            log.error("服务器启动失败");
        }
    }

    public void start(){
        this.listener.start();
        log.info("服务器启动");
    }


    public void close(){
        this.listener.interrupt();
        this.pool.shutdown();
//        System.exit(0);
        log.info("Server close");
    }


    public static void main(String[] args) {

        log.info("Test Log working");
        HttpServer server = new HttpServer();

        Scanner scanner = new Scanner(System.in);
        String order = null;
        server.start();
        while(scanner.hasNext()){
            order = scanner.next();
            if(order.equals("EXIT")){
                server.close();
            }

        }
    }
}
