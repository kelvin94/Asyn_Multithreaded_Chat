package com.jyl.portfolio.socketprogramming;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

class Client {


    public Client() {
    }

    public static void main(final String[] args) {
        // Create a socket object
        Socket socket = new Socket();
        int port = 9099;
        String hostname = "localhost";

        SocketAddress endpoint = new InetSocketAddress(hostname, port);
        int timeout = 999;

        // try-with-resource block to execute ".connect(SocketAddressEndPoint, Milliseconds to time out of trying to
      // connect)"
        String identifier = null;
        try {
            System.out.println("creating connection...");
            socket.connect(endpoint, timeout);

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true
            );
            boolean flag = true;
            while(flag) {
                System.out.println("Your name in the room?");
                Scanner sc = new Scanner(System.in);
                identifier = sc.nextLine();
                out.println("client#"+identifier);
                if(!identifier.equals("")) flag = false;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()
            ));

            BufferedReader stdInput = new BufferedReader(
                    new InputStreamReader(
                            System.in
                    )
            );
            System.out.println("sending data...");
            String inline;
            while (!(inline = stdInput.readLine()).equals("bye")) {
                    System.out.println("client sending: "+inline);
                    out.println(inline);
                    String serverSpeech = in.readLine();
                    System.out.println("Server said: "+serverSpeech);
            }
            out.close();

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}