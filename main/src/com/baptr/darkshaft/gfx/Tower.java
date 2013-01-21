package com.baptr.darkshaft.gfx;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.util.TargetHelper;

public class Tower extends Defense {

    public enum TowerType {
        //tileId, color(r,g,b[,a]),        range,  dmg, dur, cooldown
        BASIC (0, 1f, 1f, 1f,              80f,    10f, 0f,  1f),
        FIRE  (0, 1f, 0.25f, 0.25f,        40f,    2f,  10f, 5f),
        WATER (0, 0.4f, 0.5f, 1f,          50f,    15f, 1f,  8f),
        SPIRIT(0, 0.25f, 1f, 0.25f, 0.5f,  60f,    10f, 2f,  5f),
        NONE  (0, 0f, 0f, 0f,              0f,     0f,  0f,  0f);

        private int tileId;
        private int maxTargets = 1;
        private float r, g, b, a;
        private float range, damagePerSecond, attackLength, attackCooldown;

        TowerType(int tileOffset, float r, float g, float b, float a,
                float range, float damage, float attackLength, float attackCooldown) {
            this.tileId = MapUtils.findTileSetGid(TILE_SET_NAME)+tileOffset;
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            this.range = range;
            this.damagePerSecond = damage;
            this.attackLength = attackLength;
            this.attackCooldown = attackCooldown;
        }

        TowerType(int tileId, float r, float g, float b,
                float range, float damage, float attackLength, float attackCooldown) {
            this(tileId, r, g, b, 1.0f, range, damage, attackLength, attackCooldown);
        }

        int getTileId() {
            return tileId;
        }

        void setTint(Entity t) {
            t.setColor(r, g, b, a);
        }
        
        void setTint(Tower t, boolean isMarker){
            if(isMarker){
                t.setColor(r, g, b, a/2f);
            } else {
                t.setColor(r, g, b, a);
            }
        }
    }

    TowerType type;
    Array<Mob> targets = new Array<Mob>(false, 8);
    Array<Bullet> bullets = new Array<Bullet>(false, 8);
    float attackDuration;
    float cooldownDuration;
    boolean isCooldown;

    static final String TILE_SET_NAME = "towers";

    public Tower(TowerType t, int x, int y, boolean isMarker) {
        super(t.tileId, x, y);
        this.type = t;
        type.setTint(this, isMarker);
    }

    public Tower(TowerType t, int x, int y) {
        this(t, x, y, false);
    }

    public void setTowerType(TowerType t, boolean isMarker){
        this.type = t;
        type.setTint(this, isMarker);
    }
    
    public TowerType getTowerType(){
        return type;
    }
    
    @Override
    public boolean update(float delta){
        super.update(delta);
        detectCreeps();
        
        // Check if we're in cooldown mode
        if(isCooldown){
            
            cooldownDuration += delta;
            // if enough time passed that we're out of cooldown, use the remaining
            // time for any attacks that might happen
            if(cooldownDuration >= this.type.attackCooldown){
                delta = cooldownDuration - this.type.attackCooldown;
                //Gdx.app.log( Darkshaft.LOG, "Done with cooldown. New delta = " +
                        //delta);
                cooldownDuration = 0;
                isCooldown = false;
            }
        }
        
        if(!isCooldown && this.targets.size > 0){
            shoot(delta);
        }

        for(Bullet b : bullets) {
            if(b.update(delta)) {
                bullets.removeValue(b, true);
            }
        }
        for(Mob m : targets) {
            if(!m.isAlive()) {
                targets.removeValue(m,true);
            }
        }
        return false;
    }
    
    public void detectCreeps() {
        Vector2 mobVec, towerVec;
        float range = this.type.range;
        float distance;
        for (Mob m : TargetHelper.getMobs()) {
                mobVec = new Vector2(m.getX(), m.getY());
                towerVec = new Vector2(this.getX(), this.getY());
                distance = towerVec.dst(mobVec);
                if (distance <= range && this.targets.size < this.type.maxTargets) {
                    Gdx.app.log( Darkshaft.LOG, m.getName() + " was detected");
                    targets.add(m);
                }
                if (distance > range && targets.contains(m, true)) {
                    Gdx.app.log( Darkshaft.LOG, m.getName() +
                            " was removed as a target");
                    targets.removeValue(m, true);
                }
        }
    }
    
    public void shoot(float delta){
        // Check if the delta runs longer than this tower should attack and only deal
        // damage for the remaining amount
        float cooldownDelta = 0;
        float attackDelta = delta;
        if(this.type.attackLength > 0) {
            attackDuration += delta;
            if(attackDuration > this.type.attackLength){
                attackDelta = attackDuration - this.type.attackLength;
                cooldownDelta = delta - attackDelta;
                isCooldown = true;
            }
        } else {
            isCooldown = true;
            cooldownDelta = delta;
        }
        
        float damage = 0;
        if(this.type.attackLength > 0) {
            damage = this.type.damagePerSecond * attackDelta;
        } else {
            damage = this.type.damagePerSecond;
        }
        
        
        for(Mob m : targets) {
            bullets.add(new Bullet(m, this, 100.0f, damage));
        }
        
        if(isCooldown) {
            cooldownDuration += cooldownDelta;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        for(Bullet b : bullets) {
            b.draw(batch);
        }
    }
}
