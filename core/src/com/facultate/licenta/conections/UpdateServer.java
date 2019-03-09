package com.facultate.licenta.conections;

import com.badlogic.gdx.Gdx;
import com.facultate.licenta.objects.DefaultBullet;
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
                        jspider.put("id", spider.getSpiderId());
                        jspider.put("x", spider.enemyBody.getPosition().x);
                        jspider.put("y", spider.enemyBody.getPosition().y);
                        jspider.put("xv", spider.enemyBody.getLinearVelocity().x);
                        jspider.put("yv", spider.enemyBody.getLinearVelocity().y);
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
    public void updateBullets( DefaultBullet bullet)
    {
        if(timer>=UPDATE_TIME) {
            JSONObject data = new JSONObject();
            try {
                    if(bullet.bulletBody!=null) {
                        data.put("x", bullet.bulletBody.getPosition().x);
                        data.put("y", bullet.bulletBody.getPosition().y);
                        data.put("xv", bullet.bulletBody.getLinearVelocity().x);
                        data.put("yv", bullet.bulletBody.getLinearVelocity().y);
                    }
                connectionHandler.getSocket().emit("updateBullets", data);//trimite event catre server.
                //timer = 0;
            } catch (JSONException e) {
                Gdx.app.log("socketIO", "error sending update data for bullets");
            }
        }
    }
}

//    public void updateDestroyedSpiders(float deltaTime)
//    {
//        if(timer>=UPDATE_TIME) {
//            JSONArray data = new JSONArray();
//            try {
//                for (Spider spider: playScreen.getWorldCreator().getSpiders()) {
//                        JSONObject jspider = new JSONObject();
//                        jspider.put("destroyed",spider.setToDestroy);
//                        data.put(jspider);
//                }
//                //Gdx.app.log("socketIO", "Trimis" );
//
//                connectionHandler.getSocket().emit("spidersDestroyed", data);//trimite event catre server.
//                //timer = 0;
//            } catch (JSONException e) {
//                Gdx.app.log("socketIO", "error sending update data for dead spiders");
//            }
//        }
//    }
//public void updateBullets2()
//    {
//        if(timer>=UPDATE_TIME) {
//            JSONArray data = new JSONArray();
//            try {
//                for (DefaultBullet bullet: playScreen.getBullets()) {
//                    if(bullet.bulletBody!=null) {
//                        JSONObject jBullet = new JSONObject();
//                        jBullet.put("x", bullet.bulletBody.getPosition().x);
//                        jBullet.put("y", bullet.bulletBody.getPosition().y);
//                        jBullet.put("xv", bullet.bulletBody.getLinearVelocity().x);
//                        jBullet.put("yv", bullet.bulletBody.getLinearVelocity().y);
//                        data.put(jBullet);
//                    }
//                }
//
//                connectionHandler.getSocket().emit("updateBullets", data);//trimite event catre server.
//                //timer = 0;
//            } catch (JSONException e) {
//                Gdx.app.log("socketIO", "error sending update data for bullets");
//            }
//        }
//    }