package com.facultate.licenta.conections;

import java.net.Inet4Address;
import java.net.InetAddress;

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
            String ip = Inet4Address.getLocalHost().getHostAddress();
            //socket = IO.socket("http://"+ip+":8081");

            socket = IO.socket("http://192.168.0.103:8081");
            socket.connect();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
