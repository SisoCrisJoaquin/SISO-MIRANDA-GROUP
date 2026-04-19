# SISO-MIRANDA-GROUP

## Endless Runner Game

A feature-rich endless runner game built in Java with Swing, offering dynamic day/night cycles, detailed graphics, and engaging gameplay mechanics.

### Features

#### Gameplay Mechanics
- **3 Lives System**: Players start with 3 lives and lose one upon collision with obstacles
- **Score System**: Continuous score tracking as the player progresses
- **Game Over Screen**: Displays final score with restart and quit options
- **Collision Detection**: Precise collision detection with obstacles

#### Day/Night Cycle
- **Score-Based Cycling**: Every 1000 points completes one full cycle
  - **Day Mode**: 0-499 points
  - **Night Mode**: 500-999 points
- **Smooth Transitions**: Dynamic sky color changes during transitions
- **Continuous Looping**: Cycle repeats infinitely throughout gameplay

#### Day Cycle Background
- **Sky**: Light blue sky with gradient effect
- **Clouds**: Detailed fluffy clouds with shadow layers for depth, positioned at various heights
- **Flags**: Colorful flags with long wooden poles reaching to the platform
  - 8 Different flag colors: Red, Blue, Yellow, Green, Orange, Purple, Pink, Cyan
  - Flags positioned far in the background creating depth perception
- **Railings**: Continuous gray railings with horizontal rails and vertical posts
  - No cut-off lines, seamless scrolling
  - Professional track-like appearance

#### Night Cycle Background
- **Starry Sky**: Deterministic starfield with 50 white stars that parallax scroll
- **Pine Trees**: Varied forest with trees of different sizes creating depth
  - Tree sizes range from 0.7x to 1.6x scale
  - Larger trees appear closer, smaller trees appear farther
  - Shadow shading for 3D dimension effect

#### Detailed Platform
- **Road Track Design**: 
  - Day mode: Brown road with texture
  - Night mode: Dark asphalt road
- **Center Line Markings**: Yellow dashed lines across the road (day) / light markings (night)
- **Road Edges**: Darker shading on top and bottom edges
- **Pebbles & Texture**: Random stones scattered across the road for visual detail

#### Obstacles
- **Rock & Boulder Obstacles**: Replaced spikes with detailed rocks
  - Multi-layered boulder design with stacked smaller rocks
  - Gray coloring with shadow and highlight effects for 3D appearance
  - Small pebbles clustered around main boulder
  - Crack texture details for realism

#### Graphics
- **Parallax Scrolling**: Background elements scroll at different speeds
  - Clouds and trees scroll slower than the player
  - Creates depth perception and immersion
- **Antialiasing**: Smooth rendering of all graphics
- **Color Variety**: Rich color palette with detailed shading
- **Seamless Wrapping**: All background elements wrap seamlessly every 3200 pixels

### Game Controls
- **Arrow Keys / A/D**: Move left and right
- **Space / W**: Jump
- **Restart/Quit**: Available from game over screen

### Technical Details
- **Language**: Java
- **Graphics**: Java Swing (Graphics2D)
- **Resolution**: 800x600 pixels
- **Frame Rate**: 30 FPS
- **Platform Base**: Y-coordinate at 520 (GROUND_Y = 500)

### File Structure
```
EndlessRunner/
├── src/
│   ├── GamePanel.java      (Main game logic and rendering)
│   ├── Main.java           (Window initialization)
│   ├── Player.java         (Player character mechanics)
│   ├── Obstacle.java       (Obstacle/boulder rendering)
│   ├── Scoreboard.java     (Score and lives tracking)
│   └── run.bat             (Launch script)
└── README.md
```

### How to Run
1. Navigate to the `EndlessRunner` directory
2. Run `run.bat` to compile and start the game
3. Or manually compile with: `javac src/*.java` and run with: `java -cp src Main`

### Gameplay Tips
- Master the timing of jumps to avoid rocks
- Watch for the score milestones - the environment changes at 500 points
- The night cycle brings trees closer to the foreground
- Railings mark the safe running area boundaries

### Version History
- **Latest Update**: Enhanced visual details with varied tree sizes, detailed platform design, realistic rock obstacles, and continuous railings. Replaced bushes with colorful flags and improved background depth perception.

---

*Developed by Siso*