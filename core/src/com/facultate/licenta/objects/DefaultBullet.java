package com.facultate.licenta.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.facultate.licenta.screens.PlayScreen;
import com.facultate.licenta.stats.BulletStats;
import com.facultate.licenta.tools.Constants;

public class DefaultBullet extends Bullet {

    private float stateTime;
    private Animation walk;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;

    public DefaultBullet(PlayScreen playScreen,float x,float y)
    {
        super(playScreen, x, y);
        frames = new Array<TextureRegion>();
        for (int i=0;i<1;i++)
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("Character"),32*8, 0, 32, 32));
        walk = new Animation(0.4f,frames);
        stateTime =0;
        setBounds(x,y,25/ Constants.PPM,25/Constants.PPM);//pentru a stii cat de mare e sprite-ul
        setToDestroy = false;
        destroyed = false;

    }

    @Override
    protected void defineBullet() {
        if(!playScreen.getWorld().isLocked()) {

            BodyDef bodyDef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            FixtureDef fixtureDef = new FixtureDef();
            bodyDef.position.set(getX() / Constants.PPM, getY() / Constants.PPM);
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bulletBody = this.world.createBody(bodyDef);
            shape.setAsBox(12 / Constants.PPM, 12 / Constants.PPM);
                                                                                                                //defineste categoria fixturii ca fiind bit de glont pentru coliziune selectiva.
            fixtureDef.filter.categoryBits = Constants.BULLET_BIT;
                                                                                                                //biti cu care corpul poate avea coliziuni (MASCA)
            fixtureDef.filter.maskBits = Constants.DEFAULT_BIT | Constants.OBJECT_BIT | Constants.ENEMY_BIT;
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;
            bulletBody.createFixture(fixtureDef).setUserData(this);
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
        if((int)stateTime == (int)bulletStats.getLifeSpan())
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