import java.io.*;

/**
 * ========== GAMESAVE.JAVA ==========
 * Serializable class to store and load game state
 * Allows players to save/load up to 2 game saves
 */
public class GameSave implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public int score;
    public int lives;
    public int playerLane;
    public int currentSpeed;
    public long timestamp;
    
    public GameSave(int score, int lives, int playerLane, int currentSpeed) {
        this.score = score;
        this.lives = lives;
        this.playerLane = playerLane;
        this.currentSpeed = currentSpeed;
        this.timestamp = System.currentTimeMillis();
    }
    
    public static void saveToDisk(GameSave save, int slotNumber) {
        String filename = "save" + slotNumber + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(save);
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }
    
    public static GameSave loadFromDisk(int slotNumber) {
        String filename = "save" + slotNumber + ".dat";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (GameSave) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
    
    public static boolean saveExists(int slotNumber) {
        String filename = "save" + slotNumber + ".dat";
        return new File(filename).exists();
    }
    
    public static void deleteSave(int slotNumber) {
        String filename = "save" + slotNumber + ".dat";
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }
}
