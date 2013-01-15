package com.baptr.darkshaft;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.utils.Array;
import com.baptr.darkshaft.Darkshaft;

public class DesktopGame {
    public static void main(String[] args) {
        boolean splash = true;
        if(args.length > 0) {
            splash = false;
        }
        new LwjglApplication(new Darkshaft(splash), "Operation: Darkshaft", 800, 600, true);
    }
}
