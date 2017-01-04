package net.baptr.darkshaft.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.baptr.darkshaft.gfx.Tower.TowerType;
import net.baptr.darkshaft.screen.GameScreen;

public class GameUI {
  Table buttons;
  final GameScreen gameScreen;

  static final Color HIT_COLOR = Color.BLUE;

  public GameUI(GameScreen screen, Stage stage) {
    this.gameScreen = screen;
    Skin skin = screen.getSkin();
    buttons = new Table(skin);
    buttons.setFillParent(false);
    buttons.pad(1.0f);

    final Label moveButton = (Label) buttons.add("Move").space(3.0f).getActor();
    buttons.add("Basic").space(3.0f);
    buttons.add("Fire").space(3.0f);
    buttons.add("Water").space(3.0f);
    buttons.add("Spirit").space(3.0f);

    moveButton.setColor(HIT_COLOR);

    buttons.pack();
    buttons.addListener(
        new ClickListener() {
          Label lastHit = moveButton;

          public void clicked(InputEvent e, float x, float y) {
            Actor hit = buttons.hit(x, y, true);
            if (hit instanceof Label) {
              if (lastHit != null) {
                lastHit.setColor(1, 1, 1, 1);
              }
              lastHit = (Label) hit;
              lastHit.setColor(GameUI.HIT_COLOR);
              String label = lastHit.getText().toString();
              if (label.equals("Move")) gameScreen.setTowerMarker(TowerType.NONE);
              if (label.equals("Basic")) gameScreen.setTowerMarker(TowerType.BASIC);
              if (label.equals("Fire")) gameScreen.setTowerMarker(TowerType.FIRE);
              if (label.equals("Water")) gameScreen.setTowerMarker(TowerType.WATER);
              if (label.equals("Spirit")) gameScreen.setTowerMarker(TowerType.SPIRIT);
              Gdx.app.log("GameUI", "" + hit);
            }
          }
        });

    stage.addActor(buttons);
  }
}
