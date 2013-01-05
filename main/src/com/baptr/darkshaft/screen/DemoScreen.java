package com.baptr.darkshaft.screen;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;

//import com.baptr.darkshaft.core.TileMapRenderer;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.util.MapUtils;
import com.baptr.darkshaft.util.NetworkClient;
import com.baptr.darkshaft.util.NetworkServer;
import com.baptr.darkshaft.gfx.*;
import com.baptr.darkshaft.gfx.Tower.TowerType;

public class DemoScreen extends GameScreen {

    NetworkServer server;
    NetworkClient client;
    IntMap<Avatar> remoteAvatars;

    public DemoScreen(Darkshaft game, boolean selfServe) {
        super(game, "demo.tmx");

        camera.translate(-400, -400, 0);
        camera.update();

        remoteAvatars = new IntMap<Avatar>();
        client = new NetworkClient(this);

        if(selfServe) {
            try {
                server = new NetworkServer();
                initConnection("localhost", "Local Player");
            } catch(IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    void initConnection(String host, String name) throws IOException{
        client.connect(host, name);
    }

    public void addRemoteAvatar(String name, int id) {
        Avatar av = new Avatar(0f, 0f, getAtlas());
        av.setOwner(name);
        remoteAvatars.put(id, av);
    }

    public void updateRemoteAvatar(int id, float x, float y) {
        Avatar av = remoteAvatars.get(id);
        av.setPosition(x, y);
    }

    public void removeRemoteAvatar(int id) {
        remoteAvatars.remove(id);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render( float delta ) {
        super.render( delta );	

        batch.begin();
        for(Avatar av : remoteAvatars.values()) {
            if(av != null) av.draw(batch);
        }
        batch.end();
        client.sendPlayerUpdate(frank);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
