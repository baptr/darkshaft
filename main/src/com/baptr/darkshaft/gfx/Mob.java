package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.util.PathPlanner;

public class Mob extends Unit {

    TextureAtlas atlas;
    MobType type;
    
    public enum MobType {
        RED("Characters/Mobs/Mob1", 100),
        GREEN("Characters/Mobs/Mob2", 150),
        DRAGON("gamescreen/dargorn", 60),
        NONE(null, 0);

        private float speed;
        String regionName;

        MobType(String name, float speed) {
            this.regionName = name;
            this.speed = speed;
        }
    }
    
    public Mob(TextureRegion region, float x, float y) {
        super(region, x, y);
    }
    
    public Mob(TextureAtlas atlas, MobType mobType, float x, float y){
        super(atlas.findRegion(mobType.regionName), x, y);
        this.atlas= atlas;
        type = mobType;       
        this.speed = mobType.speed;
    }
}
