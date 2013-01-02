package com.baptr.darkshaft.gfx;

import com.baptr.darkshaft.util.MapUtils;

public class Tower extends Defense {

    public enum TowerType {
        BASIC(0,1f,1f,1f),
        FIRE(0,1f,0.25f,0.25f),
        WATER(0,0.4f,0.5f,1f),
        SPIRIT(0,0.25f,1f,0.25f, 0.5f),
        NONE(0,0f,0f,0f);

        private int tileId;
        private float r, g, b, a;

        TowerType(int tileOffset, float r, float g, float b, float a) {
            this.tileId = MapUtils.findTileSetGid(TILE_SET_NAME)+tileOffset;
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        TowerType(int tileId, float r, float g, float b) {
            this(tileId, r, g, b, 1.0f);
        }

        int getTileId() {
            return tileId;
        }

        void setTint(Tower t) {
            t.setColor(r, g, b, a);
        }
        
        void setTint(Tower t, boolean isMarker){
            if(isMarker){
                t.setColor(r, g, b, a/2f);
            } else {
                t.setColor(r, g, b, a);
            }
        }
    }

    TowerType type;

    static final String TILE_SET_NAME = "towers";

    public Tower(TowerType t, int x, int y) {
        super(t.tileId, x, y);
        this.type = t;
        type.setTint(this);
    }
    
    public Tower(TowerType t, int x, int y, boolean isMarker) {
        super(t.tileId, x, y);
        this.type = t;
        type.setTint(this, isMarker);
    }

    public Tower(int x, int y) {
        this(TowerType.BASIC, x, y);
    }
    
    public Tower(int x, int y, boolean isMarker) {
        this(TowerType.BASIC, x, y, isMarker);
    }
    
    public void setTowerType(TowerType t, boolean isMarker){
        this.type = t;
        type.setTint(this, isMarker);
    }
    
    public TowerType getTowerType(){
        return type;
    }
}
