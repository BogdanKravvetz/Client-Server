package com.facultate.licenta.conections;

import com.badlogic.gdx.Gdx;
import com.facultate.licenta.screens.LobbyScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class LobbyEventHandler {

    private ConnectionHandler connectionHandler;
    private LobbyScreen lobbyScreen;

    public JSONArray getPlayersFromServer() {
        return playersFromServer;
    }

    private JSONArray playersFromServer;

    public boolean isReadyToStart() {
        return isReadyToStart;
    }

    private boolean isReadyToStart;


    public LobbyEventHandler(ConnectionHandler connectionHandler, LobbyScreen lobbyScreen){
        this.connectionHandler = connectionHandler;
        this.lobbyScreen = lobbyScreen;
        isReadyToStart = false;
    }


    public void lobbyConfig()
    {
        connectionHandler.getSocket().on("newPlayerConnected", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];//ia obiectul json din argumentele eventului de pe server
                try {
                    String playerId = data.getString("id"); //ia proprietatea id al obiectului json

                    Gdx.app.log("socketIO","New Player Connected: "+ playerId);
                    lobbyScreen.getAllPlayers().add(playerId);
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Error getting New Player id");
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                playersFromServer = (JSONArray) objects[0];//ia lista de jucatori de pe server

            }
        }).on("start", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];
                try {
                    isReadyToStart = data.getBoolean("startBool");
                }
                catch (JSONException e)
                {
                    Gdx.app.log("LobbyEvent", "Start Error");
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

                    lobbyScreen.getAllPlayers().removeValue(id,true);
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Errorgetting Player id");
                }
            }
        });
    }
}
