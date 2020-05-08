package com.jyl.portfolio.socketprogramming;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {
    private static String initType = "init";
    private static String talkType = "talk";
    private static String disconnectType = "disconnect";

    private String clientIdentifier;
    private Socket socket;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("connected socket remoteAddr: "+socket.getRemoteSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void close() {
        System.out.println("closing connection..");
        // Close OutputStream
        if(out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
                Message fromClient = null;
                while (!((fromClient = (Message) in.readObject()).getMsgType().equalsIgnoreCase(disconnectType))) {
                    if (fromClient.getMsgType().equalsIgnoreCase(initType)) {
                        clientIdentifier = fromClient.getClientName();
                        System.out.println(this.clientIdentifier + " is connected");

                    } else {
                        System.out.println("Client tells server: " + fromClient.getMessageBody());
                        out.writeObject(fromClient);
                        out.flush();
                    }
                    if (fromClient.getMessageBody().equalsIgnoreCase(disconnectType)) {
                        keepGoing =false;
                    }
                }

                // one last message to tell the client's "listenFromServer" thread to end its own infinite loop
                out.writeObject(fromClient);
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
                close();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


}
