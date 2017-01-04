package net.baptr.darkshaft.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.baptr.darkshaft.Darkshaft;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.width = 800;
    config.height = 600;
    config.fullscreen = false;
    config.title = "Operation: Darkshaft";
    boolean splash = arg.length == 0;
		new LwjglApplication(new Darkshaft(splash), config);
	}
}
