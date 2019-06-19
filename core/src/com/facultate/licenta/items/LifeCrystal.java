package com.facultate.licenta.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.facultate.licenta.objects.Player;
import com.facultate.licenta.screens.PlayScreen;
import com.facultate.licenta.tools.Constants;

import org.json.JSONObject;

public class LifeCrystal extends Item{


    public LifeCrystal(PlayScreen playScreen, float x, float y) {
        super(playScreen, x, y);
        setRegion(playScreen.getAtlas().findRegion("Character"),32*9,0,32,32);
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
            fixtureDef.filter.categoryBits = Constants.PICKUP_BIT;
            fixtureDef.filter.maskBits = Constants.DEFAULT_BIT | Constants.PLAYER_BIT;
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;
            body.createFixture(fixtureDef).setUserData(this);
            //shape.dispose();
        }
        else
        {
            defineItem();
        }
    }

    @Override
    public void use(Player player) {
        Gdx.app.log("Crystal", "player ID " + player.getId());
        Gdx.app.log("Crystal", "Socket ID " + playScreen.getConnectionHandler().getSocket().id());

        if (player.getId().equals(playScreen.getConnectionHandler().getSocket().id()))
        {
                playScreen.getPlayer().getPlayerStats().setCurrentHp(playScreen.getPlayer().getPlayerStats().getCurrentHp()+10);
                if(playScreen.getPlayer().getPlayerStats().getCurrentHp()>100)
                {
                    playScreen.getPlayer().getPlayerStats().setCurrentHp(100);
                }
            Gdx.app.log("Crystal", "current HP" + playScreen.getPlayer().getPlayerStats().getCurrentHp());
        }
        setToDestroy();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if(!destroyed) {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            body.setLinearVelocity(velocity);
        }
    }
    public void setToDestroy()
    {
        setToDestroy = true;
    }
    public void destroy()
    {
        world.destroyBody(body);
        destroyed = true;
        //stateTime = 0;
        body = null;
    }
}
