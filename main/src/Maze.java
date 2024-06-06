import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

public class Maze extends JFrame implements Runnable{
    public static final int MAP_WALL = 1;
    public static final int MAP_ROAD = 0;

    private final int MAP_WIDTH = 15;
    private final int MAP_HEIGHT = 15;
    private final int IMG_WIDTH = 640;
    private final int IMG_HEIGHT = 400;
    private final double NANO_SEC = 1000000000.0 / 60.0; // fresh rate for the window, 60 times a sec

    private Thread thread;
    private BufferedImage img; // variable that holds the texture
    private boolean isRunning;

    private int[] pixels; // hold the pixels that displaying to user
    private int[][] map = {
            {1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
			{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
			{1,0,3,3,3,3,3,0,0,0,0,0,0,0,2},
			{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
			{1,0,3,0,0,0,3,0,2,2,2,0,2,2,2},
			{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
			{1,0,3,3,0,3,3,0,2,0,0,0,0,0,2},
			{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
			{1,1,1,1,1,1,1,1,4,4,4,0,4,4,4},
			{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
			{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
			{1,0,0,2,0,0,1,4,0,3,3,3,3,0,4},
			{1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
			{1,1,1,1,1,1,1,4,4,4,4,4,4,4,4}
    };
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

        // default start point
        player = new Player(4.5, 4.5, 1, 0, 0, -.66);

        // default screen
        screen = new Screen(map, textures, 640, 480);

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

        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                delta --;
            }
            render();
        }
    }
    
}
