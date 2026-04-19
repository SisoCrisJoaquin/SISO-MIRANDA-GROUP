# SISO-MIRANDA-GROUP

## Endless Runner Game

A feature-rich 2D Endless Runner game built in Java using Swing graphics API. The game features dynamic day/night cycles, detailed parallax scrolling backgrounds, and engaging gameplay mechanics.

### Features

#### Core Gameplay
- **Endless Running**: Continuous infinite gameplay with increasing difficulty
- **Lives System**: 3 lives before game over
- **Score Tracking**: Real-time scoreboard displaying current score and remaining lives
- **Collision Detection**: Accurate obstacle detection with immediate feedback
- **Game Over Screen**: Interactive game over menu with restart and quit options

#### Visual Elements

##### Day Cycle (0-499 Points)
- Light blue sky background
- Detailed fluffy clouds with shadow effects for depth
- Colorful flags on long poles (8 different colors: Red, Blue, Yellow, Green, Orange, Purple, Pink, Cyan)
- Continuous gray railings with horizontal and vertical posts
- Detailed road/track platform with center lane markings
- Random pebbles scattered across the road for texture

##### Night Cycle (500-999 Points, Repeats)
- Dark blue starry night sky
- Deterministic star field with parallax scrolling
- Pine trees of varied sizes (0.7x to 1.6x scale) creating depth perception
- Trees appear to be far and near based on size
- Dark road/track platform with lighter lane markings
- Seamless looping every 1000 points

#### Advanced Features
- **Parallax Scrolling**: Background elements move at different speeds creating depth illusion
- **Continuous Wrapping**: All background elements seamlessly wrap around with no visible seams
- **Dynamic Obstacles**: Gray rocks and boulders with 3D shading and stacked pebbles effect
- **Anti-aliasing**: Smooth rendering of all graphics for polished appearance
- **Day/Night Cycle**: Automatically transitions between day and night modes based on score

### Game Controls
- **Arrow Keys or WASD**: Move left and right
- **Space Bar**: Jump
- **Game Over Menu**: Press keys to restart or quit

### Running the Game

#### Prerequisites
- Java Development Kit (JDK) 8 or higher

#### Compilation and Execution
```bash
cd EndlessRunner/src
javac *.java
java Main
```

Or use the provided `run.bat` script:
```bash
run.bat
```

### Game Files
- `Main.java`: Window initialization and game setup
- `GamePanel.java`: Main game controller, rendering pipeline, game loop
- `Player.java`: Player character with jump mechanics and collision bounds
- `Obstacle.java`: Rock/boulder obstacles with 3D rendering
- `Scoreboard.java`: Score and lives management system

### Game Mechanics

**Scoring**: Points increase continuously as the player survives. Every 500 points triggers a cycle transition.

**Obstacles**: Rocks and boulders spawn at regular intervals. Collision with an obstacle costs one life.

**Cycle System**: 
- Points 0-499: Day cycle (light environment)
- Points 500-999: Night cycle (dark environment)
- Repeats infinitely at 1000-point intervals

**Death/Restart**: When all 3 lives are lost, the game over screen appears. Choose to restart or quit.

### Technical Details
- **Resolution**: 800x600 pixels
- **Frame Rate**: 30 FPS
- **Platform Height**: 100 pixels
- **Game Loop**: Timer-based with consistent update and rendering cycle
- **Rendering**: Graphics2D with anti-aliasing for smooth visuals

### Asset Design
All graphics are procedurally generated using Java Swing Graphics API:
- **Clouds**: Overlapping circles with layered shading
- **Flags**: Wooden poles with rectangular colored fabric
- **Railings**: Continuous horizontal and vertical structural elements
- **Rocks**: Multi-layered boulders with shadow and highlight effects
- **Trees**: Stacked triangular foliage with realistic depth shading
- **Road**: Detailed asphalt with lane markings and scattered pebbles

### Future Enhancement Ideas
- Sound effects and background music
- Power-ups and special items
- Multiple difficulty levels
- Leaderboard system
- Mobile touch controls
- Additional obstacles and environmental hazards

### Version History
- **Latest**: Updated with detailed visual improvements, dynamic day/night cycles, and enhanced background elements
