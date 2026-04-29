# SISO-MIRANDA-GROUP - Endless Runner Game

A fun and addictive endless runner game built in pure Java using Swing!

## Features

✅ **Smooth Day/Night Transitions** - Beautiful continuous color blending every 1000 points  
✅ **3-Lane System** - Switch between top, middle, and bottom lanes with W/S keys for depth perception  
✅ **Fullscreen Display** - Game automatically scales to your monitor's resolution  
✅ **Responsive UI** - Menu and all text scale proportionally to screen size  
✅ **Main Menu** - Beautiful start screen with instructions before gameplay  
✅ **Pause Menu** - Press P to pause and see your score and lives  
✅ **Background Music System** - Menu and in-game music with smooth transitions  
✅ **Player with 3 Lives** - Lose all lives and it's game over  
✅ **Grassy Field Platform** - Run on a beautiful green field  
✅ **Spike Obstacles** - Avoid rocks and obstacles to keep running  
✅ **Dynamic Scoreboard** - Track your score and remaining lives (top-left corner)  
✅ **Collision Detection** - Hit an obstacle? Lose a life and restart  
✅ **Game Over Screen** - Restart, return to menu, or quit when you run out of lives  
✅ **Increasing Difficulty** - Game speeds up as your score increases  
✅ **Fully Customizable Graphics** - All visual elements use image-based rendering for easy customization  
✅ **Background Elements** - Clouds, railings, flags, and trees with parallax scrolling  
✅ **Boost Items** - Collect boost items for double points (100 points per boost)  

## How to Play

### Controls
- **SPACEBAR** - Jump over obstacles / Start game from menu
- **W** - Move up to top lane (gets smaller for 3D effect)
- **S** - Move down to bottom lane (gets larger for 3D effect)
- **ESC** - Pause/Unpause game during gameplay
- **SHIFT** - Activate boost (doubles player speed temporarily)
- **R** - Restart game (when game over)
- **M** - Return to menu (when game over)
- **Q** - Quit to menu (when paused) or quit game (when game over)

### Objective
- Run as far as possible without hitting obstacles
- Dodge obstacles in all 3 lanes (top, middle, bottom)
- Each frame you survive increases your score by 1
- Avoid obstacles to keep your 3 lives
- Switch lanes strategically to avoid incoming traffic
- When score reaches 100, the sky darkens - but the obstacles keep coming!

## Compilation & Running

### ⚡ Quick Start (Recommended - Windows)
```
1. Double-click build.bat      (compiles the game)
2. Double-click play.bat       (runs the game)
```
That's it! The game will start immediately.

### Option 1: From src Folder
```bash
cd EndlessRunner/src
javac *.java
java Main
```

### Option 2: From EndlessRunner Folder
```bash
cd EndlessRunner
javac -d bin src/*.java
java -cp bin Main
```

### Option 3: Using Batch Scripts
```bash
cd EndlessRunner
build.bat
play.bat
```

## Game Structure

### Java Source Files
- **MAIN.JAVA** - Entry point, creates window and manages menu/game switching, audio lifecycle
- **MENUPANEL.JAVA** - Main menu screen with game title, controls display, and background music
- **GAMEPANEL.JAVA** - Main game loop, rendering, collisions, smooth transitions, and game music
- **MUSICPLAYER.JAVA** - Reusable audio playback system using PowerShell Media Foundation
- **PLAYER.JAVA** - Player character with jump mechanics and gravity
- **OBSTACLE.JAVA** - Obstacle spawning and collision detection (image-based)
- **CLOUD.JAVA** - Cloud background elements with parallax scrolling (image-based)
- **RAILING.JAVA** - Platform railings decoration (image-based)
- **FLAG.JAVA** - Background flags decoration (image-based)
- **TREE.JAVA** - Background trees for day/night modes (image-based)
- **SCOREBOARD.JAVA** - Score tracking and life management system
- **IMAGELOADER.JAVA** - Custom image loading from resources folder

## Game Mechanics

### Music System
- **Menu Background Music** - Loops continuously when on the main menu
- **Game Background Music** - Plays when in-game, seamlessly transitions from menu music
- **Audio Management** - Automatically handles music lifecycle (starts/stops when switching between menu and game)
- **No Audio Window** - All music playback is internal; no separate media player window opens
- **Smooth Transitions** - Music stops cleanly when switching screens to avoid overlap

### Player
- Starts in the middle lane
- Can jump with SPACEBAR
- Can switch lanes with W (up) and S (down)
- Uses 3D perspective scaling:
  - **Top lane**: Player appears smaller (0.8x scale)
  - **Middle lane**: Normal size (1.0x scale)
  - **Bottom lane**: Player appears larger (1.2x scale)
- Gravity pulls the player back down to the platform
- Resets to middle lane when hitting an obstacle

### Obstacles
- Randomly spawn from the right side in all 3 lanes (top, middle, bottom)
- 10 randomized obstacle types (obstacle1.png through obstacle10.png)
- Move left at increasing speeds
- Apply the same 3D perspective scaling as the player
- Removed when they leave the screen

