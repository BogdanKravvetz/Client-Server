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
//evenimente primite de la server in camera de asteptare.
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

                    Gdx.app.log("lobby event","New Player Connected: "+ name);
                    lobbyScreen.getAllPlayers().add(name);
                }
                catch (JSONException e)
                {
                    Gdx.app.log("lobby event", "Error getting New Player id");
                }
            }
        });
        connectionHandler.getSocket().on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                //playersFromServer = null;
                playersFromServer = (JSONArray) objects[0];         //ia lista de jucatori de pe server

            }
        });
        connectionHandler.getSocket().on("start", new Emitter.Listener() {
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
        connectionHandler.getSocket().on("started", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                Gdx.app.log("Lobby Event", "Started");
                started = true;
               // lobbyScreen.getMyGame().setScreen( new MenuScreen(lobbyScreen.getMyGame()));
            }
        });
        connectionHandler.getSocket().on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                if(started == false) {

                    Gdx.app.log("lobby event", "Disconnected");
                }

            }
        });
        connectionHandler.getSocket().on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];
                try {
                    if(started == false) {
                        String name = data.getString("nameC");
                        Gdx.app.log("socketIO", "Player with name: " + name + " disconnected");
                        lobbyScreen.getAllPlayers().removeValue(name, false);
                    }
                }
                catch (JSONException e)
                {
                    Gdx.app.log("lobby event", "Errorgetting Player id");
                }
            }
        });
        connectionHandler.getSocket().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = new JSONObject();
                try {
                    Gdx.app.log("lobby event", "I sent my name!");
                    data.put("name", Constants.name.getText());
                    connectionHandler.getSocket().emit("myName",data);
                }
                catch (JSONException e)
                {
                    Gdx.app.log("lobby event", "nameError");
                }

            }
        });
    }
}
