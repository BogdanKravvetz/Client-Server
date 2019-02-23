package com.facultate.licenta.conections;

import com.badlogic.gdx.Gdx;
import com.facultate.licenta.objects.Spider;
import com.facultate.licenta.screens.PlayScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateServer {


    private PlayScreen playScreen;
    private ConnectionHandler connectionHandler;

    private final float UPDATE_RATE = 60;
    private final float UPDATE_TIME = 1/UPDATE_RATE;
    private float timer;
    public UpdateServer (PlayScreen playScreen,ConnectionHandler connectionHandler)
    {
        this.playScreen=playScreen;
        this.connectionHandler = connectionHandler;
    }

    public void updatePlayerPosition(float deltaTime)
    {
        timer+= deltaTime;//timer pentru cat timp a trecut de la ultimul update
        //&& playScreen.getPlayer().hasMoved()
        if(timer>=UPDATE_TIME && playScreen.getPlayer()!= null && playScreen.getPlayer().hasMoved()) //daca a trecut destul timp de  la ultimul update si jucatorul exista si s-a miscat(has moved din player class).
        {
            //UPDATE
            //data contine positia x si y a jucatorului local
            //apoi este trimisa catre server prin event-ul "playerMoved"
            JSONObject data = new JSONObject();
            try {
                data.put("x",playScreen.getPlayer().playerBody.getPosition().x);
                data.put("y",playScreen.getPlayer().playerBody.getPosition().y);
                data.put("xv",playScreen.getPlayer().playerBody.getLinearVelocity().x);
                data.put("yv",playScreen.getPlayer().playerBody.getLinearVelocity().y);
                //data.put("x",playScreen.getPlayer().getX());
                //data.put("y",playScreen.getPlayer().getY());
                connectionHandler.getSocket().emit("playerMoved", data);//trimite event catre server.
                timer = 0;
            }
            catch (JSONException e) {
                Gdx.app.log("socketIO", "error sending update data");
            }
        }
        else if(timer>=UPDATE_TIME && playScreen.getPlayer()!= null)
        {
            JSONObject data = new JSONObject();
            try {
                data.put("x",playScreen.getPlayer().playerBody.getPosition().x);
                data.put("y",playScreen.getPlayer().playerBody.getPosition().y);
                data.put("xv",0);
                data.put("yv",0);
                //data.put("x",playScreen.getPlayer().getX());
                //data.put("y",playScreen.getPlayer().getY());
                connectionHandler.getSocket().emit("playerMoved", data);//trimite event catre server.
            }
            catch (JSONException e) {
                Gdx.app.log("socketIO", "error sending update data");
            }
        }
    }
    public void updateSpiders(float deltaTime)
    {
        if(timer>=UPDATE_TIME) {
            JSONArray data = new JSONArray();
            try {
                for (Spider spider: playScreen.getWorldCreator().getSpiders()) {
                    if(spider.enemyBody!=null) {
                        JSONObject jspider = new JSONObject();
                        jspider.put("x", spider.enemyBody.getPosition().x);
                        jspider.put("y", spider.enemyBody.getPosition().y);
                        jspider.put("xv", spider.enemyBody.getLinearVelocity().x);
                        jspider.put("yv", spider.enemyBody.getLinearVelocity().y);
                        jspider.put("spawned", true);
                        data.put(jspider);
                    }
                }

                connectionHandler.getSocket().emit("spidersMove", data);//trimite event catre server.
                //timer = 0;
            } catch (JSONException e) {
                Gdx.app.log("socketIO", "error sending update data for spiders");
            }
        }
    }
}
