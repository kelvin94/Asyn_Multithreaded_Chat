package com.jyl.portfolio.socketprogramming;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Client {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdInput;
    private ExecutorService executor = Executors.newFixedThreadPool(1);


    public Client() {
    }

    public void initConnection() {
        // Create a socket object
        socket = new Socket();
        int port = 9099;
        String hostname = "localhost";

        SocketAddress endpoint = new InetSocketAddress(hostname, port);
        int timeout = 999;
        try {
            socket.connect(endpoint, timeout);
        } catch (IOException e) {
            e.printStackTrace();
            print(e.getMessage());
        }
    }

    public void initStreams() {
        try {
            // init stream for sending msg
            out = new PrintWriter(
                    socket.getOutputStream(), true
            );
            // init stream reading console input
            stdInput = new BufferedReader(
                    new InputStreamReader(
                            System.in
                    )
            );
            // init stream for listening msgs coming from server
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initClientInfo() {
        String identifier = null;
        boolean flag = true;
        while (flag) {
            System.out.println("Your name in the room?");
            Scanner sc = new Scanner(System.in);
            identifier = sc.nextLine();
            out.println("client#" + identifier);
            if (!identifier.equals("")) flag = false;
        }
    }

    public void initServerListener() {
        // Open a separate thread to listen for msgs coming from server
        ListenFromServer listeningThread = new ListenFromServer(in);
        executor.execute(listeningThread);
    }

    public void sendMessage() {
        String inline;
        try {
            // infinite loop to get input from Client in the console
            while (!(inline = stdInput.readLine()).equals("bye")) {
                System.out.println("client sending: " + inline);
                out.println(inline);
            }
            out.println(inline); // Client sends "bye"
            System.out.println("Client said bye");
        } catch (IOException e) {
            e.printStackTrace();
            print(e.getMessage());
        } finally {
            close();
        }
    }

    private void close() {
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            print(e.getMessage());
        }
        try {
            out.close();
            in.close();
            stdInput.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
            print(e.getMessage());
        }

    }

    public static void main(final String[] args) {
        Client client = new Client();
        client.initConnection();
        client.initStreams();
        client.initServerListener();
        client.initClientInfo();
        client.sendMessage();

    }


    /**
     * Helper methods
     */
    private void print(String msgToPrint) {
        System.out.println(msgToPrint);
    }
}