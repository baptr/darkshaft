package net.baptr.darkshaft.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import java.util.EnumMap;
import java.util.HashMap;
import net.baptr.darkshaft.Darkshaft;
import net.baptr.darkshaft.core.Entity.*;
import net.baptr.darkshaft.gfx.Mob;
import net.baptr.darkshaft.gfx.Mob.MobType;
import net.baptr.darkshaft.util.MapUtils;
import net.baptr.darkshaft.util.PathPlanner;
import net.baptr.darkshaft.util.PathPlanner.Node;
import net.baptr.darkshaft.util.TargetHelper;

public class Spawner {
  int id;
  int waveNumber;
  int goalCol;
  int goalRow;
  int col;
  int row;
  float currentSpawnRate;
  float spawnTimer;
  Array<Wave> waves;
  int tick;
  EnumMap<UnitType, PathPlanner> planners;

  public Spawner(int id) {
    this.id = id;
    waves = new Array<Wave>();
    waveNumber = -1;
    currentSpawnRate = 0;
    spawnTimer = 0;
    this.col = Integer.MIN_VALUE;
    this.row = Integer.MIN_VALUE;
    this.goalCol = Integer.MIN_VALUE;
    this.goalRow = Integer.MIN_VALUE;
    tick = 0;
    this.planners = new EnumMap<UnitType, PathPlanner>(UnitType.class);
  }

  public void addWave(int number, float spawnRate, String wave) {
    this.waves.add(ParseWave(number, spawnRate, wave));
  }

  public void setWave(int wave) {
    if (wave < waves.size) {
      this.currentSpawnRate = waves.get(wave).getSpawnRate();
      spawnTimer = 0;
      tick = 0;
      waveNumber = wave;
    } else {
      Gdx.app.log(Darkshaft.LOG, "No waves remain! WINNER");
    }
  }

