# Endless Runner Game - Setup & Build Guide

## Quick Start

1. **Build the game:** Double-click `run.bat`
2. **Play the game:** Double-click `play.bat` or `EndlessRunner.jar`

That's it! The game will start immediately.

---

## Building the Game

### Automatic Build (Windows)

Simply double-click **`run.bat`** in the EndlessRunner folder.

The script will:
1. ✅ Check if Java is installed
2. ✅ Compile all Java source files
3. ✅ Create output folders
4. ✅ Generate `EndlessRunner.jar` - Your executable game!

**What gets created:**
- `bin/` folder - Compiled Java files
- `bin/META-INF/` - JAR manifest information
- `EndlessRunner.jar` - Executable game (you can run this!)

### Manual Build (for advanced users)

```bash
# 1. Navigate to project folder
cd EndlessRunner

# 2. Create output directory
mkdir bin

# 3. Compile all Java files
cd src
javac -d ../bin *.java
cd ..

# 4. Create META-INF directory
mkdir bin\META-INF

# 5. Create manifest file
echo Manifest-Version: 1.0 > bin\META-INF\MANIFEST.MF
echo Main-Class: Main >> bin\META-INF\MANIFEST.MF

# 6. Build the JAR
cd bin
jar cfm ../EndlessRunner.jar META-INF\MANIFEST.MF *.class
cd ..
```

---

## Running the Game

### Option 1: Double-click the Play Script ⭐ (Easiest)
Double-click **`play.bat`** - This automatically finds and runs the game!

### Option 2: Double-click the JAR File
Double-click **`EndlessRunner.jar`** directly to run the game.
- Make sure Java is installed
- The game window will appear immediately

### Option 3: Run from Command Prompt
```bash
java -jar EndlessRunner.jar
```

### Option 4: Run from Compiled Classes
```bash
java -cp bin Main
```

---

## System Requirements

- **Java Version:** JDK 8 or higher (Java 8, 11, 17, 21, etc.)
- **RAM:** 100MB minimum
- **Disk Space:** 50MB for downloaded JDK + 10MB for game files
- **OS:** Windows, Mac, or Linux

### Download Java

If you don't have Java installed:

1. Visit **oracle.com/java/technologies/javase-downloads.html**
2. Download "JDK" (Java Development Kit) - not JRE
3. Run the installer and follow instructions
4. Restart your computer
5. Try run.bat again

**Alternative Java Sources:**
- AdoptOpenJDK.net
- Eclipse Adoptium (free RECOMMENDED)
- Amazon Corretto

---

## Adding Custom Images

The game supports custom graphics! Here's how:

### 1. Create Images

You'll need three PNG images:

| File | Size | Purpose |
|------|------|---------|
| `player.png` | 30x40 px | Player sprite |
| `obstacle.png` | 40x50 px | Obstacle sprite |
| `background.png` | 800x500+ px | Background image |

**How to create images:**
- Use Paint, GIMP, or any image editor
- Resize existing images to recommended dimensions
- Save as PNG format
- Remove backgrounds using transparency (optional but recommended)

**Where to find free images:**
- OpenGameArt.org - Game sprites
- itch.io - Indie game assets  
- Pixabay.com - Free photos
- Unsplash.com - High-quality images

### 2. Add Images to Game

1. Place your PNG files in the **`resources/`** folder
2. Name them exactly: `player.png`, `obstacle.png`, `background.png`
3. Run `run.bat` to rebuild
4. Run `play.bat` to see your custom images!

### 3. Folder Structure

After adding images:
```
EndlessRunner/
├── src/
│   └── *.java          (Java source code)
├── resources/          ← Create this or it already exists
│   ├── player.png      ← Add your player image
│   ├── obstacle.png    ← Add your obstacle image
│   └── background.png  ← Add your background image (optional)
├── bin/                (Created automatically)
├── run.bat             ← Click to build
├── play.bat            ← Click to play
└── EndlessRunner.jar   (Created after build)
```

### 4. Image Guide

See **resources/IMAGES_README.md** for detailed instructions on:
- Image dimensions
- How to create/edit images
- Transparency in images
- Troubleshooting
- Free image resources

---

## File Descriptions

### Main Files
- **Main.java** - Game entry point (creates window)
- **GamePanel.java** - Main game loop and rendering
- **Player.java** - Player character with physics
- **Obstacle.java** - Obstacles to avoid
- **Scoreboard.java** - Score and lives tracking
- **ImageLoader.java** - Image loading utility

