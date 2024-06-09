import java.util.ArrayList;

import java.awt.Color;

public class Screen {
    private int[][] map;
    private int width;
    private int height;
    private ArrayList<Texture> textures;

    public Screen(int[][] map, ArrayList<Texture> textures, int width, int height){
        this.map = map;
        this.textures = textures;
        this.width = width;
        this.height = height;
    }

    /**
     * Update the screen with player's direction and position. Return the pixels to display to user
     * 
     * @param player
     * @param pixels
     * @return
     */
    public int[] update(Player player, int[] pixels){
        // grayout the screen
        for (int i = 0; i < pixels.length / 2; i++) {
            pixels[i] = Color.DARK_GRAY.getRGB();
        }
        for (int i = pixels.length / 2; i < pixels.length; i++) {
            pixels[i] = Color.GRAY.getRGB();
        }

        // raycasting
        for (int x = 0; x < width; x++) {
            // direction of ray
            double cameraX = 2 * x / (double)width - 1;
            double rayDirX = player.getxDir() + player.getxPlane() * cameraX;
            double rayDirY = player.getyDir() + player.getyPlane() * cameraX;

            int xPos = (int)player.getxPos();
            int yPos = (int)player.getyPos();

            // dist of ray from one side to the next side in map
            double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
            double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));

            // distance of ray from current position to the next side
            double sideDistX;
            double sideDistY;
            // use to define the direction of the step
            int stepX;
            int stepY;
            if(rayDirX < 0){
                stepX = -1;
                sideDistX = (player.getxPos() - xPos) * deltaDistX;
            }else{
                stepX = 1;
                sideDistX = (xPos + 1 - player.getxPos()) * deltaDistX;
            }
            if(rayDirY < 0){
                stepY = -1;
                sideDistY = (player.getyPos() - yPos) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (yPos + 1 - player.getyPos()) * deltaDistY;
            }

            // side = 0 if hit a horizon (x) or vertical (y) wall
            int side = 0;

            // find where the ray hit a wall
            boolean hitWall = false;
            while(!hitWall){
                // update position
                if(sideDistX < sideDistY){
                    sideDistX += deltaDistX;
                    xPos += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    yPos += stepY;
                    side = 1;
                }

                // check if wall is hit
                if(map[xPos][yPos] > 0){
                    hitWall = true;
                }
            }

            // dist from player to the first wall the ray hits.
            double wallDist;
            if(side == 0){
                wallDist = Math.abs((xPos - player.getxPos() + ((1 - stepX) / 2)) / rayDirX);
            } else {
                wallDist = Math.abs((yPos - player.getyPos() + ((1 - stepY) / 2)) / rayDirY);
            }
            
            // get height of wall that will show on screen based on the dist
            int wallHeight;
            if(wallDist > 0){
                wallHeight = (int)Math.abs(height / wallDist);
            } else {
                // when the wall to close and cover the whole line on the screen
                wallHeight = height;
            }

            // get the start and end point of the wall
            int start = (height / 2) - (wallHeight / 2);
            if(start < 0){
                start = 0;
            }
            int end = (wallHeight / 2) + (height / 2);
            if(end >= height){
                end = height - 1;
            }
            
            // get the texture of the wall
            Texture texture = textures.get(map[xPos][yPos] - 1);
            // get the exact position where the wall is hit
            double wallPos;
            if(side == 1){
                wallPos = player.getxPos() + (((yPos - player.getyPos() + ((1 - stepY) / 2)) / rayDirY) * rayDirX);
            } else {
                wallPos = player.getyPos() + (((xPos - player.getxPos() + ((1 - stepX) / 2)) / rayDirX) * rayDirY);
            }
            wallPos -= Math.floor(wallPos);

            // x coordinate on the texture
            int textureX = (int)(wallPos * texture.SIZE);
            if((side == 0 && rayDirX > 0) || (side == 1 && rayDirY < 0)){
                textureX = texture.SIZE - textureX - 1;
            }

            // set the color by each y coordinate on the texture
            for (int y = start; y < end; y++) {
                int textureY = (((y * 2 - height + wallHeight) << 6) / wallHeight) / 2;

                int color;
                int[] TexturePixels = texture.getPixels();
                if(side == 0){
                    color = TexturePixels[textureX + (textureY * texture.SIZE)];
                } else {
                    color = (TexturePixels[textureX + (textureY * texture.SIZE)] >> 1) & 835571; // make the y-side darker
                }
                pixels[x + (y * width)] = color;
            }
        }

        return pixels;
    }
}
