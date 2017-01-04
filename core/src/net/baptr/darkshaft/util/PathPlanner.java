package net.baptr.darkshaft.util;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectMap;
import java.util.Iterator;
import net.baptr.darkshaft.core.Entity.*;
import net.baptr.darkshaft.gfx.*;

public class PathPlanner {
  private static int width;
  private static int height;
  private static WeightMap weights;

  private UnitType unitType;

  private Node goal;

  private ObjectIntMap<Node> visited; // used as a set
  private ObjectIntMap<Node> toVisit; // used as a set
  private ObjectMap<Node, Node> parent; // got to Key from Value

  private ObjectMap<Node, Long> knownCost; // Known cost from Start to Node
  private ObjectMap<Node, Long> estCost; // Estimated score from Node to Goal

  private Array<Node> path; // result
  private Array<Node> neighbors;

  private Array<Array<Node>> activePaths;

  private int iterations;

  private static long MAX_TILE_COST = 64L;
  protected static long TOWER_COST = MAX_TILE_COST - 1; // XXX big tunable
  private static int MAX_NEIGHBORS = 6;

  public static void setTerrain(TiledMap terrain, Array<Defense> defenses) {
    TiledMapTileLayer l = (TiledMapTileLayer) terrain.getLayers().get(0);
    assert l.getWidth() * l.getHeight() * MAX_TILE_COST < Long.MAX_VALUE;

    PathPlanner.width = l.getWidth();
    PathPlanner.height = l.getHeight();
    PathPlanner.weights = new WeightMap(terrain, defenses);
  }

  public PathPlanner(UnitType type) {
    this.unitType = type;
    this.goal = new Node(0, 0);

    this.visited = new ObjectIntMap<Node>();
    this.toVisit = new ObjectIntMap<Node>();
    this.parent = new ObjectMap<Node, Node>();
    this.knownCost = new ObjectMap<Node, Long>();
    this.estCost = new ObjectMap<Node, Long>();
    this.path = new Array<Node>();
    this.neighbors = new Array<Node>(MAX_NEIGHBORS);
    this.activePaths = new Array<Array<Node>>();
  }

  public synchronized void setGoal(int goalCol, int goalRow) {
    if (goal.col != goalCol || goal.row != goalRow) {
      goal.set(goalCol, goalRow);
      // When the goal changes, we need to clear any previously calculated
      // weights and start fresh
      reInit();
    }
  }

  private void reInit() {
    visited.clear();
    toVisit.clear();
    parent.clear();
    knownCost.clear();
    estCost.clear();
    path.clear();

    iterations = 0;
  }

  public synchronized Array<Node> findPath(
      int startCol, int startRow, int endCol, int endRow, Unit p) {
    assert p.unitType.equals(this.unitType);
    setGoal(endCol, endRow);

    Array<Node> path = findPath(new Node(startCol, startRow));
    if (p != null) {
      path.removeIndex(0);
      p.setPath(path);
      activePaths.add(path);
    }
    return path;
  }

  /**
   * Find a path from the specified startCol, startRow tile to the goal tile specified in the
   * constructor. Returns null if no path exists. Actually calculated from the goal node towards the
   * start node to allow re-use of calculated weights from different start positions.
   */
  public synchronized Array<Node> findPath(Node startNode) {
    if (startNode == null) {
      System.out.println("Attempt to navigate from a null node!");
      return null;
    }
    // Shortcut if the goal node is itself unpassable
    if (!isPassable(goal)) {
      return null;
    }

    if (parent.containsKey(startNode)) {
      // We already have a path
      return recreatePath(startNode);
    } else if (!visited.containsKey(goal)) {
      // First run
      toVisit.put(goal, 0);

      knownCost.put(goal, 0L);
      estCost.put(goal, 0L + estimateCost(goal, startNode));
    } else {
      // running a new start with the same goal. Need to update estCost??
      for (Node n : estCost.keys()) {
        estCost.put(n, knownCost.get(n) + estimateCost(n, startNode));
      }
    }

    while (toVisit.size > 0) {
      iterations++;
      // pop the element of toVisit with the lowest estCost
      long guess = Long.MAX_VALUE;
      Node current = null;
      for (Node test : toVisit.keys()) {
        long testCost = estCost.get(test);
        if (testCost < guess) {
          guess = testCost;
          current = test;
        }
      }

      if (startNode.equals(current)) { // XXX == if primitive long
        return recreatePath(startNode);
      }

      toVisit.remove(current, -1);
      visited.put(current, 0);
      long myCost = knownCost.get(current);
      for (Node neighbor : unvisitedNeighbors(current)) {
        if (!isPassable(neighbor)) continue;
        long neighborCost = myCost + getCost(current, neighbor);
        if (!toVisit.containsKey(neighbor) || neighborCost < knownCost.get(neighbor)) {
          parent.put(neighbor, current);
          knownCost.put(neighbor, neighborCost);
          estCost.put(neighbor, neighborCost + estimateCost(neighbor, startNode));
          if (!toVisit.containsKey(neighbor)) {
            toVisit.put(neighbor, 0);
          }
        }
      }
    }

    return null;
  }

