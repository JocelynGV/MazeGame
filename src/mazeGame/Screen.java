package mazeGame;

import java.awt.*;
import java.util.ArrayList;

public class Screen {
    private int[][] map;
    private int mapWidth, mapHeight, screenWidth, screenHeight;
    private ArrayList<Texture> textures;

    public Screen(int[][] map, int mapWidth, int mapHeight, ArrayList<Texture> textures, int screenWidth, int screenHeight) {
        this.map = map;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.textures = textures;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void update(Camera camera, int[] pixels) {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }

        for (int x = 0; x < screenWidth; x++) {
            double cameraX = 2 * x / (double) screenWidth - 1;
            double rayDirX = camera.xDir + camera.xPlane * cameraX;
            double rayDirY = camera.yDir + camera.yPlane * cameraX;

            int mapX = (int) camera.xPos;
            int mapY = (int) camera.yPos;

            double sideDistX, sideDistY;
            double deltaDistX = Math.abs(1 / rayDirX);
            double deltaDistY = Math.abs(1 / rayDirY);
            double perpWallDist;

            int stepX, stepY;

            boolean hit = false;
            int side = 0;

            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (camera.xPos - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - camera.xPos) * deltaDistX;
            }
            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (camera.yPos - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - camera.yPos) * deltaDistY;
            }

            while (!hit) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                if (map[mapY][mapX] > 0) hit = true;
            }

            if (side == 0) {
                perpWallDist = (mapX - camera.xPos + (1 - stepX) / 2) / rayDirX;
            } else {
                perpWallDist = (mapY - camera.yPos + (1 - stepY) / 2) / rayDirY;
            }

            int lineHeight = (int) (screenHeight / perpWallDist);
            int drawStart = -lineHeight / 2 + screenHeight / 2;
            if (drawStart < 0) drawStart = 0;
            int drawEnd = lineHeight / 2 + screenHeight / 2;
            if (drawEnd >= screenHeight) drawEnd = screenHeight - 1;

            int textureIndex = map[mapY][mapX] - 1;
            int color = (side == 1) ? 0xAAAAAA : 0xFFFFFF;

            for (int y = drawStart; y < drawEnd; y++) {
                pixels[x + y * screenWidth] = color;
            }
        }

        drawExitSign(pixels, camera);
    }

    private void drawExitSign(int[] pixels, Camera camera) {
        int exitX = 19;
        int exitY = 15;

        if (exitX >= 0 && exitX < mapWidth && exitY >= 0 && exitY < mapHeight && map[exitY][exitX] == 9) {
            int textStartX = screenWidth / 2 - 50;
            int textStartY = screenHeight / 2 - 100;
            String exitMessage = "EXIT";
            drawText(pixels, textStartX, textStartY, exitMessage, Color.RED.getRGB());
        }
    }

    private void drawText(int[] pixels, int startX, int startY, String message, int color) {
        int fontSize = 10;
        for (int i = 0; i < message.length(); i++) {
            int x = startX + i * fontSize;
            int y = startY;
            for (int dx = 0; dx < fontSize; dx++) {
                for (int dy = 0; dy < fontSize; dy++) {
                    if ((x + dx) + (y + dy) * screenWidth < pixels.length) {
                        pixels[(x + dx) + (y + dy) * screenWidth] = color;
                    }
                }
            }
        }
    }
}

