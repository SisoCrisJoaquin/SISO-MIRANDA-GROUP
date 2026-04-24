import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * ========== MENUPANEL.JAVA ==========
 * Main menu screen displayed at game start
 * Shows game title, instructions, and controls
 */
public class MenuPanel extends JPanel implements KeyListener {
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int WIDTH = screenSize.width;
    private static final int HEIGHT = screenSize.height;
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
        g.fillRect(0, (int)(HEIGHT * 0.85), WIDTH, (int)(HEIGHT * 0.15));

        // Calculate responsive font sizes
        int titleFontSize = Math.max(40, WIDTH / 15);
        int startFontSize = Math.max(24, WIDTH / 40);
        int sectionFontSize = Math.max(18, WIDTH / 50);
        int controlFontSize = Math.max(16, WIDTH / 60);

        // Draw title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, titleFontSize));
        FontMetrics fm = g.getFontMetrics();
        String title = "ENDLESS RUNNER";
        int x = (WIDTH - fm.stringWidth(title)) / 2;
        int titleY = (int)(HEIGHT * 0.15);
        g.drawString(title, x, titleY);

        // Draw decorative box around title
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(x - 20, titleY - fm.getAscent(), fm.stringWidth(title) + 40, fm.getAscent() + fm.getDescent() + 10);

        // Draw instructions
        g.setFont(new Font("Arial", Font.BOLD, startFontSize));
        String startText = "Press SPACEBAR to Start";
        fm = g.getFontMetrics();
        x = (WIDTH - fm.stringWidth(startText)) / 2;
        g.setColor(Color.WHITE);
        g.drawString(startText, x, (int)(HEIGHT * 0.35));

        // Draw how to play section
        g.setFont(new Font("Arial", Font.BOLD, sectionFontSize));
        g.setColor(new Color(255, 255, 200));
        String howToPlay = "HOW TO PLAY";
        fm = g.getFontMetrics();
        x = (WIDTH - fm.stringWidth(howToPlay)) / 2;
        g.drawString(howToPlay, x, (int)(HEIGHT * 0.48));

        // Draw controls
        g.setFont(new Font("Arial", Font.PLAIN, controlFontSize));
        g.setColor(Color.WHITE);
        int leftMargin = (int)(WIDTH * 0.15);
        int controlY = (int)(HEIGHT * 0.55);
        int controlSpacing = (int)(HEIGHT * 0.05);
        
        g.drawString("SPACEBAR - Jump", leftMargin, controlY);
        g.drawString("W / S - Change Lane", leftMargin, controlY + controlSpacing);
        g.drawString("ESC - Pause/Unpause", leftMargin, controlY + controlSpacing * 2);
        g.drawString("R - Restart (on Game Over)", leftMargin, controlY + controlSpacing * 3);

        // Draw objective
        g.setFont(new Font("Arial", Font.BOLD, sectionFontSize));
        g.setColor(new Color(255, 220, 100));
        String objective = "Avoid obstacles and survive!";
        fm = g.getFontMetrics();
        x = (WIDTH - fm.stringWidth(objective)) / 2;
        g.drawString(objective, x, (int)(HEIGHT * 0.78));
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
