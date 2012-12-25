package com.baptr.darkshaft.gfx;

public class Tower extends Defense {

    public enum TowerType {
        BASIC(1f,1f,1f),
        FIRE(1f,0.25f,0.25f),
        WATER(0.4f,0.5f,1f),
        SPIRIT(0.25f,1f,0.25f, 0.5f);

        private float r, g, b, a;

        TowerType(float r, float g, float b, float a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        TowerType(float r, float g, float b) {
            this(r, g, b, 1.0f);
        }

        void setTint(Tower t) {
            t.setColor(r, g, b, a);
        }
    }

    TowerType type;

    public Tower(TowerType t, float x, float y) {
        super("tower.png", x, y);
        this.type = t;
        type.setTint(this);
    }

    public Tower(String imgPath, float x, float y) {
        super(imgPath, x, y);
    }

}
