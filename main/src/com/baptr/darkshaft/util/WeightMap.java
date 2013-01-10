package com.baptr.darkshaft.util;

import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLayer;
import com.badlogic.gdx.utils.Array;

import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.gfx.Defense;

/** Cache of pre-calculated tile weights. Accounts for {@link Defense} and
 *  {@link TerrainAffinity}.
 *  @author baptr
 */
public class WeightMap {
    public static final int IMPASSABLE = Integer.MAX_VALUE;

    private int[][] weights;
    private int width;
    private int height;

    private TiledMap map;

    public WeightMap(TiledMap terrain) {
        this(terrain, null);
    }

    public WeightMap(TiledMap terrain, Array<Defense> defenses) {
        weights = new int[terrain.height][terrain.width];
        height = terrain.height;
        width = terrain.width;
        map = terrain;
        bakeMap();
        if(defenses != null)
            addDefenses(defenses);
    }

    private void bakeMap() {
        // Loop through each layer of the terrain, accumulating each tile weight
        
        // TODO create unique combinations of passability matricies for quick
        //      mob affinity differentiation
        for(TiledLayer layer : map.layers) {
            int[][] tiles = layer.tiles;

            for(int j = 0; j < height; j++) {
                for(int i = 0; i < width; i++) {
                    if(MapUtils.isTilePassable(tiles[j][i]) &&
                            isPassable(i, j)) {
                        weights[j][i] +=
                                MapUtils.getTileWeight(tiles[j][i]);
                    } else {
                        weights[j][i] = IMPASSABLE;
                    }
                }
            }
        }
    }

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

    public boolean isPassable(int col, int row) {
        if(col < 0 || col >= width || row < 0 || row >= width)
            return false;
        return weights[row][col] != IMPASSABLE;
    }

    public int get(int col, int row) {
        if(col < 0 || col >= width || row < 0 || row >= width)
            return IMPASSABLE;
        return weights[row][col];
    }

    public void addDefense(Defense d) {
        int row = d.getRow();
        int col = d.getCol();
        if(isPassable(col, row))
            weights[row][col] += PathPlanner.TOWER_COST;
    }

    public void addDefenses(Array<Defense> defenses) {
        for(Defense d : defenses) {
            addDefense(d);
        }
    }
}
