package com.baptr.darkshaft.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.baptr.darkshaft.Darkshaft;

import com.baptr.darkshaft.gfx.*;

public class TowerScreen extends AbstractScreen {

    Tower tower;
    Terrain[] terrain;
    
    public TowerScreen(Darkshaft game) {
        super(game);

        tower = new Tower("tower.png", 20, 20);
        terrain = new Terrain[4];
        for(int i = 0; i < 4; i++) {
            terrain[i] = new Terrain(game.manager, i, 20+64*i, 100 );
        }
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

        for(int i = 0; i < 4; i++) {
            terrain[i].draw(batch);
        }

        tower.draw(batch);
        tower.translate(4.0f*Gdx.graphics.getDeltaTime(),0);

        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 20, 100);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
