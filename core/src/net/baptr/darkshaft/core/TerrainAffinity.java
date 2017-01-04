package net.baptr.darkshaft.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import java.util.EnumMap;
import net.baptr.darkshaft.Darkshaft;
import net.baptr.darkshaft.core.Entity.*;

/**
 * Represents a unit's speed modifier over a given type of terrain. Each unit type may have
 * modifiers that multiply the weight of a matching tile type (terrain widget or tower). A
 * multiplier of 0 is expected to set the weight to 1.
 *
 * @author baptr
 */
public class TerrainAffinity extends EnumMap<TileType, Float> {
  public static EnumMap<UnitType, TerrainAffinity> affinityMap =
      new EnumMap<UnitType, TerrainAffinity>(UnitType.class);

  private TerrainAffinity() {
    super(TileType.class);
    // Default to 1x mapping
    for (TileType t : TileType.values()) {
      this.put(t, 1.0f);
    }
  }

  public int getWeight(TileType type, int baseWeight) {
    int weight = (int) (baseWeight * get(type));
    if (weight == 0) {
      return 1;
    } else {
      return weight;
    }
  }

  public static void load(String file) {
    JsonReader reader = new JsonReader();
    JsonValue o = reader.parse(Gdx.files.internal(file));
    for (JsonValue unit : o) {
      TerrainAffinity affinity = new TerrainAffinity();
      UnitType unitType = UnitType.valueOf(unit.name);
      for (JsonValue tile : unit) {
        TileType tileType = TileType.valueOf(tile.name);
        float multiplier = tile.asFloat();
        affinity.put(tileType, multiplier);
      }
      affinityMap.put(unitType, affinity);
    }
    Gdx.app.log(Darkshaft.LOG, "AffinityMap: " + affinityMap);
  }

  public static TerrainAffinity forUnitType(UnitType type) {
    return affinityMap.get(type);
  }
}
