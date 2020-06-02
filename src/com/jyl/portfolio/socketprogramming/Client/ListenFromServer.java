package com.jyl.portfolio.socketprogramming.Client;

import com.jyl.portfolio.socketprogramming.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ListenFromServer implements Runnable {
    private static String disconnectType = "disconnect";

    ObjectInputStream in;
     Socket socket;
    public ListenFromServer(ObjectInputStream in, Socket socket) {
        this.in = in;
        this.socket = socket;
    }
    @Override
    public void run() {

        boolean keepGoing = true;
        Message msg = null;
        try {
            while((msg = (Message) in.readObject()) != null) {
                if(msg.getMsgType().equalsIgnoreCase(disconnectType)) break;
                else System.out.println("Server sends back: "+msg.toString());
            }
        } catch (IOException e) {
                e.printStackTrace();
                keepGoing = false;
        } catch (ClassNotFoundException e) {
                e.printStackTrace();
        }
    }
}
