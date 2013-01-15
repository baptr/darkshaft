package com.baptr.darkshaft.domain;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObjectGroup;
import com.badlogic.gdx.utils.Array;
import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.gfx.Mob;
import com.baptr.darkshaft.gfx.Unit;
import com.baptr.darkshaft.gfx.Mob.MobType;
import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.util.PathPlanner;
import com.baptr.darkshaft.util.PathPlanner.Node;

public class Spawner {
    int id;
    int waveNumber;
    float endX;
    float endY;
    float x;
    float y;
    float currentSpawnRate;
    float spawnTimer;
    Array<Wave> waves;
    int tick;
    TextureAtlas atlas;
    PathPlanner planner;
    
    public Spawner(int id, TextureAtlas atlas){
        this.id = id;
        waves = new Array<Wave>();
        waveNumber = -1;
        currentSpawnRate = 0;
        spawnTimer = 0;
        this.x = Integer.MIN_VALUE;
        this.y = Integer.MIN_VALUE;
        this.endX = Integer.MIN_VALUE;
        this.endY = Integer.MIN_VALUE;
        tick = 0;
        this.atlas = atlas;
        this.planner = planner;
    }
    
    public void addWave(int number, float spawnRate, String wave){
        this.waveNumber = 0;
        this.waves.add(ParseWave(number, spawnRate, wave));
    }
    
    public Array<Mob> check(float delta){
        spawnTimer += delta;
        if(delta > currentSpawnRate){
            if(tick != 0){
                spawnTimer -= currentSpawnRate;
            }
            Array<Mob> mobs = spawnNext();
            tick++;
            return mobs;
        }
        
        return null;
    }
    
    // This will parse the wave string and add all the proper types
    // to the current wave. This will expand the shorthand of AxB to add
    // mob "A" B times to the current wave
    private Wave ParseWave(int number, float spawnRate, String waveString){
        
        Wave wave = new Wave(number);
        int tick = 0;
        Array<String> tokens = tokenizeWave(waveString);
        for(; tick < tokens.size; tick++){
            String token = tokens.get(tick);
            String[] split = token.split(",");
            Array<Integer> mobs = new Array<Integer>();
            for(int i = 0; i < split.length; i++){
                String mob = split[i];
                if(mob != null && !mob.isEmpty()){
                    mobs.add(Integer.parseInt(mob));
                }
            }
            wave.addTick(tick, mobs);
        }
        return wave;
    }
    
    private Array<String> tokenizeWave(String waveString){
        String cleaned = waveString.replaceAll("\\s","");
        Array<String> tokens = new Array<String>();
        String token = "";
        boolean group = false;
        for(int i = 0; i < cleaned.length(); i++){
            char c = cleaned.charAt(i);
            if(c == ',' && !group){
                tokens.add(token);
                token = "";
            } else if (c == '('){
                group = true;
            } else if (c == ')'){
                group = false;
                if(i + 1 < cleaned.length() && cleaned.charAt(i + 1) == ','){
                    i++;
                    tokens.add(token);
                    token = "";
                } else if (i + 1 < cleaned.length() && cleaned.charAt(i + 1) == 'x'){
                    String multiplier = "";
                    i += 2;
                    c = cleaned.charAt(i);
                    while(c != ',' && i < cleaned.length()){
                        multiplier += c;
                        i++;
                        if(i < cleaned.length()){
                            c = cleaned.charAt(i);
                        }
                    }
                    int mult = Integer.parseInt(multiplier);
                    String baseToken = "," + token;
                    for(int j = 1; j < mult; j++){
                        token += baseToken;
                    }
                    tokens.add(token);
                    token = "";
                }
            } else if (c == 'x'){
                String multiplier = "";
                i++;
                c = cleaned.charAt(i);
                while(c != ',' && c != ')' && i < cleaned.length()){
                    multiplier += c;
                    i++;
                    if( i < cleaned.length()){
                        c = cleaned.charAt(i);
                    }
                }
                int mult = Integer.parseInt(multiplier);
                String baseToken = "," + token.substring(token.lastIndexOf(',') + 1);
                for(int j = 1; j < mult; j++){
                    token += baseToken;
                }
                
                if(!group){
                    tokens.add(token);
                    token = "";
                } else if (c == ')'){
                    i--;
                }
                else {
                    token += ',';
                }
            } else {
                token += c;
            }
        }
        tokens.add(token);
        return tokens;
    }
    
    private Array<Mob> spawnNext(){
        if(this.endX == Integer.MIN_VALUE || this.endY == Integer.MIN_VALUE){
            return null;
        }
        Array<Mob> mobs = new Array<Mob>();
        Wave wave = waves.get(waveNumber);
        if(wave.mobs.get(tick) != null){
            for(int i = 0; i < wave.mobs.get(tick).size; i++){
                int mobType = wave.mobs.get(tick).get(i);
                if(mobType > 0){
                    mobType--;
                    Mob m = new Mob(atlas, MobType.values()[mobType], this.x, this.y);
                    mobs.add(m);
                    PathPlanner planner = new PathPlanner(MapUtils.getMap(), MapUtils.getDefenses());
                    /*
                     *these map coords are a bit off for objects...
                    int sCol = MapUtils.getMapCol(m.getX(), m.getY());
                    int sRow = MapUtils.getMapRow(m.getX(), m.getY());
                    int gCol = MapUtils.getMapCol(endX, endY);
                    int gRow = MapUtils.getMapRow(endX, endY);
                    */

                    int sCol = 1;
                    int sRow = 1;
                    int gCol = 10;
                    int gRow = 10;
                    Array<Node> path = planner.findPath(sCol, sRow, gCol, gRow, m);
                    Gdx.app.log(Darkshaft.LOG, "Path length: " +
                            ((path == null) ? "NULL" : ""+path.size)
                            + " after " + planner.getIterations() + " steps");                              
                }
            }
        }
        return mobs;
    }
    
    public void setEnd(int x, int y){
        this.endX = x;
        this.endY = y;
    }
    
    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    private class Wave{
        int number;
        HashMap<Integer, Array<Integer>> mobs;
        
        public Wave(int number){
            this.number = number;
            this.mobs = new HashMap<Integer, Array<Integer>>();
        }
        
        public Wave(int number, HashMap<Integer, Array<Integer>> mobs){
            this.number = number;
            this.mobs = mobs;
        }
        
        public void addTick(Integer tick, Array<Integer> mobs){
            this.mobs.put(tick, mobs);
        }
    }
}
