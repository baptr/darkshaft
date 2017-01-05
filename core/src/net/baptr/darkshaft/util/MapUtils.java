package net.baptr.darkshaft.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import java.util.Iterator;
import net.baptr.darkshaft.Darkshaft;
import net.baptr.darkshaft.core.Entity.*;
import net.baptr.darkshaft.core.Spawner;
import net.baptr.darkshaft.gfx.Defense;
import net.baptr.darkshaft.util.PathPlanner.Node;

public class MapUtils {
  private static IntMap<TileType> tileTypes;
  private static TiledMap map;
  private static Array<Defense> defenses;

  private static int unitsPerTileX, unitsPerTileY;
  private static int mapWidth, mapHeight; // in tiles

  public static void setMap(TiledMap m, Array<Defense> defenses) {
    MapUtils.map = m;
    //assert "isometric".equals(r.getMap().orientation);
    TiledMapTileLayer l = (TiledMapTileLayer) m.getLayers().get(0);
    // TODO(baptr): units should probably stay floats.
    MapUtils.unitsPerTileX = (int) l.getTileWidth() / 2;
    MapUtils.unitsPerTileY = (int) l.getTileHeight() / 2;
    MapUtils.mapWidth = l.getWidth();
    MapUtils.mapHeight = l.getHeight();
    MapUtils.defenses = defenses;
    tileTypes = new IntMap<TileType>();
  }

  /** Return all the defenses on this map */
  public static Array<Defense> getDefenses() {
    return MapUtils.defenses;
  }

  /** Determine if map col,row contains a defense. */
  public static boolean isDefense(int mapCol, int mapRow) {
    // TODO Use WeightMap or something to avoid iterating each time
    for (Defense d : getDefenses()) {
      if (d.getCol() == mapCol && d.getRow() == mapRow) {
        return true;
      }
    }
    return false;
  }

  public static boolean isDefense(Node n) {
    return isDefense(n.col, n.row);
  }

  /** Get the map */
  public static TiledMap getMap() {
    return MapUtils.map;
  }

  /** Calculate the map row index for a given world point */
  public static int getMapRow(float worldX, float worldY) {
    return (int)
        Math.floor(
            mapHeight
                + (((unitsPerTileY - worldY) / unitsPerTileY) - ((worldX) / unitsPerTileX)) / 2f);
  }

  /** Calculate the map column index for a given world point. */
  public static int getMapCol(float worldX, float worldY) {
    return (int)
        Math.floor((((unitsPerTileY - worldY) / unitsPerTileY) + ((worldX) / unitsPerTileX)) / 2f);
  }

  /** Calculate the world x coordinate from a given map row/col pair */
  public static int getWorldX(int mapCol, int mapRow) {
    return (mapCol - mapRow + mapHeight - 1) * unitsPerTileX;
  }

  /** Calculate the world y coordinate from a given map row/col pair */
  public static int getWorldY(int mapCol, int mapRow) {
    return (-mapCol - mapRow + mapHeight - 1) * unitsPerTileY;
  }

  public static TextureRegion getTileRegion(int tileId) {
    return map.getTileSets().getTile(tileId).getTextureRegion();
  }

  public static boolean isInMap(int col, int row) {
    if (col < 0 || col >= mapWidth || row < 0 || row >= mapHeight) return false;
    return true;
  }

  public static TiledMapTile getTile(int layer, int col, int row) {
    TiledMapTileLayer l = (TiledMapTileLayer) map.getLayers().get(layer);
    return l.getCell(col, row).getTile();
  }

  public static boolean isTilePassable(int col, int row) {
    if (col < 0 || col >= mapWidth || row < 0 || row >= mapHeight) return false;
    return isTilePassable(getTile(0, col, row));
  }

  public static boolean isTilePassable(TiledMapTile tile) {
    // TODO memoize
    String passable = tile.getProperties().get("passable", String.class);
    try {
      return 1 == Integer.parseInt(passable);
    } catch (NumberFormatException e) {
    }
    return true;
  }

