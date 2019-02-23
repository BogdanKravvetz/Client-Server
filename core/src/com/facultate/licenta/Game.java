package com.facultate.licenta;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.facultate.licenta.screens.PlayScreen;

public class Game extends com.badlogic.gdx.Game {
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	//pixeli/metru, scalarea pentru Box2d
	public static final float PPM = 100;
	public static SpriteBatch batch;

	public static final short DEFAULT_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short OBJECT_BIT = 4;
	public static final short DESTROYED_BIT = 8;//bit pentru ignorarea coliziunii unui obiect distrus? sau filtru(cand nu mai exista inamici pune filtru pe DESTROYED_BIT pentru a avansa la urmatoarea camera.)
	public static final short ENEMY_BIT = 16;
	public static final short BULLET_BIT = 32;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}
	@Override
	public void render () {
		super.render();
	}
	@Override
	public void dispose () {
		batch.dispose();
	}
}