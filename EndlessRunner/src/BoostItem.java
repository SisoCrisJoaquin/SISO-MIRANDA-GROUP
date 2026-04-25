import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * ========== BOOSTITEM.JAVA ==========
 * Represents a nitro boost power-up that spawns randomly on lanes
 * Collision with player charges the boost fuel bar
 * Player can use boost with LEFT_SHIFT to increase game speed
 * 
 * IMAGE SPECIFICATIONS FOR boostitem.png:
 * ========================================
 * - Recommended Size: 128x128 pixels or 256x256 pixels (PNG format)
 * - Minimum Size: 80x80 pixels
 * - The image will be automatically scaled based on lane:
 *   * Lane 0 (top): 90% scale (72x72 or 115x115 displayed)
 *   * Lane 1 (middle): 100% scale (80x80 or 128x128 displayed)
 *   * Lane 2 (bottom): 110% scale (88x88 or 141x141 displayed)
 * - Use a transparent background (PNG with alpha channel)
 * - Design: Golden/yellow color recommended for nitro/boost aesthetic
 * - File Location: Place boostitem.png in the resources/ folder
 * - If image not found, a gold rectangle will be displayed as fallback
 */
public class BoostItem {
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private BufferedImage boostImage;
    private boolean useImage;
    private int lane; // 0 = top, 1 = middle, 2 = bottom
    private float scale; // 1.0 = normal, 0.8 = small (top), 1.2 = large (bottom)
    private final int LANE_HEIGHT = 40;
    private final int BOOST_FUEL_AMOUNT = 30; // Frames of boost when collected

    public BoostItem(int startX, int groundY, int speed, int lane) {
        this.x = startX;
        this.width = 80;
        this.height = 80;
        this.speed = speed;
        this.lane = lane;
        
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
        
        // Load boost item image
        this.boostImage = ImageLoader.loadImage("/boostitem.png");
        this.useImage = (boostImage != null);
    }

    public void update() {
        update(1.0f);
    }
    
    public void update(float speedMultiplier) {
        x -= (int)(speed * speedMultiplier);
    }

    public void draw(Graphics g) {
        int scaledWidth = (int)(width * scale);
        int scaledHeight = (int)(height * scale);
        
        if (useImage && boostImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(boostImage, x, y, scaledWidth, scaledHeight, null);
        } else {
            // Fallback: draw a yellow rectangle
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(255, 215, 0)); // Gold color
            g2d.fillRect(x, y, scaledWidth, scaledHeight);
            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(x, y, scaledWidth, scaledHeight);
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

    public int getBoostFuelAmount() {
        return BOOST_FUEL_AMOUNT;
    }

    public int getLane() {
        return lane;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
