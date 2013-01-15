package com.baptr.darkshaft.screen;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.assets.loaders.TileMapRendererLoader.TileMapParameter;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObjectGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.graphics.Color;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.input.CameraInputProcessor;
import com.baptr.darkshaft.input.GameInputProcessor;
import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.screen.AbstractScreen;
import com.baptr.darkshaft.domain.Spawner;
import com.baptr.darkshaft.gfx.*;
import com.baptr.darkshaft.gfx.Tower.TowerType;
import com.baptr.darkshaft.util.PathPlanner;

public abstract class GameScreen extends AbstractScreen {
    protected TileMapRenderer mapRenderer;
    protected TileMapParameter mapParameter;

    protected Pool<Mob> mobPool;
    protected Array<Defense> defenses;
    protected Tower towerMarker;

    protected Color bgColor = new Color(0f, 0f, 0.5f, 1f);

    protected Avatar frank;
    protected Array<Entity> entities;

    protected PathPlanner pathPlanner;
    protected PathMarker pathMarker;
    
    protected Spawner[] spawners;
    
    private static final int INITIAL_DEFENSE_CAPACITY = 64;

    public GameScreen(Darkshaft game, String mapName) {
        super(game);
        input.addProcessor(new GameInputProcessor(camera, this));
        input.addProcessor(new CameraInputProcessor(camera));
        this.mapParameter = new TileMapParameter("maps", 8, 8);
        assetManager.load("maps/" + mapName, TileMapRenderer.class,
                mapParameter);
        assetManager.finishLoading();
        mapRenderer = assetManager.get("maps/" + mapName,
                TileMapRenderer.class);
        
        defenses = new Array<Defense>(false, INITIAL_DEFENSE_CAPACITY);
        frank = new Avatar(15, -64, getAtlas());
        entities = new Array<Entity>(false, INITIAL_DEFENSE_CAPACITY + 2);
        entities.add(frank);
        MapUtils.setRenderer(mapRenderer, defenses);
        pathPlanner = new PathPlanner(mapRenderer.getMap(), defenses);
        pathMarker = new PathMarker(getAtlas().findRegion("gamescreen/marker"));
        GameUI ui = new GameUI(this, stage);
        towerMarker = new Tower(TowerType.NONE,0,0,true);
        spawners = MapUtils.getSpawners(getAtlas());
    }

    public PathPlanner getPathPlanner() {
        return pathPlanner;
    }

    public PathMarker getPathMarker() {
        return pathMarker;
    }

    public void addDefense(Defense d) {
        if(!defenses.contains(d, false)){
            defenses.add(d);
            entities.add(d);
            entities.sort();
            pathPlanner.addDefense(d);
        }
    }
    
    public void setTowerMarker(Tower t) {
        this.towerMarker = t;
    }
    
    public void setTowerMarker(TowerType t){
        towerMarker.setTowerType(t, true);
    }

    public void setTowerMarkerPos(int col, int row) {
        towerMarker.setMapPosition(col, row);
    }

    public TowerType getTowerMarkerType() {
        return towerMarker.getTowerType();
    }

    /* temporary */
    public void movePlayer(float x, float y) {
        frank.setPosition(x, y);
        entities.sort();
    }
    public Avatar getPlayer() {
        return frank;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        camera.update();
        for(int i = 0; i < spawners.length; i++){
            Array<Mob> mobs = spawners[i].check(delta);
            if(mobs != null && mobs.size > 0){
                entities.addAll(mobs);
            }
        }
        if(!frank.isMoving()) {
            pathMarker.setPath(null);
        }
        for(Entity e : entities) {
            e.update(delta);
        }
        entities.sort();
    }

    public void render(float delta) {
        this.update(delta);

        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.render(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        pathMarker.draw(batch);
        for(Entity e : entities) {
            e.draw(batch);
        }

        if(towerMarker != null &&
                towerMarker.getTowerType() != TowerType.NONE ){
            //TODO move/skip on android (no mouse moves)
            towerMarker.draw(batch);
        }
        
        batch.end();

        stage.draw();
    }
}
