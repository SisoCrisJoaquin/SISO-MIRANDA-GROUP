import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
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
    private BufferedImage backgroundImage;
    private boolean useBackgroundImage;
    private boolean isPaused = false;

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
        
        // Try to load background image
        backgroundImage = ImageLoader.loadImage("/background.png");
        useBackgroundImage = (backgroundImage != null);

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
                // Draw night background
                drawNightBackground(g);
            } else {
                // Draw day background
                if (useBackgroundImage && backgroundImage != null) {
                    // Draw background image with parallax scrolling
                    drawBackgroundImage(g2d);
                } else {
                    // Draw sky
                    g.setColor(skyColor);
                    g.fillRect(0, 0, WIDTH, GROUND_Y - 80);

                    // Draw clouds
                    drawClouds(g);

                    // Draw detailed road platform
                    drawDetailedRoad(g, false);
                    
                    // Draw flags and railings (background elements)
                    drawRailings(g);
                    drawFlags(g);
                }
            }

            // Draw player
            player.draw(g);

            // Draw obstacles
            for (Obstacle obstacle : obstacles) {
                obstacle.draw(g);
            }

            // Draw scoreboard
            scoreboard.draw(g);
            
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
        } else if (!gameOver && isPaused) {
            // Still repaint when paused to show pause menu
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
        // Draw cloud using circles to create a fluffy effect with more detail
        Graphics2D g2d = (Graphics2D) g;
        
        // Highlight/shadow for depth
        g2d.setColor(new Color(255, 255, 255, 240)); // Bright white
        g2d.fillOval(x, y, width / 3, height);
        g2d.fillOval(x + width / 4, y - height / 3, width / 2, height);
        g2d.fillOval(x + width / 2, y, width / 3, height);
        
        // Darker layer for shadow/depth
        g2d.setColor(new Color(200, 200, 200, 180));
        g2d.fillOval(x + 2, y + 3, width / 4, height - 8);
        g2d.fillOval(x + width / 3, y + height - 5, width / 3, height / 4);
    }

    private void drawRailings(Graphics g) {
        // Draw continuous railings (horizontal lines) at fixed height
        Graphics2D g2d = (Graphics2D) g;
        
        int railingY1 = GROUND_Y - 110; // Upper railing
        int railingY2 = GROUND_Y - 95;  // Lower railing
        
        // Draw continuous railings across the screen with wrapping
        g2d.setColor(new Color(100, 100, 100)); // Gray railing color
        g2d.setStroke(new BasicStroke(2));
        
        // Draw horizontal rails as continuous lines (no gaps)
        for (int segmentX = 0; segmentX < WIDTH + 3200; segmentX += 3200) {
            int start = segmentX - backgroundOffset;
            int end = start + 3200;
            
            // Upper horizontal rail
            if (start < WIDTH && end > 0) {
                int drawStart = Math.max(0, start);
                int drawEnd = Math.min(WIDTH, end);
                g2d.drawLine(drawStart, railingY1, drawEnd, railingY1);
            }
            
            // Lower horizontal rail
            if (start < WIDTH && end > 0) {
                int drawStart = Math.max(0, start);
                int drawEnd = Math.min(WIDTH, end);
                g2d.drawLine(drawStart, railingY2, drawEnd, railingY2);
            }
        }
        
        // Vertical posts for railings (continuous without cutoffs)
        g2d.setStroke(new BasicStroke(3));
        for (int baseX = 0; baseX < 3200; baseX += 60) {
            for (int segmentX = 0; segmentX < WIDTH + 3200; segmentX += 3200) {
                int postX = baseX + segmentX - backgroundOffset;
                
                if (postX >= 0 && postX < WIDTH) {
                    // Draw vertical post
                    g2d.drawLine(postX, railingY1, postX, railingY2);
                }
            }
        }
    }
    
    private void drawFlags(Graphics g) {
        // Define flag positions (same spacing as trees/bushes)
        int[] flagX = {100, 250, 400, 550, 700, 850, 1000, 1150, 1300, 1450, 1600, 1750, 1900, 2050, 2200, 2350, 2500, 2650, 2800, 2950, 3100};
        int flagY = 130; // Far from platform, in the background
        
        // Flag colors - vary the colors
        Color[] flagColors = {
            new Color(255, 0, 0),       // Red
            new Color(0, 0, 255),       // Blue
            new Color(255, 255, 0),     // Yellow
            new Color(0, 255, 0),       // Green
            new Color(255, 165, 0),     // Orange
            new Color(128, 0, 128),     // Purple
            new Color(255, 192, 203),   // Pink
            new Color(0, 255, 255),     // Cyan
        };
        
        // Draw flags with parallax scrolling and wrapping
        for (int i = 0; i < flagX.length; i++) {
            int scrolledX = (flagX[i] - backgroundOffset) % 3200;
            if (scrolledX < 0) {
                scrolledX += 3200;
            }
            
            // Determine flag color (cycle through colors)
            Color flagColor = flagColors[i % flagColors.length];
            
            // Draw current position
            drawFlag(g, scrolledX, flagY, flagColor);
            
            // Draw wrapped version if needed
            if (scrolledX + 50 < WIDTH) {
                int wrappedX = scrolledX + 3200;
                if (wrappedX < WIDTH) {
                    drawFlag(g, wrappedX, flagY, flagColor);
                }
            }
        }
    }
    
    private void drawFlag(Graphics g, int x, int y, Color flagColor) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Flag pole - long, reaching from flag position to near ground
        g2d.setColor(new Color(139, 69, 19));
        int poleHeight = (GROUND_Y - 80) - y; // Height from flag to platform
        g2d.fillRect(x - 3, y, 6, poleHeight);
        
        // Flag fabric - rectangular flag at the top
        g2d.setColor(flagColor);
        g2d.fillRect(x + 3, y, 30, 20);
        
        // Flag border for definition
        g2d.setColor(new Color(0, 0, 0));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x + 3, y, 30, 20);
        
        // Flag wave effect (small curves on flag edge)
        g2d.setColor(flagColor);
        int[] flagWaveX = {x + 32, x + 32, x + 32};
        int[] flagWaveY = {y + 2, y + 10, y + 17};
        g2d.fillPolygon(flagWaveX, flagWaveY, 3);
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
        // Draw starry night sky
        g.setColor(new Color(25, 35, 65)); // Dark blue night sky
        g.fillRect(0, 0, WIDTH, GROUND_Y - 80);
        drawStars(g);
        
        // Draw forest trees as background elements (like clouds)
        drawNightForestBackground(g);
        
        // Draw detailed road platform for night
        drawDetailedRoad(g, true);
    }
    
    private void drawNightForestBackground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Define tree positions that repeat continuously (like clouds and bushes)
        int[] treeX = {100, 250, 400, 550, 700, 850, 1000, 1150, 1300, 1450, 1600, 1750, 1900, 2050, 2200, 2350, 2500, 2650, 2800, 2950, 3100};
        int[] treeY = {GROUND_Y - 90, GROUND_Y - 85, GROUND_Y - 85, GROUND_Y - 95, GROUND_Y - 90, GROUND_Y - 85, GROUND_Y - 95, GROUND_Y - 88, GROUND_Y - 90, GROUND_Y - 85, GROUND_Y - 95, GROUND_Y - 85, GROUND_Y - 90, GROUND_Y - 88, GROUND_Y - 95, GROUND_Y - 90, GROUND_Y - 85, GROUND_Y - 95, GROUND_Y - 88, GROUND_Y - 90, GROUND_Y - 85};
        float[] treeScale = {1.0f, 1.3f, 0.7f, 1.5f, 0.8f, 1.2f, 1.4f, 0.9f, 1.1f, 1.6f, 0.7f, 1.3f, 1.0f, 1.4f, 0.8f, 1.2f, 1.5f, 0.9f, 1.1f, 1.3f, 0.8f};
        
        // Draw trees with parallax scrolling and wrapping (same pattern as clouds/bushes)
        for (int i = 0; i < treeX.length; i++) {
            int scrolledX = (treeX[i] - backgroundOffset) % 3200;
            if (scrolledX < 0) {
                scrolledX += 3200;
            }
            
            // Draw current position
            drawPineTree(g2d, scrolledX, treeY[i], treeScale[i]);
            
            // Draw wrapped version if needed
            if (scrolledX + 50 < WIDTH) {
                int wrappedX = scrolledX + 3200;
                if (wrappedX < WIDTH) {
                    drawPineTree(g2d, wrappedX, treeY[i], treeScale[i]);
                }
            }
        }
    }
    
    private void drawPineTree(Graphics2D g, int x, int y, float scale) {
        // Trunk
        g.setColor(new Color(101, 67, 33));
        int trunkW = (int)(16 * scale);
        int trunkH = (int)(30 * scale);
        g.fillRect((int)(x - trunkW / 2), (int)(y + 5 * scale), trunkW, trunkH);
        
        // Shadow/dark side for depth
        g.setColor(new Color(60, 40, 20));
        g.fillRect((int)(x - trunkW / 2), (int)(y + 5 * scale), (int)(trunkW / 2), trunkH);
        
        // Foliage - multiple triangles stacked tightly
        g.setColor(new Color(34, 120, 50)); // Dark green
        
        // Layer 1 (bottom) - widest
        int[] xPoints1 = {(int)(x - 20 * scale), (int)(x + 20 * scale), (int)x};
        int[] yPoints1 = {(int)(y + 5 * scale), (int)(y + 5 * scale), (int)(y - 20 * scale)};
        g.fillPolygon(xPoints1, yPoints1, 3);
        
        // Layer 2 (middle)
        int[] xPoints2 = {(int)(x - 15 * scale), (int)(x + 15 * scale), (int)x};
        int[] yPoints2 = {(int)(y - 10 * scale), (int)(y - 10 * scale), (int)(y - 35 * scale)};
        g.fillPolygon(xPoints2, yPoints2, 3);
        
        // Layer 3 (top) - narrowest
        int[] xPoints3 = {(int)(x - 10 * scale), (int)(x + 10 * scale), (int)x};
        int[] yPoints3 = {(int)(y - 25 * scale), (int)(y - 25 * scale), (int)(y - 45 * scale)};
        g.fillPolygon(xPoints3, yPoints3, 3);
        
        // Darker shade on left side for depth
        g.setColor(new Color(20, 80, 30));
        int[] shadowX = {(int)(x - 20 * scale), (int)x, (int)(x - 5 * scale)};
        int[] shadowY = {(int)(y + 5 * scale), (int)(y - 20 * scale), (int)(y - 5 * scale)};
        g.fillPolygon(shadowX, shadowY, 3);
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
            } else if (e.getKeyCode() == KeyEvent.VK_P) {
                isPaused = !isPaused;
            } else if (e.getKeyCode() == KeyEvent.VK_Q && isPaused) {
                // Quit from pause menu - restart the game
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
    
    private void drawPauseMenu(Graphics g) {
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
        
        // Instructions
        g.setFont(new Font("Arial", Font.PLAIN, 28));
        String continueText = "Press 'P' to Continue";
        fm = g.getFontMetrics();
        x = (WIDTH - fm.stringWidth(continueText)) / 2;
        g.drawString(continueText, x, 400);
        
        String quitText = "Press 'Q' to Main Menu";
        fm = g.getFontMetrics();
        x = (WIDTH - fm.stringWidth(quitText)) / 2;
        g.drawString(quitText, x, 450);
    }
}
