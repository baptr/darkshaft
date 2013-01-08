package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.baptr.darkshaft.gfx.*;

public class Avatar extends Unit {

    private String owner = "Local Player";
    
    public Avatar(float x, float y, TextureAtlas atlas) {
        super(x, y, atlas, "Characters/Frank/Down", "Characters/Frank/East",
                "Characters/Frank/South");
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

        // TODO Facing calculation should be based on row/col, not x/y
        float deltaX = getX() - dest.x;
        float deltaY = getY() - dest.y;
        if(deltaX < 0) {
            if(facing != Facing.EAST) {
                setAnimation("Characters/Frank/East");
                facing = Facing.EAST;
            }
        } else if(deltaX > 0) {
            if(facing != Facing.WEST) {
                setAnimation("Characters/Frank/South");
                facing = Facing.WEST;
            }
        } else {
            if(facing != Facing.DOWN) {
                setAnimation("Characters/Frank/Down");
                facing = Facing.DOWN;
            }
        }
    }

}
