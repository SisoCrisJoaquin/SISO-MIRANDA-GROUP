# Endless Runner Game - Java

A fun and addictive endless runner game built in pure Java using Swing!

## Features

✅ **Player with 3 Lives** - Lose all lives and it's game over  
✅ **Grassy Field Platform** - Run on a beautiful green field  
✅ **Spike Obstacles** - Avoid red spikes to keep running  
✅ **Dynamic Scoreboard** - Track your score and remaining lives (top-left corner)  
✅ **Sky Transition** - Background changes from sunny (light blue) to darkened skies at score 100  
✅ **Collision Detection** - Hit an obstacle? Lose a life and restart  
✅ **Game Over Screen** - Restart or quit when you run out of lives  
✅ **Increasing Difficulty** - Game speeds up as your score increases  

## How to Play

### Controls
- **SPACEBAR** - Jump over obstacles
- **R** - Restart game (when game over)
- **Q** - Quit game (when game over)

### Objective
- Run as far as possible without hitting spikes
- Each frame you survive increases your score by 1
- Avoid obstacles to keep your 3 lives
- When score reaches 100, the sky darkens - but the obstacles keep coming!

## Compilation & Running

### Option 1: Using Batch Script (Windows)
```
run.bat
```

### Option 2: Manual Compilation
```bash
cd src
javac *.java
cd ..
```

### Option 3: Manual Running (after compilation)
```bash
cd bin
java -cp ../src Main
```

## Game Structure

- **Main.java** - Entry point, creates the game window
- **GamePanel.java** - Main game loop, rendering, and collision detection
- **Player.java** - Player character with jump mechanics
- **Obstacle.java** - Spike obstacle with collision bounds
- **Scoreboard.java** - Score tracking and life management

## Game Mechanics

### Player
- Starts on the left side of the platform
- Can jump with spacebar
- Gravity pulls the player back down to the platform
- Resets to starting position when hitting an obstacle

### Obstacles
- Randomly spawn from the right side of the screen
- Move left at increasing speeds
- Designed as red spike triangles
- Removed when they leave the screen

### Scoring System
- Score increases by 1 each frame (30 FPS)
- Score resets to 0 when player hits an obstacle
- Final score displayed when game ends
- Speed increases with score

### Lives System
- Player starts with 3 lives
- Lose 1 life when hitting an obstacle
- Game over when lives reach 0
- Remaining lives shown in top-left scoreboard

### Background Transition
- **Score 0-99**: Clear sunny skies (light blue)
- **Score 100+**: Gradually darkens to deep blue skies
- Creates a sense of progressing through time

## Tips for Playing

1. **Timing is Key** - Jump early to clear obstacles safely
2. **Watch the Pattern** - Obstacles spawn at intervals, learn the rhythm
3. **Stay Centered** - The player automatically moves back to starting position
4. **Go for Score** - Each frame counts! Higher scores = more challenging gameplay
5. **Watch the Sky** - When it darkens, you've reached score 100! Keep going!

---

Enjoy the game! Good luck beating your high score! 🏃‍♂️
