package com.facultate.licenta.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.facultate.licenta.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Snake Hunters";
		config.height = 500;  //900
		config.width = 700;	//1600
		new LwjglApplication(new Game(), config);
	}
}
