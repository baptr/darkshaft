package com.baptr.darkshaft;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Game implements ApplicationListener {
    Texture towerImage;
    OrthographicCamera camera;
    SpriteBatch batch;
    BitmapFont font;

    Rectangle tower;

    public void create() {
        towerImage = new Texture(Gdx.files.internal("tower.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        font = new BitmapFont(Gdx.files.internal("arial-15.fnt"),
                Gdx.files.internal("arial-15.png"), false, true);
        batch = new SpriteBatch();

        tower = new Rectangle();
        tower.x = 20;
        tower.y = 20;
        tower.width = 32;
        tower.height = 64;
    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        //camera.update();

        //batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(towerImage, tower.x, tower.y);
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 20, 100);
        batch.end();
    }

    public void resize(int width, int height) {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void dispose() {
    }
}

