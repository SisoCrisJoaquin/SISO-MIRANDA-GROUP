import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * ========== IMAGELOADER.JAVA ==========
 * Utility for loading and caching custom game images
 * Supports PNG images from resources folder or file system
 * Gracefully falls back to default graphics if images not found
 */
public class ImageLoader {
    private static final Map<String, BufferedImage> imageCache = new HashMap<>();
    
    /**
     * Load an image from resources or file system
     * @param path The path to the image (resource path like "/player.png" or file path like "resources/player.png")
     * @return BufferedImage, or null if unable to load
     */
    public static BufferedImage loadImage(String path) {
        // Check cache first
        if (path != null && imageCache.containsKey(path)) {
            return imageCache.get(path);
        }
        
        BufferedImage image = null;
        
        try {
            // Try loading from resources first (JAR resources)
            if (path != null && path.startsWith("/")) {
                InputStream inputStream = ImageLoader.class.getResourceAsStream(path);
                if (inputStream != null) {
                    image = ImageIO.read(inputStream);
                    if (image != null) {
                        imageCache.put(path, image);
                        return image;
                    }
                }
            }
            
            // Try loading from file system
            String filePath = path;
            if (!path.startsWith("/")) {
                filePath = "resources/" + path;
            } else {
                filePath = "resources" + path;
            }
            
            File file = new File(filePath);
            if (file.exists()) {
                image = ImageIO.read(file);
                if (image != null && path != null) {
                    imageCache.put(path, image);
                }
            } else {
                System.err.println("Image not found: " + path + " (checked: " + filePath + ")");
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
            e.printStackTrace();
        }
        
        return image;
    }
    
    /**
     * Clear the image cache to free memory
     */
    public static void clearCache() {
        imageCache.clear();
    }
    
    /**
     * Get the number of cached images
     */
    public static int getCacheSize() {
        return imageCache.size();
    }
    
    /**
     * Load and scale a background image to fit screen dimensions
     * @param path The path to the image
     * @param targetWidth Target width for the background
     * @param targetHeight Target height for the background
     * @return Scaled BufferedImage, or null if unable to load
     */
    public static BufferedImage loadAndScaleBackgroundImage(String path, int targetWidth, int targetHeight) {
        BufferedImage original = loadImage(path);
        if (original == null) return null;
        
        // Create scaled image that maintains aspect ratio
        BufferedImage scaled = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(original, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        
        return scaled;
    }
}
