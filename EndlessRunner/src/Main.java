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
        
        // Pass gamePanel reference to menuPanel
        menuPanel.setGamePanel(gamePanel);
        
        // Create main container with CardLayout
        JPanel container = new JPanel(new CardLayout());
        container.add(menuPanel, "Menu");
        container.add(gamePanel, "Game");
        
        frame.add(container);
        frame.pack();
        
        // Set fullscreen
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);
        
        // Use array wrapper to allow reference in inner class
        Timer[] menuTimerRef = new Timer[1];
        
        // Game loop to check if user wants to start
        Timer menuTimer = new Timer(50, e -> {
            if (menuPanel.shouldStartGame()) {
                // Switch to game
                menuPanel.stopMusic(); // Stop menu music before switching to game
                try {
                    Thread.sleep(100); // Give menu music time to fully stop
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                CardLayout layout = (CardLayout) container.getLayout();
                layout.show(container, "Game");
                gamePanel.startGameMusic(); // Start game music when entering game
                menuPanel.reset(); // Reset UI state AFTER music is handled
                gamePanel.requestFocus();
                ((Timer) e.getSource()).stop();
                
                // Start game monitoring timer to detect return to menu
                Timer gameTimer = new Timer(50, ev -> {
                    if (gamePanel.shouldReturnToMenu()) {
                        // Switch back to menu
                        gamePanel.stopGameMusic(); // Stop game music before switching to menu
                        try {
                            Thread.sleep(100); // Give game music time to fully stop
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                        CardLayout layout2 = (CardLayout) container.getLayout();
                        layout2.show(container, "Menu");
                        gamePanel.resetGameState();
                        menuPanel.requestFocus();
                        menuPanel.resumeMusic(); // Resume menu music when returning from game
                        ((Timer) ev.getSource()).stop();
                        menuTimerRef[0].start(); // Restart menu timer
                    }
                });
                gameTimer.start();
            }
        });
        menuTimerRef[0] = menuTimer;
        menuTimer.start();
        
        // Shutdown hook to ensure all music is stopped when application closes
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            menuPanel.cleanup();
            gamePanel.cleanup();
        }));
    }
}
