package com.baptr.darkshaft;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.baptr.darkshaft.screen.*;

public class Darkshaft extends Game {

    public static final String LOG = Darkshaft.class.getSimpleName();

    FPSLogger fpsLogger;
    private static final boolean CHECK_FPS = false;

    private boolean showSplash = true;

    public AssetManager manager;
    
    public Darkshaft() {
        this(true);
    }

    public Darkshaft(boolean showSplash) {
        super();
        this.showSplash = showSplash;
    }

    @Override
    public void create() {
    	fpsLogger =  new FPSLogger();
        manager = new AssetManager();
        //Terrain.init(this, manager); 
        
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Gdx.app.log(Darkshaft.LOG, "Version 0.1 starting");
        if(CHECK_FPS) Gdx.graphics.setVSync(false);

        manager.finishLoading();

        if(showSplash) {
            setScreen(GetSplashScreen());
        } else {
            setScreen(new DemoScreen(this, true));
        }

    }

    @Override
    public void render() {
    	super.render();
    	if(CHECK_FPS) fpsLogger.log();
    }

    @Override
    public void resize(int width, int height) {
    	super.resize(width, height);
    }

    @Override
    public void pause() {
    	super.pause();
    }

    @Override
    public void resume() {
    	super.resume();
    }

    @Override
    public void dispose() {
    	super.dispose();
    }

    @Override
    public void setScreen(Screen screen) {
            super.setScreen(screen);
            Gdx.app.log(Darkshaft.LOG, screen.getClass().getSimpleName());
    }
    
    public SplashScreen GetSplashScreen() {
    	return new SplashScreen(this);
    }
}

