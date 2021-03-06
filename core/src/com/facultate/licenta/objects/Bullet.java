package com.facultate.licenta.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.facultate.licenta.screens.PlayScreen;
import com.facultate.licenta.stats.BulletStats;

public abstract class Bullet extends Sprite {

    public PlayScreen getPlayScreen() {
        return playScreen;
    }

    protected PlayScreen playScreen;
    protected World world;
    public Body bulletBody;
    public Vector2 velocity;



    public String getPlayerId() {
        return playerId;
    }

    private String playerId;

    public BulletStats getBulletStats() {
        return bulletStats;
    }

    protected BulletStats bulletStats;

    public Bullet(PlayScreen playScreen,float x ,float y,String myPlayerId)
    {
        super(playScreen.getAtlas().findRegion("Character"));
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        setPosition(x,y);
        defineBullet();
        velocity = new Vector2(0,0);
        bulletStats =  new BulletStats();
        playerId = myPlayerId;
    }
    protected abstract void defineBullet();
    public abstract void update(float deltaTime);
    public abstract void onHit();
    protected abstract void destroy();

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
}
