package com.jyl.portfolio.socketprogramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private String clientIdentifier;
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    private String extractClientIdentifier(String tokenizedIdentifier) {
        String[] strings = tokenizedIdentifier.split("#");
        return strings[1];
    }

    @Override
    public void run() {
        PrintWriter out = null;
         BufferedReader in = null;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        String received;
        while (true) {
            System.out.println("start run()");
            System.out.println("socket remoteAddr: "+socket.getRemoteSocketAddress());
            try {
                String fromClient = null;
                while ((fromClient = in.readLine()) != null) {
                    if (fromClient.contains("#") && clientIdentifier == null) {
                        clientIdentifier = extractClientIdentifier(fromClient);
                        System.out.println(this.clientIdentifier + " is connected");

                    } else {
                        System.out.println("Client tells server: " + fromClient);
                    }
                    if (fromClient.equals("bye")) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
