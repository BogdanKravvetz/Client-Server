package com.facultate.licenta.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.facultate.licenta.Game;
import com.facultate.licenta.screens.PlayScreen;

import io.socket.emitter.Emitter;

public class Spider extends Enemy {


    private float stateTime;
    private Animation walk;
    private Array<TextureRegion> frames;
    public boolean setToDestroy;
    private boolean destroyed;
    private int changeDirectionTimer;
    private float timer;
    public Spider(PlayScreen playScreen, float x, float y) {
        super(playScreen, x, y);
        frames = new Array<TextureRegion>();
        for (int i=0;i<4;i++)
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("sprite"),i * 90, 0, 90, 150));
        walk = new Animation(0.4f,frames);
        stateTime =0;
        setBounds(x,y,90/Game.PPM,150/Game.PPM);//pentru a stii cat de mare e sprite-ul
        setToDestroy = false;
        destroyed = false;
        changeDirectionTimer = 2;
        timer = 0;
    }

    public void update(float deltaTime)
    {
        stateTime +=deltaTime;
        timer +=deltaTime;
        if(setToDestroy && !destroyed) {
            destroy();
        }
        if(!destroyed) {
//            if ((int)timer >= changeDirectionTimer) {
//                pickVelocity();
//                reverseVelocity(Math.random() < 0.5 ? true : false,Math.random() < 0.5 ? true : false );
//                timer=0;
//            }
//            enemyBody.setLinearVelocity(velocity);
            setPosition(enemyBody.getPosition().x - getWidth() / 2, enemyBody.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) walk.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        bodyDef.position.set(getX()/Game.PPM,getY()/ Game.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        enemyBody = this.world.createBody(bodyDef);
        shape.setAsBox(30/ Game.PPM,30/ Game.PPM);
        //defineste categoria fixturii ca fiind bit de jucator pentru coliziune selectiva.
        fixtureDef.filter.categoryBits = Game.ENEMY_BIT;
        //biti cu care jucatorul poate avea coliziuni (MASCA)
        fixtureDef.filter.maskBits = Game.DEFAULT_BIT | Game.OBJECT_BIT;
        fixtureDef.shape = shape;
        enemyBody.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
    }
    @Override
    public void draw(Batch batch)
    {
        if(!destroyed || stateTime<0.01f)
        {
            super.draw(batch);
        }
    }

    @Override
    public void onHit() {
        setToDestroy = true;
    }
    public void destroy()
    {
        world.destroyBody(enemyBody);
        destroyed = true;
        stateTime = 0;
    }

}