### Build Scripts
- **run.bat** - Compiles code and creates JAR file
- **play.bat** - Runs the game
- **README.md** - Game documentation
- **SETUP.md** - This file

### Directories
- **src/** - Java source code
- **bin/** - Compiled .class files and JAR
- **resources/** - Custom images (create if needed)

---

## Troubleshooting

### "Java is not installed"
**Problem:** run.bat shows "Java is not installed"
**Solution:**
1. Download and install Java (see above)
2. Restart your computer
3. Run run.bat again

### Compilation fails
**Problem:** "Compilation failed" error
**Solution:**
1. Make sure all .java files are in `src/` folder
2. Check for typos in file names
3. Verify Java is installed: Open Command Prompt and type `java -version`
4. Try deleting `bin/` folder and running run.bat again

### JAR won't run
**Problem:** "EndlessRunner.jar cannot be opened"
**Solution:**
1. Right-click EndlessRunner.jar
2. Select "Open with" → "Java"
3. If Java isn't listed, install Java first
4. Or try: `java -jar EndlessRunner.jar` in Command Prompt

### Images not showing
**Problem:** Game runs but images are colored shapes
**Solution:**
1. Create a `resources/` folder
2. Place images with exact names: player.png, obstacle.png, background.png
3. Run `run.bat` to rebuild
4. Run `play.bat` to test
5. Check the console for error messages (right-click run.bat → Edit to see more output)

### Game is too slow/jerky
**Problem:** Low frame rate
**Solution:**
1. Close other applications
2. Update graphics drivers
3. The game should run at 60+ FPS on modern computers

### Images are too big/small
**Problem:** Graphics don't look right
**Solution:**
1. Make sure images are exactly:
   - player.png: 30x40 pixels
   - obstacle.png: 40x50 pixels
   - background.png: 800x500+ pixels
2. Rebuild with correct dimensions

---

## File Management

### Backing Up Your Game

If you want to keep a backup:
```
Copy these files to a safe location:
- EndlessRunner.jar (the game executable)
- resources/ folder (your custom images)
```

To distribute your game to friends:
1. Give them `EndlessRunner.jar`
2. If using custom images, also include `resources/` folder
3. They just need Java installed to play!

### Updating/Rebuilding

To rebuild after changes:
1. Double-click `run.bat`
2. The script will recompile and update EndlessRunner.jar
3. Run `play.bat` to test immediately

---

## Game Controls (While Playing)

- **SPACE** or **UP ARROW** - Jump
- **R** - Restart (when game is over)
- **Q** - Quit (when game is over)

---

## Game Features

✅ **Endless Gameplay** - Survive as long as possible
✅ **Increasing Difficulty** - Game gets faster with score
✅ **Day/Night Cycle** - Environment changes at score milestones
✅ **Lives System** - Start with 3 lives
✅ **High Score Potential** - No upper limit!
✅ **Smooth Physics** - Realistic jumping and gravity
✅ **Custom Graphics** - Add your own images!
✅ **JAR Executable** - Run like a real game application

---

## Performance Tips

**For faster compilation:**
- Close unnecessary programs
- Make sure you have 1GB+ free disk space
- Use a solid-state drive (SSD) if possible

**For better gameplay:**
- Close applications using network/graphics
- Use fullscreen mode if available
- Update your graphics drivers

---

## For Developers

Want to modify the game? Here's how:

1. Edit the .java files in `src/` folder
2. Run `run.bat` to rebuild
3. Run `play.bat` to test changes
4. Share your improvements!

**Key files to modify:**
- **GamePanel.java** - Game speed, spawning rate, mechanics
- **Player.java** - Jump height, gravity, size
- **Obstacle.java** - Size, shape, behavior
- **Scoreboard.java** - Scoring formula, lives

---

## Still Having Issues?

1. **Check the console output** - run.bat will show error messages
2. **Review the error carefully** - Most errors are due to Java not being installed
3. **Verify all files exist** - Check that all .java files are in src/
4. **Try the Manual Build** - Follow "Manual Build" section above
5. **Restart your computer** - Sometimes this fixes Java-related issues

---

## Next Steps

1. ✅ Run `run.bat` to build
2. ✅ Run `play.bat` to play
3. ✅ Create custom images in the resources/ folder
4. ✅ Rebuild with `run.bat` to use your images
5. ✅ Enjoy your game!

**Have fun playing and customizing your Endless Runner!** 🏃‍♂️🎮
