import java.awt.*;
import java.awt.image.BufferedImage;

public class Obstacle {
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private BufferedImage obstacleImage;
    private boolean useImage;

    public Obstacle(int startX, int groundY, int speed) {
        this.x = startX;
        this.width = 40;
        this.height = 50;
        this.y = groundY - height;
        this.speed = speed;
        
        // Try to load obstacle image from resources
        this.obstacleImage = ImageLoader.loadImage("/obstacle.png");
        this.useImage = (obstacleImage != null);
    }

    public void update() {
        x -= speed;
    }

    public void draw(Graphics g) {
        if (useImage && obstacleImage != null) {
            // Draw obstacle image
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(obstacleImage, x, y, width, height, null);
        } else {
            // Fallback to detailed rock drawing
            // Draw rock/boulder obstacle with more detail
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Main boulder - dark gray
            g.setColor(new Color(80, 80, 80));
            g2d.fillOval(x, y + 15, width, width - 5);
            
            // Rock shadow for depth
            g.setColor(new Color(40, 40, 40));
            g2d.fillOval(x + 5, y + 20, width - 10, width - 15);
            
            // Rock highlights
            g.setColor(new Color(120, 120, 120));
            g2d.fillOval(x + 5, y + 15, width - 15, 12);
            
            // Additional rocks/pebbles stacked effect
            g.setColor(new Color(90, 90, 90));
            g2d.fillOval(x + 8, y, width - 16, width - 20);
            
            // Highlight on top rock
            g.setColor(new Color(140, 140, 140));
            g2d.fillOval(x + 12, y - 2, width - 24, 8);
            
            // Small pebbles around main rock
            g.setColor(new Color(100, 100, 100));
            g2d.fillOval(x - 5, y + 30, 12, 12);
            g2d.fillOval(x + width + 2, y + 25, 10, 10);
            
            // Rock texture details (small cracks)
            g.setColor(new Color(50, 50, 50));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawLine(x + 10, y + 20, x + 20, y + 30);
            g2d.drawLine(x + width - 15, y + 25, x + width - 5, y + 35);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
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
}
