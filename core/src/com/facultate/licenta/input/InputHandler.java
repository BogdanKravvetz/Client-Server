package com.facultate.licenta.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.facultate.licenta.items.ItemDef;
import com.facultate.licenta.items.LifeCrystal;
import com.facultate.licenta.objects.DefaultBullet;
import com.facultate.licenta.objects.Player;
import com.facultate.licenta.screens.MenuScreen;
import com.facultate.licenta.screens.PlayScreen;
import com.facultate.licenta.tools.Constants;

import org.json.JSONObject;

public class InputHandler {

    private PlayScreen playScreen;

    public Vector2 getMovementVector() {
        return movementVector;
    }

    public void setMovementVector(Vector2 movementVector) {
        this.movementVector = movementVector;
    }

    private Vector2 movementVector ;

    public InputHandler(PlayScreen playScreen)
    {
        this.playScreen = playScreen;
    }

    public void movementInput(float deltaTime)//delta time cat timp a trecut de la ultimul update
    {
        if(playScreen.getSocketEvents().getIsGameOver())
        {
            return;
        }
        if (playScreen.getPlayer()!=null)
        {
            //Gdx.app.log("POS", "x: "+ playScreen.getPlayer().playerBody.getPosition().x + " y:" +playScreen.getPlayer().playerBody.getPosition().y);
            if(playScreen.getController().isLeftPressed() && playScreen.getPlayer().playerBody.getLinearVelocity().x>= -playScreen.getPlayer().getPlayerStats().getSpeed())
            {
                if(!playScreen.getWorld().isLocked()) {
                    movementVector = new Vector2(-playScreen.getPlayer().getPlayerStats().getAccelerationRate(), 0f);
                    playScreen.getPlayer().playerBody.applyLinearImpulse(movementVector, playScreen.getPlayer().playerBody.getWorldCenter(), true);
                    //playScreen.getConnectionHandler().getSocket().disconnect();
                    //playScreen.getMyGame().setScreen(new MenuScreen(playScreen.getMyGame()));

                }
            }
            if(playScreen.getController().isRightPressed() && playScreen.getPlayer().playerBody.getLinearVelocity().x<=playScreen.getPlayer().getPlayerStats().getSpeed())
            {
                if(!playScreen.getWorld().isLocked()) {


                    movementVector = new Vector2(playScreen.getPlayer().getPlayerStats().getAccelerationRate(), 0f);
                    playScreen.getPlayer().playerBody.applyLinearImpulse(movementVector, playScreen.getPlayer().playerBody.getWorldCenter(), true);


                }

            }
            if(playScreen.getController().isDownPressed()&& playScreen.getPlayer().playerBody.getLinearVelocity().y >= -playScreen.getPlayer().getPlayerStats().getSpeed())
            {
                if(!playScreen.getWorld().isLocked()) {
                    movementVector = new Vector2(0f, -playScreen.getPlayer().getPlayerStats().getAccelerationRate());
                    playScreen.getPlayer().playerBody.applyLinearImpulse(movementVector, playScreen.getPlayer().playerBody.getWorldCenter(), true);
                }
            }
            if(playScreen.getController().isUpPressed() && playScreen.getPlayer().playerBody.getLinearVelocity().y<=playScreen.getPlayer().getPlayerStats().getSpeed())
            {
                if(!playScreen.getWorld().isLocked()) {
                    movementVector = new Vector2(0f, playScreen.getPlayer().getPlayerStats().getAccelerationRate());
                    playScreen.getPlayer().playerBody.applyLinearImpulse(movementVector, playScreen.getPlayer().playerBody.getWorldCenter(), true);

                }
            }
            if (!movementPressed())
            {
                if(!playScreen.getWorld().isLocked()) {
                    playScreen.getPlayer().playerBody.setLinearVelocity(0, 0);
                }
            }
            if(Gdx.input.justTouched() && !movementPressed())
            {
                if(!playScreen.getWorld().isLocked()) {
                    if (movementVector == null)
                        movementVector = new Vector2(playScreen.getPlayer().getPlayerStats().getAccelerationRate(),0);
                    DefaultBullet bullet = new DefaultBullet(playScreen,playScreen.getPlayer().playerBody.getPosition().x * Constants.PPM,playScreen.getPlayer().playerBody.getPosition().y *Constants.PPM,playScreen.getPlayer().getId());
                    Vector2 shotVector = new Vector2(movementVector.x * bullet.getBulletStats().getSpeed(),movementVector.y * bullet.getBulletStats().getSpeed());
                    //Gdx.app.log("in","shot"+shotVector);
                    bullet.bulletBody.applyLinearImpulse(shotVector,bullet.bulletBody.getWorldCenter(),true);
                    playScreen.getBullets().add(bullet);
                    playScreen.getUpdateServer().updateBullets(bullet,deltaTime);
                    //playScreen.spawnItem(new ItemDef(new Vector2(playScreen.getPlayer().getX(),playScreen.getPlayer().getY()), LifeCrystal.class));
//                    for(Player player :playScreen.getAllPlayers().values())
//                    {
//                        Gdx.app.log("input", "ID:" + player.getId());
//                    }
                }
            }
        }
    }
    private boolean movementPressed()
    {
        if (!playScreen.getController().isLeftPressed() && !playScreen.getController().isUpPressed() && !playScreen.getController().isDownPressed() && !playScreen.getController().isRightPressed()) {
            return false;
        }
        return true;
    }
}
