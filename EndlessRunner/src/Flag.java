import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * ========== FLAG.JAVA ==========
 * Represents decorative flags in the game background
 * Image-based rendering for customizable sprites
 */
public class Flag {
    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage flagImage;
    private boolean useImage;
    private Color defaultFlagColor;

    public Flag(int x, int y, int width, int height, Color flagColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.defaultFlagColor = flagColor;
        
        // Try to load flag image from resources
        this.flagImage = ImageLoader.loadImage("/flag.png");
        this.useImage = (flagImage != null);
    }

    public void draw(Graphics g) {
        if (useImage && flagImage != null) {
            // Draw flag image
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(flagImage, x, y, width, height, null);
        }
        // Note: Image-based rendering only. Provide flag.png in resources folder for custom flags.
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

    public Color getFlagColor() {
        return defaultFlagColor;
    }

    public boolean isOffScreen(int screenWidth) {
        return x + width < 0 || x > screenWidth;
    }
}
