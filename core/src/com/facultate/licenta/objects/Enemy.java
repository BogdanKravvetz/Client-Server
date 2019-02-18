package com.facultate.licenta.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.facultate.licenta.screens.PlayScreen;

import java.util.Random;


//TODO Sintronizarea inamicilor
public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen playScreen;
    public Body enemyBody;
    public Vector2 velocity;

    public Enemy(PlayScreen playScreen,float x, float y)
    {
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(0,0);
    }
    protected abstract void defineEnemy();
    public abstract void update(float deltaTime);
    public abstract void onHit();

    public void reverseVelocity(boolean x,boolean y)
    {
        if (x)
            velocity.x  = -velocity.x;
        if (y)
            velocity.y = -velocity.y;
    }
    public void pickVelocity()
    {
        Random rdm = new Random();
        velocity.x = rdm.nextFloat();
        velocity.y = rdm.nextFloat();
    }
}
