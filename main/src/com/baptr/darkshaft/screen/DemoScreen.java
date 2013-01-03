package com.baptr.darkshaft.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

//import com.baptr.darkshaft.core.TileMapRenderer;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.util.NetworkClient;
import com.baptr.darkshaft.gfx.*;
import com.baptr.darkshaft.gfx.Tower.TowerType;

public class DemoScreen extends GameScreen {

    Entity testEntity;
    NetworkClient client;
    
    public DemoScreen(Darkshaft game) {
        super(game, "demo.tmx");

        for(int i = 0; i < 4; i++) {
            this.addDefense(new Tower(TowerType.values()[i], 0, 0+i));
        }

        camera.translate(-400, -400, 0);
        camera.update();
        
        testEntity = new Entity(100, -250, getAtlas(), "gamescreen/dargorn");

        client = new NetworkClient();
        client.connect("localhost", "DemoScreen Player");
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render( float delta ) {
        super.render( delta );	

        batch.begin();
        testEntity.update(delta);
        testEntity.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
