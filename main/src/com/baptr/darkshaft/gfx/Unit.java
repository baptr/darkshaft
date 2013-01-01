package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.util.PathPlanner.Node;

public class Unit extends Entity {

    Array<Node> currentPath;
    float speed = 150;
    
    public Unit(TextureRegion region, float x, float y) {
        super(region, x, y);
    }
    
    public void setPath(Array<Node> path){
        currentPath = path;
    }
    
    @Override
    public void update(float delta) {
        if(currentPath != null && currentPath.size > 0 && currentPath.get(0) != null){

            Node n = currentPath.get(0);
            // TODO: Refactor getWorldX/Y to return the center of the tile
            float nodeX = MapUtils.getWorldX(n.col, n.row) + 32;
            float nodeY = MapUtils.getWorldY(n.col, n.row) + 16;
            float spriteX = this.getX();
            float spriteY = this.getY();
            float dx = nodeX - spriteX; 
            float dy = nodeY - spriteY;

            Vector2 v = new Vector2(dx, dy);
            float distance = v.len();
            v.nor().mul(speed*delta);
            
            float moveToX = spriteX + v.x;
            float moveToY = spriteY + v.y;
            this.setPosition(moveToX, moveToY);
            
            if(distance <= 2.0f) {
                currentPath.removeIndex(0);
            }
            //Gdx.app.log( Darkshaft.LOG, "unit update: node(" + n.row + "," + n.col + ")[" + nodeX + "," + nodeY + "] sprite(" + spriteX + "," + spriteY +"), d(" + 
             //       dx + "," + dy + ") v(" + v.x + "," + v.y + ") move (" + moveToX + "," + moveToY + ")");
        }
         
    }
}
