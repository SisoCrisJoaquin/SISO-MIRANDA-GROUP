# SISO-MIRANDA-GROUP - Endless Runner Game

A fun and addictive endless runner game built in pure Java using Swing! Features dynamic day/night transitions, smooth gameplay, and engaging mechanics.

## 🎮 Features

✅ **Smooth Day/Night Transitions** - Beautiful continuous color blending every 1000 points  
✅ **Main Menu** - Beautiful start screen with instructions before gameplay  
✅ **Pause Menu** - Press P to pause and see your score and lives  
✅ **Player with 3 Lives** - Lose all lives and it's game over  
✅ **Grassy Field Platform** - Run on a beautiful green field  
✅ **Detailed Obstacles** - Avoid rocks and obstacles with realistic 3D effects  
✅ **Dynamic Scoreboard** - Track your score and remaining lives in real-time  
✅ **Collision Detection** - Precise collision detection with obstacles  
✅ **Game Over Screen** - Restart or quit when you run out of lives  
✅ **Increasing Difficulty** - Game speeds up as your score increases  
✅ **Custom Graphics Support** - Add your own PNG images for player, obstacles, and backgrounds
✅ **Parallax Scrolling** - Background elements with depth perception  
✅ **Seamless Wrapping** - Smooth infinite scrolling backgrounds

## 🎮 How to Play

### Controls
- **SPACEBAR** - Jump over obstacles / Start game from menu
- **P** - Pause/Unpause game during gameplay
- **R** - Restart game (when game over)
- **Q** - Quit to menu (when paused) or quit game (when game over)

### Objective
- Run as far as possible without hitting obstacles
- Each frame you survive increases your score by 1
- Avoid obstacles to keep your 3 lives
- Score reaches different zones every 1000 points with smooth day/night transitions!

### Tips for Playing
1. **Timing is Key** - Jump early to clear obstacles safely
2. **Watch the Pattern** - Obstacles spawn at intervals, learn the rhythm
3. **Stay Centered** - The player automatically moves back to starting position
4. **Go for Score** - Each frame counts! Higher scores = more challenging gameplay
5. **Watch the Sky** - Enjoy the smooth transitions between day and night!

## 📁 Project Structure

```
SISO-MIRANDA-GROUP/
│
├── README.md                    (Main documentation)
├── START_HERE.txt              (Quick start guide)
│
└── EndlessRunner/              (Game folder)
    │
    ├── README.md               (Game-specific docs)
    ├── build.bat               (Build script - double-click to compile)
    ├── play.bat                (Play script - double-click to run)
    ├── build.ps1               (PowerShell build script)
    │
    ├── src/                    (Java source code)
    │   ├── Main.java           (Entry point, window management)
    │   ├── MenuPanel.java      (Main menu screen)
    │   ├── GamePanel.java      (Main game loop & rendering)
    │   ├── Player.java         (Player character & jumping)
    │   ├── Obstacle.java       (Obstacle spawning & collision)
    │   ├── Scoreboard.java     (Score & lives tracking)
    │   └── ImageLoader.java    (Custom image support)
    │
    ├── bin/                    (Compiled Java classes)
    │   └── *.class
    │
    └── resources/              (Custom images folder - optional)
        ├── player.png          (Custom player image)
        ├── obstacle.png        (Custom obstacle image)
        └── background.png      (Custom background image)
```

## ⚡ Quick Play

### 🏃 Just Want to Play? (Fastest!)
If you already have the game compiled:
```
Go to EndlessRunner folder and double-click play.bat
Game starts immediately!
```

## ⚡ Quick Start

### Windows (Easiest - First Time Setup)
```
1. Go to EndlessRunner folder
2. Double-click build.bat      (compiles the game)
3. Double-click play.bat       (runs the game)
Done! The game will start immediately.
```

### From Command Line
```bash
# Option 1: From src folder
cd EndlessRunner/src
javac *.java
java Main

# Option 2: From EndlessRunner folder
cd EndlessRunner
javac -d bin src/*.java
java -cp bin Main

# Option 3: Using batch scripts
cd EndlessRunner
build.bat
play.bat
```

## 🏗️ Game Architecture

### Java Source Files
- **MAIN.JAVA** - Entry point, creates window and manages menu/game switching
- **MENUPANEL.JAVA** - Main menu screen with game title and controls display
- **GAMEPANEL.JAVA** - Main game loop, rendering, collisions, and smooth transitions
- **PLAYER.JAVA** - Player character with jump mechanics and gravity
- **OBSTACLE.JAVA** - Obstacle spawning and collision detection
- **SCOREBOARD.JAVA** - Score tracking and life management system
- **IMAGELOADER.JAVA** - Custom image loading from resources folder

### Game Mechanics

#### Player
- Starts on the left side of the platform
- Jumps with SPACEBAR
- Gravity pulls back down to platform
- Resets position when hitting an obstacle

#### Obstacles
- Randomly spawn from right side
- Move left at increasing speeds
- Remove when off-screen

#### Scoring System
- Score increases by 1 each frame (30 FPS)
- Score resets when hitting obstacle
- Speed increases with score

#### Lives System
- Player starts with 3 lives
- Lose 1 life per collision
- Game over when lives = 0

#### Day/Night Cycle (Every 1000 Points)
- **0-400 points**: Full day (light blue sky)
- **400-600 points**: Smooth day→night transition
- **600-900 points**: Full night (dark sky with stars)
- **900-1000 points**: Smooth night→day transition
- Repeats infinitely

#### User Interface
- **Main Menu**: Shows at startup with controls and title
- **Pause Menu**: Press P to pause, shows score and lives
- **Game Over Screen**: Shows final score, press R to restart or Q to quit

## 📝 Build System

