package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Sprite {
    Texture img;
    Rectangle pos;

    public Sprite(Texture t, Rectangle p) {
        img = t;
        pos = p;
    }

    public Sprite(Texture t, float x, float y) {
        img = t;
        pos = new Rectangle(x, y, img.getWidth(), img.getHeight());
    }

    public Sprite(String imgPath, float x, float y) {
        img = new Texture(Gdx.files.internal(imgPath));
        pos = new Rectangle(x, y, img.getWidth(), img.getHeight());
    }

    public void setPosition(Rectangle p) {
        pos = p;
    }

    public void draw(SpriteBatch b) {
        b.draw(img, pos.x, pos.y);
    }
}
