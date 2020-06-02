package com.jyl.portfolio.socketprogramming.Client;

import com.jyl.portfolio.socketprogramming.Message;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Client {
    private static String initType = "init";
    private static String talkType = "talk";
    private static String disconnectType = "disconnect";

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private BufferedReader stdInput;
    private ExecutorService executor = Executors.newFixedThreadPool(2);
    private String clientName;
    private String targetedClient;

    public Client() {
        initConnection();
        initStreams();
        initServerListener();
        initClientInfo();
    }

    private void initConnection() {
        // Create a socket object
        socket = new Socket();
        int serverPort = 30005;
;
        String hostname = "chatserver";

        SocketAddress serverEndpoint = new InetSocketAddress(hostname, serverPort);
        int timeout = 999;
        try {

            socket.connect(serverEndpoint, timeout);
        } catch (IOException e) {
            e.printStackTrace();
            print(e.getMessage());
        }
    }

    private void initStreams() {
        try {
            // init stream for sending msg
            out = new ObjectOutputStream(
                    socket.getOutputStream()
            );
            // init stream reading console input
            stdInput = new BufferedReader(
                    new InputStreamReader(
                            System.in
                    )
            );
            // init stream for listening msgs coming from server
            in = new ObjectInputStream(
                    socket.getInputStream()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initClientInfo() {
        boolean flag = true;
        while (flag) {
            System.out.println("Your name in the room?");
            Scanner sc = new Scanner(System.in);

            clientName = sc.nextLine();
            System.out.println("Who are you chatting to?");

            targetedClient = sc.nextLine();

            Date date = new Date();
            Message msg = new Message(initType, clientName, targetedClient, "", date.toString());
            try {
                out.writeObject(msg);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
                print(e.getMessage());
            }
            if (!clientName.equals("")) flag = false;
        }
    }

    private void initServerListener() {
        // Open a separate thread to listen for msgs coming from server
        ListenFromServer listeningThread = new ListenFromServer(in, socket);
        executor.execute(listeningThread);
    }

    private void sendMessage() {
        String inline;
        try {
            // infinite loop to get input from Client in the console
            while (!(inline = stdInput.readLine()).equals("bye")) {
                System.out.println("client sending: " + inline);
                 Date date = new Date();
                Message msg = new Message(talkType, clientName,targetedClient, inline, date.toString());
                out.writeObject(msg);
                out.flush();
            }
            Date date = new Date();
            Message msg = new Message(disconnectType, clientName, targetedClient,"", date.toString());
            out.writeObject(msg); // Client sends "bye"
            out.flush();
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
            // Time out value is needed here - the task that the thread is executing is an infinite loop(see "ListenFromServer" class for the task)
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
        System.out.println("everything is shutted down ");
    }

    public static void main(final String[] args) {
        Client client = new Client();
        client.sendMessage();
    }


    /**
     * Helper methods
     */
    private void print(String msgToPrint) {
        System.out.println(msgToPrint);
    }
}