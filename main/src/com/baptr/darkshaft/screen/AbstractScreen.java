package com.baptr.darkshaft.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.baptr.darkshaft.Darkshaft;

public abstract class AbstractScreen implements Screen {

    protected final Darkshaft game;
    protected final BitmapFont font;
    protected final SpriteBatch batch;
    protected final Stage stage;
    protected final AssetManager assetManager;
    protected final OrthographicCamera camera;
    private TextureAtlas atlas;

    public AbstractScreen( Darkshaft game ) {
        this.game = game;
        this.assetManager = game.manager;
        this.font = new BitmapFont(Gdx.files.internal("arial-15.fnt"),
                Gdx.files.internal("arial-15.png"), false, true);
        this.batch = new SpriteBatch();
        this.stage = new Stage(0, 0, true);
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600);
    }

    protected String getName()
    {
        return getClass().getSimpleName();
    }
    
    public TextureAtlas getAtlas()
    {
        if( atlas == null ) {
            atlas = new TextureAtlas( Gdx.files.internal( "image-atlases/pages-info.atlas" ) );
        }
        
        return atlas;
    }
       

    // Screen implementation

    @Override
    public void show()
    {
        Gdx.app.log( Darkshaft.LOG, "Showing screen: " + getName() );
    }

    @Override
    public void resize(
        int width,
        int height )
    {
        Gdx.app.log( Darkshaft.LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height );

        // resize the stage
        stage.setViewport( width, height, true );
    }

    @Override
    public void render(
        float delta )
    {
        // Continue loading any queued assets
        assetManager.update();

        // the following code clears the screen with the given RGB color (black)
        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        // update and draw the stage actors
        stage.act( delta );
        stage.draw();
    }

    @Override
    public void hide()
    {
        Gdx.app.log( Darkshaft.LOG, "Hiding screen: " + getName() );
    }

    @Override
    public void pause()
    {
        Gdx.app.log( Darkshaft.LOG, "Pausing screen: " + getName() );
    }

    @Override
    public void resume()
    {
        Gdx.app.log( Darkshaft.LOG, "Resuming screen: " + getName() );
    }

    @Override
    public void dispose()
    {
        Gdx.app.log( Darkshaft.LOG, "Disposing screen: " + getName() );

        // dispose the collaborators
        stage.dispose();
        batch.dispose();
        font.dispose();
    }
}
