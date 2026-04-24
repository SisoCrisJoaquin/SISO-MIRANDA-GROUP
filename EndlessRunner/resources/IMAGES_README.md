# Adding Custom Images to Endless Runner

This folder contains the custom images for the Endless Runner game. All visual elements can be customized with PNG images!

## Image Files

Place your PNG images in this folder with these exact names:

### Game Elements (Core)

#### 1. player.png (Required)
**Size:** 140x100 pixels (flexible - scales to any PNG)
**Format:** PNG with transparency
**Description:** The player character sprite
**Example:** A character, person, vehicle, or any game entity
**Note:** This file must be named exactly "player.png"

#### 2. obstacle.png (Fallback)
**Size:** 140x100 pixels (flexible - scales to any PNG)
**Format:** PNG with transparency
**Description:** Default obstacle sprite (used as fallback when obstacle1-10 not found)
**Example:** A car, truck, crate, or obstacle
**Note:** Game randomly selects from obstacle1.png through obstacle10.png first

#### 3. obstacle1.png through obstacle10.png (Optional - Randomized)
**Size:** 140x100 pixels (flexible - scales to any PNG)
**Format:** PNG with transparency
**Description:** 10 different obstacle variations - randomly spawned during gameplay
**Example:** Different vehicle types (cars, trucks, vans, etc.) or obstacle types
**How it Works:** When an obstacle spawns, the game randomly picks one of these files (obstacle1.png to obstacle10.png). If a specific type isn't found, it falls back to obstacle.png
**Note:** You can create as many as you want (1-10). Even having just obstacle1.png will work!

#### 4. background.png (Optional)
**Size:** 800x500 pixels (or wider for smoother scrolling)
**Format:** PNG
**Description:** The scrolling background image for day mode
**Example:** A landscape, street, sky, or repeating pattern
**Note:** Should be wider than 800px for seamless parallax scrolling

#### 4b. background-night.png (Optional)
**Size:** 800x500 pixels (or wider for smoother scrolling)
**Format:** PNG
**Description:** The scrolling background image for night mode
**Example:** A night landscape, stars, dark sky, or night scene
**Note:** If not provided, night mode will use a simple dark sky

### Background Elements (Image-Based)

#### 5. cloud.png (Optional)
**Size:** 60-80 pixels wide, flexible height
**Format:** PNG with transparency
**Description:** Cloud sprites that appear in the sky during day mode
**Example:** Fluffy clouds, simple white clouds, weather clouds

#### 6. railing.png (Optional)
**Size:** 80x25 pixels
**Format:** PNG with transparency
**Description:** Platform railing decorations
**Example:** Metal railings, wooden posts, guard rails, decorative barriers

#### 7. flag.png (Optional)
**Size:** 50x30 pixels
**Format:** PNG with transparency
**Description:** Background flag decorations on poles
**Example:** Flags, banners, pennants, signage

#### 8. tree.png (Optional)
**Size:** 50x80 pixels
**Format:** PNG with transparency
**Description:** Tree sprites for day mode background
**Example:** Deciduous trees, pine trees, forest elements, bushes

#### 9. tree-night.png (Optional)
**Size:** 50x80 pixels
**Format:** PNG with transparency
**Description:** Tree sprites for night mode background
**Example:** Dark/silhouetted trees, night forest elements

## Customization Quick Start

### Essential Files (Minimum)
- `player.png` - Your character sprite
- `obstacle.png` or `obstacle1.png` - At least one obstacle type

### Recommended Files (Best Experience)
- `player.png` (70x50 px)
- `obstacle.png` + `obstacle1.png` through `obstacle10.png` (70x50 px each - mix and match!)
- `cloud.png` (60-80 px wide)
- `railing.png` (80x25 px)
- `flag.png` (50x30 px)
- `tree.png` (50x80 px)
- `tree-night.png` (50x80 px)

### All Optional
All image files are completely optional. If an image is missing, that element simply won't render, but the game will continue to work.

## Required Pixel Sizes Reference

