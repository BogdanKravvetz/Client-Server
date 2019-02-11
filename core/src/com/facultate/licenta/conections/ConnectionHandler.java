package com.facultate.licenta.conections;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ConnectionHandler {

    public Socket getSocket() {
        return socket;
    }

    private Socket socket;

    public void connectSocket()
    {
        //89.137.255.89
        //192.168.0.94 - local
        try {
            socket = IO.socket("http://89.137.255.89:8081");
            socket.connect();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
