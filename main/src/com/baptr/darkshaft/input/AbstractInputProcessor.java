package com.baptr.darkshaft.input;

import com.badlogic.gdx.InputProcessor;

public abstract class AbstractInputProcessor implements InputProcessor {

    public AbstractInputProcessor() {
        // TODO Auto-generated constructor stub
    }

    protected String getName() {
        return getClass().getSimpleName();
    }

    public boolean keyDown(int keyCode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean keyUp(int keyCode) {
        return false;
    }
    
    public boolean mouseMoved(int x, int y) {
        return false;
    }

    public boolean scrolled(int direction) {
        return false;
    }
    
    public boolean touchDown(int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int x, int y, int pointer) {
        return false;
    }

    public boolean touchUp(int x, int y, int pointer, int button) {
        return false;
    }

}
