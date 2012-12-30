package com.baptr.darkshaft.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.gfx.Tower;
import com.baptr.darkshaft.gfx.Avatar;
import com.baptr.darkshaft.gfx.Tower.TowerType;
import com.baptr.darkshaft.screen.GameScreen;
import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.util.PathPlanner;
import com.baptr.darkshaft.util.PathPlanner.Node;

public class GameInputProcessor extends AbstractInputProcessor {

    boolean dragged;
    boolean moving;
    Vector3 touch;
    Vector3 move;
    TowerType placeType = TowerType.NONE;
    GameScreen screen;
    OrthographicCamera camera;

    public GameInputProcessor(OrthographicCamera camera, GameScreen screen) {
        super();
        this.camera = camera;
        this.screen = screen;
        this.touch = new Vector3();
        this.move = new Vector3();
    }

    @Override
    public boolean keyTyped(char character) {
        switch(character) {
            case '1':
                placeType = TowerType.BASIC;
                screen.setTowerMarker(TowerType.BASIC);
                break;
            case '2':
                placeType = TowerType.FIRE; 
                screen.setTowerMarker(TowerType.FIRE);
                break;
            case '3':
                placeType = TowerType.WATER; 
                screen.setTowerMarker(TowerType.WATER);
                break;
            case '4':
                placeType = TowerType.SPIRIT; 
                screen.setTowerMarker(TowerType.SPIRIT);
                break;
        }
        
        return true;
    }
    
    @Override
    public boolean keyDown(int keyCode)
    {
        Gdx.app.log("Input Test", "key down: " + keyCode);
        // Esc
        if(keyCode == 131){
            screen.setTowerMarker(TowerType.NONE);
            placeType = TowerType.NONE;
        }
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        dragged = false;
        if(button == 1) {
            moving = true;
        }
        touch.set(x, y, 0);
        camera.unproject(touch);
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        dragged = true;
        if(moving) {
            touch.set(x, y, 0);
            camera.unproject(touch);
            screen.movePlayer(touch.x, touch.y);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if(!dragged && button == 0) {
            // clicked
            if(placeType != TowerType.NONE) { // Place a tower
                int mapRow = MapUtils.getMapRow(touch.x, touch.y);
                int mapCol = MapUtils.getMapCol(touch.x, touch.y);
                Gdx.app.log( Darkshaft.LOG, "click: screen (" + x + ", " + y +
                        ") world (" + touch.x + ", " + touch.y + ") map (" +
                        mapCol + ", " + mapRow + ")" );
                if(mapRow >= 0 && mapCol >= 0) {
                    screen.addDefense(new Tower(placeType, mapCol, mapRow));
                }
            } else {
                Avatar p = screen.getPlayer();
                PathPlanner pp = screen.getPathPlanner();
                int sCol = MapUtils.getMapCol(p.getX(), p.getY());
                int sRow = MapUtils.getMapRow(p.getX(), p.getY());
                int gCol = MapUtils.getMapCol(touch.x, touch.y);
                int gRow = MapUtils.getMapRow(touch.x, touch.y);
                Array<Node> path = pp.findPath(sCol, sRow, gCol, gRow);
                Gdx.app.log(Darkshaft.LOG, "Path: " + path
                        + " after " + pp.getIterations() + " steps");
                screen.getPathMarker().setPath(path);
            }

            return true;
        } else if(button == 1) {
            moving = false;
        }
        return false;
    }
    
    @Override
    public boolean mouseMoved(int x, int y) {
        move.set(x, y, 0);
        camera.unproject(move);
        int mapRow = MapUtils.getMapRow(move.x, move.y);
        int mapCol = MapUtils.getMapCol(move.x, move.y);
        //Gdx.app.log( Darkshaft.LOG, "move: screen (" + x + ", " + y + ") world (" + touch.x + ", " + touch.y + ") map (" + mapCol + ", " + mapRow + ")" );
        if(mapRow >= 0 && mapCol >= 0){
            screen.setTowerMarker(new Tower(placeType, mapCol, mapRow, true));
        }
        return false;
    }
}
