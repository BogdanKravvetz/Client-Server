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

        //193.254.231.208  -camin
        //192.168.82.38   -local - ok

        //192.168.137.42  -hotspot?
        try {
            socket = IO.socket("http://89.137.255.89:8081");
            socket.connect();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
