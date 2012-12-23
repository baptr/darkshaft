package com.baptr.darkshaft.gfx;

import com.baptr.darkshaft.Darkshaft;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.assets.AssetManager;

public class Terrain extends Sprite {

    public static int TILE_TYPES    = 4;
    public static int TILE_WIDTH    = 62;
    public static int TILE_X_OFFSET = 30;
    public static int TILE_HEIGHT   = 30;
    public static int TILE_Y_OFFSET = 15;

    private static int TILE_X_SPACING = 64;
    private static int TILE_Y_SPACING = 32;

    public Terrain(AssetManager m, int idx, float x, float y) {
        super();
        Texture t = m.get("terrain.png", Texture.class);
        TextureRegion[][] splits = TextureRegion.split(t, TILE_X_SPACING, TILE_Y_SPACING);
        this.setTexture(t);
        this.setRegion(splits[idx%2][idx/2]);
        this.setBounds(x, y, TILE_X_SPACING, TILE_Y_SPACING);
    }

    public static void init(Darkshaft g, AssetManager m) {
        m.load("terrain.png", Texture.class);
    }

}
