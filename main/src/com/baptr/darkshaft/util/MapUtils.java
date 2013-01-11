package com.baptr.darkshaft.util;

import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TileSet;

import com.baptr.darkshaft.entity.Entity.*;

public class MapUtils {
    private static TileMapRenderer renderer;
    private static Map<Integer, TileType> tileTypes;

    private static int unitsPerTileX, unitsPerTileY;
    private static int baseX, baseY;
    private static int mapWidth, mapHeight; // in tiles

    public static void setRenderer(TileMapRenderer r) {
        MapUtils.renderer = r;
        assert "isometric".equals(r.getMap().orientation);
        MapUtils.unitsPerTileX = r.getMap().tileWidth / 2;
        MapUtils.unitsPerTileY = r.getMap().tileHeight / 2;
        MapUtils.baseX = r.getMapWidthUnits() / 2;
        MapUtils.baseY = r.getMapHeightUnits() / 2;
        MapUtils.mapWidth = r.getMap().width;
        MapUtils.mapHeight = r.getMap().height;
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
        return isTilePassable(renderer.getMap().layers.get(0).tiles[row][col]);
    }

    public static boolean isTilePassable(int tileId) {
        // TODO memoize
        String passable = renderer.getMap().getTileProperty(tileId, "passable");
        try {
            return 1 == Integer.parseInt(passable);
        } catch(NumberFormatException e) {}
        return true;
    }

    public static int getTileWeight(int layer, int col, int row) {
        return getTileWeight(renderer.getMap().layers.get(layer).tiles[row][col]);
    }

    public static int getTileWeight(int col, int row) {
        int weight = 0;
        for(int layer = 0; layer < renderer.getMap().layers.size(); layer++) {
            weight += getTileWeight(layer, col, row);
        }
        return weight;
    }

    public static int getTileWeight(int tileId) {
        // TODO memoize
        String weight = renderer.getMap().getTileProperty(tileId, "weight");
        try {
            return Integer.parseInt(weight);
        } catch(NumberFormatException e) {}
        return 1;
    }

    public static int findTileSetGid(String tileSetName) {
        for(TileSet t : renderer.getMap().tileSets) {
            if(t.name.equals(tileSetName)) {
                return t.firstgid;
            }
        }
        return -1;
    }
}
