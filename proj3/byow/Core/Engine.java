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

public class Engine {
    TERenderer ter;
    /* Feel free to change the width and height. */
    public int WIDTH = 40;
    public int HEIGHT = 30;
    private InputSource input;
    private Random rd;
    private TETile[][] world = new TETile[WIDTH][HEIGHT];

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
        input = new KeyboardInputSource();
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
        StringInputDevice SID = new StringInputDevice(input);
        this.input = SID;
        return world;

    }
    private void interactMenu(){

    }

    private void processAction(char curr) {
    }

    public void createWorld(int seed){
        rd.setSeed(seed);

        this.world = new Generator(rd,WIDTH,HEIGHT).generateWorld();
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
}
