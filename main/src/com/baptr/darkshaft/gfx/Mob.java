package com.baptr.darkshaft.gfx;

import com.badlogic.gdx.Gdx;
import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.core.Entity.*;
import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.util.TargetHelper;
import java.util.Random;

public class Mob extends Unit {

  MobType type;
  float currentLife;
  String name;

  public enum MobType {
    RED("Characters/Mobs/Mob1", 100, UnitType.BASIC, 100f),
    GREEN("Characters/Mobs/Mob2", 150, UnitType.WATER, 200f),
    DRAGON("Characters/Mobs/Dragon", 60, UnitType.FLYER, 150f),
    NONE(null, 0, UnitType.BASIC, 0f);
    private float speed, baseLife;
    private String regionName;
    private UnitType unitType;

    MobType(String name, float speed, UnitType unitType, float baseLife) {
      this.regionName = name;
      this.speed = speed;
      this.unitType = unitType;
      this.baseLife = baseLife;
    }
  }

  public Mob(MobType mobType, float x, float y) {
    super(x, y, mobType.regionName);
    type = mobType;
    // TODO(baptr): Mobs need random speeds because they clump up when they
    // spawn...  eventually they will try and not clump up and we can
    // remove this
    Random r = new Random();
    this.speed = mobType.speed * r.nextFloat();
    this.unitType = type.unitType;
    xOffset = -32;
    yOffset = -16;
    this.currentLife = mobType.baseLife;
  }

  @Override
  public boolean update(float delta) {
    super.update(delta);
    if (currentPath == null) {
    } else if (currentPath.size == 0) {
      Gdx.app.log(Darkshaft.LOG, name + " reached the goal");
      TargetHelper.removeTarget(this);
      return true;
    } else if (MapUtils.isDefense(currentPath.first())) {
      // TODO(baptr): Actually stop to fight it.
      Gdx.app.log(Darkshaft.LOG, name + " fighting tower");
    }
    return false;
  }

  // Returns true if this mob was killed
  public boolean shoot(float rawDamage) {
    this.currentLife -= rawDamage;
    Gdx.app.log(
        Darkshaft.LOG,
        name + " shot for " + rawDamage + ". (" + currentLife + "/" + this.type.baseLife + ")");
    if (this.currentLife <= 0) {
      Gdx.app.log(Darkshaft.LOG, name + " was killed!");
      TargetHelper.removeTarget(this);
      return true;
    }

    return false;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public boolean isAlive() {
    return currentLife > 0;
  }
}
