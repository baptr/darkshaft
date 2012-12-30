package com.baptr.darkshaft.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.gfx.Tower;
import com.baptr.darkshaft.gfx.Avatar;
import com.baptr.darkshaft.gfx.Tower.TowerType;
import com.baptr.darkshaft.screen.GameScreen;
import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.util.PathPlanner;

public class GameInputProcessor extends AbstractInputProcessor {

    boolean dragged;
    boolean moving;
    Vector3 touch;
    TowerType placeType = TowerType.BASIC;
    GameScreen screen;
    OrthographicCamera camera;

    public GameInputProcessor(OrthographicCamera camera, GameScreen screen) {
        super();
        this.camera = camera;
        this.screen = screen;
        this.touch = new Vector3();
    }

    @Override
    public boolean keyTyped(char character) {
        switch(character) {
            case '1':
                placeType = TowerType.BASIC; break;
            case '2':
                placeType = TowerType.FIRE; break;
            case '3':
                placeType = TowerType.WATER; break;
            case '4':
                placeType = TowerType.SPIRIT; break;
        }
        
        return true;
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
            int mapRow = MapUtils.getMapRow(touch.x, touch.y);
            int mapCol = MapUtils.getMapCol(touch.x, touch.y);
            Gdx.app.log( Darkshaft.LOG, "click: screen (" + x + ", " + y +
                    ") world (" + touch.x + ", " + touch.y + ") map (" +
                    mapCol + ", " + mapRow + ")" );
            if(mapRow >= 0 && mapCol >= 0) {
                screen.addDefense(new Tower(placeType, mapCol, mapRow));
            }

            return true;
        } else if(button == 1) {
            moving = false;
            if(!dragged) {
                Avatar p = screen.getPlayer();
                PathPlanner pp = screen.getPathPlanner();
                int sCol = MapUtils.getMapCol(p.getX(), p.getY());
                int sRow = MapUtils.getMapRow(p.getX(), p.getY());
                int gCol = MapUtils.getMapCol(touch.x, touch.y);
                int gRow = MapUtils.getMapRow(touch.x, touch.y);
                Gdx.app.log(Darkshaft.LOG, "Path: " +
                        pp.findPath(sCol, sRow, gCol, gRow));
                return true;
            }
        }
        return false;
    }
}