  public static int getTileWeight(TiledMapTile tile) {
    // TODO memoize
    String weight = tile.getProperties().get("weight", String.class);
    try {
      return Integer.parseInt(weight);
    } catch (NumberFormatException e) {
    }
    return 1;
  }

  public static int findTileSetGid(String tileSetName) {
    // TODO(baptr): Is firstgid a property now?
    for (TiledMapTile t : map.getTileSets().getTileSet(tileSetName)) {
      return t.getId();
    }
    return -1;
  }

  private static int getNumberOfSpawners() {
    int numSpawners = 0;
    // Iterate over all the objects in the map
    for (MapLayer layer : map.getLayers()) {
      for (MapObject object : layer.getObjects()) {
        // If we find a spawn we add its spawn rate and spawn types to our current wave
        if (object.getName().startsWith("Spawn")) {
          numSpawners++;
        }
      }
    }
    return numSpawners;
  }

  public static Spawner[] getSpawners() {
    int numSpawners = getNumberOfSpawners();
    Spawner[] spawners = new Spawner[numSpawners];
    for (int i = 0; i < numSpawners; i++) {
      spawners[i] = new Spawner(i);
    }
    // Iterate over all the objects in the map
    for (MapLayer layer : map.getLayers()) {
      for (MapObject o : layer.getObjects()) {
        RectangleMapObject object = (RectangleMapObject) o;
        Gdx.app.debug(Darkshaft.LOG, "Object: " + object.getName());
        Iterator<String> it = object.getProperties().getKeys();
        while (it.hasNext()) {
          String key = it.next();
          Gdx.app.debug(Darkshaft.LOG, "  " + key + ": " + object.getProperties().get(key));
        }
        // If we find a spawn we add its spawn rate and spawn types to our
        // current wave
        if (object.getName().startsWith("Spawn")) { // XXX "type" property?
          int waveNum = 0;
          float spawnRate = 0;
          // Get this spawner's number
          int spawnNum = Integer.parseInt(object.getName().replace("Spawn", ""));
          Rectangle rec = object.getRectangle();
          spawners[spawnNum]
              .setLocation((int) (rec.x / rec.width), (int) (mapHeight - 1 - rec.y / rec.height));
          // Iterate over all the wave properties.
          MapProperties props = object.getProperties();
          for (waveNum = 0; props.containsKey("Wave" + waveNum + "_SpawnRate"); waveNum++) {
            String rateKey = "Wave" + waveNum + "_SpawnRate";
            String typeKey = "Wave" + waveNum + "_SpawnTypes";
            spawnRate = Float.parseFloat(props.get(rateKey, String.class));
            spawners[spawnNum].addWave(waveNum, spawnRate, props.get(typeKey, String.class));
          }
        } else if (object.getName().startsWith("End")) {
          int endNum = Integer.parseInt(object.getName().replace("End", ""));
          Rectangle rec = object.getRectangle();
          spawners[endNum]
              .setEnd((int) (rec.x / rec.width), (int) (mapHeight - 1 - rec.y / rec.height));
        }
      }
    }

    for (Spawner spawn : spawners) {
      spawn.sortWaves();
    }
    return spawners;
  }

  /**
   * Find the {@link TileType} for a given tile. Will return TileType.BASIC if not specified in the
   * map.
   */
  public static TileType getTileType(TiledMapTile tile) {
    int id = tile.getId();
    TileType ret = tileTypes.get(id);
    if (ret == null) {
      String strType = tile.getProperties().get("TileType", String.class);
      try {
        ret = TileType.valueOf(strType);
      } catch (IllegalArgumentException e) {
        Gdx.app.debug(Darkshaft.LOG, "TileType not recognized for tile #" + id);
        ret = TileType.BASIC;
      } catch (NullPointerException e) {
        Gdx.app.debug(Darkshaft.LOG, "TileType not defined for tile #" + id);
        ret = TileType.BASIC;
      }
      tileTypes.put(id, ret);
    }
    return ret;
  }
}
