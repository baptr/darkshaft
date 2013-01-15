package com.baptr.darkshaft.gfx;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.util.PathPlanner;
import com.baptr.darkshaft.entity.Entity.*;

public class Mob extends Unit {

    TextureAtlas atlas;
    MobType type;
    
    public enum MobType {
        RED("Characters/Mobs/Mob1", 100, UnitType.BASIC),
        GREEN("Characters/Mobs/Mob2", 150, UnitType.WATER),
        DRAGON("Characters/Mobs/Dragon", 60, UnitType.FLYER),
        NONE(null, 0, UnitType.BASIC);

        private float speed;
        String regionName;
        UnitType unitType;

        MobType(String name, float speed, UnitType unitType) {
            this.regionName = name;
            this.speed = speed;
            this.unitType = unitType;
        }
    }
    
    public Mob(TextureRegion region, float x, float y) {
        super(region, x, y);
    }
    
    public Mob(TextureAtlas atlas, MobType mobType, float x, float y){
        super(atlas.findRegion(mobType.regionName), x, y);
        this.atlas= atlas;
        type = mobType;       
        // Mobs will have random speeds because they clump up when they spawn... eventually they will try and not clump up and we can remove this
        Random r = new Random();
        this.speed = mobType.speed * r.nextFloat();
        this.unitType = type.unitType;
        xOffset = -32;
        yOffset = -16;
    }
}
