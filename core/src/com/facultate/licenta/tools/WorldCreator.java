package com.facultate.licenta.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.facultate.licenta.Game;
import com.facultate.licenta.objects.Gate;
import com.facultate.licenta.objects.Spider;
import com.facultate.licenta.screens.PlayScreen;

public class WorldCreator {
    public Array<Spider> getSpiders() {
        return spiders;
    }

    private Array<Spider> spiders;

    public  WorldCreator(PlayScreen playScreen)
    {
        World world = playScreen.getWorld();
        TiledMap map = playScreen.getMap();
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

            //seteaza categoria de coliziune.
            fixtureDef.filter.categoryBits = Game.OBJECT_BIT;

            fixtureDef.shape = shape;
            //adauga fixture in body
            body.createFixture(fixtureDef);
        }
        //creaza portile
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class))//!!!!!!!!GET 3 HARD CODAT !!!!!!!!!!!!! layer-ele se numara de jos in sus de la 0 in tiled
        {
            //dreptunigiul care definste coliziunea
            Rectangle rect = ((RectangleMapObject) object) .getRectangle();
            new Gate(playScreen,rect);
        }
        //creaza paienjeni
        spiders = new Array<Spider>();
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class))//!!!!!!!!GET 4 HARD CODAT !!!!!!!!!!!!! layer-ele se numara de jos in sus de la 0 in tiled
        {
            //dreptunigiul care definste coliziunea
            Rectangle rect = ((RectangleMapObject) object) .getRectangle();

            //rect.getX()/Game.PPM
            spiders.add(new Spider(playScreen,rect.getX(),rect.getY()));
        }
        shape.dispose();
    }

}
