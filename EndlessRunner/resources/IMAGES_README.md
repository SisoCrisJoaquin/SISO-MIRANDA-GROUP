# Adding Custom Images to Endless Runner

This folder contains the custom images for the Endless Runner game.

## Image Files

Place your PNG images in this folder with these exact names:

### 1. player.png (Required)
**Size:** 30x40 pixels
**Format:** PNG with transparency
**Description:** The sprite for the player character
**Example:** A simple humanoid figure, character sprite, or icon

### 2. obstacle.png (Required)
**Size:** 40x50 pixels  
**Format:** PNG with transparency
**Description:** The sprite for obstacles the player must avoid
**Example:** A rock, spike, boulder, or enemy sprite

### 3. background.png (Optional)
**Size:** 800x500 pixels (or wider for smoother scrolling)
**Format:** PNG
**Description:** The scrolling background image
**Example:** A landscape, street, or repeating pattern
**Note:** Should be wider than 800px for seamless parallax scrolling

## How to Create Images

### Option 1: Resize Existing Images
1. Find or download images online (see resources below)
2. Use an image editor (Paint, GIMP, Photoshop, etc.)
3. Resize to the recommended dimensions
4. Save as PNG format

### Option 2: Create New Images
- Use online pixel art tools: Piskel.app, Aseprite
- Use drawing programs: GIMP, Paint.NET, Photoshop
- Hand-draw on paper and scan/photograph

### Option 3: Download Assets
Popular free resources:
- **OpenGameArt.org** - Free game sprites and assets
- **itch.io** - Indie game assets (filter by free)
- **Pixabay** - Free images that can be modified
- **Unsplash** - High-quality free photos
- **Game-Icons.net** - Icons that can be turned into sprites

## Image Conversion Guide

### Using Windows Paint
1. Open Paint and your image
2. Image → Resize → Set width to 30, height to 40 (for player)
3. File → Export As → Save as PNG

### Using GIMP (Free)
1. Image → Scale Image
2. Set dimensions (30x40 for player, 40x50 for obstacle)
3. File → Export As → Save as filename.png

### Using Paint.NET (Free)
1. Image → Resize
2. Enter new dimensions
3. File → Export As → PNG

### Using Online Tools
- **TinyPNG.com** - Compress and resize images
- **Pixlr.com** - Online image editor
- **Canva.com** - Design tools

## Transparency in Images

For player.png and obstacle.png, use transparency (alpha channel):

**With GIMP:**
1. Layer → Transparency → Add Alpha Channel
2. Use the eraser tool to remove background
3. Export as PNG

**With Paint.NET:**
1. Layers → Add Alpha Channel
2. Select the eraser tool
3. Erase the background
4. File → Export As

**With Photoshop:**
1. Background layer → Layer
2. Use eraser or magic wand to remove background
3. Export as PNG-24

## Testing Your Images

1. Place your PNG files in the resources/ folder
2. Run run.bat to rebuild
3. Run play.bat to test
4. If images appear, you're done!
5. If not, check:
   - Filenames are lowercase: player.png, obstacle.png, background.png
   - Image dimensions match recommendations
   - Files are in PNG format
   - Check console for error messages

## Troubleshooting

**Images not showing up?**
- Check filenames are exact (lowercase): player.png, obstacle.png, background.png
- Ensure PNG format (not JPG or other formats)
- Make sure files are in the resources/ folder
- Check file permissions (should be readable)

**Images look pixelated?**
- Use higher resolution images if possible
- Ensure images match the recommended sizes

**Game looks wrong with new background?**
- Make sure background.png is wide enough (800px minimum)
- Consider making it 3x wider (2400px) for better parallax effect

**Animated sprites?**
- Currently, the game uses static images
- For animations, you'd need to modify the code

## Recommended Free Image Packs

- **Kenney.nl** - High-quality free game assets
- **OpenGameArt Spritesheets** - Animated character sprites
- **Pixel Art Top-Down Characters** - On various art sites
- **Game Over Screen Art** - Search on itch.io

## After Adding Images

1. Rebuild: double-click run.bat
2. Play: double-click play.bat
3. Enjoy your custom game!

For questions or issues, check the main README.md file in the parent directory.
