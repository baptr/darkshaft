package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Entity extends Sprite {

    public Entity(String imgPath, float x, float y) {
        super(new Texture(Gdx.files.internal(imgPath)));
        this.setPosition(x, y);
    }
}
