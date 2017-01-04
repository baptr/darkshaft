package net.baptr.darkshaft.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.baptr.darkshaft.Darkshaft;
import net.baptr.darkshaft.core.Entity.*;
import net.baptr.darkshaft.core.Spawner;
import net.baptr.darkshaft.gfx.*;
import net.baptr.darkshaft.gfx.Tower.TowerType;
import net.baptr.darkshaft.input.CameraInputProcessor;
import net.baptr.darkshaft.input.GameInputProcessor;
import net.baptr.darkshaft.util.MapUtils;
import net.baptr.darkshaft.util.PathPlanner;
import net.baptr.darkshaft.util.TargetHelper;

public abstract class GameScreen extends AbstractScreen {
  protected TiledMap map;
  protected TiledMapRenderer mapRenderer;

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
    input.addProcessor(new GameInputProcessor((FitViewport) viewport, this));
    input.addProcessor(new CameraInputProcessor((FitViewport) viewport));
    assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
    assetManager.load("maps/" + mapName, TiledMap.class);
    assetManager.finishLoading();
    map = assetManager.get("maps/" + mapName);
    mapRenderer = new IsometricTiledMapRenderer(map, 1f);

    defenses = new Array<Defense>(false, INITIAL_DEFENSE_CAPACITY);
    frank = new Avatar(15, -64); // TODO(baptr): Position near goal.
    entities = new Array<Entity>(false, INITIAL_DEFENSE_CAPACITY + 2);
    entities.add(frank);
    MapUtils.setMap(map, defenses);
    PathPlanner.setTerrain(map, defenses);
    pathPlanner = new PathPlanner(UnitType.PLAYER);
    pathMarker = new PathMarker();
    GameUI ui = new GameUI(this, stage);
    towerMarker = new Tower(TowerType.NONE, 0, 0, true);
    spawners = MapUtils.getSpawners();
  }

  public PathPlanner getPathPlanner() {
    return pathPlanner;
  }

  public PathMarker getPathMarker() {
    return pathMarker;
  }

  public void addDefense(Defense d) {
    if (!defenses.contains(d, false)) {
      defenses.add(d);
      entities.add(d);
      entities.sort();
      pathPlanner.addDefense(d);
      // invalidate spawner paths
      for (Spawner spawn : spawners) {
        spawn.invalidatePaths();
      }
    }
  }

  public void setTowerMarker(Tower t) {
    this.towerMarker = t;
  }

  public void setTowerMarker(TowerType t) {
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
    // camera.update(); // XXX Needed?
    for (Spawner spawn : spawners) {
      Array<Mob> mobs = spawn.check(delta);
    }
    if (!frank.isMoving()) {
      pathMarker.setPath(null);
    }
    for (Entity e : entities) {
      e.update(delta);
    }
    TargetHelper.updateMobs(delta);
    entities.sort();
  }

  public void render(float delta) {
    this.update(delta);

    Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    mapRenderer.setView(camera);
    mapRenderer.render();

    batch.setProjectionMatrix(camera.combined);
    batch.begin();

    pathMarker.draw(batch);
    // TODO need to interleave entities and mobs for drawing
    for (Entity e : entities) {
      e.draw(batch);
    }
    for (Mob m : TargetHelper.getMobs()) {
      m.draw(batch);
    }

    if (towerMarker != null && towerMarker.getTowerType() != TowerType.NONE) {
      //TODO move/skip on android (no mouse moves)
      towerMarker.draw(batch);
    }

    batch.end();

    stage.draw();
  }
}
