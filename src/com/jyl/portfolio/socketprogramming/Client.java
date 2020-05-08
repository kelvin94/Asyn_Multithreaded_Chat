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

class Client {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdInput;

    public Client() {
    }

    public void initConnection() {
        // Create a socket object
        socket = new Socket();
        int port = 9099;
        String hostname = "localhost";

        SocketAddress endpoint = new InetSocketAddress(hostname, port);
        int timeout = 999;
        System.out.println("creating connection...");
        try {
            socket.connect(endpoint, timeout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initStreams() {
        try {
            out = new PrintWriter(
                    socket.getOutputStream(), true
            );
            stdInput = new BufferedReader(
                    new InputStreamReader(
                            System.in
                    )
            );

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
            ExecutorService executor = Executors.newFixedThreadPool(1);
            ListenFromServer listeningThread = new ListenFromServer(in);
            executor.execute(listeningThread);
    }

    public void sendMessage() {
        System.out.println("sending data...");
        String inline;
        try {
            while (!(inline = stdInput.readLine()).equals("bye")) {
                System.out.println("client sending: " + inline);
                out.println(inline);
            }
            out.println(inline);
            System.out.println("Client said bye, closing outputstream");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("closing socket...");
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
}