### Build Scripts (Windows)
- **build.bat** - Compiles all Java source files to the `bin/` folder
  - Creates necessary directories automatically
  - Shows "Build Complete!" when done
  
- **play.bat** - Launches the game from compiled classes in `bin/` folder
  - Automatically finds and runs the game
  - Just double-click and play!

- **build.ps1** - PowerShell build script with better error handling

## 🚀 Recent Updates

### Miranda's Session Updates - Comprehensive Development

#### 🎮 Menu System Implementation
- **MenuPanel.java** - New class created for main menu screen
  - Displays game title and welcome message
  - Shows all available controls (SPACEBAR to start, P to pause, Q to quit)
  - Implements KeyListener for player input detection
  - SPACEBAR press triggers game start via Main.java

#### ⏸️ Pause Menu with Score Display
- Added pause functionality via P key during gameplay
- Pause menu displays:
  - Current score
  - Remaining lives
  - Instructions to resume (P) or quit (Q)
- Semi-transparent overlay for better visibility
- Game loop only updates when NOT paused

#### 🌅 Smooth Day/Night Transitions with Color Blending
- Implemented linear RGB color interpolation algorithm
  - Formula: `new_value = old_value * (1 - progress) + target_value * progress`
- Extended transition zones for seamless changes:
  - **400-600 points**: Day to Night transition zone (200-point blend)
  - **600-900 points**: Full night mode (stars and trees visible)
  - **900-1000 points**: Night to Day transition zone (100-point blend)
- Color updates happen every frame for smooth visual effect

#### 🐛 Bug Fix: Eliminated Abrupt Transitions
- **Problem**: Day/night environment switched abruptly mid-transition
- **Root Cause**: `isNightTime()` threshold didn't align with color transition completion
- **Solution**: 
  - Changed `isNightTime()` to return true at score >= 550 (after full color blend completes)
  - Extended transition zones ensure color blending finishes before environment switches
  - Result: Perfectly smooth transitions with no visual jarring

#### 🔨 Simplified Build System
- **build.bat** - Created simple compilation script
  - Automatically creates `bin/` directory
  - Compiles all `.java` files from `src/` folder
  - Displays success message when complete
  - No external jar dependencies needed
  
- **play.bat** - Created simple execution script
  - Runs compiled game from `bin/` folder using `java -cp bin Main`
  - Just double-click to play!
  - No complex Java commands needed

- **build.ps1** - PowerShell alternative for advanced users
  - Enhanced error handling and feedback
  - Color-coded output for better readability
  - Cross-platform compatibility

#### 📖 Code Documentation & File Headers
- Added clear file headers to all Java classes:
  - `Main.java` - Application entry point and window management
  - `MenuPanel.java` - Main menu screen and controls
  - `GamePanel.java` - Core game loop and rendering engine
  - `Player.java` - Player character with jump mechanics
  - `Obstacle.java` - Obstacle spawning and collision detection
  - `Scoreboard.java` - Score and lives tracking
  - `ImageLoader.java` - Custom image loading with fallback support
- Each header clearly states file purpose and key features

#### 🎮 Multiple Run Options Verified
- Tested and confirmed game runs from three different methods:
  1. **From src folder**: `cd EndlessRunner/src; javac *.java; java Main`
  2. **From EndlessRunner folder**: `javac -d bin src/*.java; java -cp bin Main`
  3. **Via batch scripts**: `build.bat` then `play.bat`
- All methods validated and working perfectly

#### 📚 Documentation Consolidation
- **Root README.md** - Completely reorganized and expanded
  - Consolidated with EndlessRunner/README.md content
  - Added comprehensive project structure with both root and game folders
  - Included all run options and build methods
  - Professional formatting with emoji section markers
  
- **START_HERE.txt** - Updated with generic file paths
  - Removed personal machine-specific file paths
  - Made guide usable for anyone cloning the repository
  
- **EndlessRunner/README.md** - Updated with Version 2.2 documentation
  - Highlighted smooth transition fixes
  - Added code quality improvements
  - Documented multiple run options

#### 🏗️ Code Quality Improvements
- All Java files compile without errors
- Clear variable naming and comments
- Organized class hierarchy with proper inheritance
- Efficient game loop with 30 FPS consistent performance
- Proper resource management and cleanup

---

### Version 2.2 - Bug Fixes & Code Quality
- 🐛 **Fixed Smooth Transitions**: Eliminated abrupt day/night switching - now perfectly smooth
  - Extended transition zones (400-600 & 900-1000 point ranges)
  - Seamless color blending throughout entire cycle
- 📖 **Code Headers**: All Java files now have clear file names and descriptions for easy navigation
- 🎮 **Multiple Run Options**: Game now runs from both `src/` and `EndlessRunner/` folders
- ✨ **Better Readability**: Clear file structure with organized class descriptions

### Version 2.1 - Simplified Build System
- 🔨 **build.bat**: Simple double-click compilation to `bin/` folder
- ▶️ **play.bat**: Simple double-click to launch the game
- 🎨 **Smooth Transitions**: Day/night color transitions instead of abrupt changes
- ⚡ **Quick Start**: Just 2 clicks to build and play!

### Version 2.0 - Menu & Pause System
- ✨ **Main Menu**: Beautiful start screen with game instructions and controls
- ⏸️ **Pause Menu**: Press P to pause and view current score and lives
- 🎮 **Enhanced UI**: Cleaner game flow with menu-to-game transitions
- 📋 **MenuPanel.java**: New class for menu rendering and controls
- 🔄 **Updated Main.java**: Uses CardLayout for smooth menu/game switching

### Version 1.0 - Initial Release
- Core game mechanics with 3 lives system
- Score tracking and increasing difficulty
- Collision detection and game over screen

---

**Updated by Miranda** 🎮