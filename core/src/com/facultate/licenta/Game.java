package com.facultate.licenta;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.facultate.licenta.screens.MenuScreen;
import com.facultate.licenta.screens.PlayScreen;

public class Game extends com.badlogic.gdx.Game {

	public SpriteBatch batch;
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MenuScreen(this));
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