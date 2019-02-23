package com.facultate.licenta.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.facultate.licenta.Game;
import com.facultate.licenta.objects.Player;
import com.facultate.licenta.objects.Spider;
import com.facultate.licenta.screens.PlayScreen;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateObjects {

    private PlayScreen playScreen;

    public UpdateObjects(PlayScreen playScreen)
    {
        this.playScreen = playScreen;
    }
    public void moveOtherPlayers() {
        if (playScreen.getSocketEvents().otherPlayerMovedData != null) {
            try {
                //preia datele din obiectul data primit de la server
                String playerId = playScreen.getSocketEvents().otherPlayerMovedData.getString("id");
                Double xPosition = playScreen.getSocketEvents().otherPlayerMovedData.getDouble("x");
                Double yPosition = playScreen.getSocketEvents().otherPlayerMovedData.getDouble("y");
                Double xVelocity = playScreen.getSocketEvents().otherPlayerMovedData.getDouble("xv");
                Double yVelocity = playScreen.getSocketEvents().otherPlayerMovedData.getDouble("yv");
                if (playScreen.getAllPlayers().get(playerId) != null && playScreen.getWorld().isLocked() == false)//daca exista jucatorul respectiv in hashmap atunci
                {
                    //schimba pozitia jucatorlui respectiv pe propriul ecran
                    if (xVelocity.floatValue() == 0 && yVelocity.floatValue() == 0 && (playScreen.getAllPlayers().get(playerId).playerBody.getPosition().x != xPosition.floatValue() || playScreen.getAllPlayers().get(playerId).playerBody.getPosition().y != yPosition.floatValue())) {
                        playScreen.getAllPlayers().get(playerId).playerBody.setLinearVelocity(0, 0);
                        playScreen.getAllPlayers().get(playerId).playerBody.setTransform(xPosition.floatValue(), yPosition.floatValue(), 0);
                    } else {
                        playScreen.getAllPlayers().get(playerId).playerBody.setLinearVelocity(xVelocity.floatValue(), yVelocity.floatValue());
                    }
                }
            } catch (JSONException e) {
                Gdx.app.log("socketIO", "error moving player");
            }
        }
    }
    public void getOtherPlayers() {
        if (playScreen.getSocketEvents().otherPlayerMovedData != null) {
            try {
                for (int i = 0; i < playScreen.getSocketEvents().playersFromServer.length(); i++)//itereaza peste lista de jucatori si-i creaza pe ecranele celorlalti jucatori
                {
                    Player otherPlayer = new Player(playScreen);
                    Vector2 position = new Vector2();
                    position.x = ((Double) playScreen.getSocketEvents().playersFromServer.getJSONObject(i).getDouble("x")).floatValue();
                    position.y = ((Double) playScreen.getSocketEvents().playersFromServer.getJSONObject(i).getDouble("y")).floatValue();
                    if (playScreen.getWorld().isLocked() == false)
                        otherPlayer.playerBody.setTransform(position, 0f);
                    //otherPlayer.setPosition(position.x,position.y);
                    playScreen.getAllPlayers().put(playScreen.getSocketEvents().playersFromServer.getJSONObject(i).getString("id"), otherPlayer);//pune jucatorii in hash-map=ul local
                }
            } catch (JSONException e) {
                Gdx.app.log("socketIO", "Error getting players from server");
            }
        }
    }
    public void getSpiders()
    {
        if (playScreen.getSocketEvents().getSpidersFromServer != null) {
            try {
                for (int i = 0; i < playScreen.getSocketEvents().getSpidersFromServer.length(); i++)//itereaza peste lista de jucatori si-i creaza pe ecranele celorlalti jucatori
                {

                    Vector2 position = new Vector2();
                    position.x = ((Double) playScreen.getSocketEvents().getSpidersFromServer.getJSONObject(i).getDouble("x")).floatValue();
                    position.y = ((Double) playScreen.getSocketEvents().getSpidersFromServer.getJSONObject(i).getDouble("y")).floatValue();
                    float xVelocity = ((Double) playScreen.getSocketEvents().getSpidersFromServer.getJSONObject(i).getDouble("xv")).floatValue();
                    float yVelocity = ((Double) playScreen.getSocketEvents().getSpidersFromServer.getJSONObject(i).getDouble("yv")).floatValue();
                    //Gdx.app.log("socketIO", "spider x " + position.x + " spider y: " + position.y);
                    Spider spider = new Spider(playScreen, position.x * Game.PPM, position.y * Game.PPM);
                    if (playScreen.getWorld().isLocked() == false)
                        spider.enemyBody.setLinearVelocity(xVelocity, yVelocity);
                    playScreen.getWorldCreator().spawned = playScreen.getSocketEvents().getSpidersFromServer.getJSONObject(i).getBoolean("spawned");
                    playScreen.getWorldCreator().getSpiders().add(spider);
                    //Gdx.app.log("socketIO", "Got Spider" + i);

                }
                if (!playScreen.getWorldCreator().spawned) {
                    playScreen.getWorldCreator().spawn(playScreen);
                }

            } catch (JSONException e) {
                Gdx.app.log("socketIO", "Error getting spiders from server");
            }
        }
    }


    public void moveSpiders()
    {
        try {
            //Gdx.app.log("socketIO", data.length() + "");
            if(playScreen.getSocketEvents().spidersFromServer !=null) {
                for (int i = 0; i < playScreen.getSocketEvents().spidersFromServer.length(); i++) {
                    if(playScreen.getWorldCreator().getSpiders().get(i).enemyBody!=null) {
                        JSONObject spider = (JSONObject) playScreen.getSocketEvents().spidersFromServer.get(i);
                        Double xPosition = spider.getDouble("x") * Game.PPM;
                        Double yPosition = spider.getDouble("y") * Game.PPM;
                        Double xVelocity = spider.getDouble("xv");
                        Double yVelocity = spider.getDouble("yv");
                        //Gdx.app.log("socketIO", "xv" + xVelocity + "yv" + yVelocity);
                        if (playScreen.getWorldCreator().getSpiders().get(i) != null && playScreen.getWorld().isLocked() == false) {
                            playScreen.getWorldCreator().getSpiders().get(i).enemyBody.setLinearVelocity(xVelocity.floatValue(), yVelocity.floatValue());
                        }
                    }
                }
            }
        }
        catch (JSONException e)
        {
            Gdx.app.log("socketIO", "Error updating spiders");
        }
    }
    public void stopSpiders()
    {
        try {
            if (playScreen.getSocketEvents().spidersFromServerAtStop != null) {
                //Gdx.app.log("socketIO", playScreen.getSocketEvents().spidersFromServerAtStop.length() + "");
                for (int i = 0; i < playScreen.getSocketEvents().spidersFromServerAtStop.length(); i++) {
                    if(playScreen.getWorldCreator().getSpiders().get(i).enemyBody!=null) {
                        JSONObject spider = (JSONObject) playScreen.getSocketEvents().spidersFromServerAtStop.get(i);
                        //playScreen.getWorldCreator().getSpiders().add(spider);
                        Double xPosition = spider.getDouble("x");
                        Double yPosition = spider.getDouble("y");
                        Double xVelocity = spider.getDouble("xv");
                        Double yVelocity = spider.getDouble("yv");
                        //Gdx.app.log("socketIO", "x" + xPosition + "y " + yPosition);
                        if (playScreen.getWorldCreator().getSpiders().get(i) != null && playScreen.getWorld().isLocked() == false) {
                            playScreen.getWorldCreator().getSpiders().get(i).enemyBody.setLinearVelocity(0, 0);
                            playScreen.getWorldCreator().getSpiders().get(i).enemyBody.setTransform(xPosition.floatValue(), yPosition.floatValue(), 0);
                            //playScreen.getWorldCreator().getSpiders().get(i).setToDestroy = true;
                            //if(playScreen.getWorldCreator().getSpiders().get(i).destroyed) {
                            //playScreen.getWorldCreator().getSpiders().removeIndex(i);

                            //asta e aproape ok DAR
//                        Spider spiderNew = new Spider(playScreen, xPosition.floatValue() * Game.PPM, yPosition.floatValue() * Game.PPM);
//                        spiderNew.enemyBody.setLinearVelocity(0, 0);
//                        playScreen.getWorldCreator().getSpiders().removeIndex(i);
//                        playScreen.getWorldCreator().getSpiders().insert(i, spiderNew);
                            /////////////////
                            //playScreen.getWorldCreator().getSpiders().get(i).enemyBody.setLinearVelocity(0, 0);
                            //playScreen.getWorldCreator().getSpiders().insert(i, spiderNew);
                            //playScreen.getWorldCreator().getSpiders().get(i).enemyBody.setTransform(xPosition.floatValue(),yPosition.floatValue(),0);
                            //}

                        }
                    }
                }
            }
        }
        catch (JSONException e)
        {
            Gdx.app.log("socketIO", "Error stopping spiders");
        }
    }
}
