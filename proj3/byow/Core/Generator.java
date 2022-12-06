package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class Generator {
    private Random random;
    private ArrayList<Room> Rooms;
    private TETile[][] world;

    private int WIDTH;

    private int HEIGHT;
    private int[] lst;

    private static class Room {
        int W;
        int H;
        int XCoord;
        int YCoord;

        int Dis;

        Room(int xCoord, int yCoord, int w, int h, int dis) {
            W = w;
            H = h;
            XCoord = xCoord;
            YCoord = yCoord;
            Dis = dis;
        }

        public boolean isInside(int x, int y) {
            if (x >= this.XCoord && x < this.XCoord + this.W && y >= this.YCoord && y < this.YCoord + this.H) {
                return true;
            }
            return false;
        }

    }

    public Generator(Random random, int WIDTH, int HEIGHT) {
        this.random = random;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        this.Rooms = new ArrayList<>();
        this.world = new TETile[WIDTH][HEIGHT];
    }

    public TETile[][] generateWorld() {
        this.world = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                this.world[i][j] = Tileset.NOTHING;
            }
        }
        this.generateRoom(RandomUtils.uniform(random, 30, 40));
        this.sortRooms();
        this.makeHallways();
        this.makeWall();
        this.makechicks();
        makeikun();
        return world;
    }

    public void generateRoom(int numRoom) {
        for (int i = 0; i < numRoom; i++) {
            int width = RandomUtils.uniform(random, WIDTH / 7 - 3) + 3;
            int height = RandomUtils.uniform(random, HEIGHT / 7 - 3) + 3;
            int fx = RandomUtils.uniform(random, WIDTH - 1 - width) + 1;
            int fy = RandomUtils.uniform(random, HEIGHT - 1 - height) + 1;
            int distance = (int) Math.sqrt(Math.pow(fx + width / 2 - WIDTH / 2, 2) + Math.pow(fy + height / 2 - HEIGHT / 2, 2));

            Room newRoom = new Room(fx, fy, width, height, distance);
            if (canPlace(newRoom, Rooms)) {
                Rooms.add(newRoom);
            }
        }
        for (Room r : Rooms) {
            for (int i = r.XCoord; i < r.XCoord + r.W; i++) {
                for (int j = r.YCoord; j < r.YCoord + r.H; j++) {
                    world[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    public void sortRooms() {
        Collections.sort(Rooms, new Comparator<Room>() {
            @Override
            public int compare(Room o1, Room o2) {
                return o1.Dis - o2.Dis;
            }
        });

    }

    public boolean canPlace(Room room, ArrayList<Room> rooms) {
        if (room.XCoord + room.W < WIDTH && room.YCoord + room.H < HEIGHT) {
            for (Room r : rooms) {
                if (r.XCoord == room.XCoord && r.YCoord == room.YCoord && r.W == room.W && r.H == room.H) {
                    return false;
                } else if (room.isInside(r.XCoord, r.YCoord)) {
                    return false;
                } else if (room.isInside(r.XCoord + r.W - 1, r.YCoord)) {
                    return false;
                } else if (room.isInside(r.XCoord + r.W - 1, r.YCoord + r.H - 1)) {
                    return false;
                } else if (room.isInside(r.XCoord, r.YCoord + r.H - 1)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private void connect(Room r1, Room r2) {
        int minX = Math.min(r1.XCoord, r2.XCoord);
        int maxX = Math.max(r1.XCoord, r2.XCoord);
        int minY = Math.min(r1.YCoord, r2.YCoord);
        int maxY = Math.max(r1.YCoord, r2.YCoord);

        for (int i = minY; i <= maxY; i++) {
            world[r1.XCoord][i] = Tileset.FLOOR;
        }
        for (int i = minX; i <= maxX; i++) {
            world[i][r2.YCoord] = Tileset.FLOOR;
        }
    }

    public void makeHallways() {
        for (int i = 0; i < Rooms.size() - 1; i++) {
            connect(Rooms.get(i), Rooms.get(i + 1));
        }
    }


    public void makeWall() {
        for (int i = 1; i < WIDTH - 1; i++) {
            for (int j = 1; j < HEIGHT - 1; j++) {
                if (world[i + 1][j].equals(Tileset.FLOOR)
                        || world[i - 1][j].equals(Tileset.FLOOR)
                        || world[i][j + 1].equals(Tileset.FLOOR)
                        || world[i][j - 1].equals(Tileset.FLOOR)
                        || world[i + 1][j + 1].equals(Tileset.FLOOR)
                        || world[i - 1][j - 1].equals(Tileset.FLOOR)
                        || world[i - 1][j + 1].equals(Tileset.FLOOR)
                        || world[i + 1][j - 1].equals(Tileset.FLOOR)
                ) {
                    if (world[i][j] != Tileset.FLOOR) {
                        world[i][j] = Tileset.WALL;
                    }
                }
            }
        }
        for (int j = 0; j < HEIGHT; j++) {
            if (world[1][j].equals(Tileset.FLOOR)) {
                world[0][j] = Tileset.WALL;
            }
        }
        for (int i = 0; i < WIDTH; i++) {
            if (world[i][1].equals(Tileset.FLOOR)) {
                world[i][0] = Tileset.WALL;
            }
        }
        for (int j = 0; j < HEIGHT; j++) {
            if (world[WIDTH - 2][j].equals(Tileset.FLOOR)) {
                world[WIDTH - 1][j] = Tileset.WALL;
            }
        }
        for (int i = 0; i < WIDTH; i++) {
            if (world[i][HEIGHT - 2].equals(Tileset.FLOOR)) {
                world[i][HEIGHT - 1] = Tileset.WALL;
            }
        }

    }

    public void makechicks() {
        int num = 0;
        while (num < 6) {
            int x = RandomUtils.uniform(random, WIDTH);
            int y = RandomUtils.uniform(random, HEIGHT);
            if (world[x][y].equals(Tileset.FLOOR)) {
                world[x][y] = Tileset.CHICKEN;
                num += 1;
            }
        }
    }

    public void makeikun() {
        int x = RandomUtils.uniform(random, WIDTH);
        int y = RandomUtils.uniform(random, HEIGHT);
        if (world[x][y].equals(Tileset.FLOOR)) {
            world[x][y] = Tileset.AVATAR;
            lst = new int[2];
            lst[0] = x;
            lst[1] = y;
        }
        else {
            makeikun();
        }
    }
    public int[] findIkun() {
        return lst;
    }
}
