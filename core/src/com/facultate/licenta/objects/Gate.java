package com.facultate.licenta.objects;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.facultate.licenta.Game;
import com.facultate.licenta.screens.PlayScreen;


//TODO (poate) sincronizare daca jucatorul poate avansa sau nu
//daca ma asigur ca potiti curenta a jucatorului este corect updatata atunci e posibil sa sar peste asta.
public class Gate extends InteractiveTileObject{
    private PlayScreen playScreen;

    public Gate(PlayScreen playScreen, Rectangle bounds)
    {
        super(playScreen,bounds);
        this.playScreen = playScreen;
        fixture.setUserData(this);
        setCategoryFilter(Game.OBJECT_BIT);
    }
    @Override
    public void onNoEnemies() {
        if (playScreen.test) {
            setCategoryFilter(Game.DESTROYED_BIT);
            for (TiledMapTileLayer.Cell cell: getGateCells()) {
                if(cell!=null)
                    cell.setTile(null);
            }
        }
    }
}