package com.facultate.licenta.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.facultate.licenta.objects.Player;
import com.facultate.licenta.screens.PlayScreen;
import com.facultate.licenta.tools.Constants;

public abstract class Item extends Sprite {
    protected PlayScreen playScreen;
    protected World world;
    protected Vector2 velocity;
    protected boolean setToDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen playScreen,float x, float y)
    {
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();

        setPosition(x,y);
        setBounds(getX(),getY(),32/ Constants.PPM,32/Constants.PPM);

        defineItem();
        setToDestroy = false;
        destroyed = false;
    }


    public abstract void defineItem();
    public abstract void use(Player player);

    public void update(float dt)
    {
        if(setToDestroy && !destroyed)
        {
            world.destroyBody(body);
            destroyed = true;
        }
    }
    public void draw(Batch batch)
    {
        if(!destroyed)
            super.draw(batch);
    }
    public void destroy(){
        setToDestroy = true;
    }
}