| Image File | Width | Height | Purpose |
|-----------|-------|--------|---------|
| player.png | 70 | 50 | Player character |
| obstacle.png | 70 | 50 | Default obstacle (fallback) |
| obstacle1.png - obstacle10.png | 70 | 50 | Randomized obstacles (1-10 types) |
| cloud.png | 60-80 | flexible | Sky clouds (day mode) |
| railing.png | 80 | 25 | Platform railings |
| flag.png | 50 | 30 | Background flags |
| tree.png | 50 | 80 | Trees (day mode) |
| tree-night.png | 50 | 80 | Trees (night mode) |
| background.png | 800+ | 500+ | Scrolling background (day mode) |
| background-night.png | 800+ | 500+ | Scrolling background (night mode) |

**Important:** Use these exact dimensions for best results. The game scales images to fit these sizes.

## How to Create Images

### Option 1: Resize Existing Images
1. Find or download images online (see resources below)
2. Use an image editor (Paint, GIMP, Photoshop, etc.)
3. Resize to the **exact pixel dimensions** shown in the Reference table above
4. Save as PNG format

### Option 2: Create New Images
- Use online pixel art tools: Piskel.app, Aseprite
- Use drawing programs: GIMP, Paint.NET, Photoshop
- Hand-draw on paper and scan/photograph
- Remember to resize to required pixel dimensions!





### Example: Resizing tree.png to 50x80 pixels

**Using Windows Paint**
1. Open Paint and your image
2. Image → Resize → Set width to 50, height to 80 pixels
3. File → Export As → Save as "tree.png"

**Using GIMP (Free)**
1. Image → Scale Image
2. Set width to 50, height to 80
3. File → Export As → Save as "tree.png"

### Using Paint.NET (Free)
1. Image → Resize
2. Enter width and height from the reference table
3. File → Export As → PNG

### Using Online Tools
- **TinyPNG.com** - Compress and resize images
- **Pixlr.com** - Online image editor with resize feature
- **Canva.com** - Design tools with size presets

## Transparency in Images

For all sprite images (obstacle.png, cloud.png, railing.png, flag.png, tree.png, tree-night.png), use transparency (alpha channel) for best results:

**With GIMP:**
1. Layer → Transparency → Add Alpha Channel
2. Use the eraser tool to remove background
3. Export as PNG

**With Paint.NET:**
1. Layers → Add Alpha Channel
2. Select the eraser tool
3. Erase the background
4. File → Export As → PNG

**With Photoshop:**
1. Background layer → Layer
2. Use eraser or magic wand to remove background
3. Export as PNG-24

## Testing Your Images

1. Place your PNG files in the resources/ folder
2. Run build.bat to compile
3. Run play.bat to test the game
4. Watch for your custom images during gameplay
5. If images don't appear, check:
   - **Filenames are exact and lowercase:**
     - obstacle.png, cloud.png, railing.png, flag.png, tree.png, tree-night.png
   - **Image dimensions match the reference table exactly**
   - **Files are in PNG format (not JPG or other formats)**
   - **Check console output for error messages during startup**

## Troubleshooting

**Images not showing up?**
- Check filenames are exact (all lowercase): obstacle.png, cloud.png, railing.png, flag.png, tree.png, tree-night.png
- Ensure PNG format (not JPG or other formats)
- Make sure files are in the resources/ folder
- Check file permissions (should be readable)
- Look at console output for image loading errors

**Images look wrong or distorted?**
- Use images that match the recommended pixel dimensions exactly
- Avoid scaling up very small images (quality will suffer)

**Only some elements show up?**
- Different images are optional - if one is missing, only that element won't render
- The game will continue working even if all custom images are missing

**Game looks wrong with new background?**
- Make sure background.png is wide enough (800px minimum)
- Consider making it 2-3x wider (1600-2400px) for better parallax effect

## Recommended Free Image Packs

- **Kenney.nl** - High-quality free game assets with sprite sheets
- **OpenGameArt.org** - Community game art repository
- **itch.io** - Thousands of free game assets
- **Pixel Art Top-Down Assets** - Various art sites
- **Game Asset Bundles** - Search on specialized sites

## After Adding Images

1. Rebuild: double-click run.bat
2. Play: double-click play.bat
3. Enjoy your custom game!

For questions or issues, check the main README.md file in the parent directory.