  // Check if it's time to spawn more mobs.
  public Array<Mob> check(float delta) {
    spawnTimer += delta;
    if (spawnTimer > currentSpawnRate || tick == 0) {
      if (tick != 0) {
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
  private Wave ParseWave(int number, float spawnRate, String waveString) {
    Wave wave = new Wave(number, spawnRate);
    int tick = 0;
    Array<String> tokens = tokenizeWave(waveString);
    for (; tick < tokens.size; tick++) {
      String token = tokens.get(tick);
      String[] split = token.split(",");
      Array<Integer> mobs = new Array<Integer>();
      for (int i = 0; i < split.length; i++) {
        String mob = split[i];
        if (mob != null && !mob.isEmpty()) {
          mobs.add(Integer.parseInt(mob));
        }
      }
      wave.addTick(tick, mobs);
    }
    return wave;
  }

  private Array<String> tokenizeWave(String waveString) {
    String cleaned = waveString.replaceAll("\\s", "");
    Array<String> tokens = new Array<String>();
    String token = "";
    boolean group = false;
    for (int i = 0; i < cleaned.length(); i++) {
      char c = cleaned.charAt(i);
      if (c == ',' && !group) {
        tokens.add(token);
        token = "";
      } else if (c == '(') {
        group = true;
      } else if (c == ')') {
        group = false;
        if (i + 1 < cleaned.length() && cleaned.charAt(i + 1) == ',') {
          i++;
          tokens.add(token);
          token = "";
        } else if (i + 1 < cleaned.length() && cleaned.charAt(i + 1) == 'x') {
          String multiplier = "";
          i += 2;
          c = cleaned.charAt(i);
          while (c != ',' && i < cleaned.length()) {
            multiplier += c;
            i++;
            if (i < cleaned.length()) {
              c = cleaned.charAt(i);
            }
          }
          int mult = Integer.parseInt(multiplier);
          String baseToken = "," + token;
          for (int j = 1; j < mult; j++) {
            token += baseToken;
          }
          tokens.add(token);
          token = "";
        }
      } else if (c == 'x') {
        String multiplier = "";
        i++;
        c = cleaned.charAt(i);
        while (c != ',' && c != ')' && i < cleaned.length()) {
          multiplier += c;
          i++;
          if (i < cleaned.length()) {
            c = cleaned.charAt(i);
          }
        }
        int mult = Integer.parseInt(multiplier);
        String baseToken = "," + token.substring(token.lastIndexOf(',') + 1);
        for (int j = 1; j < mult; j++) {
          token += baseToken;
        }

        if (!group) {
          tokens.addAll(token.split(","));
          token = "";
        } else if (c == ')') {
          i--;
        } else {
          token += ',';
        }
      } else {
        token += c;
      }
    }
    tokens.add(token);
    return tokens;
  }

  private Array<Mob> spawnNext() {
    if (this.goalCol == Integer.MIN_VALUE || this.goalRow == Integer.MIN_VALUE) {
      return null;
    }
    Array<Mob> mobs = new Array<Mob>();
    Wave wave = waves.get(waveNumber);
    if (wave.mobs.get(tick) != null) {
      for (int i = 0; i < wave.mobs.get(tick).size; i++) {
        int mobType = wave.mobs.get(tick).get(i);
        if (mobType > 0) {
          mobType--;
          // TODO(baptr): jitter time or position offset slightly so
          // they spawn as a pack, not on top of eachother.
          Mob m =
              new Mob(
                  MobType.values()[mobType],
                  MapUtils.getWorldX(col, row),
                  MapUtils.getWorldY(col, row));
          mobs.add(m);

          if (!planners.containsKey(m.unitType)) {
            planners.put(m.unitType, new PathPlanner(m.unitType));
          }
          PathPlanner planner = planners.get(m.unitType);

          Array<Node> path = planner.findPath(col, row, goalCol, goalRow, m);
          Gdx.app.log(
              Darkshaft.LOG,
              "Path length: "
                  + ((path == null) ? "NULL" : "" + path.size)
                  + " after "
                  + planner.getIterations()
                  + " steps");
        }
      }
    } else if (TargetHelper.getNumMobs() == 0) {
      // TODO(baptr): Seems like an awkward place for this logic.
      Gdx.app.log(Darkshaft.LOG, "Wave " + waveNumber + " complete, starting next wave.");
      setWave(waveNumber + 1);
    }

    if (mobs.size > 0) {
      TargetHelper.addTargets(mobs);
    }

    return mobs;
  }

  public void setEnd(int col, int row) {
    this.goalCol = col;
    this.goalRow = row;
  }

  public void setLocation(int col, int row) {
    this.col = col;
    this.row = row;
  }

  public void sortWaves() {
    this.waves.sort();
  }

  public void invalidatePaths() {
    for (PathPlanner pp : planners.values()) {
      pp.invalidatePaths();
    }
  }

  private class Wave implements Comparable<Wave> {
    int number;
    HashMap<Integer, Array<Integer>> mobs; // TODO IntMap
    float spawnRate;

    public Wave(int number, float spawnRate) {
      this.number = number;
      this.mobs = new HashMap<Integer, Array<Integer>>();
      this.spawnRate = spawnRate;
    }

    public void addTick(Integer tick, Array<Integer> mobs) {
      this.mobs.put(tick, mobs);
    }

    public float getSpawnRate() {
      return spawnRate;
    }

    @Override
    public int compareTo(Wave o) {
      if (this.number < o.number) {
        return -1;
      }

      if (this.number > o.number) {
        return 1;
      }
      return 0;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Float.floatToIntBits(number);
      result = prime * result + Float.floatToIntBits(number);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      Wave other = (Wave) obj;
      if (Float.floatToIntBits(number) != Float.floatToIntBits(other.number)) return false;
      if (Float.floatToIntBits(number) != Float.floatToIntBits(other.number)) return false;
      return true;
    }
  }
}
