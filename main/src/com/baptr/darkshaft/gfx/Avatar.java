package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.baptr.darkshaft.gfx.*;

public class Avatar extends Unit {

    public Avatar(float x, float y, TextureAtlas atlas) {
        super(x, y, atlas, "Characters/Frank/Down");
        xOffset = -getRegionWidth()/2;
        yOffset = -2;
    }

}