### Scoring System
- Score increases by 1 each frame (30 FPS)
- **Boost Items** - Collecting a boost item grants 100 bonus points
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

### Game Over Screen
- Display when all lives are lost
- Three options:
  - Press **R** to Restart the game
  - Press **M** to Return to main menu
  - Press **Q** to Quit the program
- Shows final score achieved

### Enhanced Main Menu
- **Clean Design** - Minimalist layout focused on gameplay
- **Title Display** - Large "Motorcyclist seeker" title with game description
- **Interactive Buttons** - Styled START GAME, SAVES, HOW TO PLAY, and EXIT buttons
- **Controls Display** - Press HOW TO PLAY to view all game controls
- **Smooth Transitions** - Controls panel expands when clicked without covering exit button
- **Background Music** - Continuous background music loops while on menu

## Tips for Playing

1. **Timing is Key** - Jump early to clear obstacles safely
2. **Watch the Pattern** - Obstacles spawn at intervals, learn the rhythm
3. **Stay Centered** - The player automatically moves back to starting position
4. **Go for Score** - Each frame counts! Higher scores = more challenging gameplay
5. **Watch the Sky** - When it darkens, you've reached score 100! Keep going!

## Customizing Graphics with Your Own Images

All game graphics are now **image-based** instead of hardcoded! You can easily customize the game appearance by providing PNG images in the `resources/` folder.

### Required Image Files
- **player.png** - Game player character (140x100 pixels)
- **obstacle.png** - Game obstacles fallback (140x100 pixels)
- **obstacle1.png through obstacle10.png** - 10 randomized obstacle types (140x100 pixels each)
- **cloud.png** - Sky clouds (60-80 pixels)
- **railing.png** - Platform railings (80x25 pixels)
- **flag.png** - Background flags (50x30 pixels)
- **tree.png** - Day mode trees (50x80 pixels)
- **tree-night.png** - Night mode trees (50x80 pixels)
- **background.png** - Day mode background (800x500+ pixels)
- **background-night.png** - Night mode background (800x500+ pixels)

### How to Add Custom Images
1. Create or source PNG images for the elements you want to customize
2. Place them in the `resources/` folder
3. Name them exactly as specified above
4. Restart the game - your custom images will load automatically!

### Image Tips
- Use PNG format with transparent backgrounds (alpha channel)
- If an image is not found, that element simply won't render
- Check the console output for any image loading errors
- For best results, use sprites with consistent aspect ratios

See `ASSETS_REMOVED.txt` for detailed technical information about removed hardcoded graphics.

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

### Version 4.0 - Audio System & Enhanced Menu (Latest)
- 🎵 **Background Music System** - Added menu and in-game background music using PowerShell Media Foundation
- 🎮 **MusicPlayer.java** - New reusable audio system for seamless music transitions
- 🖼️ **Enhanced Menu UI** - Redesigned menu with shadow effects, rounded buttons, and better visual hierarchy
- 🔄 **Menu Music Lifecycle** - Music properly starts/stops when transitioning between menu and game
- ➕ **Boost Item Rewards** - Collecting boost items grants 100 bonus points
- 🔙 **Return to Menu** - New M key option on game over screen to return to main menu without quitting
- 📋 **Improved Game Over** - Three distinct options (Restart, Return to Menu, Quit)

### Version 3.5 - Menu & Pause System
- 🚀 Nitro boost: The player can now press Shift to boost themselves
- 📝 Added player animation to give the illusion of movement

### Version 3.0 - Complete Image-Based Refactor ✨
- 🎨 **Fully Customizable Graphics** - ALL visual elements now use image-based rendering
- ❌ **Removed Hardcoded Graphics:**
  - Obstacle rocks (detailed 3D-style drawings)
  - Cloud formations (fluffy white shapes)
  - Platform railings (geometric lines)
  - Background flags (colored rectangles)
  - Forest trees (polygon-based shapes)
- 📦 **New Classes:**
  - `Cloud.java` - Image-based cloud management
  - `Railing.java` - Image-based railing decoration
  - `Flag.java` - Image-based flag decoration
  - `Tree.java` - Image-based tree management (day/night)
- 🖼️ **Complete Customization Support** - Provide PNG images in resources/ folder
- 📄 **Documentation** - Updated IMAGES_README.md with new image specifications
- 🐛 **Code Quality** - Eliminated duplicate hardcoded drawing methods
- 📋 **Asset Tracking** - Created ASSETS_REMOVED.txt for technical reference

### Version 2.2 - Bug Fixes & Code Quality
- 🐛 **Fixed Smooth Transitions**: Eliminated abrupt day/night switching - now perfectly smooth (400-600 & 900-1000 point zones)
- 📖 **Code Headers**: All Java files now have clear file names and descriptions for easy navigation
- 🎮 **Multiple Run Options**: Game now runs from both `src/` and `EndlessRunner/` folders
- ✨ **Better Readability**: Clear file structure with organized class descriptions

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
