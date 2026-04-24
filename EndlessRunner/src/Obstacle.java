import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * ========== OBSTACLE.JAVA ==========
 * Represents obstacles that move across the screen with lane support
 * Handles collision detection and rendering with 3D perspective
 * Supports randomized obstacle types (1-10)
 */
public class Obstacle {
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private BufferedImage obstacleImage;
    private boolean useImage;
    private int obstacleType; // 1-10
    private int lane; // 0 = top, 1 = middle, 2 = bottom
    private float scale; // 1.0 = normal, 0.8 = small (top), 1.2 = large (bottom)
    private final int LANE_HEIGHT = 40;

    public Obstacle(int startX, int groundY, int speed, int lane) {
        this.x = startX;
        this.width = 140;
        this.height = 100;
        this.speed = speed;
        this.lane = lane;
        this.obstacleType = new Random().nextInt(10) + 1;
        
        // Set scale based on lane
        if (lane == 0) {
            this.scale = 0.9f;
            this.y = groundY - LANE_HEIGHT - (int)(height * scale);
        } else if (lane == 1) {
            this.scale = 1.0f;
            this.y = groundY - (int)(height * scale);
        } else {
            this.scale = 1.1f;
            this.y = groundY + LANE_HEIGHT - (int)(height * scale);
        }
        
        // Try to load the selected obstacle image
        String obstaclePath = "/obstacle" + obstacleType + ".png";
        this.obstacleImage = ImageLoader.loadImage(obstaclePath);
        
        if (this.obstacleImage == null) {
            this.obstacleImage = ImageLoader.loadImage("/obstacle.png");
        }
        this.useImage = (obstacleImage != null);
    }

    public void update() {
        x -= speed;
    }

    public void draw(Graphics g) {
        int scaledWidth = (int)(width * scale);
        int scaledHeight = (int)(height * scale);
        
        if (useImage && obstacleImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(obstacleImage, x, y, scaledWidth, scaledHeight, null);
        }
    }

    public Rectangle getBounds() {
        int scaledWidth = (int)(width * scale);
        int scaledHeight = (int)(height * scale);
        return new Rectangle(x, y, scaledWidth, scaledHeight);
    }

    public boolean isOffScreen() {
        return x + width < 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLane() {
        return lane;
    }
}
