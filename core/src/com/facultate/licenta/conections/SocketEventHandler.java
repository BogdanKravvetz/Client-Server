package com.facultate.licenta.conections;

import com.badlogic.gdx.Gdx;
import com.facultate.licenta.objects.Player;

import com.facultate.licenta.screens.MenuScreen;
import com.facultate.licenta.screens.PlayScreen;
import com.facultate.licenta.tools.Constants;

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

    public Integer getScore() {
        return score;
    }

    private Integer score;

    public String getMyId() {
        return myId;
    }

    private String myId;


    public JSONArray getEnemyBullets() {
        return enemyBullets;
    }

    private JSONArray enemyBullets;

    public boolean bulletSpawned;

    public boolean getIsGameOver() {
        return isGameOver;
    }

    private  boolean isGameOver;

    public String getWinnerName() {
        return winnerName;
    }

    private String winnerName;

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
        isGameOver = false;
        bulletSpawned = false;
        score = 0;
        winnerName="constructor";
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
                //playScreen.setPlayer(new Player(playScreen));
//                JSONObject data = new JSONObject();
//                try {
//                    data.put("name", Constants.name.getText());
//                    connectionHandler.getSocket().emit("myName",data);
//                }
//                catch (JSONException e)
//                {
//                    Gdx.app.log("event hadler", "nameError");
//                }

            }
            //face rost de id-ul socket-ului (clientului) curent
        });
        connectionHandler.getSocket().on("socketId", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];
                try {
                    myId = data.getString("id");
                    Gdx.app.log("socketIO","my id: "+ myId);
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Errorgetting user id");

                }

            }
            //cand un jucator nou se conecteaza face rost de id-ul lui
            //acest event il primeste toti jucatorii deja conectati.
        });
        connectionHandler.getSocket().on("newPlayerConnected", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];                                          //ia obiectul json din argumentele eventului de pe server
                try {
                    String playerId = data.getString("id");                                         //ia proprietatea id al obiectului json
                    Gdx.app.log("socketIO","New Player Connected: "+ playerId);
                    Player newPlayer  = new Player(playScreen);

                    newPlayer.setId(playerId);
                    playScreen.getAllPlayers().put(playerId,newPlayer);
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Error getting New Player id");
                }
            }
        });
        connectionHandler.getSocket().on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                playersFromServer = null;
                playScreen.getAllPlayers().values().removeAll(playScreen.getAllPlayers().values());
                playersFromServer = (JSONArray) objects[0];//ia lista de jucatori de pe server

            }
        });
        connectionHandler.getSocket().on("playerMoved", new Emitter.Listener() {//event primit de la server atunci cand un al jucator s-a miscat
            @Override
            public void call(Object... objects) {
                otherPlayerMovedData = (JSONObject) objects[0];

            }
        });
        connectionHandler.getSocket().on("sendTimer", new Emitter.Listener() {
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
        connectionHandler.getSocket().on("getSpiders", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                getSpidersFromServer = (JSONArray) objects[0];//ia lista de jucatori de pe server


            }
        });
        connectionHandler.getSocket().on("spidersMove", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                spidersFromServer = (JSONArray) objects[0];

            }
        });
        connectionHandler.getSocket().on("spidersStop", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                spidersFromServerAtStop = (JSONArray) objects[0];

            }
        });
        connectionHandler.getSocket().on("updateBullets", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                buletFromServer = (JSONObject) objects[0];
            }

        });
        connectionHandler.getSocket().on("spidersDestroyed", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                destroyedSpidersFromServer = (JSONArray) objects[0];
            }
        });
        connectionHandler.getSocket().on("start", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {

            }
        });
        connectionHandler.getSocket().on("gameOver", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
               JSONObject data = (JSONObject)objects[0];
               try {
                   isGameOver = data.getBoolean("gameOver");
                   winnerName = data.getString("winner");
               }
               catch (JSONException e)
               {
                   Gdx.app.log("socketIO", "game over Error!");
               }

            }
        });
        connectionHandler.getSocket().on("gainHp", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];
                try {
                    String id = data.getString("id");
                    Integer hp = data.getInt("hp");
                    if(playScreen.getConnectionHandler().getSocket().id().equals(id))
                    {
                        playScreen.getPlayer().getPlayerStats().setCurrentHp(hp);
                    }
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Error getting hp");
                }

            }
        });
        connectionHandler.getSocket().on("score", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];
                try {
                    String id = data.getString("id");
                    score = data.getInt("score");
                    Gdx.app.log("LobbyEvent", "Score: " + score);

                }
                catch (JSONException e)
                {
                    Gdx.app.log("LobbyEvent", "Score error");
                }
            }
        });
        connectionHandler.getSocket().on("enemyShoot", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                enemyBullets = (JSONArray) objects[0];
                //Gdx.app.log("socketIO", "SIZE = " + enemyBullets.length());
                bulletSpawned = false;
            }
        });
        connectionHandler.getSocket().on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                Gdx.app.log("socketIO", "Disconnected");
            }
        });
        connectionHandler.getSocket().on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("socketIO","Player with id: "+ id+" disconnected");
                    if(playScreen.getAllPlayers()!=null && id != null && playScreen.getAllPlayers().get(id)!=null ) {
                        playScreen.getAllPlayers().get(id).setToDestroy();
                        playScreen.getAllPlayers().remove(id);
                    }
                    //playScreen.getConnectionHandler().getSocket().close();
                }
                catch (JSONException e)
                {
                    Gdx.app.log("socketIO", "Errorgetting Player id");
                }
            }
        });
    }
}
