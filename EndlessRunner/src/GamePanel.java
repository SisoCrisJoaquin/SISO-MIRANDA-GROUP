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

            // Draw ground/grass
            g.setColor(grassColor);
            g.fillRect(0, GROUND_Y - 80, WIDTH, HEIGHT - (GROUND_Y - 80));

            // Draw ground line
            g.setColor(new Color(0, 100, 0));
            g.fillRect(0, GROUND_Y - 80, WIDTH, 10);

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

            // Update sky based on score
            updateSkyColor();

            // Increase speed slightly as score increases
            currentSpeed = BASE_SPEED + (scoreboard.getScore() / 200);

            repaint();
        }
    }

    private void updateSkyColor() {
        int score = scoreboard.getScore();
        if (score >= 100) {
            // Transition from light blue to dark blue
            int progress = Math.min((score - 100) / 100, 1); // 0 to 1 over 100 points
            int r = (int) (135 * (1 - progress * 0.7));
            int gb = (int) (206 * (1 - progress * 0.8));
            skyColor = new Color(r, gb, gb);
        } else {
            skyColor = new Color(135, 206, 235); // Light blue
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
        gameOver = false;
        skyColor = new Color(135, 206, 235);
        repaint();
    }
}
