package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Entity extends Sprite implements Comparable<Entity>{

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

    @Override
    public int compareTo(Entity o) {
        if(this.getY() < o.getY()){
            return 1;
        }
        
        if(this.getY() > o.getY()){
            return -1;
        }
        
        if(this.getX() < o.getX()){
            return 1;
        }
        return 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(getX());
        result = prime * result + Float.floatToIntBits(getY());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Entity other = (Entity) obj;
        if (Float.floatToIntBits(getX()) != Float
                .floatToIntBits(other.getX()))
            return false;
        if (Float.floatToIntBits(getY()) != Float
                .floatToIntBits(other.getY()))
            return false;
        return true;
    }
    
    public void update(float delta)
    {
        
    }
    
    
}
