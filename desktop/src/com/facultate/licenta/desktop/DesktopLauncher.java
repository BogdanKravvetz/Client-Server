package com.facultate.licenta.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.facultate.licenta.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Snake Hunters";
		config.height = 576;  //900
		config.width = 1024;	//1600
		config.foregroundFPS = 60;
		config.addIcon("Icon128.png", Files.FileType.Internal);
		config.addIcon("Icon32.png", Files.FileType.Internal);
		config.addIcon("Icon16.png", Files.FileType.Internal);
		new LwjglApplication(new Game(), config);

	}
}
