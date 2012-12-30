package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Array;

import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.util.PathPlanner.Node;

public class PathMarker extends Sprite {

    private FloatArray worldX;
    private FloatArray worldY;

    private int pathLength;

    private static final int INITIAL_PATH_CAPACITY = 32;

    public PathMarker(TextureRegion gfx) {
        super(gfx);
        worldX = new FloatArray(true, INITIAL_PATH_CAPACITY);
        worldY = new FloatArray(true, INITIAL_PATH_CAPACITY);
        pathLength = 0;
    }

    public void setPath(Array<Node> path) {
        if(path == null) {
            pathLength = 0;
            return;
        }
        pathLength = path.size;
        worldX.clear();
        worldY.clear();
        worldX.ensureCapacity(pathLength);
        worldY.ensureCapacity(pathLength);
        for(Node n : path) {
            worldX.add(MapUtils.getWorldX(n.col, n.row));
            worldY.add(MapUtils.getWorldY(n.col, n.row));
        }
    }

    public void draw(SpriteBatch batch) {
        for(int i = 0 ; i < pathLength; i++) {
            setPosition(worldX.get(i), worldY.get(i));
            super.draw(batch);
        }
    }
}
