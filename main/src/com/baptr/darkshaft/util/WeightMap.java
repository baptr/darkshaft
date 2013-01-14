package com.baptr.darkshaft.util;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLayer;
import com.badlogic.gdx.utils.Array;

import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.entity.TerrainAffinity;
import com.baptr.darkshaft.gfx.Defense;
import com.baptr.darkshaft.entity.Entity.*;

/** Cache of pre-calculated tile weights. Accounts for {@link Defense} and
 *  {@link TerrainAffinity}.
 *  @author baptr
 */
public class WeightMap {
    public static final int IMPASSABLE = Integer.MAX_VALUE;

    // Map Affinity -> WeightMap? - for now, invert if memory becomes an issue
    //      TileType -> WeightMap?
    // Refactor map to idx a TileStack with a weight matrix?
    private HashMap<TerrainAffinity, int[][]> weights;
    private int width;
    private int height;

    private TiledMap map;

    public WeightMap(TiledMap terrain) {
        this(terrain, null);
    }

    public WeightMap(TiledMap terrain, Array<Defense> defenses) {
        height = terrain.height;
        width = terrain.width;
        map = terrain;
        TerrainAffinity.load("terrain_affinity.cfg"); // TODO config file
        weights = new HashMap<TerrainAffinity, int[][]>();
        for(UnitType unitType : UnitType.values()) {
            TerrainAffinity affinity = TerrainAffinity.forUnitType(unitType);
            weights.put(affinity, new int[height][width]);
            bakeMap(affinity);
        }
        //weights = new int[terrain.height][terrain.width];
        if(defenses != null)
            addDefenses(defenses);
    }

    // TODO create unique combinations of passability matricies for quick
    //      mob affinity differentiation
    private void bakeMap(TerrainAffinity type) {
        int[][] weight = weights.get(type);
        
        // Loop through each layer of the terrain, accumulating each tile weight
        for(TiledLayer layer : map.layers) {
            int[][] tiles = layer.tiles;

            for(int j = 0; j < height; j++) {
                for(int i = 0; i < width; i++) {
                    if(weight[j][i] != IMPASSABLE) {
                        TileType tileType = MapUtils.getTileType(tiles[j][i]);
                        float mod = type.get(tileType);
                        if(MapUtils.isTilePassable(tiles[j][i])) {
                            int tmpWeight = MapUtils.getTileWeight(tiles[j][i]);
                            if(mod == 0.0f && tmpWeight > 0) {
                                weight[j][i] += 1;
                            } else {
                                weight[j][i] += tmpWeight * mod;
                            }
                        } else if(mod == 0.0f) {
                            if(weight[j][i] == 0) weight[j][i] = 1;
                        } else {
                            weight[j][i] = IMPASSABLE;
                        }
                    }
                }
            }
        }
    }

    /*
    public void set(int col, int row, int weight) {
        if(col < 0 || col >= width || row < 0 || row >= width)
            return;
        weights[row][col] = weight;
    }

    public void update(int col, int row, int modifier) {
        if(col < 0 || col >= width || row < 0 || row >= width)
            return;
        weights[row][col] += modifier;
    }
    */

    public boolean isPassable(TerrainAffinity type, int col, int row) {
        return get(type, col, row) != IMPASSABLE;
    }

    public boolean isPassable(UnitType type, int col, int row) {
        return get(type, col, row) != IMPASSABLE;
    }

    public int get(UnitType type, int col, int row) {
        return get(TerrainAffinity.forUnitType(type), col, row);
    }

    public int get(TerrainAffinity type, int col, int row) {
        if(col < 0 || col >= width || row < 0 || row >= width)
            return IMPASSABLE;
        return (weights.get(type))[row][col];
    }

    public void addDefense(Defense d) {
        int row = d.getRow();
        int col = d.getCol();
        for(TerrainAffinity t : weights.keySet()) {
            if(isPassable(t, col, row))
                (weights.get(t))[row][col] +=
                        PathPlanner.TOWER_COST * t.get(TileType.TOWER);
        }
    }

    public void addDefenses(Array<Defense> defenses) {
        for(Defense d : defenses) {
            addDefense(d);
        }
    }
}
