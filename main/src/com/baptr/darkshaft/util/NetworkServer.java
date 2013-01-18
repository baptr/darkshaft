package com.baptr.darkshaft.util;

import java.io.IOException;
import java.util.HashSet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.baptr.darkshaft.util.Network;
import com.baptr.darkshaft.util.Network.*;

public class NetworkServer {
    Server server;
    HashSet<Player> players = new HashSet<Player>();
    // need a list of towers, and mobs to send for sync
    // Do we get from game, or keep here?
    HashSet<TowerPlaced> towers = new HashSet<TowerPlaced>();

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

                    // Tell the new player the game state
                    syncTo(connection);

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
                    towers.add(update);
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

    /** Send a Sync message containing full game state to a given connection.
     */
    void syncTo(PlayerConnection conn) {
        Sync msg = new Sync();
        msg.players = players.toArray(new Player[players.size()]);
        msg.towers = towers.toArray(new TowerPlaced[towers.size()]);
        msg.step = 0;
        server.sendToTCP(conn.getID(), msg);
    }

    public class PlayerConnection extends Connection {
        Player player;
    }
}
