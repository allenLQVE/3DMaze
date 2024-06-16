import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Maze extends JFrame implements Runnable{
    public static final int MAP_GOAL = 4;
    public static final int MAP_WALL = 1;
    public static final int MAP_ROAD = 0;

    private final int IMG_WIDTH = 640;
    private final int IMG_HEIGHT = 400;
    private final double NANO_SEC = 1000000000.0 / 60.0; // fresh rate for the window, 60 times a sec

    private Thread thread;
    private BufferedImage img; // variable that holds the texture
    private boolean isRunning;

    private int[] pixels; // hold the pixels that displaying to user
    private int[][] map;
    // private int[][] map = {
    //         {1,1,1,1,1,4,1,1,2,2,2,2,2,2,2},
	// 		{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
	// 		{1,0,3,3,3,3,3,0,0,0,0,0,0,0,2},
	// 		{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
	// 		{1,0,3,0,0,0,3,0,2,2,2,0,2,2,2},
	// 		{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
	// 		{1,0,3,3,0,3,3,0,2,0,0,0,0,0,2},
	// 		{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
	// 		{1,1,1,1,1,1,1,1,3,3,3,0,3,3,3},
	// 		{1,0,0,0,0,0,1,3,0,0,0,0,0,0,3},
	// 		{1,0,0,0,0,0,1,3,0,0,0,0,0,0,3},
	// 		{1,0,0,2,0,0,1,3,0,3,3,3,3,0,3},
	// 		{1,0,0,0,0,0,1,3,0,3,3,3,3,0,3},
	// 		{1,0,0,0,0,0,0,0,0,0,0,0,0,4,3},
	// 		{1,1,1,1,1,1,1,3,3,3,3,3,3,3,3}
    // };
    private ArrayList<Texture> textures;
    private Player player;
    private Screen screen;

    /**
     * Constructor of Maze
     */
    public Maze(){
        thread = new Thread(this);
        img = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData(); // get the pixels from img

        textures = new ArrayList<Texture>();
        textures.add(Texture.brick);
        textures.add(Texture.brick2);
        textures.add(Texture.brick_moss);
        textures.add(Texture.dark);

        // generate randomized maze
        map = randomMap();
        // for (int[] row : map) {
        //     for (int c : row) {
        //         System.out.print(c + " ");
        //     }
        //     System.out.println();
        // }
        

        // default start point
        player = new Player(1.5, 1.5, .5, 0, 0, -.66);

        // default screen
        screen = new Screen(map, textures, IMG_WIDTH, IMG_HEIGHT);
        addKeyListener(player);

        // properties for the JFrame
        setSize(IMG_WIDTH, IMG_HEIGHT);
        setResizable(false);
        setTitle("3D Maze");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setLocationRelativeTo(null);
        setVisible(true);
        start();
    }

    /**
     * Generate a random maze with Eller's Algorithm
     * 
     * @return
     */
    private int[][] randomMap() {
        Random rdm = new Random();

        final double ALPHA = .5; // chance of union. samller alpha generates more vertical maze
        // width and height for eller's algorithm
        final int MAZE_WIDTH = 7;
        final int MAZE_HEIGHT = 7;

        // width and height for actual map
        final int MAP_WIDTH = 2 * MAZE_WIDTH + 1;
        final int MAP_HEIGHT = 2 * MAZE_HEIGHT + 1;

        int[][] new_map = new int[MAP_HEIGHT][];

        // fill the map with roads suround by walls
        for (int r = 0; r < MAP_HEIGHT; r++) {
            new_map[r] = new int[MAP_WIDTH];
            for (int c = 0; c < MAP_WIDTH; c++) {
                if(r == 0 || c == 0 || r == MAP_HEIGHT - 1 || c == MAP_WIDTH - 1){
                    new_map[r][c] = MAP_WALL;
                } else {
                    new_map[r][c] = MAP_ROAD;
                }
            }
        }

        // Eller's algorithm with double linked list
        // int[] left = new int[MAZE_WIDTH];
        // int[] right = new int[MAZE_WIDTH];
        // int mapR;
        // int mapC;

        // // each cell links to itself
        // for (int i = 0; i < MAZE_WIDTH; i++) {
        //     left[i] = right[i] = i;
        // }

        // for (int r = 0; r < MAZE_HEIGHT - 1; r++) {
        //     mapR = r * 2 + 1;
        //     for (int c = 0; c < MAZE_WIDTH - 1; c++) {
        //         mapC = c * 2 + 1;

        //         // union cells
        //         if(right[c] != c + 1 && rdm.nextDouble() < ALPHA){
        //             left[right[c]] = left[c + 1];
        //             right[left[c + 1]] = right[c];
        //             left[c + 1] = c;
        //             right[c] = c + 1;
        //         } else {
        //             // build a wall between
        //             new_map[mapR][mapC + 1] = MAP_WALL;
        //         }

        //         // down passage
        //         if(right[c] != c && rdm.nextDouble() < ALPHA){
        //             // remove c from linked list
        //             left[right[c]] = left[c];
        //             right[left[c]] = right[c];
        //             right[c] = left[c] = c;

        //             // build a wall below
        //             new_map[mapR + 1][mapC] = MAP_WALL;
        //         }

        //         new_map[mapR + 1][mapC + 1] = MAP_WALL;
        //     }
            
        //     // down passage for last col
        //     if(right[MAZE_WIDTH - 1] != MAZE_WIDTH - 1 && rdm.nextDouble() < ALPHA){
        //         left[right[MAZE_WIDTH - 1]] = left[MAZE_WIDTH - 1];
        //         right[left[MAZE_WIDTH - 1]] = right[MAZE_WIDTH - 1];
        //         left[MAZE_WIDTH - 1] = right[MAZE_WIDTH - 1] = MAZE_WIDTH - 1;

        //         new_map[mapR + 1][(MAZE_WIDTH - 1) * 2 + 1] = MAP_WALL;
        //     }
        // }

        // // last row
        // for (int c = 0; c < MAZE_WIDTH - 1; c++) {
        //     if(right[c] == c +  1){
        //         new_map[MAZE_HEIGHT - 1][c * 2 + 2] = MAP_WALL;
        //     }
        // }

        // Eller's Algorithm
        int[] row = new int[MAZE_WIDTH];
        for (int i = 0; i < MAZE_WIDTH; i++) {
            row[i] = i;
        }
        int mapR;
        int mapC;
        for (int r = 0; r < MAZE_HEIGHT - 1; r++) {
            mapR = r * 2 + 1;
            
            // union cells randomly to create walls
            int down = -1; // use to mark if a set has at least one down passage
            int newNum = 0; // use to set the cell that do not connect with previous level
            for (int c = 0; c < row.length - 1; c++) {
                mapC = 2 * c + 1;

                if(row[c] != row[c + 1] && rdm.nextDouble() < ALPHA){
                    row[c + 1] = row[c];
                } else {
                    new_map[mapR][mapC + 1] = MAP_WALL;
                }

                // randomly generate down passage
                if(rdm.nextDouble() < ALPHA){
                    down = row[c];
                } else{
                    new_map[mapR + 1][mapC] = MAP_WALL;
                }
                // each set should have at least one down passage
                if(row[c] != row[c + 1] && row[c] != down){
                    down = row[c];
                    int left = c - 1; // find the left bund of the set
                    while(left > 0 && row[c] == row[left]){
                        left -= 1;
                    }
                    // select a random col from the set
                    int rdnCell = rdm.nextInt(left, c) + 1;
                    new_map[mapR + 1][2 * rdnCell + 1] = MAP_ROAD;
                }

                // the right down corner of each cell is always wall
                new_map[mapR + 1][mapC + 1] = MAP_WALL;
            }
            
            // generate down passage for last col
            if(rdm.nextDouble() < ALPHA || row[MAZE_WIDTH - 1] != row[MAZE_WIDTH - 2]){
                down = row[MAZE_WIDTH - 1];
            } else {
                new_map[mapR + 1][(MAZE_WIDTH - 1) * 2 + 1] = MAP_WALL;
            }
        }

        mapR = (MAZE_HEIGHT - 1) * 2 + 1;
        // all cells to one set
        for (int c = 0; c < row.length - 1; c++) {
            mapC = 2 * c + 1;
            
            // build a wall between if they are already in a same group
            if(row[c] == row[c + 1]){
                new_map[mapR][mapC + 1] = MAP_WALL;
            }
        }
        
        // set the last cell (right down corner) to goal
        new_map[MAP_WIDTH - 2][MAP_HEIGHT - 2] = MAP_GOAL;
        return new_map;
    }

    /**
     * Start the maze with a new thread
     */
    private synchronized void start(){
        isRunning = true;
        thread.start();
    }

    /**
     * Terminate the maze and recycle all the threads
     */
    public synchronized void stop(){
        isRunning = false;
    }

    /**
     * Create the buffer strategy for the view if it's null. If there exist a buffer, render with the new img
     */
    public void render(){
        BufferStrategy bufferStrategy = getBufferStrategy();

        if(bufferStrategy == null){
            createBufferStrategy(3);
        }else{
            Graphics graphics = bufferStrategy.getDrawGraphics();
            graphics.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
            bufferStrategy.show();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double delta = 0;
        requestFocus();

        while(isRunning){
            long now = System.nanoTime();
            delta += ((now - lastTime) / NANO_SEC);

            lastTime = now;
            // update mostly 60 times in each sec
            while(delta >= 1){
                screen.update(player, pixels);
                player.update(map);

                if(map[(int)player.getxPos()][(int)player.getyPos()] == MAP_GOAL){
                    stop();
                    break;
                }
                delta --;
            }
            render();
        }
        dispose();
    }
    
}
