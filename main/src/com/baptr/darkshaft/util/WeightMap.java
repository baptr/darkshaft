package com.baptr.darkshaft.util;

import com.badlogic.gdx.graphics.g2d.tiled.TiledLayer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.baptr.darkshaft.core.Entity.*;
import com.baptr.darkshaft.core.TerrainAffinity;
import com.baptr.darkshaft.gfx.Defense;
import java.util.HashMap;

/**
 * Cache of pre-calculated tile weights. Accounts for {@link Defense} and {@link TerrainAffinity}.
 *
 * @author baptr
 */
public class WeightMap {
  public static final int IMPASSABLE = Integer.MAX_VALUE;

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
    TerrainAffinity.load("affinity.cfg");
    weights = new HashMap<TerrainAffinity, int[][]>();
    for (UnitType unitType : UnitType.values()) {
      TerrainAffinity affinity = TerrainAffinity.forUnitType(unitType);
      weights.put(affinity, new int[height][width]);
      bakeMap(affinity);
    }
    if (defenses != null) addDefenses(defenses);
  }

  private void bakeMap(TerrainAffinity affinity) {
    int[][] weight = weights.get(affinity);

    // Loop through each layer of the terrain, accumulating each tile weight
    for (TiledLayer layer : map.layers) {
      int[][] tiles = layer.tiles;

      for (int j = 0; j < height; j++) {
        for (int i = 0; i < width; i++) {
          if (weight[j][i] != IMPASSABLE) {
            int tileId = tiles[j][i];
            TileType tileType = MapUtils.getTileType(tileId);
            if (MapUtils.isTilePassable(tileId) || affinity.get(tileType) < 1f) {
              int baseWeight = MapUtils.getTileWeight(tileId);
              weight[j][i] += affinity.getWeight(tileType, baseWeight);
            } else {
              weight[j][i] = IMPASSABLE;
            }
          }
        }
      }
    }
  }

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
    if (col < 0 || col >= width || row < 0 || row >= width) return IMPASSABLE;
    return (weights.get(type))[row][col];
  }

  public void addDefense(Defense d) {
    int row = d.getRow();
    int col = d.getCol();
    for (TerrainAffinity t : weights.keySet()) {
      if (isPassable(t, col, row))
        (weights.get(t))[row][col] += PathPlanner.TOWER_COST * t.get(TileType.TOWER);
    }
  }

  public void addDefenses(Array<Defense> defenses) {
    for (Defense d : defenses) {
      addDefense(d);
    }
  }

  // XXX Do we need to store defenses so recalculate can automatically know?
  /*
  public void removeDefense(Defense d) {
      int row = d.getRow();
      int col = d.getCol();
      recalculate(row, col, 0);
  }
  */
}
