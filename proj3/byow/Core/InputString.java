package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.util.Random;

public class InputString {
    public static void main(String[] args){
        Random rand = new Random(1112);
        Generator gan = new Generator(rand, 70, 40);
        TERenderer renderer = new TERenderer();
        renderer.initialize(70,40);
        TETile[][] tiles = gan.generateWorld();
        renderer.renderFrame(tiles);

    }
}
