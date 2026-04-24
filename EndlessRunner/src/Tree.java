import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * ========== TREE.JAVA ==========
 * Represents tree objects in the background (day and night modes)
 * Image-based rendering for customizable sprites
 */
public class Tree {
    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage treeImage;
    private boolean useImage;
    private boolean isNightTree;

    public Tree(int x, int y, int width, int height, boolean isNightTree) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isNightTree = isNightTree;
        
        // Try to load tree image from resources
        String imageName = isNightTree ? "/tree-night.png" : "/tree.png";
        this.treeImage = ImageLoader.loadImage(imageName);
        this.useImage = (treeImage != null);
    }

    public void draw(Graphics g) {
        if (useImage && treeImage != null) {
            // Draw tree image
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(treeImage, x, y, width, height, null);
        }
        // Note: Image-based rendering only. Provide tree.png or tree-night.png in resources folder for custom trees.
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

    public boolean isNightTree() {
        return isNightTree;
    }
}
