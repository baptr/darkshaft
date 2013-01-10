package com.baptr.darkshaft.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectMap;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.util.WeightMap;
import com.baptr.darkshaft.gfx.*;

public class PathPlanner {
    private TiledMap terrain; // XXX Terrain class?
    private WeightMap weights;

    private Node goal;

    private ObjectIntMap<Node> visited; // used as a set
    private ObjectIntMap<Node> toVisit; // used as a set
    private ObjectMap<Node,Node> parent; // got to Key from Value

    private ObjectMap<Node,Long> knownCost; // Known cost from Start to Node
    private ObjectMap<Node,Long> estCost; // Estimated score from Node to Goal

    private Array<Node> path; // result
    private Array<Node> neighbors;

    private int iterations;

    private static long MAX_TILE_COST = 64L;
    protected static long TOWER_COST = MAX_TILE_COST - 1; // XXX big tunable
    private static int MAX_NEIGHBORS = 6;

    public PathPlanner(TiledMap terrain, Array<Defense> defenses) {
        this.terrain = terrain;
        this.weights = new WeightMap(terrain, defenses);

        this.goal = new Node(0, 0);

        this.visited = new ObjectIntMap<Node>();
        this.toVisit = new ObjectIntMap<Node>();
        this.parent = new ObjectMap<Node,Node>();
        this.knownCost = new ObjectMap<Node,Long>();
        this.estCost = new ObjectMap<Node,Long>();
        this.path = new Array<Node>();
        this.neighbors = new Array<Node>(MAX_NEIGHBORS);

        assert terrain.width * terrain.height * MAX_TILE_COST < Long.MAX_VALUE;
    }

    public void setGoal(int goalCol, int goalRow) {
        goal.set(goalCol, goalRow);
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

    public Array<Node> findPath(int startCol, int startRow, int endCol, int endRow) {
        return findPath(startCol, startRow, endCol, endRow, null);
    }
    
    public Array<Node> findPath(int startCol, int startRow, int endCol, int endRow, Unit p) {
        setGoal(endCol, endRow);
        Array<Node> path = findPath(startCol, startRow);
        if(p != null){
            p.setPath(path);
        }
        return path;
    }

    /** Find a path from the specified startCol, startRow tile to the goal tile
     * specified in the constructor. Returns null if no path exists.
     */
    public Array<Node> findPath(int startCol, int startRow) { // XXX Need mob for terrain proficiency
        reInit();

        // Shortcut if the goal node is itself unpassable
        if(!weights.isPassable(goal.col, goal.row)) {
            return null;
        }

        Node startNode = new Node(startCol, startRow);
        toVisit.put(startNode, 0);

        knownCost.put(startNode, 0L);
        estCost.put(startNode, 0L + estimateCost(startNode));

        while(toVisit.size > 0) {
            iterations++;
            // pick the element of toVisit with the lowest estCost, pop from toVisit
            long guess = Long.MAX_VALUE;
            Node current = null;
            for(Node test : toVisit.keys()) {
                long testCost = estCost.get(test);
                if(testCost < guess) {
                    guess = testCost;
                    current = test;
                }
            }
            toVisit.remove(current,-1);

            if(goal.equals(current)) { // XXX == if primative long
                return recreatePath();
            }

            visited.put(current, 0);
            long myCost = knownCost.get(current);
            for(Node neighbor : unvisitedNeighbors(current)) {
                long neighborCost = myCost + getCost(current, neighbor); // XXX Should involve mob
                if(! toVisit.containsKey(neighbor) || neighborCost < knownCost.get(neighbor)) {
                    parent.put(neighbor, current);
                    knownCost.put(neighbor, neighborCost);
                    estCost.put(neighbor, neighborCost + estimateCost(neighbor));
                    if(! toVisit.containsKey(neighbor)) {
                        toVisit.put(neighbor,0);
                    }
                }
            }

        }
        
        return null;
    }

    /** Weighted distance between two adjacent nodes.
     * */
    private long getCost(Node from, Node to) {
        return weights.get(to.col, to.row);
    }
    
    /** Estimate the cost from specified node to the goal
     * */
    private long estimateCost(Node src) {
        // Manhattan distance
        // XXX Doesn't estimate the +row,+col and -row,-col cases properly
        return Math.abs(goal.row - src.row) + Math.abs(goal.col - src.col);
    }

    /** Return neighbors of the specified node that haven't yet been visited
     * themselves.
     * */
    private Array<Node> unvisitedNeighbors(Node n) {
        // 6 possible exits
        // -: -, =, X
        // =: -, n, +
        // +: X, =, +
        neighbors.clear();
        for(int dy = -1; dy <= 1; dy++) {
            for(int dx = -1; dx <= 1; dx++) {
                if(dx == -dy) continue;
                Node testNode = new Node(n.col + dx, n.row + dy);
                if(testNode.col >= terrain.width) continue;
                if(testNode.col < 0) continue;
                if(testNode.row >= terrain.width) continue;
                if(testNode.row < 0) continue;
                if(visited.containsKey(testNode)) continue;
                if(!weights.isPassable(testNode.col, testNode.row)) continue;
                neighbors.add(testNode);
            }
        }
        return neighbors;
    }

    public void addDefense(Defense d) {
        weights.addDefense(d);
    }

    /** Once a valid path has been found, this recreates it from "parent" data.
     */
    private Array<Node> recreatePath() {
        Node n = goal;
        path.add(n);
        while((n = parent.get(n)) != null) {
            path.add(n);
        }
        path.pop();
        path.reverse();
        return path;
    }

    /** Returns the number of iterations necessary to find the last path.
     * For debugging
     * */
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
            if(this == other) return true;
            if(!(other instanceof Node)) return false;
            Node that = (Node)other;
            return this.col == that.col && this.row == that.row;
        }

        public int hashCode() {
            return ((row+1) << 16) | (col+1);
        }

        public String toString() {
            return "(" + col + ", " + row + ")";
        }
    }
}
