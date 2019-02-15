package com.facultate.licenta.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.facultate.licenta.Game;

public class WorldCreator {

    public  WorldCreator(World world , TiledMap map)
    {
        //inainte de a crea in Body trebuie mai intai specificat ceea ce contine
        BodyDef bodyDef = new BodyDef();
        //polygon shape pentru Fixture
        PolygonShape shape = new PolygonShape();
        //inainte de a crea fixture trebuie definita mai intai inainte de a fi adaugata in body
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class))//!!!!!!!!GET 2 HARD CODAT !!!!!!!!!!!!! layer-ele se numara de jos in sus de la 0 in tiled
        {
            //dreptunigiul care definste coliziunea
            Rectangle rect = ((RectangleMapObject) object) .getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX()+rect.getWidth()/2)/ Game.PPM, (rect.getY()+rect.getHeight()/2) / Game.PPM);
            //adauga Body-ul in lumea Box2d
            body = world.createBody(bodyDef);
            //defineste forma poligonului
            shape.setAsBox((rect.getWidth()/2)/ Game.PPM,(rect.getHeight()/2)/ Game.PPM);
            fixtureDef.shape = shape;
            //adauga fixture in body
            body.createFixture(fixtureDef);
        }
        shape.dispose();
    }
}
