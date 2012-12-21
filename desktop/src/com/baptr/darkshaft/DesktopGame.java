package com.baptr.darkshaft;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.baptr.darkshaft.Game;

public class DesktopGame {
    public static void main(String[] args) {
        new LwjglApplication(new Game(), "Operation: Darkshaft", 480, 320, true);
    }
}
