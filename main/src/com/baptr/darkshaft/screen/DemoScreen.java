package com.baptr.darkshaft.screen;

import com.badlogic.gdx.Gdx;

//import com.baptr.darkshaft.core.TileMapRenderer;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.gfx.*;
import com.baptr.darkshaft.gfx.Tower.TowerType;

public class DemoScreen extends GameScreen {

    public DemoScreen(Darkshaft game) {
        super(game, "demo.tmx");

        for(int i = 0; i < 4; i++) {
            this.addDefense(new Tower(TowerType.values()[i], 0, 0+i));
        }

        camera.translate(-400, -400, 0);
        camera.update();
    }

    @Override
    public void show() {
        super.show();
    }

/*
    @Override
    public void render( float delta ) {
        super.render( delta );	
    }
*/

    @Override
    public void dispose() {
        super.dispose();
    }

}
