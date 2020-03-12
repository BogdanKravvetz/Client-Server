package com.facultate.licenta.objects;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.facultate.licenta.screens.PlayScreen;
import com.facultate.licenta.tools.Constants;


//TODO (poate) sincronizare daca jucatorul poate avansa sau nu
//daca ma asigur ca potiti curenta a jucatorului este corect updatata atunci e posibil sa sar peste asta.
public class Gate extends InteractiveTileObject{
    private PlayScreen playScreen;

    public Gate(PlayScreen playScreen, Rectangle bounds)
    {
        super(playScreen,bounds);
        this.playScreen = playScreen;
        fixture.setUserData(this);
        setCategoryFilter(Constants.OBJECT_BIT);
    }

    @Override
    public void onNoEnemies() {
        if (playScreen.test) {
            setCategoryFilter(Constants.DESTROYED_BIT);
            for (TiledMapTileLayer.Cell cell: getGateCells()) {
                if(cell!=null)
                    cell.setTile(null);
            }
        }
    }
}