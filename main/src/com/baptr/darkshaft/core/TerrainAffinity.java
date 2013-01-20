package com.baptr.darkshaft.core;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.GdxRuntimeException;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.core.Entity.*;


/** Represents a unit's speed modifier over a given type of terrain.
 * Each unit type may have modifiers that multiply the weight of a matching
 * tile type (terrain widget or tower). A multiplier of 0 is expected to 
 * set the weight to 1.
 * @author baptr
 */
public class TerrainAffinity extends EnumMap<TileType,Float>{
    public static EnumMap<UnitType,TerrainAffinity> affinityMap =
            new EnumMap<UnitType,TerrainAffinity>(UnitType.class);

    private TerrainAffinity() {
        super(TileType.class);
        // Default to 1x mapping
        for(TileType t : TileType.values()) {
            this.put(t, 1.0f);
        }
    }

    public int getWeight(TileType type, int baseWeight) {
        int weight = (int)(baseWeight * get(type));
        if(weight == 0) {
            return 1;
        } else {
            return weight;
        }
    }

    public static void load(String file) {
        JsonReader reader = new JsonReader();
        Object o = reader.parse(Gdx.files.internal(file));
        try {
            @SuppressWarnings({ "unchecked", "rawtypes" }) // Crash if malformed
            ObjectMap<String,ObjectMap> cfg = (ObjectMap<String,ObjectMap>)o;
            for(String unitName : cfg.keys()) {
                TerrainAffinity affinity = new TerrainAffinity();
                try {
                    UnitType unitType = UnitType.valueOf(unitName);
                    @SuppressWarnings("unchecked") // Crash if malformed
                    ObjectMap<String,Float> typeCfg = cfg.get(unitName);
                    for(String tileName : typeCfg.keys()) {
                        try {
                            TileType tileType = TileType.valueOf(tileName);
                            float multiplier = typeCfg.get(tileName);
                            affinity.put(tileType, multiplier);
                        } catch(IllegalArgumentException e) {
                            Gdx.app.error(Darkshaft.LOG,
                                    "Unrecognized TileType " +
                                    "in TerrainAffinity config file " + file +
                                    ": " + tileName);
                        }
                    }
                    affinityMap.put(unitType, affinity);
                } catch(IllegalArgumentException e) {
                    Gdx.app.error(Darkshaft.LOG, "Unrecognized UnitType in " +
                            "TerrainAffinity config file " + file + ": " +
                            unitName);
                }
            }
        } catch(ClassCastException e) {
            throw new GdxRuntimeException(
                    "Malformed TerrainAffinity config in " + file);
        }
    }

    public static TerrainAffinity forUnitType(UnitType type) {
        return affinityMap.get(type);
    }
}
