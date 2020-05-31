package com.jyl.portfolio.socketprogramming;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class User {
      private String name;
      private ObjectOutputStream out = null;
      private ObjectInputStream in = null;
      private Socket client;
      private int port;
    private SocketAddress ip;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public SocketAddress getIp() {
        return ip;
    }

    public void setIp(SocketAddress ip) {
        this.ip = ip;
    }

    public User(ObjectOutputStream out, ObjectInputStream in) {
        this.out = out;
        this.in = in;
    }

    public User(String name, ObjectOutputStream out, ObjectInputStream in, Socket client, int port, SocketAddress ip) {
        this.name = name;
        this.out = out;
        this.in = in;
        this.client = client;
        this.port = port;
        this.ip = ip;
    }
}
