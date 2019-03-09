package com.facultate.licenta.objects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.facultate.licenta.Game;
import com.facultate.licenta.screens.PlayScreen;

public abstract class InteractiveTileObject {

    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public InteractiveTileObject(PlayScreen playScreen,Rectangle bounds)
    {
        this.world = playScreen.getWorld();
        this.map = playScreen.getMap();
        this.bounds = bounds;

        if(playScreen.getWorld().isLocked()==false) {
            BodyDef bodyDef = new BodyDef();
            //polygon shape pentru Fixture
            PolygonShape shape = new PolygonShape();
            //inainte de a crea fixture trebuie definita mai intai inainte de a fi adaugata in body
            FixtureDef fixtureDef = new FixtureDef();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((bounds.getX() + bounds.getWidth() / 2) / Game.PPM, (bounds.getY() + bounds.getHeight() / 2) / Game.PPM);
            //adauga Body-ul in lumea Box2d
            body = world.createBody(bodyDef);
            //defineste forma poligonului
            shape.setAsBox((bounds.getWidth() / 2) / Game.PPM, (bounds.getHeight() / 2) / Game.PPM);

            //seteaza categoria de coliziune.
            //fixtureDef.filter.categoryBits = Game.OBJECT_BIT;

            fixtureDef.shape = shape;
            //adauga fixture in body
            fixture = body.createFixture(fixtureDef);
        }
    }
    public abstract void onNoEnemies();

    public void setCategoryFilter( short filterBit)
    {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
    public TiledMapTileLayer.Cell getCell()
    {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int) (body.getPosition().x * Game.PPM / 32), (int) (body.getPosition().y * Game.PPM / 32));
    }
    public Array<TiledMapTileLayer.Cell> getGateCells()
    {
        Array<TiledMapTileLayer.Cell> cells = new Array<TiledMapTileLayer.Cell>();
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        for (int i=0;i<9;i++)
        {
            cells.add(layer.getCell((int) ((body.getPosition().x+((32*i)/Game.PPM) * Game.PPM / 32)), (int) (body.getPosition().y * Game.PPM / 32)));
        }
        return cells;
    }
}
