package com.baptr.darkshaft.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.util.MapUtils;

public class CameraInputProcessor extends AbstractInputProcessor {

    int dragX;
    int dragY;
    final float MIN_ZOOM = .4f;
    final float MAX_ZOOM = 3f;

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
        // Stub
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
    //  1 == Scroll Down
    // -1 == Scroll Up
    public boolean scrolled(int direction) {
        //Gdx.app.log( Darkshaft.LOG, "scrolled: " + getName() + " direction: " + direction);
        // Zoom out
        if(direction > 0 && camera.zoom < MAX_ZOOM)
            camera.zoom += .1f;
        // Zoom in
        if(direction < 0 && camera.zoom > MIN_ZOOM)
            camera.zoom -= .1f;
        camera.update();
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

        //Gdx.app.log( Darkshaft.LOG, "touchDragged: " + getName() + " to: (" + x + ", " + y + "), pointer: " + pointer);
        this.camera.translate(dragX-x, y-dragY);
        camera.update();
        dragX = x;
        dragY = y;
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        // stub
        return true;
    }

}
