# Endless Runner Game - Java

A fun and addictive endless runner game built in pure Java using Swing!

## Features

✅ **Main Menu** - Beautiful start screen with instructions before gameplay  
✅ **Pause Menu** - Press P to pause and see your score and lives  
✅ **Player with 3 Lives** - Lose all lives and it's game over  
✅ **Grassy Field Platform** - Run on a beautiful green field  
✅ **Spike Obstacles** - Avoid rocks and obstacles to keep running  
✅ **Dynamic Scoreboard** - Track your score and remaining lives (top-left corner)  
✅ **Sky Transition** - Background changes from sunny (light blue) to darkened skies at score 500  
✅ **Collision Detection** - Hit an obstacle? Lose a life and restart  
✅ **Game Over Screen** - Restart or quit when you run out of lives  
✅ **Increasing Difficulty** - Game speeds up as your score increases  
✅ **Custom Graphics Support** - Add your own PNG images for player, obstacles, and backgrounds  

## How to Play

### Controls
- **SPACEBAR** - Jump over obstacles / Start game from menu
- **P** - Pause/Unpause game during gameplay
- **R** - Restart game (when game over)
- **Q** - Quit to menu (when paused) or quit game (when game over)

### Objective
- Run as far as possible without hitting spikes
- Each frame you survive increases your score by 1
- Avoid obstacles to keep your 3 lives
- When score reaches 100, the sky darkens - but the obstacles keep coming!

## Compilation & Running

### ⚡ Quick Start (Recommended - Windows)
```
1. Double-click build.bat      (compiles the game)
2. Double-click play.bat       (runs the game)
```
That's it! The game will start immediately.

### Option 1: Using Batch Script
```bash
cd EndlessRunner
build.bat
play.bat
```

### Option 2: Manual Compilation

```bash
cd src
javac *.java
cd ..
java -cp bin Main
```

### Option 3: Command Line
```bash
cd EndlessRunner
javac -d bin src/*.java
java -cp bin Main
```

## Game Structure

- **Main.java** - Entry point, manages menu and game switching
- **MenuPanel.java** - Main menu with instructions and controls
- **GamePanel.java** - Main game loop, rendering, collision detection, and pause menu
- **Player.java** - Player character with jump mechanics
- **Obstacle.java** - Obstacle with collision bounds
- **Scoreboard.java** - Score tracking and life management
- **ImageLoader.java** - Handles loading custom images from resources folder

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

### Main Menu
- Shows on game startup
- Displays game title and controls
- Press SPACEBAR to begin playing

### Pause Menu
- Press P during gameplay to pause
- Shows current score and lives
- Press P again to resume
- Press Q to quit and return to main menu

## Tips for Playing

1. **Timing is Key** - Jump early to clear obstacles safely
2. **Watch the Pattern** - Obstacles spawn at intervals, learn the rhythm
3. **Stay Centered** - The player automatically moves back to starting position
4. **Go for Score** - Each frame counts! Higher scores = more challenging gameplay
5. **Watch the Sky** - When it darkens, you've reached score 100! Keep going!

## Build System

### Build Scripts (Windows)
- **build.bat** - Compiles all Java source files to the `bin/` folder
  - Creates necessary directories automatically
  - Shows "Build Complete!" when done
  - Run this first before playing
  
- **play.bat** - Launches the game from compiled classes in `bin/` folder
  - Automatically finds and runs the game
  - Handles all the Java commands for you
  - Just double-click and play!

### build.ps1 (PowerShell alternative)
- Advanced build script for users who prefer PowerShell
- Provides better error handling and colored output

## Recent Updates

### Version 2.1 - Simplified Build System
- 🔨 **build.bat**: Simple double-click compilation
- ▶️ **play.bat**: Simple double-click to launch the game
- 🎨 **Smooth Transitions**: Day/night color transitions instead of abrupt changes
- ⚡ **Quick Start**: Just 2 clicks to build and play!

### Version 2.0 - Menu & Pause System
- ✨ **Main Menu**: Beautiful start screen with game instructions and controls display
- ⏸️ **Pause Menu**: Press P to pause and view current score and lives
- 🎮 **Enhanced UI**: Cleaner game flow with menu-to-game transitions
- 📋 **MenuPanel.java**: New class for menu rendering and controls
- 🔄 **Updated Main.java**: Uses CardLayout for smooth menu/game switching

---

Enjoy the game! Good luck beating your high score! 🏃‍♂️
