import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * ========== RAILING.JAVA ==========
 * Represents decorative railings on the game platform
 * Image-based rendering for customizable sprites
 */
public class Railing {
    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage railingImage;
    private boolean useImage;

    public Railing(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        // Try to load railing image from resources
        this.railingImage = ImageLoader.loadImage("/railing.png");
        this.useImage = (railingImage != null);
    }

    public void draw(Graphics g) {
        if (useImage && railingImage != null) {
            // Draw railing image
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(railingImage, x, y, width, height, null);
        }
        // Note: Image-based rendering only. Provide railing.png in resources folder for custom railings.
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
