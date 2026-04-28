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
    private Rectangle savesButtonRect; // Saves button
    private Rectangle exitButtonRect; // Exit button
    private boolean showControls = false; // Toggle for showing controls
    private boolean showSaves = false; // Toggle for showing saves
    private Rectangle backFromSavesButtonRect; // Back button when viewing saves
    private Rectangle loadSave1ButtonRect; // Load save 1 button
    private Rectangle loadSave2ButtonRect; // Load save 2 button
    private BufferedImage wallpaperImage; // Wallpaper for menu background
    private BufferedImage titleBackgroundImage; // Title background image
    private GamePanel gamePanel; // Reference to game panel for loading saves
    private MusicPlayer backgroundMusic; // Background music player

    public MenuPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(135, 206, 235)); // Light blue background
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        
        // Load wallpaper image
        wallpaperImage = ImageLoader.loadAndScaleBackgroundImage("/download.jpg", WIDTH, HEIGHT);
        
        // Load title background image
        titleBackgroundImage = ImageLoader.loadImage("/painting-ink-brush-portable-network-graphics-article-title-364854df596df777851b1411b70b6708.png");
        
        // Initialize and start background music
        String musicPath = "resources/moodmode-8-bit-game-158815.mp3";
        backgroundMusic = new MusicPlayer(musicPath);
        backgroundMusic.start();
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

        if (showSaves) {
            drawSavesMenu(g);
        } else {
            drawMainMenu(g);
        }
    }

    private void drawMainMenu(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Calculate responsive font sizes
        int titleFontSize = Math.max(40, WIDTH / 15);
        int startFontSize = Math.max(24, WIDTH / 40);
        int sectionFontSize = Math.max(18, WIDTH / 50);
        int controlFontSize = Math.max(16, WIDTH / 60);
        int descriptionFontSize = Math.max(12, WIDTH / 80);

        // Draw semi-transparent overlay for better text readability
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw enhanced title with shadow effect
        g.setFont(new Font("Arial", Font.BOLD, titleFontSize));
        FontMetrics fm = g.getFontMetrics();
        String title = "Motorcyclist seeker";
        int x = (WIDTH - fm.stringWidth(title)) / 2;
        int titleY = (int)(HEIGHT * 0.12);
        
        // Title shadow
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.drawString(title, x + 3, titleY + 3);
        
        // Title background image if available
        if (titleBackgroundImage != null) {
            int bgPadding = 50;
            int bgWidth = fm.stringWidth(title) + (bgPadding * 2);
            int bgHeight = fm.getAscent() + fm.getDescent() + (bgPadding * 2);
            int bgX = x - bgPadding;
            int bgY = titleY - fm.getAscent() - bgPadding;
            g2d.drawImage(titleBackgroundImage, bgX, bgY, bgWidth, bgHeight, null);
        }
        
        // Main title text with glow effect
        g.setColor(new Color(255, 255, 100));
        g.drawString(title, x, titleY);

        // Draw description/subtitle below title
        g.setColor(new Color(200, 200, 255));
        g.setFont(new Font("Arial", Font.ITALIC, (int)(sectionFontSize * 0.8)));
        String subtitle = "Endless Runner Game";
        fm = g.getFontMetrics();
        int subtitleX = (WIDTH - fm.stringWidth(subtitle)) / 2;
        int subtitleY = titleY + (int)(HEIGHT * 0.06);
        g.drawString(subtitle, subtitleX, subtitleY);

        // Helper method to draw button with shadow and description
        drawButtonWithDescription(g2d, (WIDTH - (int)(WIDTH * 0.25)) / 2, (int)(HEIGHT * 0.35), 
            (int)(WIDTH * 0.25), (int)(HEIGHT * 0.08), 
            "START GAME", "Begin your journey", 
            new Color(50, 150, 50), new Color(30, 100, 30));
        startButtonRect = new Rectangle((WIDTH - (int)(WIDTH * 0.25)) / 2, (int)(HEIGHT * 0.35), 
            (int)(WIDTH * 0.25), (int)(HEIGHT * 0.08));

        // Draw Saves button
        drawButtonWithDescription(g2d, (WIDTH - (int)(WIDTH * 0.2)) / 2, (int)(HEIGHT * 0.48), 
            (int)(WIDTH * 0.2), (int)(HEIGHT * 0.06), 
            "SAVES", "Load saved games", 
            new Color(120, 80, 150), new Color(80, 50, 100));
        savesButtonRect = new Rectangle((WIDTH - (int)(WIDTH * 0.2)) / 2, (int)(HEIGHT * 0.48), 
            (int)(WIDTH * 0.2), (int)(HEIGHT * 0.06));

        // Draw How To Play button
        drawButtonWithDescription(g2d, (WIDTH - (int)(WIDTH * 0.2)) / 2, (int)(HEIGHT * 0.56), 
            (int)(WIDTH * 0.2), (int)(HEIGHT * 0.06), 
            showControls ? "HIDE CONTROLS" : "HOW TO PLAY", "View game controls", 
            new Color(70, 120, 180), new Color(50, 80, 130));
        howToPlayButtonRect = new Rectangle((WIDTH - (int)(WIDTH * 0.2)) / 2, (int)(HEIGHT * 0.56), 
            (int)(WIDTH * 0.2), (int)(HEIGHT * 0.06));

        // Draw controls only if showControls is true
        if (showControls) {
            drawControlsPanel(g2d, controlFontSize, descriptionFontSize);
        }

        // Calculate objective and exit button positions dynamically based on controls visibility
        int objectiveY = showControls ? (int)(HEIGHT * 0.82) : (int)(HEIGHT * 0.75);
        int exitButtonY = showControls ? (int)(HEIGHT * 0.92) : (int)(HEIGHT * 0.88);

        // Draw game objective with styled background
        drawObjectivePanel(g2d, sectionFontSize, objectiveY);

        // Draw Exit button
        drawButtonWithDescription(g2d, (WIDTH - (int)(WIDTH * 0.15)) / 2, exitButtonY, 
            (int)(WIDTH * 0.15), (int)(HEIGHT * 0.06), 
            "EXIT", "Quit game", 
            new Color(180, 50, 50), new Color(130, 30, 30));
        exitButtonRect = new Rectangle((WIDTH - (int)(WIDTH * 0.15)) / 2, exitButtonY, 
            (int)(WIDTH * 0.15), (int)(HEIGHT * 0.06));
    }

    /**
     * Helper method to draw button with shadow and description
     */
    private void drawButtonWithDescription(Graphics2D g2d, int x, int y, int width, int height,
            String buttonText, String description, Color mainColor, Color shadowColor) {
        
        // Draw shadow
        g2d.setColor(shadowColor);
        g2d.fillRoundRect(x + 3, y + 3, width, height, 10, 10);
        
        // Draw button background with gradient effect
        g2d.setColor(mainColor);
        g2d.fillRoundRect(x, y, width, height, 10, 10);
        
        // Draw button border
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width, height, 10, 10);
        
        // Draw button text
        g2d.setFont(new Font("Arial", Font.BOLD, (int)(height * 0.5)));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (width - fm.stringWidth(buttonText)) / 2;
        int textY = y + ((height - fm.getAscent()) / 2) + fm.getAscent();
        g2d.setColor(Color.WHITE);
        g2d.drawString(buttonText, textX, textY);
    }

    /**
     * Helper method to draw controls panel
     */
    private void drawControlsPanel(Graphics2D g2d, int controlFontSize, int descriptionFontSize) {
        // Semi-transparent background for controls
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(50, (int)(HEIGHT * 0.62), WIDTH - 100, (int)(HEIGHT * 0.18), 10, 10);
        
        // Border
        g2d.setColor(new Color(100, 150, 200));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(50, (int)(HEIGHT * 0.62), WIDTH - 100, (int)(HEIGHT * 0.18), 10, 10);
        
        // Controls title
        g2d.setFont(new Font("Arial", Font.BOLD, controlFontSize));
        g2d.setColor(new Color(255, 215, 0));
        g2d.drawString("CONTROLS:", 80, (int)(HEIGHT * 0.66));
        
        // Controls list
        g2d.setFont(new Font("Arial", Font.PLAIN, controlFontSize - 2));
        g2d.setColor(Color.WHITE);
        int leftMargin = 100;
        int controlY = (int)(HEIGHT * 0.71);
        int controlSpacing = (int)(HEIGHT * 0.035);
        
        g2d.drawString("SPACEBAR - Jump", leftMargin, controlY);
        g2d.drawString("W / S - Change Lane", leftMargin, controlY + controlSpacing);
        g2d.drawString("ESC - Pause/Unpause", leftMargin, controlY + controlSpacing * 2);
        g2d.drawString("R - Restart (on Game Over)", leftMargin + (int)(WIDTH * 0.25), controlY);
        g2d.drawString("M - Return to Menu (on Game Over)", leftMargin + (int)(WIDTH * 0.25), controlY + controlSpacing);
        g2d.drawString("SHIFT - Boost", leftMargin + (int)(WIDTH * 0.25), controlY + controlSpacing * 2);
    }

    /**
     * Helper method to draw objective panel
     */
    private void drawObjectivePanel(Graphics2D g2d, int sectionFontSize, int panelY) {
        // Objective background panel
        int panelHeight = (int)(HEIGHT * 0.06);
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillRoundRect(100, panelY, WIDTH - 200, panelHeight, 8, 8);
        
        // Objective border
        g2d.setColor(new Color(255, 215, 0));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(100, panelY, WIDTH - 200, panelHeight, 8, 8);
        
        // Objective text
        g2d.setFont(new Font("Arial", Font.BOLD, sectionFontSize));
        g2d.setColor(new Color(255, 220, 100));
        String objective = "Objective: Avoid obstacles and survive as long as possible!";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (WIDTH - fm.stringWidth(objective)) / 2;
        int y = panelY + ((panelHeight - fm.getAscent()) / 2) + fm.getAscent();
        g2d.drawString(objective, x, y);
    }

    private void drawSavesMenu(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Dark overlay
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        // Title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        FontMetrics fm = g.getFontMetrics();
        String title = "SAVES";
        int x = (WIDTH - fm.stringWidth(title)) / 2;
        g.drawString(title, x, 150);
        
        // Draw Save 1
        int buttonWidth = (int)(WIDTH * 0.3);
        int buttonHeight = (int)(HEIGHT * 0.12);
        int buttonX = (WIDTH - buttonWidth) / 2;
        int buttonY = 250;
        loadSave1ButtonRect = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
        
        g2d.setColor(new Color(80, 120, 80));
        g2d.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(buttonX, buttonY, buttonWidth, buttonHeight);
        
        g.setFont(new Font("Arial", Font.BOLD, 24));
        if (GameSave.saveExists(1)) {
            GameSave save1 = GameSave.loadFromDisk(1);
            g.drawString("SAVE SLOT 1", buttonX + 20, buttonY + 30);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Score: " + save1.score, buttonX + 20, buttonY + 60);
            g.drawString("Lives: " + save1.lives, buttonX + 20, buttonY + 85);
        } else {
            g.drawString("SAVE SLOT 1 - EMPTY", buttonX + 20, buttonY + 60);
        }
        
        // Draw Save 2
        int buttonY2 = buttonY + buttonHeight + 40;
        loadSave2ButtonRect = new Rectangle(buttonX, buttonY2, buttonWidth, buttonHeight);
        
        g2d.setColor(new Color(80, 120, 80));
        g2d.fillRect(buttonX, buttonY2, buttonWidth, buttonHeight);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(buttonX, buttonY2, buttonWidth, buttonHeight);
        
        g.setFont(new Font("Arial", Font.BOLD, 24));
        if (GameSave.saveExists(2)) {
            GameSave save2 = GameSave.loadFromDisk(2);
            g.drawString("SAVE SLOT 2", buttonX + 20, buttonY2 + 30);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Score: " + save2.score, buttonX + 20, buttonY2 + 60);
            g.drawString("Lives: " + save2.lives, buttonX + 20, buttonY2 + 85);
        } else {
            g.drawString("SAVE SLOT 2 - EMPTY", buttonX + 20, buttonY2 + 60);
        }
        
        // Draw Back button
        int backButtonWidth = (int)(WIDTH * 0.2);
        int backButtonHeight = (int)(HEIGHT * 0.06);
        int backButtonX = (WIDTH - backButtonWidth) / 2;
        int backButtonY = (int)(HEIGHT * 0.88);
        backFromSavesButtonRect = new Rectangle(backButtonX, backButtonY, backButtonWidth, backButtonHeight);
        
        g2d.setColor(new Color(150, 150, 50));
        g2d.fillRect(backButtonX, backButtonY, backButtonWidth, backButtonHeight);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(backButtonX, backButtonY, backButtonWidth, backButtonHeight);
        
        g.setFont(new Font("Arial", Font.BOLD, 24));
        String backText = "BACK";
        fm = g.getFontMetrics();
        int backX = backButtonX + (backButtonWidth - fm.stringWidth(backText)) / 2;
        int backY = backButtonY + ((backButtonHeight - fm.getAscent()) / 2) + fm.getAscent();
        g.drawString(backText, backX, backY);
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
        if (showSaves) {
            // Handle saves menu clicks
            if (backFromSavesButtonRect != null && backFromSavesButtonRect.contains(e.getPoint())) {
                showSaves = false;
                repaint();
            } else if (loadSave1ButtonRect != null && loadSave1ButtonRect.contains(e.getPoint()) && GameSave.saveExists(1)) {
                if (gamePanel != null) {
                    gamePanel.loadGame(1);
                    startGame = true;
                }
            } else if (loadSave2ButtonRect != null && loadSave2ButtonRect.contains(e.getPoint()) && GameSave.saveExists(2)) {
                if (gamePanel != null) {
                    gamePanel.loadGame(2);
                    startGame = true;
                }
            }
        } else {
            // Handle main menu clicks
            if (startButtonRect != null && startButtonRect.contains(e.getPoint())) {
                startGame = true;
            } else if (savesButtonRect != null && savesButtonRect.contains(e.getPoint())) {
                showSaves = true;
                repaint();
            } else if (howToPlayButtonRect != null && howToPlayButtonRect.contains(e.getPoint())) {
                showControls = !showControls;
                repaint();
            } else if (exitButtonRect != null && exitButtonRect.contains(e.getPoint())) {
                System.exit(0);
            }
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
        showSaves = false;
        // NOTE: Music is NOT restarted here - it's managed by Main.java only
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public boolean shouldStartGameWithLoad() {
        return startGame;
    }

    /**
     * Stop the background music
     * Call this before switching to the game panel
     */
    public void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    /**
     * Resume the background music
     * Call this when returning to the menu
     */
    public void resumeMusic() {
        if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.start();
        }
    }

    /**
     * Cleanup music resources when exiting
     * Call this when the application is closing
     */
    public void cleanup() {
        stopMusic();
    }
}
