package com.facultate.licenta.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.facultate.licenta.screens.PlayScreen;


//TODO Sintronizarea inamicilor
public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen playScreen;
    public Body enemyBody;

    public Enemy(PlayScreen playScreen,float x, float y)
    {
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        setPosition(x,y);
        defineEnemy();
    }
    protected abstract void defineEnemy();
}
