import java.awt.*;
import java.awt.image.BufferedImage;

public class Player {
    private int x;
    private int y;
    private int width;
    private int height;
    private int velocityY;
    private boolean isJumping;
    private int groundY;
    private final int JUMP_STRENGTH = 15;
    private final int GRAVITY = 1;
    private final int MAX_FALL_SPEED = 20;
    private BufferedImage playerImage;
    private boolean useImage;

    public Player(int groundY, int screenWidth) {
        this.width = 30;
        this.height = 40;
        this.x = screenWidth / 4;
        this.y = groundY - height;
        this.groundY = groundY;
        this.velocityY = 0;
        this.isJumping = false;
        
        // Try to load player image from resources
        this.playerImage = ImageLoader.loadImage("/player.png");
        this.useImage = (playerImage != null);
    }

    public void update() {
        if (isJumping) {
            velocityY += GRAVITY;
            if (velocityY > MAX_FALL_SPEED) {
                velocityY = MAX_FALL_SPEED;
            }
            y += velocityY;

            if (y >= groundY - height) {
                y = groundY - height;
                isJumping = false;
                velocityY = 0;
            }
        }
    }

    public void jump() {
        if (!isJumping) {
            velocityY = -JUMP_STRENGTH;
            isJumping = true;
        }
    }

    public void draw(Graphics g) {
        if (useImage && playerImage != null) {
            // Draw player image
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(playerImage, x, y, width, height, null);
        } else {
            // Fallback to colored rectangle
            g.setColor(new Color(255, 100, 100));
            g.fillRect(x, y, width, height);
            g.setColor(Color.BLACK);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void reset(int groundY, int screenWidth) {
        this.x = screenWidth / 4;
        this.y = groundY - height;
        this.groundY = groundY;
        this.velocityY = 0;
        this.isJumping = false;
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
