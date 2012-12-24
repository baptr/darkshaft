package com.baptr.darkshaft.input;

import com.badlogic.gdx.InputProcessor;

public abstract class AbstractInputProcessor implements InputProcessor{

    public AbstractInputProcessor() {
        // TODO Auto-generated constructor stub
    }

    protected String getName()
    {
        return getClass().getSimpleName();
    }
}
