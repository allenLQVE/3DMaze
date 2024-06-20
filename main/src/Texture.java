import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Texture {
    public static Texture brick = new Texture("res/Bricks.png", 64);
    public static Texture brick2 = new Texture("res/brick-subsea64.png", 64);
    public static Texture brick_moss = new Texture("res/brick-moss-subsea64.png", 64);
    public static Texture dark = new Texture("res/dark.png", 64);
    public static Texture light = new Texture("res/light.png", 64);

    public final int SIZE;
    
    private int[] pixels;
    private String location;

    public Texture(String location, int size){
        this.location = location;
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        load();
    }

    public int[] getPixels() {
        return pixels;
    }

    /**
     * load texture img to pixels
     */
    private void load(){
        try {
            BufferedImage img = ImageIO.read(getClass().getResource(location));
            img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
