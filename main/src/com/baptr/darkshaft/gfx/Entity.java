package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Entity extends Sprite {

    protected float xOffset = 0;
    protected float yOffset = 0;

    public Entity(TextureRegion region, float x, float y) {
        super(region);
        this.setPosition(x, y);
    }

    public Entity(Texture texture, float x, float y) {
        super(texture);
        this.setPosition(x, y);
        
    }

    public Entity(String imgPath, float x, float y) {
        this(new Texture(Gdx.files.internal(imgPath)), x, y);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x + xOffset, y + yOffset);
    }
}
