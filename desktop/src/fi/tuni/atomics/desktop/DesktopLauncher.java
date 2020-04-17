package fi.tuni.atomics.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fi.tuni.atomics.Atomics;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Atomics(), config);
		config.width = 1334;
		config.height = 750;
		config.x = 0;
		config.y = 0;
	}
}
