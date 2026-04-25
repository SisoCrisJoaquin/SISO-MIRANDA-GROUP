BOOST ITEM IMAGE SPECIFICATIONS
================================

File Name: boostitem.png
Location: resources/boostitem.png
Format: PNG (with transparency/alpha channel)

RECOMMENDED DIMENSIONS:
=======================
Primary Recommendation: 128 x 128 pixels
Alternative: 256 x 256 pixels (for higher quality)
Minimum Size: 80 x 80 pixels

The boost item will be automatically scaled based on player lane:
- Lane 0 (top lane):    90% scale  → Displays as 72x72 (or 115x115 for 128px asset)
- Lane 1 (middle lane): 100% scale → Displays as 80x80 (or 128x128 for 128px asset)
- Lane 2 (bottom lane): 110% scale → Displays as 88x88 (or 141x141 for 128px asset)

DESIGN RECOMMENDATIONS:
=======================
- Use gold, yellow, or orange colors to match the "nitro/boost" theme
- Ensure transparency around the edges for smooth blending
- Consider a glowing or lightning bolt effect
- Make it visually distinct from obstacles and player

TECHNICAL REQUIREMENTS:
=======================
- Format: PNG file
- Transparency: Required (PNG 32-bit with alpha channel)
- No animation frames needed (static image)
- Color Space: RGB or RGBA

FALLBACK BEHAVIOR:
==================
If boostitem.png is not found, the game will display a solid gold rectangle
with a dark gray border as a placeholder.

INSTALLATION:
==============
1. Create or design your boostitem.png image (128x128 recommended)
2. Save it as PNG format with transparency
3. Place the file in: EndlessRunner/resources/boostitem.png
4. Rebuild the game: .\build.bat
5. Play the game: play.bat

The image will be loaded automatically and scaled appropriately for each lane.
