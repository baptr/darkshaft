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

    public DemoScreen(Darkshaft game) {
        super(game);
        input.addProcessor(new CameraInputProcessor(camera));
        TileMapParameter tileMapParameter = new TileMapParameter("maps", 8, 8);
        assetManager.load("maps/demo.tmx", com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer.class, tileMapParameter);
    }

    @Override
    public void show() {
        super.show();
        assetManager.finishLoading();
        tileMapRenderer = assetManager.get("maps/demo.tmx", TileMapRenderer.class);
    }

    @Override
    public void render( float delta ) {
        super.render( delta );	
        
        Gdx.gl.glClearColor( 0f, 0f, 0.5f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        batch.begin();

        tileMapRenderer.render(camera);

        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
