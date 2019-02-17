package com.facultate.licenta.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.facultate.licenta.objects.Gate;
import com.facultate.licenta.screens.PlayScreen;

public class InputHandler {

    PlayScreen playScreen;

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
                //playScreen.getPlayer().playerBody.setLinearVelocity(new Vector2(-3f,0f));
                playScreen.getPlayer().playerBody.applyLinearImpulse(new Vector2(-0.5f,0f),playScreen.getPlayer().playerBody.getWorldCenter(),true);
                playScreen.test = true;
            }
            if(playScreen.getController().isRightPressed() && playScreen.getPlayer().playerBody.getLinearVelocity().x<=3)
            {
                //playScreen.getPlayer().playerBody.setLinearVelocity(new Vector2(3f,0f));
                playScreen.getPlayer().playerBody.applyLinearImpulse(new Vector2(0.5f,0f),playScreen.getPlayer().playerBody.getWorldCenter(),true);
                playScreen.test = false;
            }
            if(playScreen.getController().isDownPressed()&& playScreen.getPlayer().playerBody.getLinearVelocity().y >= -3)
            {
                //playScreen.getPlayer().playerBody.setLinearVelocity(new Vector2(0f,-3f));
                playScreen.getPlayer().playerBody.applyLinearImpulse(new Vector2(0f,-0.5f),playScreen.getPlayer().playerBody.getWorldCenter(),true);
            }
            if(playScreen.getController().isUpPressed() && playScreen.getPlayer().playerBody.getLinearVelocity().y<=3)
            {
                //playScreen.getPlayer().playerBody.setLinearVelocity(new Vector2(0f,3f));
                playScreen.getPlayer().playerBody.applyLinearImpulse(new Vector2(0f,0.5f),playScreen.getPlayer().playerBody.getWorldCenter(),true);
               // playScreen.getPlayer().setPosition( playScreen.getPlayer().getX(), playScreen.getPlayer().getY()+(200 * deltaTime));
               // updateCameraPosition();
            }
            if (!playScreen.getController().isLeftPressed() && !playScreen.getController().isUpPressed() && !playScreen.getController().isDownPressed() && !playScreen.getController().isRightPressed())
            {
                playScreen.getPlayer().playerBody.setLinearVelocity(0,0);
            }
        }
    }
    private void updateCameraPosition()
    {
        playScreen.getGameCamera().position.x = playScreen.getPlayer().playerBody.getPosition().x;
        playScreen.getGameCamera().position.y = playScreen.getPlayer().playerBody.getPosition().y;
    }
}
