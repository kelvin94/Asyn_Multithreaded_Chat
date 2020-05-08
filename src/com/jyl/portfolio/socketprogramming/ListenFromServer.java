package com.jyl.portfolio.socketprogramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ListenFromServer implements Runnable {
    BufferedReader in;
    public ListenFromServer(BufferedReader in) {
        this.in = in;
    }
    @Override
    public void run() {
        boolean keepGoing = true;
        while(keepGoing) {
            try {
                String msg = in.readLine();
                System.out.println("Server sends back: "+msg);
            } catch (IOException e) {
                e.printStackTrace();
                keepGoing = false;
            }
        }
    }
}
