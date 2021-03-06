package com.facultate.licenta.conections;

import com.badlogic.gdx.Gdx;
import com.facultate.licenta.objects.DefaultBullet;
import com.facultate.licenta.objects.Spider;
import com.facultate.licenta.screens.PlayScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//trimite evenimente catre server pentru miscarea corpurilor.
public class UpdateServer {
    private PlayScreen playScreen;
    private ConnectionHandler connectionHandler;


    private final float UPDATE_RATE = 60;
    private final float UPDATE_TIME = 1/UPDATE_RATE;
    private float timer;
    private float timer2;
    private float timer3;
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
                connectionHandler.getSocket().emit("playerMoved", data);//trimite event catre server.
                timer=0;
            }
            catch (JSONException e) {
                Gdx.app.log("socketIO", "error sending update data");
            }
        }
    }
    public void updateSpiders(float deltaTime)
    {
        timer2+=deltaTime;
        if(timer2>=UPDATE_TIME) {
            JSONArray data = new JSONArray();
            try {
                for (Spider spider: playScreen.getWorldCreator().getSpiders()) {
                    if(spider.enemyBody!=null) {
                        JSONObject jspider = new JSONObject();
                        jspider.put("id", spider.getSpiderId());
                        jspider.put("x", spider.enemyBody.getPosition().x);
                        jspider.put("y", spider.enemyBody.getPosition().y);
                        jspider.put("xv", spider.enemyBody.getLinearVelocity().x);
                        jspider.put("yv", spider.enemyBody.getLinearVelocity().y);
                        jspider.put("destroyed",false);
                        jspider.put("spawned", true);
                        data.put(jspider);
                    }
                    else
                    {
                        JSONObject jspider = new JSONObject();
                        jspider.put("id", spider.getSpiderId());
                        jspider.put("x", 0);
                        jspider.put("y", 0);
                        jspider.put("xv",0);
                        jspider.put("yv", 0);
                        jspider.put("destroyed",true);
                        jspider.put("spawned", true);
                        data.put(jspider);
                    }
                }
                connectionHandler.getSocket().emit("spidersMove", data);//trimite event catre server.
                timer2 = 0;
            } catch (JSONException e) {
                Gdx.app.log("socketIO", "error sending update data for spiders");
            }
        }
    }
    public void updateBullets( DefaultBullet bullet,float deltaTime)
    {
        timer3+=deltaTime;
        if(timer3>=UPDATE_TIME) {

            JSONObject data = new JSONObject();
            try {
                if(bullet.bulletBody!=null) {
                    data.put("x", bullet.bulletBody.getPosition().x);
                    data.put("y", bullet.bulletBody.getPosition().y);
                    data.put("xv", bullet.bulletBody.getLinearVelocity().x);
                    data.put("yv", bullet.bulletBody.getLinearVelocity().y);
                    data.put("playerId", playScreen.getConnectionHandler().getSocket().id());
                }
                connectionHandler.getSocket().emit("updateBullets", data);                      //trimite event catre server.
                timer3 = 0;
            } catch (JSONException e) {
                Gdx.app.log("socketIO", "error sending update data for bullets");
            }
        }

    }
}