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
        g.setColor(Color.BLACK);
        
        // Use custom font if available, otherwise fall back to Arial
        if (customFont != null) {
            g.setFont(customFont);
        } else {
            g.setFont(new Font("Arial", Font.BOLD, 24));
        }
        
        g.drawString("Score: " + score, 20, 30);
        g.drawString("Lives: " + lives, 20, 60);
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
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
