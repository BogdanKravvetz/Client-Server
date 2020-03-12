package com.facultate.licenta.conections;

import com.badlogic.gdx.Gdx;
import com.facultate.licenta.tools.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import io.socket.client.IO;
import io.socket.client.Socket;

//creaza conexiunea cu serverul
public class ConnectionHandler {
    //89.137.255.89
    //192.168.0.94 - local

    //193.254.231.208  -camin
    //192.168.82.38   -local - ok

    //192.168.137.42  -hotspot?
    //            String ip = Inet4Address.getLocalHost().getHostAddress();
//            socket = IO.socket("http://"+ip+":8081");

    public Socket getSocket() {
        return socket;
    }


    private Socket socket;
    private String ip;
    public void connectSocket()
    {
        try{
            DatagramSocket javaSocket = new DatagramSocket();
            javaSocket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = javaSocket.getLocalAddress().getHostAddress();
            javaSocket.close();
        }
        catch (SocketException e)
        {
            System.out.println(e);
        }
        catch ( UnknownHostException u)
        {
            System.out.println(u);
        }
        try {
            IO.Options opts = new IO.Options();
            opts.forceNew = false;
            opts.reconnection = false;
            socket = IO.socket("http://10.146.1.34:8081",opts);
            //socket = IO.socket("http://"+ip+":8081");

            socket.connect();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
