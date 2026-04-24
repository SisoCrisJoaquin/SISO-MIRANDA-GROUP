import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * ========== MENUPANEL.JAVA ==========
 * Main menu screen displayed at game start
 * Shows game title, instructions, and controls
 */
public class MenuPanel extends JPanel implements KeyListener, MouseListener {
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int WIDTH = screenSize.width;
    private static final int HEIGHT = screenSize.height;
    private boolean startGame = false;
    private Rectangle startButtonRect; // Button for clicking
    private Rectangle howToPlayButtonRect; // How to Play button
    private Rectangle exitButtonRect; // Exit button
    private boolean showControls = false; // Toggle for showing controls
    private BufferedImage wallpaperImage; // Wallpaper for menu background
    private BufferedImage titleBackgroundImage; // Title background image

    public MenuPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(135, 206, 235)); // Light blue background
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        
        // Load wallpaper image
        wallpaperImage = ImageLoader.loadAndScaleBackgroundImage("/rider-motorbike-road-riding-having-fun-driving-empty-road.jpg", WIDTH, HEIGHT);
        
        // Load title background image
        titleBackgroundImage = ImageLoader.loadImage("/painting-ink-brush-portable-network-graphics-article-title-364854df596df777851b1411b70b6708.png");
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw wallpaper background or fallback color
        if (wallpaperImage != null) {
            g.drawImage(wallpaperImage, 0, 0, WIDTH, HEIGHT, null);
        } else {
            g.setColor(new Color(135, 206, 235));
            g.fillRect(0, 0, WIDTH, HEIGHT);

            // Draw grass fallback
            g.setColor(new Color(34, 177, 76));
            g.fillRect(0, (int)(HEIGHT * 0.85), WIDTH, (int)(HEIGHT * 0.15));
        }

        // Calculate responsive font sizes
        int titleFontSize = Math.max(40, WIDTH / 15);
        int startFontSize = Math.max(24, WIDTH / 40);
        int sectionFontSize = Math.max(18, WIDTH / 50);
        int controlFontSize = Math.max(16, WIDTH / 60);

        // Draw title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, titleFontSize));
        FontMetrics fm = g.getFontMetrics();
        String title = "Motorcyclist seeker";
        int x = (WIDTH - fm.stringWidth(title)) / 2;
        int titleY = (int)(HEIGHT * 0.15);
        
        // Draw title background image if available
        if (titleBackgroundImage != null) {
            int bgPadding = 50;
            int bgWidth = fm.stringWidth(title) + (bgPadding * 2);
            int bgHeight = fm.getAscent() + fm.getDescent() + (bgPadding * 2);
            int bgX = x - bgPadding;
            int bgY = titleY - fm.getAscent() - bgPadding;
            g2d.drawImage(titleBackgroundImage, bgX, bgY, bgWidth, bgHeight, null);
        }
        
        g.drawString(title, x, titleY);

        // Draw subtitle/description
        g.setColor(new Color(200, 200, 255));
        g.setFont(new Font("Arial", Font.ITALIC, (int)(sectionFontSize * 0.8)));
        String subtitle = "Endless Runner";
        fm = g.getFontMetrics();
        int subtitleX = (WIDTH - fm.stringWidth(subtitle)) / 2;
        int subtitleY = titleY + (int)(HEIGHT * 0.06);
        g.drawString(subtitle, subtitleX, subtitleY);

        // Draw Start button
        int buttonWidth = (int)(WIDTH * 0.25);
        int buttonHeight = (int)(HEIGHT * 0.08);
        int buttonX = (WIDTH - buttonWidth) / 2;
        int buttonY = (int)(HEIGHT * 0.38);
        startButtonRect = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
        
        // Draw button background
        g2d.setColor(new Color(50, 150, 50));
        g2d.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(buttonX, buttonY, buttonWidth, buttonHeight);
        
        // Draw button text
        g.setFont(new Font("Arial", Font.BOLD, startFontSize));
        String buttonText = "START GAME";
        fm = g.getFontMetrics();
        int btnX = buttonX + (buttonWidth - fm.stringWidth(buttonText)) / 2;
        int btnY = buttonY + ((buttonHeight - fm.getAscent()) / 2) + fm.getAscent();
        g.setColor(Color.WHITE);
        g.drawString(buttonText, btnX, btnY);

        // Draw How To Play button
        int howButtonWidth = (int)(WIDTH * 0.2);
        int howButtonHeight = (int)(HEIGHT * 0.06);
        int howButtonX = (WIDTH - howButtonWidth) / 2;
        int howButtonY = (int)(HEIGHT * 0.50);
        howToPlayButtonRect = new Rectangle(howButtonX, howButtonY, howButtonWidth, howButtonHeight);
        
        // Draw button background
        g2d.setColor(new Color(70, 120, 180));
        g2d.fillRect(howButtonX, howButtonY, howButtonWidth, howButtonHeight);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(howButtonX, howButtonY, howButtonWidth, howButtonHeight);
        
        // Draw button text
        g.setFont(new Font("Arial", Font.BOLD, (int)(sectionFontSize * 0.9)));
        String howText = showControls ? "HIDE CONTROLS" : "HOW TO PLAY";
        fm = g.getFontMetrics();
        int howX = howButtonX + (howButtonWidth - fm.stringWidth(howText)) / 2;
        int howY = howButtonY + ((howButtonHeight - fm.getAscent()) / 2) + fm.getAscent();
        g.setColor(Color.WHITE);
        g.drawString(howText, howX, howY);

        // Draw controls only if showControls is true
        if (showControls) {
            g.setFont(new Font("Arial", Font.PLAIN, controlFontSize));
            g.setColor(Color.WHITE);
            int leftMargin = (int)(WIDTH * 0.15);
            int controlY = (int)(HEIGHT * 0.58);
            int controlSpacing = (int)(HEIGHT * 0.05);
            
            g.drawString("SPACEBAR - Jump", leftMargin, controlY);
            g.drawString("W / S - Change Lane", leftMargin, controlY + controlSpacing);
            g.drawString("ESC - Pause/Unpause", leftMargin, controlY + controlSpacing * 2);
            g.drawString("R - Restart (on Game Over)", leftMargin, controlY + controlSpacing * 3);
        }

        // Draw objective
        g.setFont(new Font("Arial", Font.BOLD, sectionFontSize));
        g.setColor(new Color(255, 220, 100));
        String objective = "Avoid obstacles and survive!";
        fm = g.getFontMetrics();
        x = (WIDTH - fm.stringWidth(objective)) / 2;
        g.drawString(objective, x, (int)(HEIGHT * 0.78));

        // Draw Exit button
        int exitButtonWidth = (int)(WIDTH * 0.15);
        int exitButtonHeight = (int)(HEIGHT * 0.06);
        int exitButtonX = (WIDTH - exitButtonWidth) / 2;
        int exitButtonY = (int)(HEIGHT * 0.88);
        exitButtonRect = new Rectangle(exitButtonX, exitButtonY, exitButtonWidth, exitButtonHeight);
        
        // Draw button background
        g2d.setColor(new Color(180, 50, 50));
        g2d.fillRect(exitButtonX, exitButtonY, exitButtonWidth, exitButtonHeight);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(exitButtonX, exitButtonY, exitButtonWidth, exitButtonHeight);
        
        // Draw button text
        g.setFont(new Font("Arial", Font.BOLD, (int)(sectionFontSize * 0.9)));
        String exitText = "EXIT";
        fm = g.getFontMetrics();
        int exitX = exitButtonX + (exitButtonWidth - fm.stringWidth(exitText)) / 2;
        int exitY = exitButtonY + ((exitButtonHeight - fm.getAscent()) / 2) + fm.getAscent();
        g.setColor(Color.WHITE);
        g.drawString(exitText, exitX, exitY);
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

    @Override
    public void mousePressed(MouseEvent e) {
        if (startButtonRect != null && startButtonRect.contains(e.getPoint())) {
            startGame = true;
        } else if (howToPlayButtonRect != null && howToPlayButtonRect.contains(e.getPoint())) {
            showControls = !showControls;
            repaint();
        } else if (exitButtonRect != null && exitButtonRect.contains(e.getPoint())) {
            System.exit(0);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public void reset() {
        startGame = false;
        showControls = false;
    }
}
