package com.baptr.darkshaft.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.gfx.Entity;

public class TowerScreen extends AbstractScreen {

	Entity tower;
	
	public TowerScreen(Darkshaft game) {
		super(game);

		tower = new Entity("tower.png", 20, 20);
		
	}
	
	@Override
    public void show()
    {
        super.show();
    }
 
    @Override
    public void render(
        float delta )
    {
        super.render( delta );	
    	
    	batch.begin();
    	font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 20, 100);

        tower.draw(batch);
        tower.moveBy(4.0f*Gdx.graphics.getDeltaTime(),0);

        batch.end();
    }
 
    @Override
    public void dispose()
    {
        super.dispose();
    }

}
