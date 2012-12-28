package com.baptr.darkshaft.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.gfx.Tower;
import com.baptr.darkshaft.gfx.Tower.TowerType;
import com.baptr.darkshaft.screen.GameScreen;
import com.baptr.darkshaft.util.MapUtils;

public class GameInputProcessor extends AbstractInputProcessor {

    boolean dragged;
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
        touch.set(x, y, 0);
        camera.unproject(touch);
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        dragged = true;
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if(!dragged) {
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
        }
        return false;
    }
}
