package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;

public class Defense extends Entity {

    private static TileAtlas atlas;

    public static void setAtlas(TileAtlas atlas) {
        if(Defense.atlas != null)
            throw new RuntimeException("Attempt to redefine Defense.atlas");
        Defense.atlas = atlas;
    }

    public Defense(int tileId, float x, float y) {
        super(atlas.getRegion(tileId), x, y);
    }

}
