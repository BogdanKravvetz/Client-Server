package com.facultate.licenta.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.facultate.licenta.Game;
import com.facultate.licenta.screens.PlayScreen;

public class Spider extends Enemy {


    private float stateTime;
    private Animation walk;
    private Array<TextureRegion> frames;
    public Spider(PlayScreen playScreen, float x, float y) {
        super(playScreen, x, y);
        frames = new Array<TextureRegion>();
        for (int i=0;i<4;i++)
            frames.add(new TextureRegion(playScreen.getAtlas().findRegion("sprite"),i * 90, 0, 90, 150));
        walk = new Animation(0.4f,frames);
        stateTime =0;
        setBounds(getX(),getY(),90/Game.PPM,150/Game.PPM);//pentru a stii cat de mare e sprite-ul
    }

    public void update(float deltaTime)
    {
        stateTime +=deltaTime;
        setPosition(enemyBody.getPosition().x - getWidth()/2,enemyBody.getPosition().y - getHeight()/2);
        setRegion((TextureRegion) walk.getKeyFrame(stateTime,true));
    }

    @Override
    protected void defineEnemy() {

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        bodyDef.position.set(300/ Game.PPM,300/ Game.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        enemyBody = this.world.createBody(bodyDef);
        shape.setAsBox(30/ Game.PPM,30/ Game.PPM);
        //defineste categoria fixturii ca fiind bit de jucator pentru coliziune selectiva.
        fixtureDef.filter.categoryBits = Game.ENEMY_BIT;

        //biti cu care jucatorul poate avea coliziuni
        //MASCA
        fixtureDef.filter.maskBits = Game.DEFAULT_BIT | Game.OBJECT_BIT;
        fixtureDef.shape = shape;
        enemyBody.createFixture(fixtureDef).setUserData("player");
        shape.dispose();

    }
}
