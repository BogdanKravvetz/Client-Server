package com.facultate.licenta.conections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.facultate.licenta.objects.Player;
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
                playScreen.setPlayer(new Player(playScreen.getPlayerSprite()));
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
                    playScreen.getAllPlayers().put(playerId,new Player(playScreen.getPlayerSprite()));
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
                        Player otherPlayer = new Player(playScreen.getPlayerSprite());
                        Vector2 position = new Vector2();
                        position.x = ((Double) playersFromServer.getJSONObject(i).getDouble("x")).floatValue();
                        position.y = ((Double) playersFromServer.getJSONObject(i).getDouble("y")).floatValue();
                        otherPlayer.setPosition(position.x,position.y);
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
                    if(playScreen.getAllPlayers().get(playerId) !=null)//daca exista jucatorul respectiv in hashmap atunci
                    {
                        //schimba pozitia jucatorlui respectiv pe propriul ecran
                        playScreen.getAllPlayers().get(playerId).setPosition(xPosition.floatValue(),yPosition.floatValue());
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
    }}
