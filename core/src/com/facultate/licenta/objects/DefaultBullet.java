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

public class DefaultBullet extends Bullet {

    private float stateTime;
    private Animation walk;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private float lifeSpan;

    public DefaultBullet(PlayScreen playScreen,float x,float y)
    {
        super(playScreen, x, y);
        frames = new Array<TextureRegion>();
        for (int i=0;i<1;i++)
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("sprite"),i * 90, 0, 90, 50));
        walk = new Animation(0.4f,frames);
        stateTime =0;
        setBounds(x,y,90/ Game.PPM,50/Game.PPM);//pentru a stii cat de mare e sprite-ul
        setToDestroy = false;
        destroyed = false;
        lifeSpan = 8;
    }

    @Override
    protected void defineBullet() {
        if(!playScreen.getWorld().isLocked()) {

            BodyDef bodyDef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            FixtureDef fixtureDef = new FixtureDef();
            bodyDef.position.set(getX() / Game.PPM, getY() / Game.PPM);
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bulletBody = this.world.createBody(bodyDef);
            //Gdx.app.log("Bullet",bulletBody.toString());
            shape.setAsBox(30 / Game.PPM, 30 / Game.PPM);
            //defineste categoria fixturii ca fiind bit de jucator pentru coliziune selectiva.
            fixtureDef.filter.categoryBits = Game.BULLET_BIT;
            //biti cu care jucatorul poate avea coliziuni (MASCA)
            fixtureDef.filter.maskBits = Game.DEFAULT_BIT | Game.OBJECT_BIT | Game.ENEMY_BIT;
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;
            bulletBody.createFixture(fixtureDef).setUserData(this);
            //shape.dispose();
        }
        else
        {
            defineBullet();
        }
    }
    @Override
    public void update(float deltaTime)
    {
        stateTime +=deltaTime;
        if((int)stateTime == (int)lifeSpan)
        {
            setToDestroy = true;
        }
        if(setToDestroy && !destroyed) {
            destroy();
        }
        if(!destroyed) {
            setPosition(bulletBody.getPosition().x - getWidth() / 2, bulletBody.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) walk.getKeyFrame(stateTime, true));
        }
    }
    @Override
    public void draw(Batch batch)
    {
        if(!destroyed || stateTime<=0.0001f)
        {
            super.draw(batch);
        }
    }
    @Override
    public void onHit() {
        setToDestroy = true;
    }
    protected void destroy()
    {
        world.destroyBody(bulletBody);
        bulletBody = null;
        destroyed = true;
        stateTime = 0;
        playScreen.getBullets().removeValue(this,true);
    }
}