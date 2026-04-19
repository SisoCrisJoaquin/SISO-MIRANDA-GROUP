import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int GROUND_Y = HEIGHT - 100;
    private static final int OBSTACLE_SPAWN_RATE = 100; // Frames between obstacle spawns
    private static final int BASE_SPEED = 5;

    private Player player;
    private Scoreboard scoreboard;
    private ArrayList<Obstacle> obstacles;
    private Timer gameTimer;
    private int obstacleSpawnCounter;
    private int currentSpeed;
    private boolean gameOver;
    private Color skyColor;
    private Color grassColor;
    private int backgroundOffset = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        player = new Player(GROUND_Y, WIDTH);
        scoreboard = new Scoreboard();
        obstacles = new ArrayList<>();
        obstacleSpawnCounter = 0;
        currentSpeed = BASE_SPEED;
        gameOver = false;
        backgroundOffset = 0;
        skyColor = new Color(135, 206, 235); // Light blue
        grassColor = new Color(34, 177, 76); // Green

        gameTimer = new javax.swing.Timer(30, this);
        gameTimer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!gameOver) {
            // Draw sky
            g.setColor(skyColor);
            g.fillRect(0, 0, WIDTH, GROUND_Y - 80);

            // Draw clouds
            drawClouds(g);

            // Draw ground/grass
            g.setColor(grassColor);
            g.fillRect(0, GROUND_Y - 80, WIDTH, HEIGHT - (GROUND_Y - 80));

            // Draw bushes on the grass
            drawBushes(g);

            // Draw ground line (darker green)
            g.setColor(new Color(0, 100, 0));
            g.fillRect(0, GROUND_Y - 80, WIDTH, 10);

            // Draw soil/dirt
            g.setColor(new Color(139, 69, 19));
            g.fillRect(0, GROUND_Y - 70, WIDTH, 70);

            // Draw player
            player.draw(g);

            // Draw obstacles
            for (Obstacle obstacle : obstacles) {
                obstacle.draw(g);
            }

            // Draw scoreboard
            scoreboard.draw(g);
        } else {
            // Draw game over screen
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            FontMetrics fm = g.getFontMetrics();
            String gameOverText = "GAME OVER";
            int x = (WIDTH - fm.stringWidth(gameOverText)) / 2;
            g.drawString(gameOverText, x, 150);

            g.setFont(new Font("Arial", Font.BOLD, 40));
            String finalScore = "Final Score: " + scoreboard.getScore();
            fm = g.getFontMetrics();
            x = (WIDTH - fm.stringWidth(finalScore)) / 2;
            g.drawString(finalScore, x, 220);

            g.setFont(new Font("Arial", Font.PLAIN, 30));
            String restartText = "Press 'R' to Restart or 'Q' to Quit";
            fm = g.getFontMetrics();
            x = (WIDTH - fm.stringWidth(restartText)) / 2;
            g.drawString(restartText, x, 350);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // Update player
            player.update();

            // Update obstacles
            for (int i = obstacles.size() - 1; i >= 0; i--) {
                obstacles.get(i).update();
                if (obstacles.get(i).isOffScreen()) {
                    obstacles.remove(i);
                }
            }

            // Spawn obstacles
            obstacleSpawnCounter++;
            if (obstacleSpawnCounter >= OBSTACLE_SPAWN_RATE) {
                obstacles.add(new Obstacle(WIDTH, GROUND_Y, currentSpeed));
                obstacleSpawnCounter = 0;
            }

            // Check collisions with obstacles
            for (Obstacle obstacle : obstacles) {
                if (player.getBounds().intersects(obstacle.getBounds())) {
                    scoreboard.loseLife();
                    if (scoreboard.isGameOver()) {
                        gameOver = true;
                    } else {
                        player.reset(GROUND_Y, WIDTH);
                        scoreboard.resetScore();
                        obstacles.clear();
                    }
                    break;
                }
            }

            // Update scoreboard
            scoreboard.update();

            // Update background offset for parallax scrolling
            backgroundOffset += currentSpeed;
            // Keep offset within bounds to prevent overflow
            if (backgroundOffset > 3200) {
                backgroundOffset -= 3200;
            }

            // Update sky based on score
            updateSkyColor();

            // Increase speed slightly as score increases
            currentSpeed = BASE_SPEED + (scoreboard.getScore() / 200);

            repaint();
        }
    }

    private void drawClouds(Graphics g) {
        g.setColor(new Color(255, 255, 255, 220)); // White clouds with slight transparency
        
        // Define cloud patterns that repeat
        int[] cloudX = {100, 300, 420, 600, 700};
        int[] cloudY = {80, 160, 120, 140, 100};
        int[] cloudW = {80, 50, 100, 70, 90};
        int[] cloudH = {40, 25, 45, 35, 42};
        
        // Draw clouds with parallax scrolling and wrapping
        for (int i = 0; i < cloudX.length; i++) {
            int scrolledX = (cloudX[i] - backgroundOffset) % 3200;
            if (scrolledX < 0) {
                scrolledX += 3200;
            }
            
            // Draw current position
            drawCloud(g, scrolledX, cloudY[i], cloudW[i], cloudH[i]);
            
            // Draw wrapped version if needed
            if (scrolledX + cloudW[i] < WIDTH) {
                int wrappedX = scrolledX + 3200;
                if (wrappedX < WIDTH) {
                    drawCloud(g, wrappedX, cloudY[i], cloudW[i], cloudH[i]);
                }
            }
        }
    }

    private void drawCloud(Graphics g, int x, int y, int width, int height) {
        // Draw cloud using circles to create a fluffy effect
        g.fillOval(x, y, width / 3, height);
        g.fillOval(x + width / 4, y - height / 3, width / 2, height);
        g.fillOval(x + width / 2, y, width / 3, height);
    }

    private void drawBushes(Graphics g) {
        // Define bush positions that repeat
        int[] bushX = {150, 450, 700};
        int[] bushY = {GROUND_Y - 90, GROUND_Y - 85, GROUND_Y - 95};
        
        // Draw bushes with parallax scrolling and wrapping
        for (int i = 0; i < bushX.length; i++) {
            int scrolledX = (bushX[i] - backgroundOffset) % 3200;
            if (scrolledX < 0) {
                scrolledX += 3200;
            }
            
            // Draw current position
            drawBush(g, scrolledX, bushY[i]);
            
            // Draw wrapped version if needed
            if (scrolledX + 80 < WIDTH) {
                int wrappedX = scrolledX + 3200;
                if (wrappedX < WIDTH) {
                    drawBush(g, wrappedX, bushY[i]);
                }
            }
        }
    }

    private void drawBush(Graphics g, int x, int y) {
        // Draw bush as grouped circles (shrub effect)
        g.setColor(new Color(34, 150, 34)); // Darker green
        
        // Main bush body
        g.fillOval(x, y, 80, 60);
        g.fillOval(x + 20, y - 30, 70, 70);
        g.fillOval(x - 15, y - 10, 70, 60);
    }

    private void updateSkyColor() {
        int score = scoreboard.getScore();
        
        if (score < 100) {
            // Day: Light blue sky
            skyColor = new Color(135, 206, 235);
        } else if (score < 500) {
            // Transition from light blue to dark blue (100-499)
            float progress = Math.min((score - 100) / 400.0f, 1.0f); // 0 to 1 over 400 points
            int r = (int) (135 * (1 - progress * 0.7));
            int gb = (int) (206 * (1 - progress * 0.8));
            skyColor = new Color(r, gb, gb);
        } else {
            // Loop the cycle after score 500
            int cycleScore = (score - 500) % 400; // Repeats every 400 points
            
            if (cycleScore < 200) {
                // First half: transition from day to night
                float progress = cycleScore / 200.0f;
                int r = (int) (135 * (1 - progress * 0.7));
                int gb = (int) (206 * (1 - progress * 0.8));
                skyColor = new Color(r, gb, gb);
            } else {
                // Second half: transition from night back to day
                float progress = (cycleScore - 200) / 200.0f;
                int r = (int) (135 * (1 - (1 - progress) * 0.7));
                int gb = (int) (206 * (1 - (1 - progress) * 0.8));
                skyColor = new Color(r, gb, gb);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                player.jump();
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                restartGame();
            } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                System.exit(0);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private void restartGame() {
        player.reset(GROUND_Y, WIDTH);
        scoreboard.reset();
        obstacles.clear();
        obstacleSpawnCounter = 0;
        currentSpeed = BASE_SPEED;
        backgroundOffset = 0;
        gameOver = false;
        skyColor = new Color(135, 206, 235);
        repaint();
    }
}
