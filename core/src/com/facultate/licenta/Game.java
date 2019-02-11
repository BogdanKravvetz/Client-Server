package com.facultate.licenta;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.facultate.licenta.screens.PlayScreen;

public class Game extends com.badlogic.gdx.Game {
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	public SpriteBatch batch;

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
