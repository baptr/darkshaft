package com.baptr.darkshaft.screen;

import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.screen.AbstractScreen;

public class ConnectScreen extends AbstractScreen {
    public ConnectScreen(Darkshaft ds) {
        super(ds);

        Table table = new Table(getSkin());
        table.setFillParent(true);
        stage.addActor(table);

        final List serverList = new List(new String[] {}, getSkin());
        final TextField serverField = new TextField("localhost", getSkin());
        final TextField nameField = new TextField("name", getSkin());

        loadServerList(serverList, serverField);
        loadName(nameField);

        serverList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                serverField.setText(((List)actor).getSelection());
            }
        });

        TextButton connectButton = new TextButton("Connect", getSkin());
        connectButton.addListener(new ClickListener() {
            private DemoScreen playScreen;
            public void clicked(InputEvent e, float x, float y) {
                try {
                    InetAddress addr = InetAddress.getByName(
                            serverField.getText());
                    if(playScreen == null)
                        playScreen = new DemoScreen(game, false);
                    playScreen.initConnection(serverField.getText(),
                            nameField.getText());
                    serverField.getOnscreenKeyboard().show(false);
                    saveServerList(serverList, serverField);
                    saveName(nameField);
                    game.setScreen(playScreen);
                } catch(java.net.UnknownHostException ue) {
                    new Dialog("Unknown Host", getSkin())
                            .text("Unable to resolve host: " +
                            ue.getMessage())
                            .button("Ok").show(stage);
                    Gdx.app.log(Darkshaft.LOG, "Unknown host: " +
                            ue.getMessage());
                } catch(java.io.IOException ioe) {
                    new Dialog("Connection Error", getSkin())
                            .text(ioe.getMessage())
                            .button("Ok").show(stage);
                    Gdx.app.log(Darkshaft.LOG, "Connection error: " +
                            ioe.getMessage());
                }
            }
        });

        table.add("Name: ").left();
        table.add(nameField).minWidth(50f).spaceBottom(10).row();
        table.add("Recent Servers").colspan(2).left().row();
        table.add(serverList).colspan(2).fillX().spaceBottom(10).row();
        table.add("Server: ").minWidth(75f).left();
        table.add(serverField).minWidth(200f).row();
        table.add(connectButton).colspan(2).right();
    }

    private void loadName(TextField nameField) {
        String name = preferences.getString("player_name");
        if(!name.isEmpty()) {
            nameField.setText(name);
        }
    }

    private void saveName(TextField nameField) {
        preferences.putString("player_name", nameField.getText());
    }

    private void loadServerList(List serverList, TextField serverField) {
        String save = preferences.getString("server_list");
        Json json = new Json();
        String[] servers = json.fromJson(String[].class, save);
        if(servers != null) {
            serverList.setItems(servers);
            serverField.setText(servers[0]);
        } else {
            serverList.setItems(new String[] { "localhost",
                    "bpu.doesntexist.org" });
            serverField.setText("localhost");
        }
    }

    private void saveServerList(List serverList, TextField serverField) {
        String[] list = serverList.getItems();
        String current = serverField.getText();
        boolean add = true;
        for(int idx = 0; idx < list.length; idx++) {
            if(current.equals(list[idx])) {
                add = false;
                String tmp = list[0];
                list[0] = list[idx];
                list[idx] = tmp;
                break;
            }
        }
        if(add) {
            String[] res = new String[list.length+1];
            res[0] = current;
            int i = 1;
            for(String s : list) {
                res[i++] = s;
            }
            list = res;
        }
        Json json = new Json();
        String save = json.toJson(list);
        preferences.putString("server_list", save);
    }
}
