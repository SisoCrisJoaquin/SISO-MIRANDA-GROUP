import java.awt.*;

public class Obstacle {
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;

    public Obstacle(int startX, int groundY, int speed) {
        this.x = startX;
        this.width = 40;
        this.height = 50;
        this.y = groundY - height;
        this.speed = speed;
    }

    public void update() {
        x -= speed;
    }

    public void draw(Graphics g) {
        // Draw spike obstacle
        g.setColor(new Color(200, 50, 50));
        int[] xPoints = {x, x + width / 2, x + width};
        int[] yPoints = {y + height, y, y + height};
        ((Graphics2D) g).fillPolygon(xPoints, yPoints, 3);
        
        g.setColor(Color.BLACK);
        ((Graphics2D) g).setStroke(new BasicStroke(2));
        ((Graphics2D) g).drawPolygon(xPoints, yPoints, 3);
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
