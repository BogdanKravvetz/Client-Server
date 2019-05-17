package com.facultate.licenta.conections;

import com.badlogic.gdx.Gdx;
import com.facultate.licenta.objects.Player;

import com.facultate.licenta.screens.PlayScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketEventHandler {
    private PlayScreen playScreen;
    private ConnectionHandler connectionHandler;
    private JSONArray spidersFromServer;
    private JSONArray spidersFromServerAtStop;
    private JSONObject otherPlayerMovedData;
    private JSONArray playersFromServer;
    private JSONArray getSpidersFromServer;


    private JSONObject buletFromServer;
    private JSONArray destroyedSpidersFromServer;

    public JSONArray getSpidersFromServer() {
        return spidersFromServer;
    }

    public JSONArray getSpidersFromServerAtStop() {
        return spidersFromServerAtStop;
    }

    public JSONObject getOtherPlayerMovedData() {
        return otherPlayerMovedData;
    }

    public JSONArray getPlayersFromServer() {
        return playersFromServer;
    }

    public JSONArray getGetSpidersFromServer() {
        return getSpidersFromServer;
    }

    public JSONObject getBuletFromServer() {
        return buletFromServer;
    }
    public void setBuletFromServer(JSONObject buletFromServer) {
        this.buletFromServer = buletFromServer;
    }

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
                playersFromServer = (JSONArray) objects[0];//ia lista de jucatori de pe server

            }
        }).on("playerMoved", new Emitter.Listener() {//event primit de la server atunci cand un al jucator s-a miscat
            @Override
            public void call(Object... objects) {
                otherPlayerMovedData = (JSONObject) objects[0];

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
                getSpidersFromServer = (JSONArray) objects[0];//ia lista de jucatori de pe server


            }
        }).on("spidersMove", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                spidersFromServer = (JSONArray) objects[0];

            }
        }).on("spidersStop", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                spidersFromServerAtStop = (JSONArray) objects[0];

            }
        }).on("updateBullets", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                buletFromServer = (JSONObject) objects[0];
            }
        }).on("spidersDestroyed", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                destroyedSpidersFromServer = (JSONArray) objects[0];
            }
        }).on("start", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {

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
                    playScreen.getAllPlayers().get(id).setToDestroy();
                    playScreen.getAllPlayers().remove(id);
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Errorgetting Player id");
                }
            }
        });
    }
}
