import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * ========== CLOUD.JAVA ==========
 * Represents individual clouds that float across the sky
 * Clouds spawn at random y positions in the upper portion of screen
 * Supports 10 different cloud images (cloud1.png through cloud10.png)
 * Automatically handles various image sizes up to 400x400 pixels
 * New clouds spawn when old ones go off-screen
 */
public class Cloud {
    private int x;
    private int y;
    private int width;
    private int height;
    private float speed; // Slow speed (independent from game speed)
    private BufferedImage cloudImage;
    private boolean useImage;
    private float scale;
    private int cloudVariant; // Track which cloud image we're using

    public Cloud(int screenWidth, int screenHeight, int groundY) {
        // Randomly select one of 10 cloud images
        this.cloudVariant = new Random().nextInt(10) + 1; // 1-10
        
        // Try loading the specific cloud variant first
        String cloudPath = "/cloud" + cloudVariant + ".png";
        this.cloudImage = ImageLoader.loadImage(cloudPath);
        
        // If specific cloud not found, try generic cloud.png
        if (this.cloudImage == null) {
            System.out.println("Cloud variant " + cloudVariant + " not found at " + cloudPath + ", trying generic cloud.png");
            this.cloudImage = ImageLoader.loadImage("/cloud.png");
        } else {
            System.out.println("Successfully loaded cloud" + cloudVariant + ".png");
        }
        
        this.useImage = (cloudImage != null);
        
        // Cloud dimensions - supports images up to 400x400
        if (cloudImage != null) {
            this.width = cloudImage.getWidth();
            this.height = cloudImage.getHeight();
        } else {
            this.width = 80;
            this.height = 60;
        }
        
        // Size variation: 0.5 to 1.5 scale for big variety
        // Larger images may spawn at smaller scales for consistency
        this.scale = 0.5f + (float)(Math.random() * 1.0f);
        
        // Spawn off-screen to the right
        this.x = screenWidth;
        
        // Random Y position in the upper portion of screen (top 30% only)
        int skyHeight = (int)(groundY * 0.3f);
        int minY = 20;
        this.y = minY + new Random().nextInt(Math.max(1, skyHeight - minY));
        
        // Slow cloud speed (0.8-2.5 pixels per frame) - more variety
        this.speed = 0.8f + (float)(Math.random() * 1.7f);
    }

    public void update() {
        // Move clouds slowly to the left (independent of game speed)
        x -= speed;
    }

    public void draw(Graphics2D g2d) {
        if (useImage && cloudImage != null) {
            int scaledWidth = (int)(width * scale);
            int scaledHeight = (int)(height * scale);
            // Ensure minimum size for visibility
            if (scaledWidth > 0 && scaledHeight > 0) {
                g2d.drawImage(cloudImage, x, y, scaledWidth, scaledHeight, null);
            }
        }
    }

    public boolean isOffScreen(int screenWidth) {
        return x + width < 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCloudVariant() {
        return cloudVariant;
    }
}
