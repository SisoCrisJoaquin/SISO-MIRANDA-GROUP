import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

/**
 * ========== GAMEPANEL.JAVA ==========
 * Main game loop and rendering engine
 * Handles player input, obstacle spawning, collisions, and smooth day/night transitions
 */
public class GamePanel extends JPanel implements KeyListener, ActionListener, MouseListener {
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int WIDTH = screenSize.width;
    private static final int HEIGHT = screenSize.height;
    private static final int GROUND_Y = (int)(HEIGHT * 0.85); // 85% down from top for ground
    private static final int OBSTACLE_SPAWN_RATE = 100; // Frames between obstacle spawns
    private static final int BASE_SPEED = 8;
    private static final int OBSTACLE_GRACE_DISTANCE = 250; // Minimum distance from player spawn to obstacle spawn
    private static final int BOOST_SPAWN_RATE = 800; // Frames between boost item spawns (very rare, different from obstacles)
    private static final int CLOUD_SPAWN_RATE = 120; // Frames between cloud spawns

    private Player player;
    private Scoreboard scoreboard;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<BoostItem> boostItems;
    private ArrayList<Cloud> clouds;
    private Timer gameTimer;
    private int obstacleSpawnCounter;
    private int boostSpawnCounter;
    private int cloudSpawnCounter;
    private int currentSpeed;
    private boolean gameOver;
    private Color skyColor;
    private Color grassColor;
    private int backgroundOffset = 0;
    private BufferedImage backgroundImage;
    private BufferedImage backgroundNightImage;
    private BufferedImage roadImage; // Road image instead of drawn road
    private BufferedImage roadNightImage; // Night version of road
    private boolean useBackgroundImage;
    private boolean useRoadImage;
    private boolean isPaused = false;
    private Rectangle continueButtonRect; // Pause menu buttons
    private Rectangle mainMenuButtonRect;
    private boolean returnToMenu = false; // Flag to return to main menu

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);

        player = new Player(GROUND_Y, WIDTH);
        scoreboard = new Scoreboard();
        obstacles = new ArrayList<>();
        boostItems = new ArrayList<>();
        clouds = new ArrayList<>();
        obstacleSpawnCounter = 0;
        boostSpawnCounter = 300; // Offset so boost spawns at different time than obstacles
        cloudSpawnCounter = 0;
        currentSpeed = BASE_SPEED;
        gameOver = false;
        backgroundOffset = 0;
        skyColor = new Color(135, 206, 235); // Light blue
        grassColor = new Color(34, 177, 76); // Green
        
        // Try to load and scale background images
        backgroundImage = ImageLoader.loadAndScaleBackgroundImage("/background.png", WIDTH, GROUND_Y);
        backgroundNightImage = ImageLoader.loadAndScaleBackgroundImage("/background-night.png", WIDTH, GROUND_Y);
        useBackgroundImage = (backgroundImage != null || backgroundNightImage != null);
        
        // Load road images
        roadImage = ImageLoader.loadAndScaleBackgroundImage("/road.png", WIDTH, HEIGHT - (GROUND_Y - 80));
        roadNightImage = ImageLoader.loadAndScaleBackgroundImage("/road-night.png", WIDTH, HEIGHT - (GROUND_Y - 80));
        useRoadImage = (roadImage != null || roadNightImage != null);

        gameTimer = new javax.swing.Timer(30, this);
        gameTimer.start();
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
                // Draw night sky
                g.setColor(new Color(25, 35, 65));
                g.fillRect(0, 0, WIDTH, GROUND_Y - 80);
                
                // Draw night background image if available
                if (backgroundNightImage != null) {
                    backgroundImage = backgroundNightImage;
                    drawBackgroundImage(g2d);
                }
            } else {
                // Draw day background
                if (backgroundImage != null) {
                    drawBackgroundImage(g2d);
                } else {
                    g.setColor(skyColor);
                    g.fillRect(0, 0, WIDTH, GROUND_Y - 80);
                }
            }

            // Draw road track platform
            drawRoadImage(g2d, isNightMode);

            // Draw clouds
            for (Cloud cloud : clouds) {
                cloud.draw(g2d);
            }

            // Draw by lane for proper depth layering
            // Lane 0 (top/furthest) -> Lane 1 (middle) -> Lane 2 (bottom/closest)
            for (int lane = 0; lane <= 2; lane++) {
                // Draw player if in this lane
                if (player.getLane() == lane) {
                    player.draw(g);
                }
                
                // Draw all obstacles in this lane
                for (Obstacle obstacle : obstacles) {
                    if (obstacle.getLane() == lane) {
                        obstacle.draw(g);
                    }
                }
                
                // Draw all boost items in this lane
                for (BoostItem boost : boostItems) {
                    if (boost.getLane() == lane) {
                        boost.draw(g);
                    }
                }
            }

            // Draw scoreboard
            scoreboard.draw(g);
            
            // Draw boost fuel bar under lives
            drawBoostBar(g);
            
            // Draw pause menu if paused
            if (isPaused) {
                drawPauseMenu(g);
            }
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
        if (!gameOver && !isPaused) {
            // Update player boost fuel
            player.updateBoostFuel();
            
            // Update player with current game speed for animation
            player.update((int)(currentSpeed * player.getSpeedMultiplier()));

            // Update obstacles
            for (int i = obstacles.size() - 1; i >= 0; i--) {
                obstacles.get(i).update(player.getSpeedMultiplier());
                if (obstacles.get(i).isOffScreen()) {
                    obstacles.remove(i);
                }
            }
            
            // Update boost items
            for (int i = boostItems.size() - 1; i >= 0; i--) {
                boostItems.get(i).update(player.getSpeedMultiplier());
                if (boostItems.get(i).isOffScreen()) {
                    boostItems.remove(i);
                }
            }
            
            // Update clouds (move independently of game speed)
            for (int i = clouds.size() - 1; i >= 0; i--) {
                clouds.get(i).update();
                if (clouds.get(i).isOffScreen(WIDTH)) {
                    clouds.remove(i);
                }
            }

            // Spawn obstacles in random lanes with grace distance
            obstacleSpawnCounter++;
            if (obstacleSpawnCounter >= OBSTACLE_SPAWN_RATE) {
                int randomLane = new Random().nextInt(3);
                // Spawn obstacles further right with grace distance applied
                int spawnX = WIDTH + OBSTACLE_GRACE_DISTANCE;
                obstacles.add(new Obstacle(spawnX, GROUND_Y, currentSpeed, randomLane));
                obstacleSpawnCounter = 0;
            }
            
            // Spawn boost items rarely
            boostSpawnCounter++;
            if (boostSpawnCounter >= BOOST_SPAWN_RATE) {
                int randomLane = new Random().nextInt(3);
                int spawnX = WIDTH + OBSTACLE_GRACE_DISTANCE;
                boostItems.add(new BoostItem(spawnX, GROUND_Y, currentSpeed, randomLane));
                boostSpawnCounter = 0;
            }
            
            // Spawn clouds regularly at random y positions
            cloudSpawnCounter++;
            if (cloudSpawnCounter >= CLOUD_SPAWN_RATE) {
                clouds.add(new Cloud(WIDTH, HEIGHT, GROUND_Y));
                cloudSpawnCounter = 0;
            }

            // Check collisions with obstacles (only same lane)
            for (int i = obstacles.size() - 1; i >= 0; i--) {
                Obstacle obstacle = obstacles.get(i);
                if (player.getBounds().intersects(obstacle.getBounds()) && player.getLane() == obstacle.getLane()) {
                    // Only take damage if not invincible
                    if (!player.isInvincible()) {
                        player.damage();
                        scoreboard.loseLife();
                        obstacles.remove(i); // Remove the obstacle that hit us
                        if (scoreboard.isGameOver()) {
                            gameOver = true;
                        }
                    }
                    break;
                }
            }
            
            // Check collisions with boost items (only same lane)
            for (int i = boostItems.size() - 1; i >= 0; i--) {
                BoostItem boost = boostItems.get(i);
                if (player.getBounds().intersects(boost.getBounds()) && player.getLane() == boost.getLane()) {
                    player.addBoostFuel(boost.getBoostFuelAmount());
                    boostItems.remove(i);
                }
            }

            // Update scoreboard
            scoreboard.update();

            // Update background offset for parallax scrolling
            backgroundOffset += (int)(currentSpeed * player.getSpeedMultiplier());
            // Keep offset within bounds to prevent overflow
            if (backgroundOffset > 3200) {
                backgroundOffset -= 3200;
            }

            // Update sky based on score
            updateSkyColor();

            // Increase speed with faster progression (obstacles get faster quicker)
            currentSpeed = BASE_SPEED + (scoreboard.getScore() / 150);

            repaint();
        } else if (!gameOver && isPaused) {
            // Still repaint when paused to show pause menu
            repaint();
        }
    }

    private void drawClouds(Graphics g) {
        // This method has been removed. Clouds are now managed by the Cloud class.
        // Provide cloud.png in the resources folder to display custom cloud sprites.
    }

    private void drawCloud(Graphics g, int x, int y, int width, int height) {
        // This method has been removed. Use cloud.png image instead.
    }

    private void drawRailings(Graphics g) {
        // This method has been removed. Railings are now managed by the Railing class.
        // Provide railing.png in the resources folder to display custom railing sprites.
    }
    
    private void drawFlags(Graphics g) {
        // This method has been removed. Flags are now managed by the Flag class.
        // Provide flag.png in the resources folder to display custom flag sprites.
    }
    
    private void drawFlag(Graphics g, int x, int y, Color flagColor) {
        // This method has been removed. Use flag.png image instead.
    }
    
    private void drawNightForestBackground(Graphics g) {
        // This method has been removed. Trees are now managed by the Tree class.
        // Provide tree-night.png in the resources folder for custom night trees.
    }
    
    private void drawPineTree(Graphics2D g, int x, int y, float scale) {
        // This method has been removed. Use tree-night.png image instead.
    }
    
    private void drawNightForest(Graphics g) {
        // This method has been removed. Trees are now managed by the Tree class.
        // Provide tree.png for day trees and tree-night.png for night trees.
    }
    
    private void drawTree(Graphics g, int x, int y, int height) {
        // This method has been removed. Use tree.png image instead.
    }
    
    private void drawBackgroundTree(Graphics g, int x, int y, int height) {
        // This method has been removed. Use tree.png image instead.
    }

    private boolean isNightTime() {
        int score = scoreboard.getScore();
        int cycleScore = score % 1000; // Cycle every 1000 points
        return cycleScore >= 550; // Night starts AFTER transition completes
    }

    private void updateSkyColor() {
        int score = scoreboard.getScore();
        int cycleScore = score % 1000; // Cycle every 1000 points
        
        // Day color: Light blue
        Color dayColor = new Color(135, 206, 235);
        // Night color: Dark blue
        Color nightColor = new Color(25, 35, 65);
        
        // Extended smooth transition zones for better effect
        if (cycleScore < 400) {
            // Full day (0-400)
            skyColor = dayColor;
        } else if (cycleScore < 600) {
            // Long transition from day to night (400-600)
            float transition = (cycleScore - 400) / 200f;
            skyColor = blendColors(dayColor, nightColor, transition);
        } else if (cycleScore < 900) {
            // Full night (600-900)
            skyColor = nightColor;
        } else {
            // Long transition from night to day (900-1000)
            float transition = (cycleScore - 900) / 100f;
            skyColor = blendColors(nightColor, dayColor, transition);
        }
    }

    private Color blendColors(Color color1, Color color2, float progress) {
        // Smooth color interpolation
        // progress: 0.0 = color1, 1.0 = color2
        int r = (int) (color1.getRed() * (1 - progress) + color2.getRed() * progress);
        int g = (int) (color1.getGreen() * (1 - progress) + color2.getGreen() * progress);
        int b = (int) (color1.getBlue() * (1 - progress) + color2.getBlue() * progress);
        return new Color(r, g, b);
    }

    private void drawNightBackground(Graphics g) {
        // Draw starry night sky
        g.setColor(new Color(25, 35, 65)); // Dark blue night sky
        g.fillRect(0, 0, WIDTH, GROUND_Y - 80);
        drawStars(g);
        
        // Draw detailed road platform for night
        drawDetailedRoad(g, true);
    }

    private void drawDetailedRoad(Graphics g, boolean isNight) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw main platform
        if (isNight) {
            g2d.setColor(new Color(40, 35, 30)); // Dark asphalt
        } else {
            g2d.setColor(new Color(80, 70, 60)); // Brown road
        }
        g2d.fillRect(0, GROUND_Y - 80, WIDTH, HEIGHT - (GROUND_Y - 80));
        
        // Draw center line (road markings)
        g2d.setColor(isNight ? new Color(200, 200, 100) : new Color(255, 255, 150));
        g2d.setStroke(new BasicStroke(3));
        for (int i = 0; i < WIDTH + 100; i += 100) {
            int lineX = (i - backgroundOffset) % 3200;
            if (lineX < 0) lineX += 3200;
            g2d.drawLine(lineX, GROUND_Y - 40, lineX + 50, GROUND_Y - 40);
        }
        
        // Draw road edges (darker)
        g2d.setColor(isNight ? new Color(20, 15, 10) : new Color(40, 30, 20));
        g2d.fillRect(0, GROUND_Y - 80, WIDTH, 5);
        g2d.fillRect(0, HEIGHT - 15, WIDTH, 15);
        
        // Draw rocks/pebbles on the road
        if (isNight) {
            g2d.setColor(new Color(100, 95, 90));
        } else {
            g2d.setColor(new Color(120, 110, 100));
        }
        Random rand = new Random(12345);
        for (int i = 0; i < 30; i++) {
            int stoneX = (rand.nextInt(3200) - backgroundOffset) % 3200;
            if (stoneX < 0) stoneX += 3200;
            int stoneY = GROUND_Y - 60 + rand.nextInt(40);
            int size = 3 + rand.nextInt(5);
            g2d.fillOval(stoneX, stoneY, size, size);
        }
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

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                player.jump();
            } else if (e.getKeyCode() == KeyEvent.VK_W) {
                player.moveUp();
            } else if (e.getKeyCode() == KeyEvent.VK_S) {
                player.moveDown();
            } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                player.setBoostActive(true);
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                isPaused = !isPaused;
            } else if (e.getKeyCode() == KeyEvent.VK_Q && isPaused) {
                System.exit(0);
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
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            player.setBoostActive(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Handle pause menu button clicks
        if (isPaused && !gameOver) {
            if (continueButtonRect != null && continueButtonRect.contains(e.getPoint())) {
                isPaused = false;
            } else if (mainMenuButtonRect != null && mainMenuButtonRect.contains(e.getPoint())) {
                returnToMenu = true; // Signal to return to main menu
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

    private void restartGame() {
        player.reset(GROUND_Y, WIDTH);
        scoreboard.reset();
        obstacles.clear();
        boostItems.clear();
        clouds.clear();
        obstacleSpawnCounter = 0;
        boostSpawnCounter = 0;
        cloudSpawnCounter = 0;
        currentSpeed = BASE_SPEED;
        backgroundOffset = 0;
        gameOver = false;
        skyColor = new Color(135, 206, 235);
        updateSkyColor();
        repaint();
    }
    
    private void drawBackgroundImage(Graphics2D g2d) {
        if (backgroundImage == null) return;
        
        int bgWidth = backgroundImage.getWidth();
        int bgHeight = backgroundImage.getHeight();
        
        // Draw background image with parallax scrolling
        int offsetX = backgroundOffset % bgWidth;
        
        // Draw the image and its repeated version for seamless scrolling
        g2d.drawImage(backgroundImage, -offsetX, 0, bgWidth, bgHeight, null);
        g2d.drawImage(backgroundImage, bgWidth - offsetX, 0, bgWidth, bgHeight, null);
        
        // If there's still a gap, draw another copy
        if (bgWidth - offsetX - bgWidth < WIDTH) {
            g2d.drawImage(backgroundImage, 2 * bgWidth - offsetX, 0, bgWidth, bgHeight, null);
        }
    }
    
    private void drawRoadImage(Graphics2D g2d, boolean isNightMode) {
        // Use night road image if available during night, otherwise use day road image
        BufferedImage roadToDraw = isNightMode && roadNightImage != null ? roadNightImage : roadImage;
        
        if (roadToDraw != null && useRoadImage) {
            int roadWidth = roadToDraw.getWidth();
            int roadHeight = roadToDraw.getHeight();
            int roadStartY = GROUND_Y - 80;
            
            // Draw road image with parallax scrolling
            int offsetX = backgroundOffset % roadWidth;
            
            // Draw the road image and its repeated version for seamless scrolling
            g2d.drawImage(roadToDraw, -offsetX, roadStartY, roadWidth, roadHeight, null);
            g2d.drawImage(roadToDraw, roadWidth - offsetX, roadStartY, roadWidth, roadHeight, null);
            
            // If there's still a gap, draw another copy
            if (roadWidth - offsetX - roadWidth < WIDTH) {
                g2d.drawImage(roadToDraw, 2 * roadWidth - offsetX, roadStartY, roadWidth, roadHeight, null);
            }
        } else {
            // Fallback to drawing the road if no image is available
            drawDetailedRoad(g2d, isNightMode);
        }
    }
    
    private void drawBoostBar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw boost fuel bar under lives text (top left area)
        int barX = 20;
        int barY = 95; // Below lives text
        int barWidth = 150;
        int barHeight = 20;
        
        // Background
        g2d.setColor(new Color(50, 50, 50));
        g2d.fillRect(barX, barY, barWidth, barHeight);
        
        // Border
        g2d.setColor(Color.YELLOW);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(barX, barY, barWidth, barHeight);
        
        // Fuel bar
        int fuelWidth = (int)(barWidth * (player.getBoostFuel() / (float)player.getMaxBoostFuel()));
        if (fuelWidth > 0) {
            g2d.setColor(new Color(255, 215, 0)); // Gold
            g2d.fillRect(barX, barY, fuelWidth, barHeight);
        }
        
        // Label
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Nitro", barX, barY - 5);
        
        // Boosting indicator
        if (player.isBoosting()) {
            g.setColor(new Color(255, 100, 0)); // Orange
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("BOOSTING!", barX + barWidth + 20, barY + 15);
        }
    }
    
    private void drawPauseMenu(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Dark overlay
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        // Title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        FontMetrics fm = g.getFontMetrics();
        String pauseText = "PAUSED";
        int x = (WIDTH - fm.stringWidth(pauseText)) / 2;
        g.drawString(pauseText, x, 150);
        
        // Show current score
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String scoreText = "Score: " + scoreboard.getScore();
        fm = g.getFontMetrics();
        x = (WIDTH - fm.stringWidth(scoreText)) / 2;
        g.drawString(scoreText, x, 250);
        
        // Show lives
        String livesText = "Lives: " + scoreboard.getLives();
        fm = g.getFontMetrics();
        x = (WIDTH - fm.stringWidth(livesText)) / 2;
        g.drawString(livesText, x, 310);
        
        // Draw Continue button
        int buttonWidth = (int)(WIDTH * 0.25);
        int buttonHeight = (int)(HEIGHT * 0.08);
        int buttonX = (WIDTH - buttonWidth) / 2;
        int buttonY = 380;
        continueButtonRect = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
        
        // Draw button background
        g2d.setColor(new Color(50, 150, 50));
        g2d.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(buttonX, buttonY, buttonWidth, buttonHeight);
        
        // Draw button text
        g.setFont(new Font("Arial", Font.BOLD, 28));
        String btnText = "CONTINUE";
        fm = g.getFontMetrics();
        int btnX = buttonX + (buttonWidth - fm.stringWidth(btnText)) / 2;
        int btnY = buttonY + ((buttonHeight - fm.getAscent()) / 2) + fm.getAscent();
        g.setColor(Color.WHITE);
        g.drawString(btnText, btnX, btnY);
        
        // Draw Main Menu button
        int menuButtonY = buttonY + buttonHeight + 20;
        mainMenuButtonRect = new Rectangle(buttonX, menuButtonY, buttonWidth, buttonHeight);
        
        // Draw button background
        g2d.setColor(new Color(150, 50, 50));
        g2d.fillRect(buttonX, menuButtonY, buttonWidth, buttonHeight);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(buttonX, menuButtonY, buttonWidth, buttonHeight);
        
        // Draw button text
        g.setFont(new Font("Arial", Font.BOLD, 28));
        String menuText = "MAIN MENU";
        fm = g.getFontMetrics();
        btnX = buttonX + (buttonWidth - fm.stringWidth(menuText)) / 2;
        btnY = menuButtonY + ((buttonHeight - fm.getAscent()) / 2) + fm.getAscent();
        g.setColor(Color.WHITE);
        g.drawString(menuText, btnX, btnY);
    }
    
    private void initializeClouds() {
        // Method removed - Clouds class no longer used
    }
    
    private void updateCloudPositions() {
        // Method removed - Clouds class no longer used
    }
    
    private void initializeRailings() {
        // Method removed - Railing class no longer used
    }
    
    private void updateRailingPositions() {
        // Method removed - Railing class no longer used
    }
    
    private void initializeFlags() {
        // Method removed - Flag class no longer used
    }
    
    private void updateFlagPositions() {
        // Method removed - Flag class no longer used
    }
    
    private void initializeTrees() {
        // Method removed - Tree class no longer used
    }
    
    private void updateTreePositions() {
        // Method removed - Tree class no longer used
    }

    public boolean shouldReturnToMenu() {
        return returnToMenu;
    }

    public void resetGameState() {
        returnToMenu = false;
        isPaused = false;
        gameOver = false;
        player.reset(GROUND_Y, WIDTH);
        scoreboard.reset();
        obstacles.clear();
        obstacleSpawnCounter = 0;
        currentSpeed = BASE_SPEED;
        backgroundOffset = 0;
        skyColor = new Color(135, 206, 235);
        updateSkyColor();
        gameTimer.stop();
    }
}

