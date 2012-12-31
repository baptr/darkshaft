package com.baptr.darkshaft.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

//import com.baptr.darkshaft.core.TileMapRenderer;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.gfx.*;
import com.baptr.darkshaft.gfx.Tower.TowerType;

public class DemoScreen extends GameScreen {

    // TEMPORARY FOR ANIMATION STUFF
    TextureAtlas ta;
    AtlasRegion[] animationFrames;
    float stateTime = 0f;
    TextureRegion currentFrame;
    Animation downWalk;
    // END TEMPORARY
    
    
    public DemoScreen(Darkshaft game) {
        super(game, "demo.tmx");

        for(int i = 0; i < 4; i++) {
            this.addDefense(new Tower(TowerType.values()[i], 0, 0+i));
        }

        camera.translate(-400, -400, 0);
        camera.update();
        
        // TEMPORARY, THIS SHOULD BE MOVED TO EACH UNIT SOMEHOW
        ta = this.getAtlas();
        
        int numberOfFrames = 4;
        float frameDuration = .175f;
        animationFrames = new AtlasRegion[numberOfFrames];
        for(int ct = 0; ct < numberOfFrames; ct++)
        {
            animationFrames[ct] =ta.findRegion("Characters/Frank/Down", ct+1);
        }
        downWalk = new Animation(frameDuration, animationFrames);
        // END TEMP ANIMATION STUFF
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render( float delta ) {
        super.render( delta );	
        // TEMPORARY FOR ANIMATION TESTING
        stateTime += delta;
        currentFrame = downWalk.getKeyFrame(stateTime, true);
        batch.begin();
        this.batch.draw(currentFrame, 50, 50);
        batch.end();
        // END TEMPORARY
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
