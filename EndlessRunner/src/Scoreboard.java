import java.awt.*;
import java.io.File;

/**
 * ========== SCOREBOARD.JAVA ==========
 * Tracks score and lives during gameplay
 * Manages game state and scoring logic
 */
public class Scoreboard {
    private int score;
    private int lives;
    private final int MAX_LIVES = 3;
    private static Font customFont;
    
    static {
        // Load custom Upheaval font from resources
        try {
            File fontFile = new File("resources/upheavtt.ttf");
            if (fontFile.exists()) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(24f);
            } else {
                customFont = null;
            }
        } catch (Exception e) {
            customFont = null;
        }
    }

    public Scoreboard() {
        this.score = 0;
        this.lives = MAX_LIVES;
    }

    public void update() {
        score++;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int HEIGHT = screenSize.height;
        
        // Larger font for bottom-left display
        Font largeFont;
        if (customFont != null) {
            largeFont = customFont.deriveFont(48f);
        } else {
            largeFont = new Font("Arial", Font.BOLD, 48);
        }
        
        g.setFont(largeFont);
        FontMetrics fm = g.getFontMetrics();
        
        // Position at bottom-left with padding
        int baseX = 30;
        int baseY = HEIGHT - 250; // Start higher to avoid overlapping with nitro bar
        int padding = 15;
        int boxHeight = 70;
        int boxWidth = 280;
        
        // Draw semi-transparent background for Lives (on top)
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(baseX - padding, baseY - padding, boxWidth, boxHeight);
        g2d.setColor(new Color(255, 100, 100, 200));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(baseX - padding, baseY - padding, boxWidth, boxHeight);
        
        // Draw Lives text
        g2d.setColor(new Color(255, 100, 100));
        g2d.drawString("Lives: " + lives, baseX, baseY + 30);
        
        // Draw semi-transparent background for Score (below Lives)
        baseY += 90;
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(baseX - padding, baseY - padding, boxWidth, boxHeight);
        g2d.setColor(new Color(255, 215, 0, 200));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(baseX - padding, baseY - padding, boxWidth, boxHeight);
        
        // Draw Score text
        g2d.setColor(new Color(255, 215, 0));
        g2d.drawString("Score: " + score, baseX, baseY + 30);
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Add points to the current score
     * @param points The number of points to add
     */
    public void addScore(int points) {
        this.score += points;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void loseLife() {
        lives--;
    }

    public void resetScore() {
        score = 0;
    }

    public void reset() {
        score = 0;
        lives = MAX_LIVES;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }
}
