package com.baptr.darkshaft.util;

import java.io.IOException;
import java.util.HashSet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import com.baptr.darkshaft.util.Network;
import com.baptr.darkshaft.util.Network.*;
import com.baptr.darkshaft.gfx.Avatar;

public class NetworkServer {
    Server server;
    HashSet<Player> players = new HashSet<Player>();

    public NetworkServer() throws IOException {
        //Log.set(Log.LEVEL_DEBUG);
        server = new Server() {
            protected Connection newConnection() {
                return new PlayerConnection();
            }
        };

        Network.register(server);

        server.addListener(new Listener() {
            public void received(Connection c, Object obj) {
                PlayerConnection connection = (PlayerConnection)c;
                Player player = connection.player;
                if(obj instanceof Login) {
                    if(player != null) return; // Already logged in
                    c.setKeepAliveTCP(4000);
                    c.setTimeout(10000);
                    player = new Player(((Login)obj).name);
                    connection.player = player;

                    YourId idMsg = new YourId();
                    idMsg.id = player.id;
                    server.sendToTCP(connection.getID(), idMsg);

                    // Tell the new player about other players
                    for(Player p : players) {
                        NewPlayer msg = new NewPlayer();
                        msg.player = p;
                        server.sendToTCP(connection.getID(), msg);
                    }

                    players.add(player);

                    NewPlayer msg = new NewPlayer();
                    msg.player = player;
                    server.sendToAllTCP(msg);
                }
                if(obj instanceof MoveAvatar) {
                    if(player == null) return; // No associated player

                    MoveAvatar msg = (MoveAvatar)obj;

                    player.x = msg.x;
                    player.y = msg.y;

                    UpdateAvatar update = new UpdateAvatar();
                    update.player = player;
                    server.sendToAllTCP(update);
                }
                if(obj instanceof PlaceTower) {
                    if(player == null) return; // No associated player

                    PlaceTower msg = (PlaceTower)obj;

                    TowerPlaced update = new TowerPlaced();
                    update.player = player;
                    update.type = msg.type;
                    update.row = msg.row;
                    update.col = msg.col;
                    server.sendToAllTCP(update);
                }
            }

            public void disconnected(Connection c) {
                PlayerConnection connection = (PlayerConnection)c;
                if(connection.player != null) {
                    players.remove(connection.player);

                    RemovePlayer msg = new RemovePlayer();
                    msg.player = connection.player;
                    server.sendToAllTCP(msg);
                }
            }
        });

        server.bind(Network.port);
        server.start();
    }

    public class PlayerConnection extends Connection {
        Player player;
    }
}
