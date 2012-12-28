package com.baptr.darkshaft.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.assets.loaders.TileMapRendererLoader.TileMapParameter;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.graphics.Color;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.input.CameraInputProcessor;
import com.baptr.darkshaft.input.GameInputProcessor;
import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.screen.AbstractScreen;
import com.baptr.darkshaft.gfx.*;

public abstract class GameScreen extends AbstractScreen {
    protected TileMapRenderer mapRenderer;
    protected TileMapParameter mapParameter;

    protected Pool<Mob> mobPool;
    protected Array<Defense> defenses;

    protected Color bgColor = new Color(0f, 0f, 0.5f, 1f);

    protected Avatar frank;

    private static final int INITIAL_DEFENSE_CAPACITY = 64;

    public GameScreen(Darkshaft game, String mapName) {
        super(game);
        input.addProcessor(new GameInputProcessor(camera, this));
        input.addProcessor(new CameraInputProcessor(camera));
        this.mapParameter = new TileMapParameter("maps", 8, 8);
        assetManager.load("maps/" + mapName, TileMapRenderer.class,
                mapParameter);
        assetManager.finishLoading();
        mapRenderer = assetManager.get("maps/" + mapName,
                TileMapRenderer.class);
        MapUtils.setRenderer(mapRenderer);
        defenses = new Array<Defense>(false, INITIAL_DEFENSE_CAPACITY);
        frank = new Avatar(getAtlas().findRegion("gamescreen/frank"), 15, -64);
    }

    public void addDefense(Defense d) {
        defenses.add(d);
    }

    /* temporary */
    public void movePlayer(float x, float y) {
        frank.setPosition(x, y);
    }

    public void render(float delta) {
        super.render(delta);

        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        mapRenderer.render(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Defense d : defenses) {
            d.draw(batch);
        }
        frank.draw(batch);
        batch.end();
    }
}
