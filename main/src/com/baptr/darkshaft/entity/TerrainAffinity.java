package com.baptr.darkshaft.entity;

import com.baptr.darkshaft.entity.Entity.UnitType;
import com.baptr.darkshaft.entity.Entity.TileType;

import java.util.EnumMap;

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

    public static void load(String file) {
        // TODO config file
        TerrainAffinity basic = new TerrainAffinity();
        affinityMap.put(UnitType.BASIC, basic);

        TerrainAffinity player = new TerrainAffinity();
        player.put(TileType.TOWER, 0.0f);
        affinityMap.put(UnitType.PLAYER, player);

        TerrainAffinity water = new TerrainAffinity();
        water.put(TileType.WATER, 0.0f);
        affinityMap.put(UnitType.WATER, water);

        TerrainAffinity flyer = new TerrainAffinity();
        flyer.put(TileType.WATER, 0.0f);
        flyer.put(TileType.MOUNTAIN, 0.5f);
        flyer.put(TileType.FOREST, 0.25f);
        flyer.put(TileType.TOWER, 0.0f);
        affinityMap.put(UnitType.FLYER, flyer);
    }

    public static TerrainAffinity forUnitType(UnitType type) {
        return affinityMap.get(type);
    }
}
