package com.jyl.portfolio.socketprogramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {
    private String clientIdentifier;
    private Socket socket;
    PrintWriter out = null;
    BufferedReader in = null;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("connected socket remoteAddr: "+socket.getRemoteSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String extractClientIdentifier(String tokenizedIdentifier) {
        String[] strings = tokenizedIdentifier.split("#");
        return strings[1];
    }

    private void close() {
        System.out.println("closing connection..");
        // Close OutputStream
        if(out != null) out.close();

        // Close inputStream
        if(in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Close socket
        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {


        String received;
        boolean keepGoing = true;
        while (keepGoing) {

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
                        keepGoing =false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    socket.close();
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
            }

        }
        close();
    }


}
