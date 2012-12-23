package com.baptr.darkshaft;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import com.baptr.darkshaft.screen.*;
import com.baptr.darkshaft.gfx.*;

public class Darkshaft extends Game {
    
    public static final String LOG = Darkshaft.class.getSimpleName();
    FPSLogger fpsLogger;

    private boolean showSplash = true;

    public AssetManager manager;

    public Darkshaft(boolean showSplash) {
        super();
        this.showSplash = showSplash;
    }

    @Override
    public void create() {
    	fpsLogger =  new FPSLogger();
        manager = new AssetManager();
        //Terrain.init(this, manager); 

        manager.finishLoading();
        
        if(showSplash) {
            setScreen(GetSplashScreen());
        } else {
            setScreen(new TowerScreen(this));
        }

    }

    @Override
    public void render() {
    	super.render();
    	fpsLogger.log();
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

