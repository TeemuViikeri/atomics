package fi.tuni.atomics.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fi.tuni.atomics.Atomics;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Atomics(), config);
		config.width = 2960;
		config.height = 1440;
		config.x = 0;
		config.y = 0;
	}
}
