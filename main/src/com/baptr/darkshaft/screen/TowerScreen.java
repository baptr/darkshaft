package com.baptr.darkshaft.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.baptr.darkshaft.Darkshaft;

import com.baptr.darkshaft.gfx.*;

public class TowerScreen extends AbstractScreen {

    Tower[] towers;
    Terrain[] terrain;
    
    public TowerScreen(Darkshaft game) {
        super(game);

        towers = new Tower[4];
        for(int i = 0; i < 4; i++) {
            towers[i] = new Tower(Tower.TowerType.values()[i], 20, 20+i*60);
        }
        /*
        terrain = new Terrain[12];
        for(int i = 0; i < 4*3; i++) {
            int col = i % Terrain.TILE_TYPES;
            int row = i / Terrain.TILE_TYPES;
            int odd = row % 2;
            int tileX = 20 + col * Terrain.TILE_WIDTH;
            if(odd == 1) {
                tileX += Terrain.TILE_X_OFFSET;
            }
            int tileY = 100 + row * Terrain.TILE_Y_OFFSET;
            terrain[i] = new Terrain(game.manager, col, tileX, tileY);
        }
        */
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render( float delta ) {
        super.render( delta );	

        Gdx.gl.glClearColor( 0f, 0f, 0.5f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        batch.begin();

/*
        for(int i = 0; i < 12; i++) {
            terrain[i].draw(batch);
        }
        */

        for(Tower t : towers) {
            t.draw(batch);
            t.translate(4.0f*Gdx.graphics.getDeltaTime(),0);
        }

        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 20, 100);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
