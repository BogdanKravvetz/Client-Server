package com.facultate.licenta.conections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.facultate.licenta.Game;
import com.facultate.licenta.objects.Player;
import com.facultate.licenta.objects.Spider;
import com.facultate.licenta.screens.PlayScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketEventHandler {
    PlayScreen playScreen;
    ConnectionHandler connectionHandler;

    public SocketEventHandler(PlayScreen playScreen,ConnectionHandler connectionHandler)
    {
        this.playScreen = playScreen;
        this.connectionHandler = connectionHandler;
    }

    //implementarea event-urilor socket
    public void configSocketEvents()
    {
        //eventul de conectare si sub-event-uri
        connectionHandler.getSocket().on(Socket.EVENT_CONNECT,new Emitter.Listener(){
            @Override
            public void call(Object... args)
            {
                Gdx.app.log("socketIO","Connected");
                playScreen.setPlayer(new Player(playScreen));
            }
            //face rost de id-ul socket-ului (clientului) curent
        }).on("socketId", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("socketIO","my id: "+ id);
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Errorgetting user id");
                }

            }
            //cand un jucator nou se conecteaza face rost de id-ul lui
            //acest event il primeste toti jucatorii deja conectati.
        }).on("newPlayerConnected", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];//ia obiectul json din argumentele eventului de pe server
                try {
                    String playerId = data.getString("id"); //ia proprietatea id al obiectului json
                    Gdx.app.log("socketIO","New Player Connected: "+ playerId);
                    playScreen.getAllPlayers().put(playerId,new Player(playScreen));
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Errorgetting New Player id");
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONArray playersFromServer = (JSONArray) objects[0];//ia lista de jucatori de pe server
                try {
                    for (int i=0;i<playersFromServer.length();i++)//itereaza peste lista de jucatori si-i creaza pe ecranele celorlalti jucatori
                    {
                        Player otherPlayer = new Player(playScreen);
                        Vector2 position = new Vector2();
                        position.x = ((Double) playersFromServer.getJSONObject(i).getDouble("x")).floatValue();
                        position.y = ((Double) playersFromServer.getJSONObject(i).getDouble("y")).floatValue();
                        if(playScreen.getWorld().isLocked() == false)
                            otherPlayer.playerBody.setTransform(position,0f);
                        //otherPlayer.setPosition(position.x,position.y);
                        playScreen.getAllPlayers().put(playersFromServer.getJSONObject(i).getString("id"),otherPlayer);//pune jucatorii in hash-map=ul local
                    }
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Error getting players from server");
                }
            }
        }).on("playerMoved", new Emitter.Listener() {//event primit de la server atunci cand un al jucator s-a miscat
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];
                try {
                    //preia datele din obiectul data primit de la server
                    String playerId = data.getString("id");
                    Double xPosition = data.getDouble("x");
                    Double yPosition = data.getDouble("y");
                    Double xVelocity = data.getDouble("xv");
                    Double yVelocity = data.getDouble("yv");
                    if(playScreen.getAllPlayers().get(playerId) !=null && playScreen.getWorld().isLocked() == false)//daca exista jucatorul respectiv in hashmap atunci
                    {
                        //schimba pozitia jucatorlui respectiv pe propriul ecran
                        if(xVelocity.floatValue() == 0 && yVelocity.floatValue()== 0 && (playScreen.getAllPlayers().get(playerId).playerBody.getPosition().x!=xPosition.floatValue() || playScreen.getAllPlayers().get(playerId).playerBody.getPosition().y !=yPosition.floatValue()))
                        {
                            playScreen.getAllPlayers().get(playerId).playerBody.setLinearVelocity(0,0);
                            playScreen.getAllPlayers().get(playerId).playerBody.setTransform(xPosition.floatValue(),yPosition.floatValue(),0);
                        }
                        else
                        {
                            playScreen.getAllPlayers().get(playerId).playerBody.setLinearVelocity(xVelocity.floatValue(),yVelocity.floatValue());
                        }
                    }
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "error moving player");
                }
            }
        }).on("sendTimer", new Emitter.Listener() {
            @Override
            //primeste statusul timer-ului de la server
            //si updateaza timerul local.
            public void call(Object... objects) {
                JSONObject inGameTimerFromServer = (JSONObject) objects[0];
                try {
                    float timerValue = (float)inGameTimerFromServer.getDouble("inGameTimer");
                    playScreen.setInGameTimer(timerValue);
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "error updating in game timer from server.");
                }
            }
        }).on("getSpiders", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONArray spidersFromServer = (JSONArray) objects[0];//ia lista de jucatori de pe server

                try {
                    for (int i=0;i<spidersFromServer.length();i++)//itereaza peste lista de jucatori si-i creaza pe ecranele celorlalti jucatori
                    {

                        Vector2 position = new Vector2();
                        position.x = ((Double) spidersFromServer.getJSONObject(i).getDouble("x")).floatValue();
                        position.y = ((Double) spidersFromServer.getJSONObject(i).getDouble("y")).floatValue();
                        float xVelocity = ((Double) spidersFromServer.getJSONObject(i).getDouble("xv")).floatValue();
                        float yVelocity =((Double) spidersFromServer.getJSONObject(i).getDouble("yv")).floatValue();
                        //Gdx.app.log("socketIO", "spider x " + position.x + " spider y: " + position.y);
                        Spider spider = new Spider(playScreen,position.x * Game.PPM,position.y * Game.PPM);
                        if(playScreen.getWorld().isLocked() == false)
                            spider.enemyBody.setLinearVelocity(xVelocity,yVelocity);
                        playScreen.getWorldCreator().spawned = spidersFromServer.getJSONObject(i).getBoolean("spawned");
                        playScreen.getWorldCreator().getSpiders().add(spider);
                        //Gdx.app.log("socketIO", "Got Spider" + i);

                    }
                    if(!playScreen.getWorldCreator().spawned)
                    {
                        playScreen.getWorldCreator().spawn(playScreen);
                    }

                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Error getting spiders from server");
                }
            }
        }).on("spidersMove", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONArray data = (JSONArray) objects[0];
                try {
                    //Gdx.app.log("socketIO", data.length() + "");
                    for (int i=0;i< data.length();i++)
                    {
                        JSONObject spider = (JSONObject) data.get(i);
                        Double xPosition = spider.getDouble("x") * Game.PPM;
                        Double yPosition = spider.getDouble("y") * Game.PPM;
                        Double xVelocity = spider.getDouble("xv");
                        Double yVelocity = spider.getDouble("yv");
                        //Gdx.app.log("socketIO", "xv" + xVelocity + "yv" + yVelocity);
                        if(playScreen.getWorldCreator().getSpiders().get(i)!=null && playScreen.getWorld().isLocked() == false)
                        {
                            playScreen.getWorldCreator().getSpiders().get(i).enemyBody.setLinearVelocity(xVelocity.floatValue(),yVelocity.floatValue());
//                            if(playScreen.getWorldCreator().getSpiders().get(i).enemyBody.getPosition().x < xPosition.floatValue()-10 || playScreen.getWorldCreator().getSpiders().get(i).enemyBody.getPosition().x > yPosition.floatValue()+10)
//                            {
//                                playScreen.getWorldCreator().getSpiders().get(i).setToDestroy = true;
//                                playScreen.getWorldCreator().getSpiders().removeIndex(i);
//                                Spider spiderNew = new Spider(playScreen,xPosition.floatValue(),yPosition.floatValue());
//                                spiderNew.enemyBody.setLinearVelocity(xVelocity.floatValue(),yVelocity.floatValue());
//                                playScreen.getWorldCreator().getSpiders().insert(i,spiderNew);
//                            }
                        }
                    }
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Error updating spiders");
                }
            }
        }).on("spidersStop", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONArray data = (JSONArray) objects[0];
                try {
                    //Gdx.app.log("socketIO", data.length() + "");
                    for (int i=0;i< data.length();i++)
                    {
                        JSONObject spider = (JSONObject) data.get(i);
                        Double xPosition = spider.getDouble("x") ;
                        Double yPosition = spider.getDouble("y") ;
                        Double xVelocity = spider.getDouble("xv");
                        Double yVelocity = spider.getDouble("yv");
                        //Gdx.app.log("socketIO", "x" + xPosition + "y " + yPosition);
                        if(playScreen.getWorldCreator().getSpiders().get(i)!=null && playScreen.getWorld().isLocked() == false)
                        {
                            playScreen.getWorldCreator().getSpiders().get(i).enemyBody.setLinearVelocity(0,0);
                            playScreen.getWorldCreator().getSpiders().get(i).enemyBody.setTransform(xPosition.floatValue(),yPosition.floatValue(),0);
//                            if(playScreen.getWorldCreator().getSpiders().get(i).enemyBody.getPosition().x < xPosition.floatValue()-10 || playScreen.getWorldCreator().getSpiders().get(i).enemyBody.getPosition().x > yPosition.floatValue()+10)
//                            {
//                                playScreen.getWorldCreator().getSpiders().get(i).setToDestroy = true;
//                                playScreen.getWorldCreator().getSpiders().removeIndex(i);
//                                Spider spiderNew = new Spider(playScreen,xPosition.floatValue(),yPosition.floatValue());
//                                spiderNew.enemyBody.setLinearVelocity(xVelocity.floatValue(),yVelocity.floatValue());
//                                playScreen.getWorldCreator().getSpiders().insert(i,spiderNew);
//                            }
                        }
                    }
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Error stopping spiders");
                }
            }
        });
        connectionHandler.getSocket().on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                Gdx.app.log("socketIO", "Disconnected");
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("socketIO","Player with id: "+ id+" disconnected");
                    playScreen.getAllPlayers().remove(id);
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Errorgetting Player id");
                }
            }
        });
    }
    public void getSpiders()
    {
        connectionHandler.getSocket().on("getSpiders", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONArray spidersFromServer = (JSONArray) objects[0];//ia lista de jucatori de pe server

                try {
                    for (int i=0;i<spidersFromServer.length();i++)//itereaza peste lista de jucatori si-i creaza pe ecranele celorlalti jucatori
                    {

                        Vector2 position = new Vector2();
                        position.x = ((Double) spidersFromServer.getJSONObject(i).getDouble("x")).floatValue();
                        position.y = ((Double) spidersFromServer.getJSONObject(i).getDouble("y")).floatValue();
                        Spider spider = new Spider(playScreen,position.x,position.y);
                        playScreen.getWorldCreator().getSpiders().add(spider);
                    }

                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Error getting players from server");
                }
            }
        });
    }
}
