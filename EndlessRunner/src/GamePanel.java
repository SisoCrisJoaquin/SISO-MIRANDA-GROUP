import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
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
    private BufferedImage nightBackgroundImage;

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
        
        // Load night background image
        loadNightBackgroundImage();

        gameTimer = new javax.swing.Timer(30, this);
        gameTimer.start();
    }
    
    private void loadNightBackgroundImage() {
        try {
            File imageFile = new File("assets/fir-tree-illustration-of-a-tall-green-pine-tree-tc8euhFV_t.jpg");
            if (imageFile.exists()) {
                nightBackgroundImage = ImageIO.read(imageFile);
            } else {
                System.out.println("Night background image not found at: " + imageFile.getAbsolutePath());
                nightBackgroundImage = null;
            }
        } catch (IOException e) {
            System.err.println("Error loading night background image: " + e.getMessage());
            nightBackgroundImage = null;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!gameOver) {
            // Determine if it's night mode based on score
            boolean isNightMode = isNightTime();
            
            if (isNightMode) {
                // Draw night background
                drawNightBackground(g);
            } else {
                // Draw day background
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
            }

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
        
        // Define cloud patterns that repeat continuously
        int[] cloudX = {100, 200, 300, 420, 550, 600, 700, 850, 950, 1050, 1150, 1300, 1400, 1550, 1700, 1850, 1950, 2100, 2250, 2400, 2550, 2700, 2850, 3000};
        int[] cloudY = {80, 140, 160, 120, 100, 140, 100, 130, 80, 150, 110, 90, 140, 100, 120, 80, 150, 110, 90, 140, 100, 120, 150, 80};
        int[] cloudW = {80, 60, 50, 100, 70, 90, 90, 70, 80, 60, 100, 90, 70, 80, 100, 90, 70, 80, 100, 90, 70, 80, 90, 100};
        int[] cloudH = {40, 30, 25, 45, 35, 42, 42, 38, 40, 30, 45, 42, 35, 40, 45, 42, 35, 40, 45, 42, 35, 40, 42, 45};
        
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
        // Define bush positions that repeat continuously
        int[] bushX = {150, 350, 450, 600, 700, 900, 1050, 1200, 1350, 1550, 1700, 1850, 2000, 2150, 2350, 2500, 2650, 2800, 2950, 3100};
        int[] bushY = {GROUND_Y - 90, GROUND_Y - 85, GROUND_Y - 85, GROUND_Y - 95, GROUND_Y - 90, GROUND_Y - 85, GROUND_Y - 95, GROUND_Y - 88, GROUND_Y - 90, GROUND_Y - 85, GROUND_Y - 95, GROUND_Y - 85, GROUND_Y - 90, GROUND_Y - 88, GROUND_Y - 95, GROUND_Y - 90, GROUND_Y - 85, GROUND_Y - 95, GROUND_Y - 88, GROUND_Y - 90};
        
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

    private boolean isNightTime() {
        int score = scoreboard.getScore();
        int cycleScore = score % 1000; // Cycle every 1000 points
        return cycleScore >= 500; // Night is second half of cycle (500-999)
    }

    private void updateSkyColor() {
        int score = scoreboard.getScore();
        int cycleScore = score % 1000; // Cycle every 1000 points
        
        if (cycleScore < 500) {
            // Day: Light blue sky
            skyColor = new Color(135, 206, 235);
        } else {
            // Night: Dark blue sky
            skyColor = new Color(25, 35, 65);
        }
    }

    private void drawNightBackground(Graphics g) {
        if (nightBackgroundImage != null) {
            // Tile the image to fill the entire background
            tileBackgroundImage(g, nightBackgroundImage);
        } else {
            // Fallback to original night background if image not loaded
            drawNightBackgroundFallback(g);
        }
        
        // Draw ground/grass
        g.setColor(new Color(20, 100, 50)); // Darker green for night
        g.fillRect(0, GROUND_Y - 80, WIDTH, HEIGHT - (GROUND_Y - 80));
        
        // Draw ground line
        g.setColor(new Color(0, 70, 0));
        g.fillRect(0, GROUND_Y - 80, WIDTH, 10);
        
        // Draw soil/dirt
        g.setColor(new Color(80, 50, 10));
        g.fillRect(0, GROUND_Y - 70, WIDTH, 70);
    }
    
    private void tileBackgroundImage(Graphics g, BufferedImage img) {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        
        // Calculate starting position based on background offset
        int offsetX = backgroundOffset % imgWidth;
        
        // Tile horizontally and vertically to fill entire screen without gaps
        for (int y = 0; y < HEIGHT; y += imgHeight) {
            for (int x = -offsetX; x < WIDTH; x += imgWidth) {
                g.drawImage(img, x, y, imgWidth, imgHeight, null);
            }
        }
    }
    
    private void drawNightBackgroundFallback(Graphics g) {
        // Draw night sky
        g.setColor(new Color(25, 35, 65)); // Dark blue night sky
        g.fillRect(0, 0, WIDTH, GROUND_Y - 80);
        
        // Draw stars
        drawStars(g);
    }

    private void drawStars(Graphics g) {
        g.setColor(Color.WHITE);
        
        // Create deterministic star pattern based on background offset
        int seedOffset = backgroundOffset % 3200;
        Random rand = new Random(12345 + seedOffset / 100);
        
        for (int i = 0; i < 50; i++) {
            int starX = (rand.nextInt(3200) - backgroundOffset) % 3200;
            if (starX < 0) starX += 3200;
            
            int starY = 20 + rand.nextInt(GROUND_Y - 100);
            int starSize = 1 + rand.nextInt(3);
            
            g.fillRect(starX, starY, starSize, starSize);
            
            // Draw wrapped stars
            if (starX + starSize < WIDTH) {
                int wrappedX = starX + 3200;
                if (wrappedX < WIDTH) {
                    g.fillRect(wrappedX, starY, starSize, starSize);
                }
            }
        }
    }

    private void drawNightForest(Graphics g) {
        // Define tree positions that repeat
        int[] treeX = {100, 250, 400, 550, 700, 850, 1000, 1150, 1300, 1450, 1600, 1750, 1900, 2050, 2200, 2350, 2500, 2650, 2800, 2950};
        int[] treeHeight = {80, 100, 90, 110, 85, 95, 100, 90, 110, 85, 95, 100, 90, 85, 100, 95, 90, 110, 85, 95};
        
        for (int i = 0; i < treeX.length; i++) {
            int scrolledX = (treeX[i] - backgroundOffset) % 3200;
            if (scrolledX < 0) {
                scrolledX += 3200;
            }
            
            // Draw current position
            drawTree(g, scrolledX, GROUND_Y - 80, treeHeight[i]);
            
            // Draw wrapped version if needed
            if (scrolledX < WIDTH) {
                int wrappedX = scrolledX + 3200;
                if (wrappedX < WIDTH) {
                    drawTree(g, wrappedX, GROUND_Y - 80, treeHeight[i]);
                }
            }
        }
        
        // Draw background forest layer
        g.setColor(new Color(15, 60, 30));
        int[] bgTreeX = {50, 200, 350, 500, 650, 800, 950, 1100, 1250, 1400, 1550, 1700, 1850, 2000, 2150, 2300, 2450, 2600, 2750, 2900, 3050};
        int[] bgTreeHeight = {50, 60, 55, 65, 50, 60, 55, 65, 50, 60, 55, 65, 50, 60, 55, 65, 50, 60, 55, 65, 50};
        
        for (int i = 0; i < bgTreeX.length; i++) {
            int scrolledX = (bgTreeX[i] - backgroundOffset) % 3200;
            if (scrolledX < 0) {
                scrolledX += 3200;
            }
            
            drawBackgroundTree(g, scrolledX, GROUND_Y - 120, bgTreeHeight[i]);
            
            if (scrolledX < WIDTH) {
                int wrappedX = scrolledX + 3200;
                if (wrappedX < WIDTH) {
                    drawBackgroundTree(g, wrappedX, GROUND_Y - 120, bgTreeHeight[i]);
                }
            }
        }
    }

    private void drawTree(Graphics g, int x, int y, int height) {
        // Draw trunk
        g.setColor(new Color(60, 40, 20));
        g.fillRect(x + 5, y - height + 30, 10, height - 30);
        
        // Draw foliage (triangle)
        g.setColor(new Color(20, 80, 40));
        int[] xPoints = {x, x + 20, x + 10};
        int[] yPoints = {y - height + 30, y - height + 30, y - height};
        ((Graphics2D) g).fillPolygon(xPoints, yPoints, 3);
        
        // Add depth with darker shade
        g.setColor(new Color(10, 50, 25));
        int[] xPoints2 = {x + 3, x + 17, x + 10};
        int[] yPoints2 = {y - height + 40, y - height + 40, y - height + 5};
        ((Graphics2D) g).fillPolygon(xPoints2, yPoints2, 3);
    }

    private void drawBackgroundTree(Graphics g, int x, int y, int height) {
        // Smaller trees for background
        g.fillRect(x + 3, y - height + 20, 6, height - 20);
        
        int[] xPoints = {x, x + 12, x + 6};
        int[] yPoints = {y - height + 20, y - height + 20, y - height};
        ((Graphics2D) g).fillPolygon(xPoints, yPoints, 3);
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
        updateSkyColor();
        repaint();
    }
}
