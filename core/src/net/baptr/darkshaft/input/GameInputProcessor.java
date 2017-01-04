package net.baptr.darkshaft.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.baptr.darkshaft.Darkshaft;
import net.baptr.darkshaft.gfx.Avatar;
import net.baptr.darkshaft.gfx.Tower;
import net.baptr.darkshaft.gfx.Tower.TowerType;
import net.baptr.darkshaft.screen.GameScreen;
import net.baptr.darkshaft.util.MapUtils;
import net.baptr.darkshaft.util.PathPlanner;
import net.baptr.darkshaft.util.PathPlanner.Node;

public class GameInputProcessor extends AbstractInputProcessor {

  boolean dragged;
  Vector3 down;
  Vector3 touch;
  Vector3 move;
  GameScreen screen;
  Viewport viewport;

  public static final int DRAG_THRESHOLD = 20;

  public GameInputProcessor(Viewport viewport, GameScreen screen) {
    super();
    this.viewport = viewport;
    this.screen = screen;
    this.down = new Vector3();
    this.touch = new Vector3();
    this.move = new Vector3();
  }

  @Override
  public boolean keyTyped(char character) {
    switch (character) {
      case '1':
        screen.setTowerMarker(TowerType.BASIC);
        break;
      case '2':
        screen.setTowerMarker(TowerType.FIRE);
        break;
      case '3':
        screen.setTowerMarker(TowerType.WATER);
        break;
      case '4':
        screen.setTowerMarker(TowerType.SPIRIT);
        break;
      case '`':
        screen.setTowerMarker(TowerType.NONE);
        break;
    }

    return true;
  }

  @Override
  public boolean keyDown(int keyCode) {
    Gdx.app.debug("Input Test", "key down: " + keyCode);
    // Esc
    if (keyCode == 131) {
      screen.setTowerMarker(TowerType.NONE);
    }
    return false;
  }

  @Override
  public boolean touchDown(int x, int y, int pointer, int button) {
    dragged = false;
    down.set(x, y, 0);
    touch.set(x, y, 0);
    viewport.unproject(touch);
    return false;
  }

  @Override
  public boolean touchDragged(int x, int y, int pointer) {
    if (!dragged && (Math.abs(x - down.x) + Math.abs(y - down.y)) >= DRAG_THRESHOLD) {
      dragged = true;
    } else if (!dragged) {
      // Keep camera from moving under DRAG_THRESHOLD
      return true;
    }
    return false;
  }

  @Override
  public boolean touchUp(int x, int y, int pointer, int button) {
    if (!dragged && button == 0) {
      TowerType placeType = screen.getTowerMarkerType();
      // clicked
      if (placeType != TowerType.NONE) { // Place a tower
        int mapRow = MapUtils.getMapRow(touch.x, touch.y);
        int mapCol = MapUtils.getMapCol(touch.x, touch.y);
        if (MapUtils.isInMap(mapCol, mapRow)) {
          screen.addDefense(new Tower(placeType, mapCol, mapRow));
        }
      } else {
        Avatar p = screen.getPlayer();
        PathPlanner pp = screen.getPathPlanner();
        int sCol = MapUtils.getMapCol(p.getX(), p.getY());
        int sRow = MapUtils.getMapRow(p.getX(), p.getY());
        int gCol = MapUtils.getMapCol(touch.x, touch.y);
        int gRow = MapUtils.getMapRow(touch.x, touch.y);
        Array<Node> path = pp.findPath(sCol, sRow, gCol, gRow, p);
        Gdx.app.log(
            Darkshaft.LOG,
            "Path length: "
                + ((path == null) ? "NULL" : "" + path.size)
                + " after "
                + pp.getIterations()
                + " steps");
        screen.getPathMarker().setPath(path);
      }

      return true;
    }
    return false;
  }

  @Override
  public boolean mouseMoved(int x, int y) {
    move.set(x, y, 0);
    viewport.unproject(move);
    int mapRow = MapUtils.getMapRow(move.x, move.y);
    int mapCol = MapUtils.getMapCol(move.x, move.y);
    if (MapUtils.isInMap(mapCol, mapRow)) {
      screen.setTowerMarkerPos(mapCol, mapRow);
    }
    return false;
  }
}
