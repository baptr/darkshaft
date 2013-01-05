package com.baptr.darkshaft.screen;

import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
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
                    DemoScreen playScreen = new DemoScreen(game, false);
                    playScreen.initConnection(serverField.getText(),
                            portField.getText());
                    game.setScreen(playScreen);
                } catch(java.net.UnknownHostException ue) {
                    new Dialog("Unknown Host", getSkin())
                            .text("Unable to resolve host: " + ue.getMessage())
                            .button("Ok").show(stage);
                    Gdx.app.log(Darkshaft.LOG, "Unknown host: " + ue.getMessage());
                } catch(java.io.IOException ioe) {
                    new Dialog("Connection Error", getSkin())
                            .text(ioe.getMessage())
                            .button("Ok").show(stage);
                    Gdx.app.log(Darkshaft.LOG, "Connection error: " + ioe.getMessage());
                }
            }
        });

        table.add(serverField).minWidth(150f);
        table.add(portField).minWidth(50f);
        table.add(connectButton);
    }
}
