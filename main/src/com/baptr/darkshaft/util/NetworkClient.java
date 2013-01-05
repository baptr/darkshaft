package com.baptr.darkshaft.util;

import java.io.IOException;

import com.badlogic.gdx.Gdx;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

import com.baptr.darkshaft.Darkshaft;
import com.baptr.darkshaft.util.Network;
import com.baptr.darkshaft.util.Network.*;
import com.baptr.darkshaft.gfx.Avatar;
import com.baptr.darkshaft.gfx.Tower;
import com.baptr.darkshaft.screen.DemoScreen;

public class NetworkClient {
    Client client;
    MoveAvatar lastPosition;
    final DemoScreen screen;

    public NetworkClient(DemoScreen s) {
        this.screen = s;

        client = new Client();
        client.start();

        lastPosition = new MoveAvatar();

        Network.register(client);

        client.addListener(new ThreadedListener(new Listener() {
            private int myId;

            public void connected(Connection c) {
            }

            public void received(Connection c, Object o) {
                if(o instanceof YourId) {
                    myId = ((YourId)o).id;
                    System.out.printf("My player id is %d\n", myId);
                }
                if(o instanceof NewPlayer) {
                    Player p = ((NewPlayer)o).player;
                    System.out.printf("Player %s connected (id %d)\n",
                            p.name, p.id);
                    if(p.id != myId) {
                        screen.addRemoteAvatar(p.name, p.id);
                        screen.updateRemoteAvatar(p.id, p.x, p.y);
                    }
                }
                if(o instanceof UpdateAvatar) {
                    Player p = ((UpdateAvatar)o).player;
                    if(p.id != myId) {
                        Gdx.app.debug(Darkshaft.LOG, "Player " + p.name +
                                " moved to (" + p.x + "," + p.y + ")");
                        screen.updateRemoteAvatar(p.id, p.x, p.y);
                    }
                }
                if(o instanceof RemovePlayer) {
                    Player p = ((RemovePlayer)o).player;
                    System.out.printf("Player %s disconnected (id %d)\n",
                            p.name, p.id);
                    if(p.id != myId)
                        screen.removeRemoteAvatar(p.id);
                }
                if(o instanceof TowerPlaced) {
                    TowerPlaced t = (TowerPlaced)o;
                    if(t.player.id != myId)
                        screen.addRemoteTower(t.player.id, t.type,
                                t.col, t.row);
                }
            }
        }));
    }

    public void connect(String host, String playerName) throws IOException {
        client.connect(Network.CONNECT_TIMEOUT, host, Network.port);

        Login login = new Login();
        login.name = playerName;

        client.sendTCP(login);
    }

    public void sendPlayerUpdate(Avatar avatar) {
        if(!client.isConnected()) return;
        float x = avatar.getX();
        float y = avatar.getY();
        if(lastPosition.x == x && lastPosition.y == y) return;
        lastPosition.x = x;
        lastPosition.y = y;
        client.sendTCP(lastPosition);
    }

    public void sendPlaceTower(Tower t) {
        if(!client.isConnected()) return;
        PlaceTower msg = new PlaceTower();
        msg.type = t.getTowerType();
        msg.row = t.getRow();
        msg.col = t.getCol();
        client.sendTCP(msg);
    }
}
