package com.jyl.portfolio.socketprogramming.Server;

import java.net.SocketAddress;

public class ClientInfo {
    private int port;
    private SocketAddress ip;

    public ClientInfo(int port, SocketAddress ip) {
        this.port = port;
        this.ip = ip;
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
}
