package com.facultate.licenta.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
            if(Gdx.input.isKeyPressed(Input.Keys.A))
            {
                playScreen.getPlayer().setPosition( playScreen.getPlayer().getX()+(-200 * deltaTime), playScreen.getPlayer().getY());
            }
            if(Gdx.input.isKeyPressed(Input.Keys.D))
            {
                playScreen.getPlayer().setPosition( playScreen.getPlayer().getX()+(200 * deltaTime), playScreen.getPlayer().getY());
            }
            if(Gdx.input.isKeyPressed(Input.Keys.S))
            {
                playScreen.getPlayer().setPosition( playScreen.getPlayer().getX(), playScreen.getPlayer().getY()+(-200 * deltaTime));
            }
            if(Gdx.input.isKeyPressed(Input.Keys.W))
            {
                playScreen.getPlayer().setPosition( playScreen.getPlayer().getX(), playScreen.getPlayer().getY()+(200 * deltaTime));
            }

            if(playScreen.getController().isLeftPressed())
            {
                playScreen.getPlayer().setPosition( playScreen.getPlayer().getX()+(-200 * deltaTime), playScreen.getPlayer().getY());
                playScreen.getGameCamera().position.x -= deltaTime*600;
            }
            if(playScreen.getController().isRightPressed())
            {
                playScreen.getPlayer().setPosition( playScreen.getPlayer().getX()+(200 * deltaTime), playScreen.getPlayer().getY());
                playScreen.getGameCamera().position.x += deltaTime *600;
            }
            if(playScreen.getController().isDownPressed())
            {
                playScreen.getPlayer().setPosition( playScreen.getPlayer().getX(), playScreen.getPlayer().getY()+(-200 * deltaTime));
                playScreen.getGameCamera().position.y -= deltaTime*600;
            }
            if(playScreen.getController().isUpPressed())
            {
                playScreen.getPlayer().setPosition( playScreen.getPlayer().getX(), playScreen.getPlayer().getY()+(200 * deltaTime));
                playScreen.getGameCamera().position.y += deltaTime*600;
            }
        }
    }
}
