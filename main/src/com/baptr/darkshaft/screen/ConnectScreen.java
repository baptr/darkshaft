package com.baptr.darkshaft.screen;

import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.screen.AbstractScreen;

public class ConnectScreen extends AbstractScreen {
    public ConnectScreen(Darkshaft ds) {
        super(ds);

        Table table = new Table(getSkin());
        table.setFillParent(true);
        stage.addActor(table);

        final TextField serverField = new TextField("localhost", getSkin());
        final TextField portField = new TextField("port", getSkin());

        TextButton connectButton = new TextButton("Connect", getSkin());
        connectButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                try {
                    InetAddress addr = InetAddress.getByName(
                            serverField.getText());
                    game.setScreen(new DemoScreen(game,
                            serverField.getText(), portField.getText()));
                } catch(java.net.UnknownHostException ue) {
                    Gdx.app.log(Darkshaft.LOG, "Unknown host: " + ue.getMessage());
                }
            }
        });

        table.add(serverField).minWidth(150f);
        table.add(portField).minWidth(50f);
        table.add(connectButton);
    }
}
