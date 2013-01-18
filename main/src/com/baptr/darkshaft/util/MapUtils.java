package com.baptr.darkshaft.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TileSet;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObjectGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.entity.Entity.*;
import com.baptr.darkshaft.domain.Spawner;
import com.baptr.darkshaft.gfx.Defense;

public class MapUtils {
    private static TileMapRenderer renderer;
    private static IntMap<TileType> tileTypes;
    private static TiledMap map;
    private static Array<Defense> defenses;

    private static int unitsPerTileX, unitsPerTileY;
    private static int mapWidth, mapHeight; // in tiles

    public static void setRenderer(TileMapRenderer r, Array<Defense> defenses) {
        MapUtils.renderer = r;
        assert "isometric".equals(r.getMap().orientation);
        MapUtils.unitsPerTileX = r.getMap().tileWidth / 2;
        MapUtils.unitsPerTileY = r.getMap().tileHeight / 2;
        MapUtils.mapWidth = r.getMap().width;
        MapUtils.mapHeight = r.getMap().height;
        MapUtils.map = r.getMap();
        MapUtils.defenses = defenses;
        tileTypes = new IntMap<TileType>(); 
    }
    /**
     * Return all the defenses on this map
     */
    public static Array<Defense> getDefenses(){
        return MapUtils.defenses;
    }
    
    /**
     * Get the map for this renderer
     */
    public static TiledMap getMap(){
        return MapUtils.map;
    }

    /** Calculate the map row index for a given world point
     */
    public static int getMapRow(float worldX, float worldY) {
        return (int)Math.floor((((unitsPerTileY-worldY) / unitsPerTileY) - ((worldX) / unitsPerTileX)) / 2f);
    }

    /** Calculate the map column index for a given world point.
     */
    public static int getMapCol(float worldX, float worldY) {
        return (int)Math.floor((((unitsPerTileY-worldY) / unitsPerTileY) + ((worldX) / unitsPerTileX)) / 2f);
    }

    /** Calculate the world x coordinate from a given map row/col pair
     */
    public static int getWorldX(int mapCol, int mapRow) {
        return (mapCol - mapRow - 1) * unitsPerTileX;
    }

    /** Calculate the world y coordinate from a given map row/col pair
     */
    public static int getWorldY(int mapCol, int mapRow) {
        return (-mapCol - mapRow - 1) * unitsPerTileY;
    }

    public static TextureRegion getTileRegion(int tileId) {
        return renderer.getAtlas().getRegion(tileId);
    }

    public static boolean isTilePassable(int col, int row) {
        if(col < 0 || col >= mapWidth || row < 0 || row >= mapHeight)
                return false;
        return isTilePassable(map.layers.get(0).tiles[row][col]);
    }

    public static boolean isTilePassable(int tileId) {
        // TODO memoize
        String passable = map.getTileProperty(tileId, "passable");
        try {
            return 1 == Integer.parseInt(passable);
        } catch(NumberFormatException e) {}
        return true;
    }

    public static int getTileWeight(int layer, int col, int row) {
        return getTileWeight(map.layers.get(layer).tiles[row][col]);
    }

    public static int getTileWeight(int col, int row) {
        int weight = 0;
        for(int layer = 0; layer < map.layers.size(); layer++) {
            weight += getTileWeight(layer, col, row);
        }
        return weight;
    }

    public static int getTileWeight(int tileId) {
        // TODO memoize
        String weight = map.getTileProperty(tileId, "weight");
        try {
            return Integer.parseInt(weight);
        } catch(NumberFormatException e) {}
        return 1;
    }

    public static int findTileSetGid(String tileSetName) {
        for(TileSet t : map.tileSets) {
            if(t.name.equals(tileSetName)) {
                return t.firstgid;
            }
        }
        return -1;
    }
    
    private static int getNumberOfSpawners() {
        int numSpawners = 0;
        // Iterate over all the object groups in the map
        for(TiledObjectGroup group : map.objectGroups){
            // Iterate over all the objects on this object layer
            for(TiledObject object : group.objects){
                // If we find a spawn we add its spawn rate and spawn types to our current wave
                if("Spawn".equals(object.type)){
                    numSpawners++;
                }
            }
        }
        return numSpawners;
    }
    
    public static Spawner[] getSpawners(TextureAtlas atlas) {   
        int numSpawners = getNumberOfSpawners();
        Spawner[] spawners = new Spawner[numSpawners];
        for(int i = 0; i < numSpawners; i++){
            spawners[i] = new Spawner(i, atlas);
        }
        // Iterate over all the object groups in the map
        for(TiledObjectGroup group : map.objectGroups){
            // Iterate over all the objects on this object layer
            for(TiledObject object : group.objects){
                // If we find a spawn we add its spawn rate and spawn types to our current wave
                if("Spawn".equals(object.type)){
                    int waveNum = 0;
                    float spawnRate = 0;
                    // Get this spawner's number
                    int spawnNum = Integer.parseInt(object.name.replace("Spawn", ""));
                    spawners[spawnNum].setLocation(object.x/object.width,
                            object.y/object.height);
                    // Iterate over all the properties for this object
                    for(String key : object.properties.keySet()){
                        String[] property = key.split("_");
                        property[0] = property[0].replace("Wave", "");
                        waveNum = Integer.parseInt(property[0]);
                        // Get this spawner's rate of spawn in seconds
                        if(("Wave" + waveNum + "_SpawnRate").equals(key)){
                            spawnRate = Float.parseFloat(object.properties.get(key));
                        }
                        
                        // Get this spawner's spawning types
                        if(("Wave" + waveNum + "_SpawnTypes").equals(key)){
                            spawners[spawnNum].addWave(waveNum, spawnRate, object.properties.get(key));
                        }
                    }
                } else if ("End".equals(object.type)){
                    int endNum = Integer.parseInt(object.name.replace("End", ""));
                    spawners[endNum].setEnd(object.x/object.width,
                            object.y/object.height);
                }
                
            }
        }
        
        for(Spawner spawn : spawners){
            spawn.sortWaves();
        }
        return spawners;
    }

    /** Find the {@link TileType} for a given tile ID. Will return
     * TileType.BASIC if not specified in the map. */
    public static TileType getTileType(int id) {
        TileType ret = tileTypes.get(id);
        if(ret == null) {
            String strType = map.getTileProperty(id, "TileType");
            try {
                ret = TileType.valueOf(strType);
            } catch(IllegalArgumentException e) {
                Gdx.app.debug(Darkshaft.LOG,
                        "TileType not recognized for tile #" + id);
                ret = TileType.BASIC;
            } catch(NullPointerException e) {
                Gdx.app.debug(Darkshaft.LOG,
                        "TileType not defined for tile #" + id);
                ret = TileType.BASIC;
            }
            tileTypes.put(id, ret);
        }
        return ret;
    }
}
