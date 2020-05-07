package com.jyl.portfolio.socketprogramming;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {
    protected static ExecutorService taskExecutor = Executors.newFixedThreadPool(3);

    public Server() {
        // create new ServerSocket listening on certain port
        int port = 9099;
        ConcurrentHashMap<String, ClientHandler> clientMap;
        try {
            System.out.println("created serverSocket...");
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("waiting incoming connection request...");
            try {
                // Note: If we replace try-catch with try-with-resource block for the "Socket socket = serverSocket
              // .accept()" to
                //          be "try(Socket socket = serverSocket.accept()){}"
                //          this will cause problem because when we reach the line "threadpool.execute(ch)" line,
              //          try-with-resource
                //          block assumes the code is done and try-with-resource block is done so "socket" object
              //          will be garbage-collected.

                Socket socket = serverSocket.accept();
                // Spawn a thread to handle this client

                ClientHandler ch = new ClientHandler(socket);
                System.out.println("ready to run task in threadpool");
                taskExecutor.execute(ch);

            } catch (Exception e) {
                //TODO: handle exception
                e.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        taskExecutor.shutdown();
        try {
            taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[]args){
            new Server();
    }
}