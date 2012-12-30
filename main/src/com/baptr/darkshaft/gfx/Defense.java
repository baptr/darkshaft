package com.baptr.darkshaft.gfx;

import com.baptr.darkshaft.util.MapUtils;

/** Defenses are immobile, grid-aligned sprites (walls and towers).
 * */
public class Defense extends Entity {

    private int col;
    private int row;

    /** Construct a defense with the given sprite and map col, row
     */
    public Defense(int tileId, int col, int row) {
        super(MapUtils.getTileRegion(tileId), MapUtils.getWorldX(col, row),
            MapUtils.getWorldY(col, row));
        this.col = col;
        this.row = row;
    }

    public void setMapPosition(int col, int row) {
        this.col = col;
        this.row = row;
        this.setPosition(MapUtils.getWorldX(col, row), MapUtils.getWorldY(col,row));
    }

    public int getCol() {
        return col;
    }
    public int getRow() {
        return row;
    }

}
