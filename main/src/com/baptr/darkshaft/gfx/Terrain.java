package com.baptr.darkshaft.gfx;

import com.baptr.darkshaft.Darkshaft;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.assets.AssetManager;

public class Terrain extends Sprite {

    public Terrain() {
        super("",0,0);
    }

    public Terrain(AssetManager m, float x, float y) {
        super(m.get("terrain.png", Texture.class), x, y);
    }

    public static void init(Darkshaft g, AssetManager m) {
        m.load("terrain.png", Texture.class);
    }

}
