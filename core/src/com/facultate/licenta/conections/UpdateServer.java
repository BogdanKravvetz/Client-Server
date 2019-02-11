package com.facultate.licenta.conections;

import com.badlogic.gdx.Gdx;
import com.facultate.licenta.screens.PlayScreen;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateServer {


    private PlayScreen playScreen;
    private ConnectionHandler connectionHandler;

    private final float UPDATE_RATE = 30;
    private final float UPDATE_TIME = 1/UPDATE_RATE;
    private float timer;
    public UpdateServer (PlayScreen playScreen,ConnectionHandler connectionHandler)
    {
        this.playScreen=playScreen;
        this.connectionHandler = connectionHandler;
    }

    public void updatePosition(float deltaTime)
    {
        timer+= deltaTime;//timer pentru cat timp a trecut de la ultimul update
        if(timer>=UPDATE_TIME && playScreen.getPlayer()!= null && playScreen.getPlayer().hasMoved()) //daca a trecut destul timp de  la ultimul update si jucatorul exista si s-a miscat(has moved din player class).
        {
            //UPDATE
            //data contine positia x si y a jucatorului local
            //apoi este trimisa catre server prin event-ul "playerMoved"
            JSONObject data = new JSONObject();
            try {
                data.put("x",playScreen.getPlayer().getX());
                data.put("y",playScreen.getPlayer().getY());
                connectionHandler.getSocket().emit("playerMoved", data);//trimite event catre server.
            }
            catch (JSONException e) {
                Gdx.app.log("socketIO", "error sending update data");
            }
        }
    }
}
