package com.baptr.darkshaft.gfx;

import java.util.Map;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class Entity extends Sprite implements Comparable<Entity>{

    protected float xOffset = 0;
    protected float yOffset = 0;

    protected Map<String,Animation> animations; // TODO EnumMap would be nice
    protected Animation currentAnimation;
    protected float animStateTime = 0f;
    protected float frameDuration = 0.175f; // TODO way to override

    public Entity(float x, float y, TextureAtlas atlas, String ... regionNames) {
        animations = new HashMap<String,Animation>();
        for(String region : regionNames) {
            Array<AtlasRegion> tr = atlas.findRegions(region);
            Animation an = new Animation(frameDuration, tr);
            an.setPlayMode(Animation.LOOP_PINGPONG);
            if(currentAnimation == null) currentAnimation = an;
            animations.put(region, an);
        }
        if(currentAnimation != null) {
            TextureRegion firstFrame = currentAnimation.getKeyFrame(0f);
            this.setRegion(firstFrame);
            this.setSize(getRegionWidth(), getRegionHeight());
        }
        this.setPosition(x, y);
        this.setColor(1,1,1,1);
    }

    public Entity(TextureRegion region, float x, float y) {
        super(region);
        this.setPosition(x, y);
    }

    public Entity(Texture texture, float x, float y) {
        super(texture);
        this.setPosition(x, y);
    }

    
    public void setAnimation(String animPath) { //TODO Enum
        currentAnimation = animations.get(animPath);
        animStateTime = 0f;
    }

    public void update(float delta) {
        if(currentAnimation != null) {
            animStateTime += delta;
            TextureRegion frame = currentAnimation.getKeyFrame(animStateTime);
            this.setRegion(frame);
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x + xOffset, y + yOffset);
    }

    @Override
    public float getX() {
        return super.getX() - xOffset;
    }

    @Override
    public float getY() {
        return super.getY() - yOffset;
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

}
