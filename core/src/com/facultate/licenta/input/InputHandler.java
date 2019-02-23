package com.facultate.licenta.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.facultate.licenta.Game;
import com.facultate.licenta.objects.DefaultBullet;
import com.facultate.licenta.objects.Gate;
import com.facultate.licenta.screens.PlayScreen;

public class InputHandler {

    PlayScreen playScreen;
    private Vector2 movementVector ;

    public InputHandler(PlayScreen playScreen)
    {
        this.playScreen = playScreen;
    }

    public void movementInput(float deltaTime)//delta time cat timp a trecut de la ultimul update
    {
        if (playScreen.getPlayer()!=null)
        {
//            if(Gdx.input.isKeyPressed(Input.Keys.A) && playScreen.getPlayer().playerBody.getLinearVelocity().x>= -3)
//            {
//                playScreen.getPlayer().playerBody.applyLinearImpulse(new Vector2(-0.5f,0f),playScreen.getPlayer().playerBody.getWorldCenter(),true);
//            }
//            if(Gdx.input.isKeyPressed(Input.Keys.D) && playScreen.getPlayer().playerBody.getLinearVelocity().x<=3)
//            {
//                playScreen.getPlayer().playerBody.applyLinearImpulse(new Vector2(0.5f,0f),playScreen.getPlayer().playerBody.getWorldCenter(),true);
//            }
//            if(Gdx.input.isKeyPressed(Input.Keys.S) && playScreen.getPlayer().playerBody.getLinearVelocity().y >= -3)
//            {
//                playScreen.getPlayer().playerBody.applyLinearImpulse(new Vector2(0f,-0.5f),playScreen.getPlayer().playerBody.getWorldCenter(),true);
//            }
//            if(Gdx.input.isKeyPressed(Input.Keys.W) && playScreen.getPlayer().playerBody.getLinearVelocity().y<=3)
//            {
//                playScreen.getPlayer().playerBody.applyLinearImpulse(new Vector2(0f,0.5f),playScreen.getPlayer().playerBody.getWorldCenter(),true);
//            }

            if(playScreen.getController().isLeftPressed() && playScreen.getPlayer().playerBody.getLinearVelocity().x>= -3)
            {
                if(playScreen.getWorld().isLocked() == false) {
                    movementVector = new Vector2(-0.5f, 0f);
                    playScreen.getPlayer().playerBody.applyLinearImpulse(movementVector, playScreen.getPlayer().playerBody.getWorldCenter(), true);
                }
            }
            if(playScreen.getController().isRightPressed() && playScreen.getPlayer().playerBody.getLinearVelocity().x<=3)
            {
                if(playScreen.getWorld().isLocked() == false) {
                    movementVector = new Vector2(0.5f, 0f);
                    playScreen.getPlayer().playerBody.applyLinearImpulse(movementVector, playScreen.getPlayer().playerBody.getWorldCenter(), true);
                }
            }
            if(playScreen.getController().isDownPressed()&& playScreen.getPlayer().playerBody.getLinearVelocity().y >= -3)
            {
                if(playScreen.getWorld().isLocked() == false) {
                    movementVector = new Vector2(0f, -0.5f);
                    playScreen.getPlayer().playerBody.applyLinearImpulse(movementVector, playScreen.getPlayer().playerBody.getWorldCenter(), true);
                }
            }
            if(playScreen.getController().isUpPressed() && playScreen.getPlayer().playerBody.getLinearVelocity().y<=3)
            {
                if(playScreen.getWorld().isLocked() == false) {
                    movementVector = new Vector2(0f, 0.5f);
                    playScreen.getPlayer().playerBody.applyLinearImpulse(movementVector, playScreen.getPlayer().playerBody.getWorldCenter(), true);

                }
            }
            if (!movementPressed())
            {
                if(playScreen.getWorld().isLocked() == false) {
                    playScreen.getPlayer().playerBody.setLinearVelocity(0, 0);
                }
            }
            if(Gdx.input.justTouched() && !movementPressed())
            {
                if(playScreen.getWorld().isLocked() == false) {
                    if (movementVector == null)
                        movementVector = new Vector2(0.5f,0);
                    //Gdx.app.log("in","INAINTE");
                    DefaultBullet bullet = new DefaultBullet(playScreen,playScreen.getPlayer().playerBody.getPosition().x * Game.PPM,playScreen.getPlayer().playerBody.getPosition().y *Game.PPM);
                    //Gdx.app.log("in","DUPA");
                    Vector2 shotVector = new Vector2(movementVector.x*30,movementVector.y*30);
                    //Gdx.app.log("in","shot"+shotVector);
                    bullet.bulletBody.applyLinearImpulse(shotVector,bullet.bulletBody.getWorldCenter(),true);
                    playScreen.getBullets().add(bullet);
                }
            }
        }
    }
    public boolean movementPressed()
    {
        if (!playScreen.getController().isLeftPressed() && !playScreen.getController().isUpPressed() && !playScreen.getController().isDownPressed() && !playScreen.getController().isRightPressed()) {
            return false;
        }
        return true;
    }
}
