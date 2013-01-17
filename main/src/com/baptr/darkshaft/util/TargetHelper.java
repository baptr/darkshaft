package com.baptr.darkshaft.util;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Array;
import com.baptr.darkshaft.gfx.Mob;

public class TargetHelper {
    private static ArrayList<Mob> mobs = new ArrayList<Mob>();
    
    public static void addTarget(Mob m){
        mobs.add(m);
        m.setName("Mob" + mobs.size());
    }
    
    public static void addTargets(Array<Mob> mobs){
        for(Mob m: mobs){
            addTarget(m);
        }
    }
    
    public static void removeTarget(Mob m){
        mobs.remove(m);
    }
    
    public static ArrayList<Mob> getMobs(){
        return mobs;
    }
}
