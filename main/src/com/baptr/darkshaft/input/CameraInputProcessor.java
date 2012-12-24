package com.baptr.darkshaft.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.baptr.darkshaft.Darkshaft;

public class CameraInputProcessor extends AbstractInputProcessor {

    int dragX;
    int dragY;
    OrthographicCamera camera;
    
    public CameraInputProcessor(OrthographicCamera camera) {
        super();
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keyCode) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean keyUp(int keyCode) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean scrolled(int direction) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        dragX = x;
        dragY = y;
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {

        Gdx.app.log( Darkshaft.LOG, "touchDragged: " + getName() + " to: (" + x + ", " + y + "), pointer: " + pointer);
        this.camera.translate(dragX-x, y-dragY);
        camera.update();
        dragX = x;
        dragY = y;
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        // TODO Auto-generated method stub
        return true;
    }

}
