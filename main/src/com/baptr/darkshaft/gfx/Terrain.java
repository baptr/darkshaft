package com.baptr.darkshaft.gfx;

import com.baptr.darkshaft.Darkshaft;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.assets.AssetManager;

public class Terrain extends Sprite {

    public Terrain(AssetManager m, int idx, float x, float y) {
        super();
        Texture t = m.get("terrain.png", Texture.class);
        TextureRegion[][] splits = TextureRegion.split(t, 64, 32);
        this.setTexture(t);
        this.setRegion(splits[idx%2][idx/2]);
        this.setBounds(x, y, 64, 32);
    }

    public static void init(Darkshaft g, AssetManager m) {
        m.load("terrain.png", Texture.class);
    }

}
