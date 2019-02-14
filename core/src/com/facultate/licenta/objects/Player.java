package com.facultate.licenta.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.facultate.licenta.Game;

public class Player extends Sprite {
    public World world ;
    public Body playerBody;
    private Vector2 position;
    public Player(Texture texture,World world)
    {
        super(texture);
        position = new Vector2(getX(),getY());
        this.world = world;
        definePlayer();
    }
    public boolean hasMoved()
    {
        if(position.x != getX() || position.y != getY())
        {
            position.x=getX();
            position.y=getY();
            return true;
        }else
        {
            return false;
        }
    }
    public void definePlayer()
    {
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        bodyDef.position.set(100/ Game.PPM,100/ Game.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBody = this.world.createBody(bodyDef);
        shape.setAsBox(30/ Game.PPM,30/ Game.PPM);
        fixtureDef.shape = shape;
        playerBody.createFixture(fixtureDef);

    }
}
