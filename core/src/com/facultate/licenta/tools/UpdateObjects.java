package com.facultate.licenta.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.facultate.licenta.objects.DefaultBullet;
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
        if (playScreen.getSocketEvents().getOtherPlayerMovedData() != null) {
            try {
                //preia datele din obiectul data primit de la server
                String playerId = playScreen.getSocketEvents().getOtherPlayerMovedData().getString("id");
                Double xPosition = playScreen.getSocketEvents().getOtherPlayerMovedData().getDouble("x");
                Double yPosition = playScreen.getSocketEvents().getOtherPlayerMovedData().getDouble("y");
                Double xVelocity = playScreen.getSocketEvents().getOtherPlayerMovedData().getDouble("xv");
                Double yVelocity = playScreen.getSocketEvents().getOtherPlayerMovedData().getDouble("yv");
                if (playScreen.getAllPlayers().get(playerId) != null && !playScreen.getWorld().isLocked())//daca exista jucatorul respectiv in hashmap atunci
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
        if (playScreen.getSocketEvents().getPlayersFromServer() != null) { //!!!!!!!!!
            try {
                for (int i = 0; i < playScreen.getSocketEvents().getPlayersFromServer().length(); i++)//itereaza peste lista de jucatori si-i creaza pe ecranele celorlalti jucatori
                {
                    Player otherPlayer = new Player(playScreen);
                    Vector2 position = new Vector2();
                    //trebuie si id
                    String id = playScreen.getSocketEvents().getPlayersFromServer().getJSONObject(i).getString("id");
                    position.x = ((Double) playScreen.getSocketEvents().getPlayersFromServer().getJSONObject(i).getDouble("x")).floatValue();
                    position.y = ((Double) playScreen.getSocketEvents().getPlayersFromServer().getJSONObject(i).getDouble("y")).floatValue();
                    otherPlayer.setId(id);
                    if (!playScreen.getWorld().isLocked())
                        otherPlayer.playerBody.setTransform(position, 0f);
                    playScreen.getAllPlayers().put(playScreen.getSocketEvents().getPlayersFromServer().getJSONObject(i).getString("id"), otherPlayer);//pune jucatorii in hash-map=ul local
                    Gdx.app.log("UpdateObjects", "ID:" + id);
                }
            } catch (JSONException e) {
                Gdx.app.log("UpdateObjects", "Error getting players from server");
            }
        }
    }
    public void getSpiders()
    {
        if (playScreen.getSocketEvents().getGetSpidersFromServer() != null) {
            try {
                playScreen.getWorldCreator().getSpiders().ensureCapacity(playScreen.getSocketEvents().getGetSpidersFromServer().length());
                for (int i = 0; i < playScreen.getSocketEvents().getGetSpidersFromServer().length(); i++)//itereaza peste lista de jucatori si-i creaza pe ecranele celorlalti jucatori
                {

                    Vector2 position = new Vector2();
                    position.x = ((Double) playScreen.getSocketEvents().getGetSpidersFromServer().getJSONObject(i).getDouble("x")).floatValue();
                    position.y = ((Double) playScreen.getSocketEvents().getGetSpidersFromServer().getJSONObject(i).getDouble("y")).floatValue();
                    float xVelocity = ((Double) playScreen.getSocketEvents().getGetSpidersFromServer().getJSONObject(i).getDouble("xv")).floatValue();
                    float yVelocity = ((Double) playScreen.getSocketEvents().getGetSpidersFromServer().getJSONObject(i).getDouble("yv")).floatValue();
                    //Gdx.app.log("socketIO", "spider x " + position.x + " spider y: " + position.y);
                    Spider spider = new Spider(playScreen, position.x * Constants.PPM, position.y * Constants.PPM);
                    if (!playScreen.getWorld().isLocked())
                        spider.enemyBody.setLinearVelocity(xVelocity, yVelocity);
                    playScreen.getWorldCreator().spawned = playScreen.getSocketEvents().getGetSpidersFromServer().getJSONObject(i).getBoolean("spawned");
                    playScreen.getWorldCreator().getSpiders().insert(i,spider);
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
            if(playScreen.getSocketEvents().getSpidersFromServer() !=null) {
                for (int i = 0; i < playScreen.getSocketEvents().getSpidersFromServer().length(); i++) {
                    JSONObject spider = (JSONObject) playScreen.getSocketEvents().getSpidersFromServer().get(i);
                    String id = spider.getString("id");
                    Double xPosition = spider.getDouble("x") * Constants.PPM;
                    Double yPosition = spider.getDouble("y") * Constants.PPM;
                    Double xVelocity = spider.getDouble("xv");
                    Double yVelocity = spider.getDouble("yv");
                    //Gdx.app.log("socketIO", "xv" + xVelocity + "yv" + yVelocity);
                    if(playScreen.getWorldCreator().getSpiders().isEmpty()) {
                        return;
                    }
                    if (playScreen.getWorldCreator().getSpiders().get(i) != null && !playScreen.getWorld().isLocked()) {
                        if (playScreen.getWorldCreator().getSpiders().get(i).enemyBody != null) {
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
/*    public void destroySpider()
    {
        try {
            //Gdx.app.log("socketIO", data.length() + "");
            if(playScreen.getSocketEvents().destroyedSpidersFromServer !=null) {
                for (int i = 0; i < playScreen.getSocketEvents().destroyedSpidersFromServer.length(); i++) {
                    JSONObject spider = (JSONObject) playScreen.getSocketEvents().destroyedSpidersFromServer.get(i);
                    String id = spider.getString("id");
                    Boolean destroyed = spider.getBoolean("destroyed");
                    ///Gdx.app.log("socketIO", i+ "-i  " + destroyed );

                    //if(destroyed)
                    //Gdx.app.log("socketIO", "PRIMIT" + destroyed );
                    //if (playScreen.getWorldCreator().getSpiders().get(i) != null && playScreen.getWorld().isLocked() == false) {
                        if(destroyed && !playScreen.getWorldCreator().getSpiders().get(i).destroyed)
                        {
                            Gdx.app.log("socketIO", "PRIMIT + UPDATE" + i );
                            playScreen.getWorldCreator().getSpiders().get(i).onEnemyHit();
                        }
                   // }
                }
            }
        }
        catch (JSONException e)
        {
            Gdx.app.log("UpdateObjects", "Error destroy spiders");
        }
    }*/
    public void stopSpiders()
    {
        try {
            if (playScreen.getSocketEvents().getSpidersFromServerAtStop() != null) {
                //Gdx.app.log("socketIO", playScreen.getSocketEvents().spidersFromServerAtStop.length() + "");
                for (int i = 0; i < playScreen.getWorldCreator().getSpiders().size; i++) {
                    if(playScreen.getWorldCreator().getSpiders().get(i).enemyBody!=null) {
                        JSONObject spider = (JSONObject) playScreen.getSocketEvents().getSpidersFromServerAtStop().get(i);
                        Double xPosition = spider.getDouble("x");
                        Double yPosition = spider.getDouble("y");
                        Double xVelocity = spider.getDouble("xv");
                        Double yVelocity = spider.getDouble("yv");
                        //Gdx.app.log("socketIO", "x" + xPosition + "y " + yPosition);
                        if (playScreen.getWorldCreator().getSpiders().get(i) != null && !playScreen.getWorld().isLocked()) {
                            playScreen.getWorldCreator().getSpiders().get(i).enemyBody.setLinearVelocity(0, 0);
                            playScreen.getWorldCreator().getSpiders().get(i).enemyBody.setTransform(xPosition.floatValue(), yPosition.floatValue(), 0);
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

    public void updateBullets() {
        try {
            if (playScreen.getSocketEvents().getBuletFromServer() != null) {
                    JSONObject jbullet = (JSONObject) playScreen.getSocketEvents().getBuletFromServer();
                    Double xPosition = jbullet.getDouble("x") * Constants.PPM;
                    Double yPosition = jbullet.getDouble("y") * Constants.PPM;
                    Double xVelocity = jbullet.getDouble("xv");
                    Double yVelocity = jbullet.getDouble("yv");
                    //Gdx.app.log("socketIO", "xv" + xVelocity + "yv" + yVelocity);
                    DefaultBullet bullet = new DefaultBullet(playScreen,xPosition.floatValue(),yPosition.floatValue());
                    bullet.bulletBody.setLinearVelocity(xVelocity.floatValue(),yVelocity.floatValue());
                    playScreen.getBullets().add(bullet);
                    playScreen.getSocketEvents().setBuletFromServer(null);                                                  //!!!!!
            }
        } catch (JSONException e) {
            Gdx.app.log("socketIO", "Error updating bullets");
        }
    }
}
