package com.facultate.licenta.tools;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class Constants {
//1280 , 720
    public static final int WIDTH =1280;
    public static final int HEIGHT =720 ;
    //pixeli/metru, scalarea pentru Box2d
    public static final float PPM = 64;

    public static final short DEFAULT_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short OBJECT_BIT = 4;
    public static final short DESTROYED_BIT = 8;    //bit pentru ignorarea coliziunii unui obiect distrus? sau filtru(cand nu mai exista inamici pune filtru pe DESTROYED_BIT pentru a avansa la urmatoarea camera.)
    public static final short ENEMY_BIT = 16;
    public static final short BULLET_BIT = 32;
    public static final short PICKUP_BIT = 64;
    public static final short ENEMY_BULLET_BIT = 128;

    public static TextField name ;


}
