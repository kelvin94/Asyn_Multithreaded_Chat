package com.jyl.portfolio.socketprogramming;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {
    protected static ExecutorService taskExecutor = Executors.newFixedThreadPool(3);


    public Server(ConcurrentHashMap<String, User> chmap) {
        // create new ServerSocket listening on certain port
        int port = 9099;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Waiting incoming request. Listening on port: "+ port);
            try {
                // Note: If we replace try-catch with try-with-resource block for the "Socket socket = serverSocket
                // .accept()" to "try(Socket socket = serverSocket.accept()){}"
                //          this will cause problem because when we reach the line "threadpool.execute(ch)" line,
                //          try-with-resource
                //          block assumes the code is done and try-with-resource block is done so "socket" object
                //          will be garbage-collected.
                while (true) {
                    Socket socket = serverSocket.accept();
                    // Spawn a thread to handle this client
                    ClientHandler ch = new ClientHandler(socket, chmap);
                    taskExecutor.execute(ch);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void close() {
        taskExecutor.shutdown();
        try {
            taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        // the map to hold the clients ip and port that the server has connection to.
        // key(ClientName, e.g. Kyle) -> value(ClientInfoObject(IP, Port))
        ConcurrentHashMap<String, User> chmap = new ConcurrentHashMap<String, User>();
        new Server(chmap);
    }
}