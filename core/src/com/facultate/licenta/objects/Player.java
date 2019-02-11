package com.facultate.licenta.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite {
    private Vector2 position;
    public Player(Texture texture)
    {
        super(texture);
        position = new Vector2(getX(),getY());
    }
    public boolean hasMoved()
    {
        if(position.x != getX() || position.y != getY())
        {
            position.x=getX();
            position.y=getY();
            return true;
        }else
        {
            return false;
        }
    }
}
