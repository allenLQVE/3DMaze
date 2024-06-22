import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Player implements KeyListener, MouseListener{
    private final double MOVE_SPEED = 0.08;
    private final double ROTATION_SPEED = 0.08;
    // private final double ROTATION_SPEED = 0.045;

    private double xPos;
    private double yPos;
    private double xDir;
    private double yDir;
    private double xPlane;
    private double yPlane;

    private double prevMouse;
    private int direct = -1;
    private boolean rotate = false;

    private boolean left;
    private boolean right;
    private boolean forward;
    private boolean back;

    public Player(double xPos, double yPos, double xDir, double yDir, double xPlane, double yPlane){
        this.xPos = xPos;
        this.yPos = yPos;
        this.xDir = xDir;
        this.yDir = yDir;
        this.xPlane = xPlane;
        this.yPlane = yPlane;
    }

    public double getxDir() {
        return xDir;
    }
    public double getyDir() {
        return yDir;
    }
    public double getxPlane() {
        return xPlane;
    }
    public double getyPlane() {
        return yPlane;
    }
    public double getxPos() {
        return xPos;
    }
    public double getyPos() {
        return yPos;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
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
        if(rotate){
            double mouse = MouseInfo.getPointerInfo().getLocation().getX();
            // direct is 1 if mouse moving leftward, 0 if rightward
            direct = prevMouse > mouse ? 0 : 1;

            // change rotation speed based on mouse distance
            double rotationSpeed = ROTATION_SPEED * (Math.abs(prevMouse - mouse) / prevMouse);
    
            // mouse moving to left, rotate leftward
            if(direct == 1){
                double newXDir = xDir * Math.cos(rotationSpeed) - yDir * Math.sin(rotationSpeed);
                double newYDir = xDir * Math.sin(rotationSpeed) + yDir * Math.cos(rotationSpeed);
                double newXPlane = xPlane * Math.cos(rotationSpeed) - yPlane * Math.sin(rotationSpeed);
                double newYPlane = xPlane * Math.sin(rotationSpeed) + yPlane * Math.cos(rotationSpeed);
    
                xDir = newXDir;
                yDir = newYDir;
                xPlane = newXPlane;
                yPlane = newYPlane;
    
            // mouse moving to right, rotate to rightward
            } else if(direct == 0) {
                double newXDir = xDir * Math.cos(-1 * rotationSpeed) - yDir * Math.sin(-1 * rotationSpeed);
                double newYDir = xDir * Math.sin(-1 * rotationSpeed) + yDir * Math.cos(-1 * rotationSpeed);
                double newXPlane = xPlane * Math.cos(-1 * rotationSpeed) - yPlane * Math.sin(-1 * rotationSpeed);
                double newYPlane = xPlane * Math.sin(-1 * rotationSpeed) + yPlane * Math.cos(-1 * rotationSpeed);
    
                xDir = newXDir;
                yDir = newYDir;
                xPlane = newXPlane;
                yPlane = newYPlane;
            }
        }
        
        
        if(forward){
            if((map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == Maze.MAP_ROAD) || (map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == Maze.MAP_GOAL)){
                xPos += xDir * MOVE_SPEED;
            }
            if((map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] == Maze.MAP_ROAD) || (map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] == Maze.MAP_GOAL)){
                yPos += yDir * MOVE_SPEED;
            }
        }
        if(back){
            if((map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == Maze.MAP_ROAD) || (map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == Maze.MAP_GOAL)){
                xPos -= xDir * MOVE_SPEED;
            }
            if((map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] == Maze.MAP_ROAD) || (map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] == Maze.MAP_GOAL)){
                yPos -= yDir * MOVE_SPEED;
            }
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

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        prevMouse = MouseInfo.getPointerInfo().getLocation().getX();
        rotate = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        rotate = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'mouseEntered'");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'mouseExited'");
    }
}
