package com.baptr.darkshaft.util;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;

public class MapUtils {
    private static TileMapRenderer renderer;

    private static int unitsPerTileX, unitsPerTileY;
    private static int baseX, baseY;

    public static void setRenderer(TileMapRenderer r) {
        MapUtils.renderer = r;
        assert "isometric".equals(r.getMap().orientation);
        MapUtils.unitsPerTileX = r.getMap().tileWidth / 2;
        MapUtils.unitsPerTileY = r.getMap().tileHeight / 2;
        MapUtils.baseX = r.getMapWidthUnits() / 2;
        MapUtils.baseY = r.getMapHeightUnits() / 2;
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
}
