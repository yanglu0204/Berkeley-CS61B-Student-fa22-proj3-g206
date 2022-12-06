package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;
import org.junit.jupiter.api.ClassOrderer;

import java.awt.*;
import java.util.Random;

import static byow.TileEngine.Tileset.*;

public class Engine {
    TERenderer ter;
    /* Feel free to change the width and height. */
    public int WIDTH = 40;
    public int HEIGHT = 30;
    private InputSource input;
    private Random rd;
    private boolean gameOver;
    private TETile[][] world = new TETile[WIDTH][HEIGHT];
    private int[] lst;
    private Generator generator;
    private static final String[] ENCOURAGEMENT = {"You got this IKUN!", "Nice Job IKUN!", "You can do it IKUN!"};


    private int chicken;
    private String s;

    public Engine(){
        rd = new Random();
        ter = new TERenderer();
        ter.initialize(WIDTH,HEIGHT);
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        rendererScreen();
        StdDraw.enableDoubleBuffering();
        searchWord();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        rendererScreen();
        StdDraw.pause(1000);
        String lower = input.toLowerCase();
        String seed = searchSeed(lower);
        Long number = Long.parseLong(seed);
        String command = searchCommand(lower, seed.length() + 1);
        Generator g = new Generator(new Random(number), WIDTH, HEIGHT);
        TERenderer renderer = new TERenderer();
        renderer.initialize(WIDTH,HEIGHT);
        TETile[][] finalWorld = g.generateWorld();
        renderer.renderFrame(finalWorld);
        return finalWorld;


    }
    private String searchCommand(String ss, int i) {
        String type = "";
        for (int a = i; a < ss.length(); a += 1) {
            if (ss.charAt(i) - ':' == 0 && a != ss.length() - 1) {
                if (ss.charAt(1 + a) == 'q' || ss.charAt(1 + a) == 'Q') {
                    break;
                }
            }
            type += ss.charAt(a);
        }
        return type;
    }
    private String searchSeed(String ss) {
        String type = "";
        for (int i = 0; i < ss.length(); i += 1) {
            if (ss.charAt(i) == 'n' || ss.charAt(i) == 'N') {
                continue;
            }
            else if (ss.charAt(i) == 's' || ss.charAt(i) == 'S') {
                break;
            }
            else {
                type += ss.charAt(i);
            }
        }
        return type;
    }

    private void searchWord() {
        s = "";
        boolean search = true;
        while (search) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'n') {
                    interactMenu();
                }
                if (Character.isDigit(c)) {
                    s += c;
                    interactMenu();
                }
                if (c == 'p') {
                    search = false;
                    Long number = Long.parseLong(s);
                    generator = new Generator(new Random(number), WIDTH, HEIGHT);
                    ter = new TERenderer();
                    ter.initialize(WIDTH,HEIGHT);
                    TETile[][] finalWorld = generator.generateWorld();
                    ter.renderFrame(finalWorld);
                    walk(finalWorld);
                }
            }
        }
    }

    public void walk(TETile[][] world) {
        gameOver = false;
        chicken = 0;
        lst = new int[2];
        lst[0] = generator.findIkun()[0];
        lst[1] = generator.findIkun()[1];
        while (!this.gameOver) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'a') {
                    if (world[lst[0] - 1][lst[1]].equals(CHICKEN)) {
                        chicken += 1;
                    }
                    if (chicken == 6) {
                        gameOver = true;
                        endGame();
                        StdDraw.show();
                    }
                    if (world[lst[0] - 1][lst[1]] != WALL) {
                        world[lst[0] - 1][lst[1]] = AVATAR;
                        world[lst[0]][lst[1]] = FLOOR;
                        lst[0] -= 1;
                        ter.renderFrame(world);
                    } else {
                        world[lst[0]][lst[1]] = AVATAR;
                    }
                }
                if (c == 'w') {
                    if (world[lst[0]][lst[1] + 1].equals(CHICKEN)) {
                        chicken += 1;
                    }
                    if (chicken == 6) {
                        gameOver = true;
                        endGame();
                        StdDraw.show();
                    }
                    if (world[lst[0]][lst[1] + 1] != WALL) {
                        world[lst[0]][lst[1] + 1] = AVATAR;
                        world[lst[0]][lst[1]] = FLOOR;
                        lst[1] += 1;
                        ter.renderFrame(world);
                    } else {
                        world[lst[0]][lst[1]] = AVATAR;
                    }
                }
                if (c == 'd') {
                    if (world[lst[0] + 1][lst[1]].equals(CHICKEN)) {
                        chicken += 1;
                    }
                    if (chicken == 6) {
                        gameOver = true;
                        endGame();
                        StdDraw.show();
                    }
                    if (world[lst[0] + 1][lst[1]] != WALL) {
                        world[lst[0] + 1][lst[1]] = AVATAR;
                        world[lst[0]][lst[1]] = FLOOR;
                        lst[0] += 1;
                        ter.renderFrame(world);
                    } else {
                        world[lst[0]][lst[1]] = AVATAR;
                    }
                }
                if (c == 's') {
                    if (world[lst[0]][lst[1] - 1].equals(CHICKEN)) {
                        chicken += 1;
                    }
                    if (chicken == 6) {
                        gameOver = true;
                        endGame();
                        StdDraw.show();
                    }
                    if (world[lst[0]][lst[1] - 1] != WALL) {
                        world[lst[0]][lst[1] - 1] = AVATAR;
                        world[lst[0]][lst[1]] = FLOOR;
                        lst[1] -= 1;
                        ter.renderFrame(world);
                    } else {
                        world[lst[0]][lst[1]] = AVATAR;
                    }
                }
            }
        }
    }

    private void interactMenu() {
        StdDraw.clear(Color.pink);
        Font font = new Font("Serif", Font.BOLD, 36);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);

        Font menu = new Font("Serif", Font.BOLD, 24);
        StdDraw.setFont(menu);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT - 20, "Seed: " + s);
        StdDraw.text(WIDTH / 2, HEIGHT - 22, "Press p to start game");
        StdDraw.show();

    }

    public void rendererScreen(){
        StdDraw.clear(Color.pink);
        Font font = new Font("Serif", Font.BOLD, 36);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT - 10,
                "CS61B: THE GAME");

        Font menu = new Font("Serif", Font.BOLD, 24);
        StdDraw.setFont(menu);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT - 20, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT - 22, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT - 24, "Quit (Q)");
        StdDraw.show();

    }

    public void endGame(){
        StdDraw.clear(Color.pink);
        Font font = new Font("Serif", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT - 10, "Congrats! " +
                "You helped IKUN to find all his chickens! 💗");
        StdDraw.show();
        StdDraw.pause(300000);

    }
}