package mazeGame;

import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Texture {
    public int[] pixels;
    private String filePath;
    public final int size;

    public Texture(String path, int dimension) {
        filePath = path;
        size = dimension;
        pixels = new int[size * size];
        loadTexture();
    }

    private void loadTexture() {
        try {
            BufferedImage image = ImageIO.read(new File(filePath));
            int width = image.getWidth();
            int height = image.getHeight();
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Texture woodTexture = new Texture("res/wood.png", 64);
    public static Texture brickTexture = new Texture("res/redbrick.png", 64);
    public static Texture blueStoneTexture = new Texture("res/bluestone.png", 64);
    public static Texture stoneTexture = new Texture("res/greystone.png", 64);
}
