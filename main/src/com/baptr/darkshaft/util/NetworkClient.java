package com.baptr.darkshaft.util;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

import com.baptr.darkshaft.util.Network;
import com.baptr.darkshaft.util.Network.*;
import com.baptr.darkshaft.gfx.Avatar;

public class NetworkClient {
    Client client;

    public NetworkClient() {
        client = new Client();
        client.start();

        Network.register(client);

        client.addListener(new ThreadedListener(new Listener() {
            public void connected(Connection c) {
            }

            public void received(Connection c, Object o) {
                if(o instanceof NewPlayer) {
                    Player p = ((NewPlayer)o).player;
                    System.out.printf("Player %s connected (id %d)\n",
                            p.name, p.id);
                }
                if(o instanceof UpdateAvatar) {
                }
                if(o instanceof RemovePlayer) {
                    Player p = ((RemovePlayer)o).player;
                    System.out.printf("Player %s disconnected (id %d)\n",
                            p.name, p.id);
                }
                if(o instanceof TowerPlaced) {
                }
            }
        }));
    }

    public void connect(String host, String playerName) {
        try {
            client.connect(Network.CONNECT_TIMEOUT, host, Network.port);
        } catch(IOException ex) {
            ex.printStackTrace();
        }

        Login login = new Login();
        login.name = playerName;

        client.sendTCP(login);
    }
}
