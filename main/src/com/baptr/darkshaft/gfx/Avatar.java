package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.baptr.darkshaft.gfx.*;

public class Avatar extends Unit {

    private String owner = "Local Player";
    
    public Avatar(float x, float y, TextureAtlas atlas) {
        super(x, y, atlas, "Characters/Frank/Down", "Characters/Frank/East");
        xOffset = -getRegionWidth()/2;
        yOffset = -2;
    }

    public void setOwner(String name) {
        owner = name;
    }

    public String getOwner() {
        return owner;
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