  /** Weighted distance between two adjacent nodes. */
  private long getCost(Node from, Node to) {
    return weights.get(unitType, to.col, to.row);
  }

  private boolean isPassable(Node node) {
    return weights.isPassable(unitType, node.col, node.row);
  }

  /** Estimate the cost between two specified nodes. */
  private long estimateCost(Node src, Node dest) {
    // Manhattan distance
    // XXX Doesn't estimate the +row,+col and -row,-col cases properly
    return Math.abs(dest.row - src.row) + Math.abs(dest.col - src.col);
  }

  /** Return neighbors of the specified node that haven't yet been visited themselves. */
  private Array<Node> unvisitedNeighbors(Node n) {
    // 6 possible exits
    // -: -, =, X
    // =: -, n, +
    // +: X, =, +
    neighbors.clear();
    for (int dy = -1; dy <= 1; dy++) {
      for (int dx = -1; dx <= 1; dx++) {
        if (dx == -dy) continue;
        Node testNode = new Node(n.col + dx, n.row + dy);
        if (testNode.col >= PathPlanner.width) continue;
        if (testNode.col < 0) continue;
        if (testNode.row >= PathPlanner.width) continue;
        if (testNode.row < 0) continue;
        if (visited.containsKey(testNode)) continue;
        neighbors.add(testNode);
      }
    }
    return neighbors;
  }

  public void addDefense(Defense d) {
    weights.addDefense(d);
    invalidatePaths();
  }

  /** Force a recalculation of all outstanding paths. */
  public synchronized void invalidatePaths() {
    reInit();
    // For each Path, group by current start/end
    // Then recalculate each group
    ObjectMap<Node, Array<Array<Node>>> commonGoal = new ObjectMap<Node, Array<Array<Node>>>();
    ObjectMap<Node, Node> maxStart = new ObjectMap<Node, Node>();
    long maxDist = 0;
    for (Iterator<Array<Node>> iter = activePaths.iterator(); iter.hasNext(); ) {
      Array<Node> path = iter.next();
      if (path.size == 0) {
        iter.remove();
        continue;
      }
      Node start = path.first();
      Node end = path.peek();
      long dist = estimateCost(start, end);
      if (dist > maxDist) {
        maxDist = dist;
        maxStart.put(end, start);
      }
      // each unique goal -> list of starts
      // XXX starts currently non-unique, otherwise need to store Unit
      if (commonGoal.containsKey(end)) {
        commonGoal.get(end).add(path);
      } else {
        commonGoal.put(end, new Array<Array<Node>>());
        commonGoal.get(end).add(path);
      }
    }
    // Loop through commonGoal and perform a new calculation for each
    // Hopefully reusing most of the planning for each start position
    // Then replacing the path with the one newly calculated
    for (Node goal : commonGoal.keys()) {
      setGoal(goal.col, goal.row);
      // Calculate from farthest?
      Node start = maxStart.get(goal);
      findPath(start);
      // Need to be able to split paths for mobs that spawned together
      // but later separated
      for (Array<Node> oldPath : commonGoal.get(goal)) {
        Node oldStart = oldPath.first();
        Array<Node> newPath = findPath(oldStart);
        if (newPath != null) {
          oldPath.clear();
          oldPath.addAll(newPath);
        }
      }
    }
  }

  /** Once a valid path has been found, this recreates it from "parent" data. */
  private Array<Node> recreatePath(Node start) {
    Node n = start;
    Array<Node> path = new Array<Node>();
    path.add(n);
    while ((n = parent.get(n)) != null) {
      path.add(n);
    }
    return path;
  }

  /** Returns the number of iterations necessary to find the last path. For debugging */
  public int getIterations() {
    return iterations;
  }

  public class Node { // XXX probably should just be a packed long
    public int col, row;

    public Node(int col, int row) {
      this.set(col, row);
    }

    public void set(int col, int row) {
      this.col = col;
      this.row = row;
    }

    public boolean equals(Object other) {
      if (this == other) return true;
      if (!(other instanceof Node)) return false;
      Node that = (Node) other;
      return this.col == that.col && this.row == that.row;
    }

    public int hashCode() {
      return ((row + 1) << 16) | (col + 1);
    }

    public String toString() {
      return "(" + col + ", " + row + ")";
    }
  }
}
