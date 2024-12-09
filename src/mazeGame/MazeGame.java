package mazeGame;

import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*;

public class MazeGame extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;
    private final int mazeWidth = 20;
    private final int mazeHeight = 20;
    private Thread gameThread;
    private boolean isRunning;
    private BufferedImage gameFrame;
    private int[] pixels;
    private ArrayList<Texture> textures;
    private Camera camera;
    private Screen screen;

    // Maze layout with an exit marked by 9
    public static final int[][] mapLayout = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public MazeGame() {
        setupGame();
        setupWindow();
    }

    private void setupGame() {
        gameThread = new Thread(this);
        gameFrame = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) gameFrame.getRaster().getDataBuffer()).getData();

        textures = new ArrayList<>();
        textures.add(Texture.woodTexture);
        textures.add(Texture.brickTexture);
        textures.add(Texture.blueStoneTexture);
        textures.add(Texture.stoneTexture);

        camera = new Camera(1.5, 1.5, 1, 0, 0, -0.66);
        screen = new Screen(mapLayout, mazeWidth, mazeHeight, textures, 640, 480);

        addKeyListener(camera);
    }

    private void setupWindow() {
        setSize(640, 480);
        setResizable(false);
        setTitle("3D Maze Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setLocationRelativeTo(null);
        setVisible(true);
        startGame();
    }

    private synchronized void startGame() {
        isRunning = true;
        gameThread.start();
    }

    public synchronized void stopGame() {
        isRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void renderFrame() {
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.drawImage(gameFrame, 0, 0, gameFrame.getWidth(), gameFrame.getHeight(), null);
        bufferStrategy.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double frameRate = 60.0;
        final double nsPerFrame = 1_000_000_000.0 / frameRate;
        double delta = 0;

        requestFocus();

        while (isRunning) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / nsPerFrame;
            lastTime = currentTime;

            while (delta >= 1) {
                screen.update(camera, pixels);
                camera.update(mapLayout);
                if (camera.xPos >= 19 && camera.yPos >= 15) {
                    showCongratulations();
                    stopGame();
                }
                delta--;
            }

            renderFrame();
        }
    }

    private void showCongratulations() {
        JOptionPane.showMessageDialog(this, "Congratulations! You found the exit!", "Victory", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new MazeGame();
    }
}

