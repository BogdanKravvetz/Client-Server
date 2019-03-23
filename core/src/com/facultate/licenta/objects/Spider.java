package com.facultate.licenta.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.facultate.licenta.screens.PlayScreen;
import com.facultate.licenta.stats.EnemyStats;
import com.facultate.licenta.tools.Constants;

import java.util.UUID;

public class Spider extends Enemy {
    private String spiderId ;
    private float stateTime;
    private Animation walk;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private EnemyStats enemyStats;

    public EnemyStats getEnemyStats() {
        return enemyStats;
    }
    public String getSpiderId() {
        return spiderId;
    }

    public Spider(PlayScreen playScreen, float x, float y) {
        super(playScreen, x, y);
        frames = new Array<TextureRegion>();
        for (int i=0;i<4;i++)
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("Character"),i * 90, 0, 90, 80));
        walk = new Animation(0.4f,frames);
        stateTime =0;
        setBounds(x,y,90/Constants.PPM,80/Constants.PPM);//pentru a stii cat de mare e sprite-ul
        setToDestroy = false;
        destroyed = false;
        spiderId = UUID.randomUUID().toString();
        enemyStats = new EnemyStats();
    }

    public void update(float deltaTime)
    {
        stateTime +=deltaTime;
        if(setToDestroy && !destroyed) {
            destroy();

        }
        if(!destroyed) {
            setPosition(enemyBody.getPosition().x - getWidth() / 2, enemyBody.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) walk.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {
        if(!playScreen.getWorld().isLocked()) {
            BodyDef bodyDef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            FixtureDef fixtureDef = new FixtureDef();
            bodyDef.position.set(getX() / Constants.PPM, getY() / Constants.PPM);
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            enemyBody = this.world.createBody(bodyDef);
            shape.setAsBox(30 / Constants.PPM, 30 / Constants.PPM);
            //defineste categoria fixturii ca fiind bit de jucator pentru coliziune selectiva.
            fixtureDef.filter.categoryBits = Constants.ENEMY_BIT;
            //biti cu care jucatorul poate avea coliziuni (MASCA)
            fixtureDef.filter.maskBits = Constants.DEFAULT_BIT | Constants.OBJECT_BIT | Constants.BULLET_BIT;
            fixtureDef.shape = shape;
            enemyBody.createFixture(fixtureDef).setUserData(this);
            //shape.dispose();
        }
        else
        {
            defineEnemy();
        }
    }
    @Override
    public void draw(Batch batch)
    {
        if(!destroyed || stateTime<0.2f)
        {
            super.draw(batch);
        }
    }

    @Override
    public void onEnemyHit(Bullet bullet) {
        enemyStats.setCurrentHp(enemyStats.getCurrentHp()-bullet.getBulletStats().getDamage());
        Gdx.app.log("SPIDER",enemyStats.getCurrentHp()+" HP");
        if(enemyStats.getCurrentHp() <= 0f)
            setToDestroy = true;
    }
    public void destroy()
    {
        world.destroyBody(enemyBody);
        destroyed = true;
        stateTime = 0;
        enemyBody = null;
    }
}