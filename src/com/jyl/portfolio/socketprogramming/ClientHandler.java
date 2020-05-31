package com.jyl.portfolio.socketprogramming;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable {
    private static String initType = "init";
    private static String talkType = "talk";
    private static String disconnectType = "disconnect";

    private String clientIdentifier;
    private String targettedClient;
    private Socket socket;
    private int clientPort;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    ConcurrentHashMap<String, User> chmap;
    public ClientHandler(Socket socket, ConcurrentHashMap<String, User> chmap) {
        this.socket = socket;
        this.chmap = chmap;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
//        chmap.putIfAbsent(socket.getRemoteSocketAddress(), new User(out, in));

//            out = chmap.get();
//            in = new ObjectInputStream(socket.getInputStream());
//            clientPort = socket.getPort();

    }

    private void close() {
        System.out.println("Server closing connection..");
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
        boolean keepGoing = true;
        while (keepGoing) {
            try {
                Message messagefromClient = null;
                while (!((messagefromClient = (Message) in.readObject()).getMsgType().equalsIgnoreCase(disconnectType))) {
                    if (messagefromClient.getMsgType().equalsIgnoreCase(initType)) {
                        clientIdentifier = messagefromClient.getClientName();
                        targettedClient = messagefromClient.getTargettedClient();
                        System.out.println("Initlizing...connected a client: " + clientIdentifier + " client's  socket remoteAddr: "+socket.getRemoteSocketAddress());

//                        if(chmap.containsKey(socket.getRemoteSocketAddress())) {
                        chmap.put(clientIdentifier, new User(targettedClient,out, in, socket, clientPort, socket.getRemoteSocketAddress()));
//                        } else {
                        Message temp = new Message(talkType, clientIdentifier, "", "You are connected to server","");
//                        }
                        chmap.get(clientIdentifier).getOut().writeObject(temp);
                        chmap.get(clientIdentifier).getOut().flush();
                        System.out.println(this.clientIdentifier + " is connected");


                    } else if (messagefromClient.getMessageBody().equalsIgnoreCase(disconnectType)) {
                        System.out.println("Client notifies server to shut down.");
                        keepGoing =false;
                    } else {
                        System.out.println("Client:" + clientIdentifier + " tells server: " + messagefromClient.getMessageBody() + " to send to "+ targettedClient);
                        System.out.println("Sending to client's targetting client: " +targettedClient + " targettedClient ip and port: " +
                                chmap.get(targettedClient).getIp() + " " + chmap.get(targettedClient).getPort());


                        chmap.get(targettedClient).getOut().writeObject(messagefromClient);
                        chmap.get(targettedClient).getOut().flush();
                    }

                }

                // one last message to tell the client's "listenFromServer" thread to end its own infinite loop
                out.writeObject(messagefromClient);
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
