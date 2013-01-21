package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.baptr.darkshaft.core.Entity.UnitType;

public class Avatar extends Unit {

    private String owner = "Local Player";

    private static final String[] GFX = {"Characters/Frank/Down",
            "Characters/Frank/East", "Characters/Frank/South" };
    
    public Avatar(float x, float y) {
        super(x, y, GFX);
        xOffset = -getRegionWidth()/2;
        yOffset = -2;
        this.unitType = UnitType.PLAYER;
    }

    public void setOwner(String name) {
        owner = name;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public boolean update(float delta) {
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
        return false;
    }
}
