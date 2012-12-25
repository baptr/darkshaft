package com.baptr.darkshaft.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TileMapRendererLoader.TileMapParameter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.baptr.darkshaft.Darkshaft;

import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
//import com.baptr.darkshaft.core.TileMapRenderer;

import com.baptr.darkshaft.gfx.*;
import com.baptr.darkshaft.input.CameraInputProcessor;

public class DemoScreen extends AbstractScreen {

    private TileMapRenderer tileMapRenderer;
    private Tower tower;

    public DemoScreen(Darkshaft game) {
        super(game);
        input.addProcessor(new CameraInputProcessor(camera));
        TileMapParameter tileMapParameter = new TileMapParameter("maps", 8, 8);
        assetManager.load("maps/demo.tmx", com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer.class, tileMapParameter);

        tower = new Tower(Tower.TowerType.BASIC, 0, 0);
    }

    @Override
    public void show() {
        super.show();
        assetManager.finishLoading();
        tileMapRenderer = assetManager.get("maps/demo.tmx", TileMapRenderer.class);

        camera.translate(-200, 0, 0);
        camera.update();
    }

    @Override
    public void render( float delta ) {
        super.render( delta );	
        
        Gdx.gl.glClearColor( 0f, 0f, 0.5f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        camera.update();

        tileMapRenderer.render(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        tower.draw(batch);
        tower.translate(4.0f*Gdx.graphics.getDeltaTime(),0);

        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
