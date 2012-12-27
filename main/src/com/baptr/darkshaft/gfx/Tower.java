package com.baptr.darkshaft.gfx;

public class Tower extends Defense {

    public enum TowerType {
        BASIC(5,1f,1f,1f),
        FIRE(5,1f,0.25f,0.25f),
        WATER(5,0.4f,0.5f,1f),
        SPIRIT(5,0.25f,1f,0.25f, 0.5f);

        private int tileId;
        private float r, g, b, a;

        TowerType(int tileId, float r, float g, float b, float a) {
            this.tileId = tileId;
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
    }

    TowerType type;

    public Tower(TowerType t, int x, int y) {
        super(t.tileId, x, y);
        this.type = t;
        type.setTint(this);
    }

    public Tower(int x, int y) {
        this(TowerType.BASIC, x, y);
    }
}
