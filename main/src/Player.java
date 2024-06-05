import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player implements KeyListener{
    private final double MOVE_SPEED = 0.8;
    private final double ROTATION_SPEED = 0.045;

    private double xPos, yPos, xDir, yDir, xPlane, yPlane;
    private boolean left, right, forward, back;

    public Player(double xPos, double yPos, double xDir, double yDir, double xPlane, double yPlane){
        this.xPos = xPos;
        this.yPos = yPos;
        this.xDir = xDir;
        this.yDir = yDir;
        this.xPlane = xPlane;
        this.yPlane = yPlane;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                left = true;    
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                right = true;    
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                forward = true;    
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                back = true;    
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                left = false;    
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                right = false;    
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                forward = false;    
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                back = false;    
                break;
            default:
                break;
        }
    }

    /**
     * Update the player position and direction based on the key pressing
     * 
     * @param map
     */
    public void update(int[][] map){
        if(forward){
            int newXPos = (int)(xPos + xDir * MOVE_SPEED);
            int newYPos = (int)(yPos + yDir * MOVE_SPEED);

            move(newXPos, newYPos, map);
        }
        if(back){
            int newXPos = (int)(xPos - xDir * MOVE_SPEED);
            int newYPos = (int)(yPos - yDir * MOVE_SPEED);

            move(newXPos, newYPos, map);
        }

        if(left){
            double newXDir = xDir * Math.cos(ROTATION_SPEED) - yDir * Math.sin(ROTATION_SPEED);
            double newYDir = xDir * Math.sin(ROTATION_SPEED) + yDir * Math.cos(ROTATION_SPEED);
            double newXPlane = xPlane * Math.cos(ROTATION_SPEED) - yPlane * Math.sin(ROTATION_SPEED);
            double newYPlane = xPlane * Math.sin(ROTATION_SPEED) + yPlane * Math.cos(ROTATION_SPEED);

            xDir = newXDir;
            yDir = newYDir;
            xPlane = newXPlane;
            yPlane = newYPlane;
        }
        if(right){
            double newXDir = xDir * Math.cos(-1 * ROTATION_SPEED) - yDir * Math.sin(-1 * ROTATION_SPEED);
            double newYDir = xDir * Math.sin(-1 * ROTATION_SPEED) + yDir * Math.cos(-1 * ROTATION_SPEED);
            double newXPlane = xPlane * Math.cos(-1 * ROTATION_SPEED) - yPlane * Math.sin(-1 * ROTATION_SPEED);
            double newYPlane = xPlane * Math.sin(-1 * ROTATION_SPEED) + yPlane * Math.cos(-1 * ROTATION_SPEED);

            xDir = newXDir;
            yDir = newYDir;
            xPlane = newXPlane;
            yPlane = newYPlane;
        }
    }

    /**
     * Move the player to the new x and y position on the map
     * 
     * @param newXPos
     * @param newYPos
     * @param map
     */
    private void move(int newXPos, int newYPos, int[][] map){
        if(map[newXPos][(int)yPos] == Maze.MAP_ROAD){
            xPos = newXPos;
        }
        if(map[(int)xPos][newYPos] == Maze.MAP_ROAD){
            yPos = newYPos;
        }
    }
}
