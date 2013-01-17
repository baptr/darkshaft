package com.baptr.darkshaft.screen;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.math.Vector3;
import com.baptr.darkshaft.Darkshaft;

public abstract class AbstractScreen implements Screen {

    protected final Darkshaft game;
    protected final SpriteBatch batch;
    protected final Stage stage;
    protected final AssetManager assetManager;
    protected final OrthographicCamera camera;
    protected final InputMultiplexer input;
    protected final Preferences preferences;
    private TextureAtlas atlas;
    private TextureAtlas uiAtlas;
    private Skin skin;

    private Vector3 tmpVec;

    public AbstractScreen( Darkshaft game ) {
        this.game = game;
        this.assetManager = game.manager;
        this.batch = new SpriteBatch();
        this.stage = new Stage(0, 0, true);
        input = new InputMultiplexer();
        input.addProcessor(stage);
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600);
        preferences = Gdx.app.getPreferences(Darkshaft.LOG);
        tmpVec = new Vector3();
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
    
    public TextureAtlas getUiAtlas()
    {
        if( uiAtlas == null) {
            uiAtlas = new TextureAtlas( Gdx.files.internal( "ui-atlases/uiskin.atlas"));
        }
        
        return uiAtlas;
    }
    
    public Skin getSkin()
    {
        if( skin == null){
            skin = new Skin(Gdx.files.internal( "ui-atlases/uiskin.json"), getUiAtlas());
        }
        
        return skin;
    }

    // Screen implementation

    @Override
    public void show() {
        Gdx.app.log( Darkshaft.LOG, "Showing screen: " + getName() );
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void resize(
        int width,
        int height )
    {
        Gdx.app.log( Darkshaft.LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height );

        // resize the stage
        stage.setViewport( width, height, true );

        // re-configure camera, maintaining position
        tmpVec.set(camera.position);

        camera.setToOrtho(false, width, height);

        camera.position.set(tmpVec);
        camera.update();
    }

    public void update(float delta) {
        // Continue loading any queued assets
        assetManager.update();

        stage.act( delta );
    }

    @Override
    public void render( float delta ) {
        this.update(delta);

        // the following code clears the screen with the given RGB color (black)
        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        // update and draw the stage actors
        stage.draw();
        Table.drawDebug(stage);
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
        preferences.flush();
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
    }
}
