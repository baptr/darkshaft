package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.baptr.darkshaft.gfx.*;

public class Avatar extends Unit {

    public Avatar(AtlasRegion texture, float x, float y) {
        super(texture, x, y);
        xOffset = -texture.originalWidth/2;
        yOffset = -2;
    }

}
