package com.baptr.darkshaft;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.baptr.darkshaft.Darkshaft;

public class DesktopGame {
    public static void main(String[] args) {
        new LwjglApplication(new Darkshaft(), "Operation: Darkshaft", 480, 320, true);
    }
}
