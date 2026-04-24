import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * ========== PLAYER.JAVA ==========
 * Represents the player character with jump mechanics and lane switching
 * Handles gravity, collision bounds, and 3D perspective rendering
 */
public class Player {
    private int x;
    private int standardX; // Starting x position to return to when landing
    private int y;
    private int width;
    private int height;
    private float velocityX;
    private float velocityY;
    private boolean isJumping;
    private int groundY;
    private int lane; // 0 = top, 1 = middle, 2 = bottom
    private float scale; // 1.0 = normal, 0.8 = small (top), 1.2 = large (bottom)
    private final int JUMP_STRENGTH = 18;
    private final float GRAVITY = 0.6f;
    private final int MAX_FALL_SPEED = 18;
    private final int LANE_HEIGHT = 40; // Vertical distance between lanes
    private final int RETURN_SPEED = 5; // Speed to gradually return to standard X position
    private final BufferedImage playerImage;
    private final boolean useImage;

    public Player(int groundY, int screenWidth) {
        this.x = screenWidth / 4;
        this.standardX = screenWidth / 4; // Store standard position
        this.groundY = groundY;
        this.lane = 1; // Start in middle lane
        this.scale = 1.0f;
        this.width = 140;
        this.height = 100;
        this.velocityX = 0;
        this.velocityY = 0;
        this.isJumping = false;
        updateLanePosition();
        
        this.playerImage = ImageLoader.loadImage("/player.png");
        this.useImage = (playerImage != null);
    }
    
    private void updateLanePosition() {
        // Update scale based on lane (always)
        if (lane == 0) {
            this.scale = 0.9f;
        } else if (lane == 1) {
            this.scale = 1.0f;
        } else {
            this.scale = 1.1f;
        }
        
        // Only update y position if not jumping (preserve jump arc during lane switch)
        if (!isJumping) {
            if (lane == 0) {
                this.y = groundY - LANE_HEIGHT - (int)(height * scale);
            } else if (lane == 1) {
                this.y = groundY - (int)(height * scale);
            } else {
                this.y = groundY + LANE_HEIGHT - (int)(height * scale);
            }
        }
    }
    
    public void moveUp() {
        if (lane > 0) {
            lane--;
            updateLanePosition();
        }
    }
    
    public void moveDown() {
        if (lane < 2) {
            lane++;
            updateLanePosition();
        }
    }
    
    public int getLane() {
        return lane;
    }

    public void update() {
        if (isJumping) {
            // Apply gravity and horizontal friction for arc motion
            velocityY += GRAVITY;
            if (velocityY > MAX_FALL_SPEED) {
                velocityY = MAX_FALL_SPEED;
            }
            velocityX *= 0.92f;  // Friction to reduce horizontal velocity over time
            
            y += velocityY;
            x += (int)velocityX;

            // Get the ground level for the current lane
            int laneGroundY;
            if (lane == 0) {
                laneGroundY = groundY - LANE_HEIGHT - (int)(height * 0.9f);
            } else if (lane == 1) {
                laneGroundY = groundY - (int)(height * 1.0f);
            } else {
                laneGroundY = groundY + LANE_HEIGHT - (int)(height * 1.1f);
            }
            
            if (y >= laneGroundY) {
                y = laneGroundY;
                isJumping = false;
                velocityY = 0;
                velocityX = 0;
            }
        } else {
            // Gradually return to standard X position when not jumping
            if (x < standardX) {
                x += RETURN_SPEED;
                if (x > standardX) x = standardX; // Don't overshoot
            } else if (x > standardX) {
                x -= RETURN_SPEED;
                if (x < standardX) x = standardX; // Don't overshoot
            }
        }
    }

    public void jump() {
        if (!isJumping) {
            velocityY = -JUMP_STRENGTH;
            velocityX = 8.0f;  // Horizontal velocity for wider arc
            isJumping = true;
        }
    }

    public void draw(Graphics g) {
        int scaledWidth = (int)(width * scale);
        int scaledHeight = (int)(height * scale);
        int adjustedX = x + (width - scaledWidth) / 2; // Center the scaled sprite
        
        if (useImage && playerImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(playerImage, adjustedX, y, scaledWidth, scaledHeight, null);
        } else {
            g.setColor(new Color(255, 100, 100));
            g.fillRect(adjustedX, y, scaledWidth, scaledHeight);
            g.setColor(Color.BLACK);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(adjustedX, y, scaledWidth, scaledHeight);
        }
    }

    public Rectangle getBounds() {
        int scaledWidth = (int)(width * scale);
        int scaledHeight = (int)(height * scale);
        int adjustedX = x + (width - scaledWidth) / 2;
        return new Rectangle(adjustedX, y, scaledWidth, scaledHeight);
    }

    public void reset(int groundY, int screenWidth) {
        this.x = screenWidth / 4;
        this.standardX = screenWidth / 4; // Reset to standard position
        this.groundY = groundY;
        this.lane = 1;
        this.scale = 1.0f;
        this.velocityX = 0;
        this.velocityY = 0;
        this.isJumping = false;
        updateLanePosition();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
