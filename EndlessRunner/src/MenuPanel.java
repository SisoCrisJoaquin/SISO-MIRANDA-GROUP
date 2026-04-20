import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * ========== MENUPANEL.JAVA ==========
 * Main menu screen displayed at game start
 * Shows game title, instructions, and controls
 */
public class MenuPanel extends JPanel implements KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private boolean startGame = false;

    public MenuPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(135, 206, 235)); // Light blue background
        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        g.setColor(new Color(135, 206, 235));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw grass
        g.setColor(new Color(34, 177, 76));
        g.fillRect(0, HEIGHT - 100, WIDTH, 100);

        // Draw title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 80));
        FontMetrics fm = g.getFontMetrics();
        String title = "ENDLESS RUNNER";
        int x = (WIDTH - fm.stringWidth(title)) / 2;
        g.drawString(title, x, 120);

        // Draw decorative box around title
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(x - 20, 80, fm.stringWidth(title) + 40, 80);

        // Draw instructions
        g.setFont(new Font("Arial", Font.BOLD, 36));
        String startText = "Press SPACEBAR to Start";
        fm = g.getFontMetrics();
        x = (WIDTH - fm.stringWidth(startText)) / 2;
        g.setColor(Color.WHITE);
        g.drawString(startText, x, 250);

        // Draw how to play section
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.setColor(new Color(255, 255, 200));
        String howToPlay = "HOW TO PLAY";
        fm = g.getFontMetrics();
        x = (WIDTH - fm.stringWidth(howToPlay)) / 2;
        g.drawString(howToPlay, x, 330);

        // Draw controls
        g.setFont(new Font("Arial", Font.PLAIN, 22));
        g.setColor(Color.WHITE);
        g.drawString("SPACEBAR - Jump", 150, 380);
        g.drawString("P - Pause/Unpause", 150, 415);
        g.drawString("R - Restart (on Game Over)", 150, 450);

        // Draw objective
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(new Color(255, 220, 100));
        String objective = "Avoid obstacles and survive!";
        fm = g.getFontMetrics();
        x = (WIDTH - fm.stringWidth(objective)) / 2;
        g.drawString(objective, x, 530);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            startGame = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public boolean shouldStartGame() {
        return startGame;
    }

    public void reset() {
        startGame = false;
    }
}
