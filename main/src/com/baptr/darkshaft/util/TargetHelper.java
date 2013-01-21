package com.baptr.darkshaft.util;

import java.util.Iterator;

import com.badlogic.gdx.utils.Array;

import com.baptr.darkshaft.gfx.Mob;

public class TargetHelper {
    private static Array<Mob> mobs = new Array<Mob>(false, 32);
    private static boolean updating = false;
    private static Array<Mob> toRemove = new Array<Mob>(false, 8);
    
    public static void addTarget(Mob m){
        mobs.add(m);
        m.setName("Mob" + mobs.size);
    }
    
    public static void addTargets(Array<Mob> mobs){
        for(Mob m: mobs){
            addTarget(m);
        }
    }
    
    public static void removeTarget(Mob m){
        if(updating) {
            toRemove.add(m);
        } else {
            mobs.removeValue(m, true);
        }
    }
    
    public static Array<Mob> getMobs(){
        return mobs;
    }

    public static int getNumMobs() {
        return mobs.size;
    }

    public static void updateMobs(float delta) {
        updating = true;
        for(Mob m : mobs) {
            m.update(delta);
        }
        for(Iterator<Mob> iter = toRemove.iterator(); iter.hasNext();) {
            mobs.removeValue(iter.next(), true);
            iter.remove();
        }
        updating = false;
    }
}
