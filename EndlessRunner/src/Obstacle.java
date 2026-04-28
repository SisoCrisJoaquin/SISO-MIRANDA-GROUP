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
    
    // Animation frame properties
    private BufferedImage[] animationFrames; // Truck 1, Truck 2, Truck 3
    private int animationFrameCounter; // Counter to cycle through frames
    private int currentAnimationFrame; // Current frame index (0-2)
    private static final int MAX_ANIMATION_CYCLE_SPEED = 3; // Limit how fast animation frames cycle

    public Obstacle(int startX, int groundY, int speed, int lane) {
        this.x = startX;
        this.width = 350;   // 2.5x player width (140 * 2.5)
        this.height = 250;  // 2.5x player height (100 * 2.5)
        this.speed = (int)(speed * 2.0f);  // 2x speed for more challenge
        this.lane = lane;
        this.obstacleType = new Random().nextInt(2) + 1;
        
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
        
        // Load animation frames for the three truck images
        this.animationFrames = new BufferedImage[3];
        animationFrames[0] = ImageLoader.loadImage("/Truck 1.png");
        animationFrames[1] = ImageLoader.loadImage("/Truck 2.png");
        animationFrames[2] = ImageLoader.loadImage("/Truck 3.png");
        
        
        
        this.animationFrameCounter = 0;
        this.currentAnimationFrame = 0;
    }

    public void update() {
        update(1.0f);
    }
    
    public void update(float speedMultiplier) {
        // Update animation frame based on game speed
        int cycleSpeed = Math.max(1, (int)(speedMultiplier * 2)); // Cycle speed relative to game speed
        cycleSpeed = Math.min(cycleSpeed, MAX_ANIMATION_CYCLE_SPEED);
        animationFrameCounter += cycleSpeed;
        
        // Cycle through 3 animation frames
        currentAnimationFrame = (animationFrameCounter / 12) % 3; // 12 is frame duration per image
        
        // Prevent counter overflow
        if (animationFrameCounter > 100000) {
            animationFrameCounter = 0;
        }
        
        x -= (int)(speed * speedMultiplier);
    }

    public void draw(Graphics g) {
        int scaledWidth = (int)(width * scale);
        int scaledHeight = (int)(height * scale);
        
        if (animationFrames != null && currentAnimationFrame >= 0 && currentAnimationFrame < animationFrames.length) {
            if (animationFrames[currentAnimationFrame] != null) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(animationFrames[currentAnimationFrame], x, y, scaledWidth, scaledHeight, null);
                return;
            }
        }
        
        // Fallback to obstacle image if animation frames not available
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
