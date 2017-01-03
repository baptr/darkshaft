package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.baptr.darkshaft.core.Entity.*;
import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.util.PathPlanner.Node;

public class Unit extends Entity {

  Array<Node> currentPath;
  float speed = 100;
  Vector2 v;
  Vector2 dest;
  Facing facing;
  public UnitType unitType = UnitType.BASIC;;

  public Unit(float x, float y, String... animations) {
    super(x, y, animations);
    v = new Vector2();
    dest = new Vector2();
  }

  public void setPath(Array<Node> path) {
    currentPath = path;
  }

  @Override
  public boolean update(float delta) {
    super.update(delta);
    // Handle movement along the current path.
    if (this.isMoving()) {
      Node n = currentPath.get(0);
      // TODO: Refactor getWorldX/Y to return the center of the tile
      float nodeX = MapUtils.getWorldX(n.col, n.row) + 32;
      float nodeY = MapUtils.getWorldY(n.col, n.row) + 16;
      float spriteX = this.getX();
      float spriteY = this.getY();
      float dx = nodeX - spriteX;
      float dy = nodeY - spriteY;

      dest.set(nodeX, nodeY);

      v.set(dx, dy);
      float distance = v.len();
      v.nor().mul(speed * delta);

      float moveToX = spriteX + v.x;
      float moveToY = spriteY + v.y;
      this.setPosition(moveToX, moveToY);

      if (distance <= 2.0f) {
        currentPath.removeIndex(0);
      }
    }
    return false;
  }

  public boolean isMoving() {
    return (currentPath != null && currentPath.size > 0);
  }

  enum Facing {
    UP,
    DOWN,
    NORTH,
    SOUTH,
    EAST,
    WEST;
  }
}
