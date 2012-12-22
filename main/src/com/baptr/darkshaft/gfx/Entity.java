package com.baptr.darkshaft.gfx;

public class Entity extends Sprite {

    public Entity(String imgPath, float x, float y) {
        super(imgPath, x, y);
    }

    public void moveBy(float dx, float dy) {
        pos.x += dx;
        pos.y += dy;
    }

    public void moveTo(float x, float y) {
        pos.x = x;
        pos.y = y;
    }

}
