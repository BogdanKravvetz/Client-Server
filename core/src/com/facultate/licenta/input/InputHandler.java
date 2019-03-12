package com.facultate.licenta.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.facultate.licenta.objects.DefaultBullet;
import com.facultate.licenta.screens.PlayScreen;
import com.facultate.licenta.tools.Constants;

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
        if (playScreen.getPlayer()!=null)
        {
            if(playScreen.getController().isLeftPressed() && playScreen.getPlayer().playerBody.getLinearVelocity().x>= -3)
            {
                if(!playScreen.getWorld().isLocked()) {
                    movementVector = new Vector2(-0.5f, 0f);
                    playScreen.getPlayer().playerBody.applyLinearImpulse(movementVector, playScreen.getPlayer().playerBody.getWorldCenter(), true);
                }
            }
            if(playScreen.getController().isRightPressed() && playScreen.getPlayer().playerBody.getLinearVelocity().x<=3)
            {
                if(!playScreen.getWorld().isLocked()) {
                    movementVector = new Vector2(0.5f, 0f);
                    playScreen.getPlayer().playerBody.applyLinearImpulse(movementVector, playScreen.getPlayer().playerBody.getWorldCenter(), true);
                }
            }
            if(playScreen.getController().isDownPressed()&& playScreen.getPlayer().playerBody.getLinearVelocity().y >= -3)
            {
                if(!playScreen.getWorld().isLocked()) {
                    movementVector = new Vector2(0f, -0.5f);
                    playScreen.getPlayer().playerBody.applyLinearImpulse(movementVector, playScreen.getPlayer().playerBody.getWorldCenter(), true);
                }
            }
            if(playScreen.getController().isUpPressed() && playScreen.getPlayer().playerBody.getLinearVelocity().y<=3)
            {
                if(!playScreen.getWorld().isLocked()) {
                    movementVector = new Vector2(0f, 0.5f);
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
                        movementVector = new Vector2(0.5f,0);
                    DefaultBullet bullet = new DefaultBullet(playScreen,playScreen.getPlayer().playerBody.getPosition().x * Constants.PPM,playScreen.getPlayer().playerBody.getPosition().y *Constants.PPM);
                    Vector2 shotVector = new Vector2(movementVector.x*5,movementVector.y*5);
                    //Gdx.app.log("in","shot"+shotVector);
                    bullet.bulletBody.applyLinearImpulse(shotVector,bullet.bulletBody.getWorldCenter(),true);
                    playScreen.getBullets().add(bullet);
                    playScreen.getUpdateServer().updateBullets(bullet);
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
