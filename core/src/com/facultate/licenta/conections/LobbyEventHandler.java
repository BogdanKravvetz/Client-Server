package com.facultate.licenta.conections;

import com.badlogic.gdx.Gdx;
import com.facultate.licenta.screens.LobbyScreen;
import com.facultate.licenta.screens.MenuScreen;
import com.facultate.licenta.tools.Constants;

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
    public boolean started;


    public LobbyEventHandler(ConnectionHandler connectionHandler, LobbyScreen lobbyScreen){
        this.connectionHandler = connectionHandler;
        this.lobbyScreen = lobbyScreen;
        isReadyToStart = false;
        started = false;
    }


    public void lobbyConfig()
    {
        connectionHandler.getSocket().on("newPlayerName", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];//ia obiectul json din argumentele eventului de pe server
                try {
                    String name = data.getString("name"); //ia proprietatea id al obiectului json

                    Gdx.app.log("socketIO","New Player Connected: "+ name);
                    lobbyScreen.getAllPlayers().add(name);
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Error getting New Player id");
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                playersFromServer = null;
                playersFromServer = (JSONArray) objects[0];         //ia lista de jucatori de pe server

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
        }).on("started", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                Gdx.app.log("LOBBYSCREEN", "change handler");
                started = true;
               // lobbyScreen.getMyGame().setScreen( new MenuScreen(lobbyScreen.getMyGame()));
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
                    String name = data.getString("nameC");
                    Gdx.app.log("socketIO","Player with name: "+ name+" disconnected");
                    lobbyScreen.getAllPlayers().removeValue(name,false);
                    //lobbyScreen.getAllPlayers().r
                    //lobbyScreen.getAllPlayers().removeValue(id,true);
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Errorgetting Player id");
                }
            }
        }).on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = new JSONObject();
                try {
                    data.put("name", Constants.name.getText());
                    connectionHandler.getSocket().emit("myName",data);
                }
                catch (JSONException e)
                {
                    Gdx.app.log("lobbyScreen", "nameError");
                }

            }
        });
    }
}
