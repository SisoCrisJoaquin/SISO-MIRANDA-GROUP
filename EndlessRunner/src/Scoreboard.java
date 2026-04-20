import java.awt.*;

/**
 * ========== SCOREBOARD.JAVA ==========
 * Tracks score and lives during gameplay
 * Manages game state and scoring logic
 */
public class Scoreboard {
    private int score;
    private int lives;
    private final int MAX_LIVES = 3;

    public Scoreboard() {
        this.score = 0;
        this.lives = MAX_LIVES;
    }

    public void update() {
        score++;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
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
