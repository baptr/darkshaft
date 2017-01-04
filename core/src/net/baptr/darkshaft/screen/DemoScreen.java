package net.baptr.darkshaft.screen;

import com.badlogic.gdx.utils.IntMap;
import java.io.IOException;
import net.baptr.darkshaft.Darkshaft;
import net.baptr.darkshaft.core.Spawner;
import net.baptr.darkshaft.gfx.*;
import net.baptr.darkshaft.gfx.Tower.TowerType;
import net.baptr.darkshaft.util.NetworkClient;
import net.baptr.darkshaft.util.NetworkServer;

public class DemoScreen extends GameScreen {

  NetworkServer server;
  NetworkClient client;
  IntMap<Avatar> remoteAvatars;

  public DemoScreen(Darkshaft game, boolean selfServe) {
    super(game, "demo.tmx");

    camera.translate(-400, -400, 0);
    camera.update();

    for (Spawner spawn : this.spawners) {
      spawn.setWave(0);
    }

    remoteAvatars = new IntMap<Avatar>();
    client = new NetworkClient(this);

    if (selfServe) {
      try {
        server = new NetworkServer();
        initConnection("localhost", "Local Player");
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  void initConnection(String host, String name) throws IOException {
    client.connect(host, name);
  }

  public void addRemoteAvatar(String name, int id, float x, float y) {
    Avatar av = new Avatar(0f, 0f);
    av.setOwner(name);
    av.setPosition(x, y);
    remoteAvatars.put(id, av);
  }

  public void updateRemoteAvatar(int id, float x, float y) {
    Avatar av = remoteAvatars.get(id);
    av.setPosition(x, y);
  }

  public void removeRemoteAvatar(int id) {
    remoteAvatars.remove(id);
  }

  public void addRemoteTower(int playerId, TowerType type, int col, int row) {
    Tower t = new Tower(type, col, row);
    super.addDefense(t);
  }

  @Override
  public void addDefense(Defense d) {
    if (!defenses.contains(d, false)) {
      client.sendPlaceTower((Tower) d);
    }
    super.addDefense(d);
  }

  @Override
  public void show() {
    super.show();
  }

  @Override
  public void render(float delta) {
    super.render(delta);

    batch.begin();
    for (Avatar av : remoteAvatars.values()) {
      if (av != null) av.draw(batch);
    }
    batch.end();
    client.sendPlayerUpdate(frank);
  }

  @Override
  public void dispose() {
    super.dispose();
  }
}
