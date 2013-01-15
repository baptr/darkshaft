package com.baptr.darkshaft.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import com.baptr.darkshaft.gfx.Tower.TowerType;

public class Network {
    public static final int port = 59432;
    public static final int CONNECT_TIMEOUT = 5000;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Login.class);
        kryo.register(YourId.class);
        kryo.register(NewPlayer.class);
        kryo.register(UpdateAvatar.class);
        kryo.register(MoveAvatar.class);
        kryo.register(RemovePlayer.class);
        kryo.register(PlaceTower.class);
        kryo.register(TowerPlaced.class);
        kryo.register(TowerType.class);
        kryo.register(Player.class);
        kryo.register(Sync.class);
        kryo.register(Player[].class);
        kryo.register(TowerPlaced[].class);
    }

    public static class Login {
        public String name;
    }

    public static class YourId {
        public int id;
    }

    public static class NewPlayer {
        public Player player;
    }

    // TODO Send paths instead of positions
    public static class UpdateAvatar {
        public Player player;
    }

    public static class MoveAvatar {
        public float x, y;
    }

    public static class RemovePlayer {
        public Player player;
    }

    public static class PlaceTower {
        public TowerType type;
        public int row, col;
    }

    public static class TowerPlaced {
        public Player player;
        public TowerType type;
        public int row, col;
    }


    public static class Player {
        private static int maxId = 0;

        public int id;
        public String name;

        public float x, y;

        public Player() {}

        public Player(String name) {
            this.name = name;
            this.id = maxId++;
        }
    }

    public static class Sync {
        // world step idx
        public long step;
        public Player[] players;
        // TODO Network.Tower class?
        public TowerPlaced[] towers;
    }

}
