import javax.swing.JFrame;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Maze extends JFrame implements Runnable{
    private final int MAP_WIDTH = 15;
    private final int MAP_HEIGHT = 15;
    private final int IMG_WIDTH = 640;
    private final int IMG_HEIGHT = 400;

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

    /**
     * Constructor of Maze
     */
    public Maze(){
        thread = new Thread(this);
        img = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData(); // get the pixels from img

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

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
    
}
