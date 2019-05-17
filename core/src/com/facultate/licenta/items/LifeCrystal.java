package com.facultate.licenta.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.facultate.licenta.screens.PlayScreen;
import com.facultate.licenta.tools.Constants;

public class LifeCrystal extends Item{


    public LifeCrystal(PlayScreen playScreen, float x, float y) {
        super(playScreen, x, y);
        setRegion(playScreen.getAtlas().findRegion("Character"),90,0,32,32);
        velocity = new Vector2(0,0);
    }

    @Override
    public void defineItem() {
        if(!playScreen.getWorld().isLocked()) {
            BodyDef bodyDef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            FixtureDef fixtureDef = new FixtureDef();
            bodyDef.position.set(getX(), getY());
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            body = this.world.createBody(bodyDef);
            shape.setAsBox(20 / Constants.PPM, 20 / Constants.PPM);
            //defineste categoria fixturii ca fiind bit de jucator pentru coliziune selectiva.
//            fixtureDef.filter.categoryBits = Constants.ENEMY_BIT;
//            //biti cu care jucatorul poate avea coliziuni (MASCA)
//            fixtureDef.filter.maskBits = Constants.DEFAULT_BIT | Constants.OBJECT_BIT | Constants.BULLET_BIT;
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef).setUserData(this);
            //shape.dispose();
        }
        else
        {
            defineItem();
        }
    }

    @Override
    public void use() {
        destroy();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if(!destroyed) {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            body.setLinearVelocity(velocity);
        }
    }
}
