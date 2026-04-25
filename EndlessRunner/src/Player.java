import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * ========== PLAYER.JAVA ==========
 * Represents the player character with jump mechanics and lane switching
 * Handles gravity, collision bounds, and 3D perspective rendering
 */
public class Player {
    // Animation state enum
    private enum AnimationState {
        IDLE, MOVING_UP, MOVING_DOWN, JUMPING
    }
    
    // Wind trail particle class
    private static class WindTrailParticle {
        int x, y;
        int width, height;
        int lifetime; // Current lifetime
        int maxLifetime; // Max lifetime before disappearing
        float velocityX;
        Color color;
        
        public WindTrailParticle(int x, int y, int width, int height, int maxLifetime, float velocityX) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.lifetime = maxLifetime;
            this.maxLifetime = maxLifetime;
            this.velocityX = velocityX;
            this.color = new Color(200, 220, 255, 100); // Light blue with transparency
        }
        
        public void update() {
            lifetime--;
            x += (int)velocityX;
        }
        
        public boolean isAlive() {
            return lifetime > 0;
        }
        
        public void draw(Graphics2D g2d) {
            // Fade out effect based on lifetime
            float alphaMult = lifetime / (float)maxLifetime;
            int alpha = (int)(100 * alphaMult);
            Color fadeColor = new Color(200, 220, 255, Math.max(0, Math.min(255, alpha)));
            g2d.setColor(fadeColor);
            
            // Scale particle down as it fades
            int scaledWidth = (int)(width * alphaMult);
            int scaledHeight = (int)(height * alphaMult);
            int offsetX = (width - scaledWidth) / 2;
            int offsetY = (height - scaledHeight) / 2;
            
            g2d.fillOval(x + offsetX, y + offsetY, scaledWidth, scaledHeight);
        }
    }
    
    private int x;
    private int standardX; // Starting x position to return to when landing
    private int y;
    private int width;
    private int height;
    private float velocityX;
    private float velocityY;
    private boolean isJumping;
    private int groundY;
    private int lane; // 0 = top, 1 = middle, 2 = bottom
    private float scale; // 1.0 = normal, 0.8 = small (top), 1.2 = large (bottom)
    private final int JUMP_STRENGTH = 18;
    private final float GRAVITY = 0.6f;
    private final int MAX_FALL_SPEED = 18;
    private final int LANE_HEIGHT = 40; // Vertical distance between lanes
    private final int RETURN_SPEED = 5; // Speed to gradually return to standard X position
    private final BufferedImage playerImage;
    private final boolean useImage;
    
    // Boost mechanics
    private int boostFuel; // Current boost fuel
    private int maxBoostFuel = 120; // Maximum fuel (frames)
    private boolean isBoosting; // Currently using boost
    private int boostRegenCounter; // Counter for slow regeneration
    private static final int BOOST_REGEN_RATE = 30; // Regenerate 1 fuel every 30 frames (1 second at 30 FPS)
    private static final float BOOST_SPEED_MULTIPLIER = 1.5f;
    private static final int BOOST_JUMP_STRENGTH = 22; // Faster jump when boosting
    
    // Wind trail VFX properties
    private ArrayList<WindTrailParticle> windTrails;
    private int windTrailSpawnCounter;
    private final int WIND_TRAIL_SPAWN_RATE = 3; // Spawn every N frames
    
    // Animation properties
    private AnimationState animationState;
    private int animationTimer;
    private final int LANE_TRANSITION_DURATION = 10; // Frames for lane change animation
    private final int JUMP_ANIMATION_DURATION = 20; // Frames for jump animation
    private int previousLane; // Track lane before animation started
    
    // Animation frame properties
    private BufferedImage[] animationFrames; // player-ani1 to player-ani4
    private int animationFrameCounter; // Counter to cycle through frames based on speed
    private int currentAnimationFrame; // Current frame index (0-3)
    private static final int MAX_ANIMATION_CYCLE_SPEED = 3; // Limit how fast animation frames cycle
    
    // Invincibility frame system
    private int invincibilityFramesRemaining = 0;
    private static final int INVINCIBILITY_DURATION = 36; // 1.2 seconds at 30 FPS

    public Player(int groundY, int screenWidth) {
        this.x = screenWidth / 4;
        this.standardX = screenWidth / 4; // Store standard position
        this.groundY = groundY;
        this.lane = 1; // Start in middle lane
        this.scale = 1.0f;
        this.width = 140;
        this.height = 100;
        this.velocityX = 0;
        this.velocityY = 0;
        this.isJumping = false;
        updateLanePosition();
        
        this.playerImage = ImageLoader.loadImage("/player.png");
        this.useImage = (playerImage != null);
        
        // Load animation frames in reverse order (ani4, ani3, ani2, ani1)
        this.animationFrames = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            // Load in reverse: ani4 at index 0, ani3 at index 1, etc.
            int frameNum = 4 - i;
            animationFrames[i] = ImageLoader.loadImage("/player-ani" + frameNum + ".png");
            if (animationFrames[i] != null) {
                System.out.println("Successfully loaded player-ani" + frameNum + ".png at index " + i);
            } else {
                System.err.println("Failed to load player-ani" + frameNum + ".png");
            }
        }
        this.animationFrameCounter = 0;
        this.currentAnimationFrame = 0;
        
        // Initialize boost system
        this.boostFuel = 0;
        this.isBoosting = false;
        this.boostRegenCounter = 0;
        
        // Initialize wind trail system
        this.windTrails = new ArrayList<>();
        this.windTrailSpawnCounter = 0;
        
        // Initialize invincibility system
        this.invincibilityFramesRemaining = 0;
    }
    
    private void updateLanePosition() {
        // Update scale based on lane (always)
        if (lane == 0) {
            this.scale = 0.9f;
        } else if (lane == 1) {
            this.scale = 1.0f;
        } else {
            this.scale = 1.1f;
        }
        
        // Only update y position if not jumping (preserve jump arc during lane switch)
        if (!isJumping) {
            if (lane == 0) {
                this.y = groundY - LANE_HEIGHT - (int)(height * scale);
            } else if (lane == 1) {
                this.y = groundY - (int)(height * scale);
            } else {
                this.y = groundY + LANE_HEIGHT - (int)(height * scale);
            }
        }
    }
    
    public void moveUp() {
        if (lane > 0) {
            lane--;
            updateLanePosition();
        }
    }
    
    public void moveDown() {
        if (lane < 2) {
            lane++;
            updateLanePosition();
        }
    }
    
    public int getLane() {
        return lane;
    }

    public void update(int gameSpeed) {
        // Decrement invincibility frames
        if (invincibilityFramesRemaining > 0) {
            invincibilityFramesRemaining--;
        }
        
        // Update animation frame based on game speed
        // Faster speeds cycle through frames faster for visual feedback
        int cycleSpeed = Math.max(1, (gameSpeed / 2)); // Cycle speed relative to game speed
        // Cap animation cycle speed to prevent excessive frame cycling
        cycleSpeed = Math.min(cycleSpeed, MAX_ANIMATION_CYCLE_SPEED);
        animationFrameCounter += cycleSpeed;
        
        // Cycle through 4 animation frames
        // Higher counter values mean faster cycling
        currentAnimationFrame = (animationFrameCounter / 6) % 4; // 6 is frame duration per image
        
        // Prevent counter overflow - reset when it gets too large
        if (animationFrameCounter > 100000) {
            animationFrameCounter = 0;
        }
        
        // Update wind trails
        windTrailSpawnCounter++;
        
        // Remove dead particles
        for (Iterator<WindTrailParticle> it = windTrails.iterator(); it.hasNext();) {
            WindTrailParticle particle = it.next();
            particle.update();
            if (!particle.isAlive()) {
                it.remove();
            }
        }
        
        // Spawn new wind trail particles
        if (windTrailSpawnCounter >= WIND_TRAIL_SPAWN_RATE) {
            windTrailSpawnCounter = 0;
            
            // Always spawn when moving (game scrolls, or player jumping)
            if (isJumping || true) { // Always spawn for continuous motion effect
                int scaledWidth = (int)(width * scale);
                int scaledHeight = (int)(height * scale);
                int adjustedX = x + (width - scaledWidth) / 2;
                
                // Spawn multiple particles around the player for denser trail
                for (int i = 0; i < 2; i++) {
                    int offsetX = (int)(Math.random() * scaledWidth - scaledWidth / 2);
                    int offsetY = (int)(Math.random() * scaledHeight - scaledHeight / 4);
                    int particleX = adjustedX + scaledWidth / 2 + offsetX;
                    int particleY = y + scaledHeight / 2 + offsetY;
                    
                    // Wind trails move leftward (opposite to player movement)
                    float windVelocity = -3.0f - (float)Math.random() * 2.0f;
                    
                    windTrails.add(new WindTrailParticle(
                        particleX,
                        particleY,
                        12 + (int)(Math.random() * 8),
                        12 + (int)(Math.random() * 8),
                        20 + (int)(Math.random() * 10),
                        windVelocity
                    ));
                }
            }
        }
        
        if (isJumping) {
            // Apply gravity and horizontal friction for arc motion
            velocityY += GRAVITY;
            if (velocityY > MAX_FALL_SPEED) {
                velocityY = MAX_FALL_SPEED;
            }
            velocityX *= 0.92f;  // Friction to reduce horizontal velocity over time
            
            y += velocityY;
            x += (int)velocityX;

            // Get the ground level for the current lane
            int laneGroundY;
            if (lane == 0) {
                laneGroundY = groundY - LANE_HEIGHT - (int)(height * 0.9f);
            } else if (lane == 1) {
                laneGroundY = groundY - (int)(height * 1.0f);
            } else {
                laneGroundY = groundY + LANE_HEIGHT - (int)(height * 1.1f);
            }
            
            if (y >= laneGroundY) {
                y = laneGroundY;
                isJumping = false;
                velocityY = 0;
                velocityX = 0;
            }
        } else {
            // Gradually return to standard X position when not jumping
            if (x < standardX) {
                x += RETURN_SPEED;
                if (x > standardX) x = standardX; // Don't overshoot
            } else if (x > standardX) {
                x -= RETURN_SPEED;
                if (x < standardX) x = standardX; // Don't overshoot
            }
        }
    }

    public void jump() {
        if (!isJumping) {
            int jumpPower = isBoosting ? BOOST_JUMP_STRENGTH : JUMP_STRENGTH;
            velocityY = -jumpPower;
            velocityX = 8.0f;  // Horizontal velocity for wider arc
            isJumping = true;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw wind trail particles first (behind the player)
        for (WindTrailParticle particle : windTrails) {
            particle.draw(g2d);
        }
        
        int scaledWidth = (int)(width * scale);
        int scaledHeight = (int)(height * scale);
        int adjustedX = x + (width - scaledWidth) / 2; // Center the scaled sprite
        
        // Flash effect when invincible (alternate visibility every 4 frames)
        if (invincibilityFramesRemaining > 0 && (invincibilityFramesRemaining / 4) % 2 == 0) {
            // Don't draw player during flash off frames - create flashing effect
            return;
        }
        
        if (useImage && playerImage != null) {
            BufferedImage imageToUse = playerImage;
            
            // Use animation frames when not jumping
            if (!isJumping && animationFrames != null && currentAnimationFrame >= 0 && currentAnimationFrame < animationFrames.length) {
                if (animationFrames[currentAnimationFrame] != null) {
                    imageToUse = animationFrames[currentAnimationFrame];
                }
            }
            
            g2d.drawImage(imageToUse, adjustedX, y, scaledWidth, scaledHeight, null);
        } else {
            g.setColor(new Color(255, 100, 100));
            g.fillRect(adjustedX, y, scaledWidth, scaledHeight);
            g.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(adjustedX, y, scaledWidth, scaledHeight);
        }
    }

    public Rectangle getBounds() {
        int scaledWidth = (int)(width * scale);
        int scaledHeight = (int)(height * scale);
        int adjustedX = x + (width - scaledWidth) / 2;
        return new Rectangle(adjustedX, y, scaledWidth, scaledHeight);
    }

    public void reset(int groundY, int screenWidth) {
        this.x = screenWidth / 4;
        this.standardX = screenWidth / 4; // Reset to standard position
        this.groundY = groundY;
        this.lane = 1;
        this.scale = 1.0f;
        this.velocityX = 0;
        this.velocityY = 0;
        this.isJumping = false;
        updateLanePosition();
        
        // Clear wind trails on reset
        windTrails.clear();
        windTrailSpawnCounter = 0;
        
        // Reset animation
        animationFrameCounter = 0;
        currentAnimationFrame = 0;
        
        // Reset boost
        boostFuel = 0;
        isBoosting = false;
        boostRegenCounter = 0;
        
        // Reset invincibility
        invincibilityFramesRemaining = 0;
    }
    
    // Boost management methods
    public void addBoostFuel(int amount) {
        boostFuel = Math.min(boostFuel + amount, maxBoostFuel);
        System.out.println("Boost fuel: " + boostFuel + "/" + maxBoostFuel);
    }
    
    public void setBoostActive(boolean active) {
        if (active && boostFuel > 0) {
            isBoosting = true;
        } else {
            isBoosting = false;
        }
    }
    
    public void updateBoostFuel() {
        if (isBoosting && boostFuel > 0) {
            // Actively boosting - consume fuel
            boostFuel--;
            boostRegenCounter = 0; // Reset regen counter while actively boosting
        } else if (boostFuel <= 0) {
            // Out of fuel - stop boosting
            isBoosting = false;
            boostFuel = 0;
        }
        
        // Regenerate fuel when not boosting and fuel < max
        if (!isBoosting && boostFuel < maxBoostFuel) {
            boostRegenCounter++;
            if (boostRegenCounter >= BOOST_REGEN_RATE) {
                boostFuel++;
                System.out.println("Boost regenerated! Fuel: " + boostFuel + "/" + maxBoostFuel);
                boostRegenCounter = 0;
            }
        }
    }
    
    public boolean isBoosting() {
        return isBoosting;
    }
    
    public int getBoostFuel() {
        return boostFuel;
    }
    
    public int getMaxBoostFuel() {
        return maxBoostFuel;
    }
    
    public float getSpeedMultiplier() {
        return isBoosting ? BOOST_SPEED_MULTIPLIER : 1.0f;
    }
    
    // Invincibility frame methods
    public void damage() {
        // Apply invincibility frames when hit by obstacle
        if (!isInvincible()) {
            invincibilityFramesRemaining = INVINCIBILITY_DURATION;
            isBoosting = false; // Lose boost on hit
        }
    }
    
    public boolean isInvincible() {
        return invincibilityFramesRemaining > 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
