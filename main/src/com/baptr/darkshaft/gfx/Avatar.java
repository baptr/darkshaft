package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.baptr.darkshaft.gfx.*;

public class Avatar extends Unit {
    
    public Avatar(float x, float y, TextureAtlas atlas) {
        super(x, y, atlas, "Characters/Frank/Down", "Characters/Frank/East");
        xOffset = -getRegionWidth()/2;
        yOffset = -2;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(getX() < dest.x) {
            if(facing != Facing.EAST) {
                setAnimation("Characters/Frank/East");
                facing = Facing.EAST;
            }
        } else if(facing != Facing.DOWN) {
            setAnimation("Characters/Frank/Down");
            facing = Facing.DOWN;
        }
    }

}
