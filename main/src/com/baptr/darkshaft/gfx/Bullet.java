package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.math.Vector2;

import com.baptr.darkshaft.screen.GameScreen;
import com.baptr.darkshaft.gfx.Mob;
import com.baptr.darkshaft.gfx.Tower;

public class Bullet extends Entity {
    private Mob target;
    private float speed;
    private float dmg;
    private Vector2 v;

    private static final String GFX = "gamescreen/bullet";

    public Bullet(Mob target, Tower src, float speed, float dmg) {
        super(src.getX(), src.getY() + 10, GFX);
        this.target = target;
        this.speed = speed;
        this.dmg = dmg;
        this.v = new Vector2();
        src.getTowerType().setTint(this);
    }

    @Override
    public boolean update(float delta) {
        v.set(target.getX()-this.getX(), target.getY()-this.getY());
        float distance = v.len();
        v.nor().mul(speed*delta);
        this.setPosition(this.getX() + v.x, this.getY() + v.y);
        if(distance <= 2.0f) {
            target.shoot(dmg);
            return true;
        }
        return false;
    }
}
