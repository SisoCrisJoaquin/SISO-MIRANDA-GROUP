import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * ========== CLOUD.JAVA ==========
 * Represents cloud objects that scroll in the background
 * Image-based rendering with customizable sprites
 */
public class Cloud {
    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage cloudImage;
    private boolean useImage;

    public Cloud(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        // Try to load cloud image from resources
        this.cloudImage = ImageLoader.loadImage("/cloud.png");
        this.useImage = (cloudImage != null);
    }

    public void draw(Graphics g) {
        if (useImage && cloudImage != null) {
            // Draw cloud image
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(cloudImage, x, y, width, height, null);
        }
        // Note: Image-based rendering only. Provide cloud.png in resources folder for custom clouds.
    }

    public void setX(int x) {
        this.x = x;
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

    public boolean isOffScreen(int screenWidth) {
        return x + width < 0 || x > screenWidth;
    }
}
