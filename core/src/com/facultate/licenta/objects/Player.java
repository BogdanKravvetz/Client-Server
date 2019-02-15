package com.facultate.licenta.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.facultate.licenta.Game;
import com.facultate.licenta.screens.PlayScreen;

public class Player extends Sprite {
    public World world ;
    public Body playerBody;
    private Vector2 position;
    //sprite = o singura imagine(pozitie) din sprite sheet
    private TextureRegion sprite;
    public Player(World world , PlayScreen playScreen)
    {
        //obtine regiunea din sprite sheet  asociata cu caracterul.
        super(playScreen.getAtlas().findRegion("sprite"));
        position = new Vector2(getX(),getY());
        this.world = world;
        definePlayer();
        //initializeaza sprite-ul cu textura de la coordonatele x,y de lungime si inaltime.
        sprite = new TextureRegion(getTexture(),0,0,70,161);
        setBounds(0,0,70/Game.PPM,161/Game.PPM);
        setRegion(sprite);
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
    public void update(float deltaTime) {
        //updateaza pozitia sprite=ului in functie de pozisia corpului fizic
        setPosition(playerBody.getPosition().x - getWidth()/2 , playerBody.getPosition().y - getHeight()/2);
    }

    public void definePlayer()
    {
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        bodyDef.position.set(300/ Game.PPM,300/ Game.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBody = this.world.createBody(bodyDef);
        shape.setAsBox(30/ Game.PPM,30/ Game.PPM);
        fixtureDef.shape = shape;
        playerBody.createFixture(fixtureDef);
        shape.dispose();
    }
}
