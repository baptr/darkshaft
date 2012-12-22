package com.baptr.darkshaft;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import com.baptr.darkshaft.gfx.*;

public class Darkshaft extends Game {
    OrthographicCamera camera;
    SpriteBatch batch;
    BitmapFont font;

    AssetManager manager;

    Tower tower;
    Terrain terrain;
    
    @Override
    public void create() {
        manager = new AssetManager();
        Terrain.init(this, manager); 

        manager.finishLoading();

        tower = new Tower("tower.png", 20, 20);
        terrain = new Terrain(manager, 20, 100 );

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        font = new BitmapFont(Gdx.files.internal("arial-15.fnt"),
                Gdx.files.internal("arial-15.png"), false, true);
        batch = new SpriteBatch();

    }

    @Override
    public void render() {
    	super.render();
    	
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        //camera.update();

        //batch.setProjectionMatrix(camera.combined);
        batch.begin();

        terrain.draw(batch);

        tower.draw(batch);
        tower.moveBy(4.0f*Gdx.graphics.getDeltaTime(),0);

        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 20, 100);
        batch.end();
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
}

