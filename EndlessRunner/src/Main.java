import java.awt.*;
import javax.swing.*;

/**
 * ========== MAIN.JAVA ==========
 * Entry point for the Endless Runner game
 * Sets up the game window and manages menu/game switching
 */
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Endless Runner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setUndecorated(true); // Fullscreen
        
        // Create panels
        MenuPanel menuPanel = new MenuPanel();
        GamePanel gamePanel = new GamePanel();
        
        // Create main container with CardLayout
        JPanel container = new JPanel(new CardLayout());
        container.add(menuPanel, "Menu");
        container.add(gamePanel, "Game");
        
        frame.add(container);
        frame.pack();
        
        // Set fullscreen
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);
        
        // Game loop to check if user wants to start
        Timer menuTimer = new Timer(50, e -> {
            if (menuPanel.shouldStartGame()) {
                // Switch to game
                CardLayout layout = (CardLayout) container.getLayout();
                layout.show(container, "Game");
                menuPanel.reset();
                gamePanel.requestFocus();
                ((Timer) e.getSource()).stop();
            }
        });
        menuTimer.start();
    }
}
