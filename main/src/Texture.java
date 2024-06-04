import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Texture {
    public static Texture brick = new Texture("res/Bricks.png", 64);
    public static Texture brick2 = new Texture("res/brick-subsea.png", 512);
    public static Texture brick_moss = new Texture("res/brick-moss-subsea.png", 512);

    private final int SIZE;
    
    private int[] pixels;
    private String location;

    public Texture(String location, int size){
        this.location = location;
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        load();
    }

    /**
     * load texture img to pixels
     */
    private void load(){
        try {
            BufferedImage img = ImageIO.read(new File(location));
            img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
