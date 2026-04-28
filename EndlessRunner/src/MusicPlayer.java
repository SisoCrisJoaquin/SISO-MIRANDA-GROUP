import java.io.File;

/**
 * ========== MUSICPLAYER.JAVA ==========
 * Background music player for the menu system
 * Plays audio files internally within the Java program
 * Uses PowerShell Media Foundation for internal playback
 * Runs in a separate thread to avoid blocking the UI
 */
public class MusicPlayer {
    private Thread playbackThread;
    private volatile boolean isPlaying = false;
    private volatile boolean shouldStop = false;
    private String musicPath;
    private Process audioProcess;

    /**
     * Create a MusicPlayer with the specified music file path
     * @param musicPath Relative path to the audio file (e.g., "resources/music.mp3")
     */
    public MusicPlayer(String musicPath) {
        this.musicPath = musicPath;
    }

    /**
     * Start playing the music on loop
     * Music will repeat continuously until stop() is called
     */
    public void start() {
        if (isPlaying) {
            return; // Already playing
        }

        shouldStop = false;
        isPlaying = true;

        playbackThread = new Thread(() -> {
            while (!shouldStop && isPlaying) {
                try {
                    playOnce();
                    if (shouldStop) break;
                } catch (Exception e) {
                    System.err.println("Music player error: " + e.getMessage());
                    if (!shouldStop) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }
            }
            isPlaying = false;
        });
        playbackThread.setDaemon(true);
        playbackThread.start();
    }

    /**
     * Stop the music playback immediately
     */
    public void stop() {
        shouldStop = true;
        isPlaying = false;
        
        // Immediately terminate the audio process
        if (audioProcess != null) {
            try {
                audioProcess.destroyForcibly();
                // Force wait with timeout
                audioProcess.waitFor(200, java.util.concurrent.TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                // Ignore
            } finally {
                audioProcess = null;
            }
        }
        
        // Interrupt and terminate the playback thread
        if (playbackThread != null) {
            try {
                playbackThread.interrupt();
                playbackThread.join(200); // Very short timeout
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                playbackThread = null;
            }
        }
        
        // Kill any remaining PowerShell processes related to this player
        try {
            ProcessBuilder killPb = new ProcessBuilder(
                "taskkill",
                "/F",
                "/IM", "powershell.exe"
            );
            Process killProcess = killPb.start();
            killProcess.waitFor(500, java.util.concurrent.TimeUnit.MILLISECONDS);
            killProcess.destroyForcibly();
        } catch (Exception e) {
            // Ignore - this is a fallback measure
        }
    }

    /**
     * Check if music is currently playing
     */
    public boolean isPlaying() {
        return isPlaying && !shouldStop;
    }

    /**
     * Play the audio file once - runs in a loop
     */
    private void playOnce() throws Exception {
        // Check if stop was requested before even trying to play
        if (shouldStop) {
            return;
        }
        
        File musicFile = new File(musicPath);
        
        // If relative path, try to resolve it
        if (!musicFile.exists()) {
            musicFile = new File(System.getProperty("user.dir"), musicPath);
        }
        
        if (!musicFile.exists()) {
            throw new Exception("Music file not found: " + musicPath + " (absolute: " + musicFile.getAbsolutePath() + ")");
        }

        String absolutePath = musicFile.getAbsolutePath();
        
        try {
            // Use PowerShell with Windows Media Foundation for internal audio playback
            // Properly detects duration and waits for full playback before returning
            String psCommand = 
                "$ErrorActionPreference = 'SilentlyContinue'; " +
                "[void][Reflection.Assembly]::LoadWithPartialName('presentationCore'); " +
                "$player = New-Object System.Windows.Media.MediaPlayer; " +
                "$player.Open([System.Uri]'file:///" + absolutePath.replace("\\", "/") + "'); " +
                "$player.Play(); " +
                "Start-Sleep -Milliseconds 500; " +  // Give player time to load metadata
                "$duration = $player.NaturalDuration.TimeSpan.TotalMilliseconds; " +
                "if ($duration -gt 100) { " +  // Only trust duration if > 100ms
                "  Start-Sleep -Milliseconds ([int]$duration + 500); " +  // Add 500ms buffer
                "} else { " +
                "  Start-Sleep -Seconds 300; " +  // Fallback: wait 5 minutes max
                "}; " +
                "$player.Stop(); " +
                "$player.Close()";
            
            ProcessBuilder pb = new ProcessBuilder(
                "powershell",
                "-WindowStyle", "Hidden",
                "-NoProfile",
                "-Command", psCommand
            );
            
            pb.redirectErrorStream(true);
            audioProcess = pb.start();
            
            // Wait for playback to complete, but check shouldStop periodically
            long startTime = System.currentTimeMillis();
            while (!shouldStop) {
                try {
                    // Wait in short intervals so we can check shouldStop frequently
                    if (audioProcess.waitFor(100, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                        // Process completed
                        audioProcess = null;
                        break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            // If shouldStop is true, forcibly terminate the process
            if (shouldStop && audioProcess != null) {
                audioProcess.destroyForcibly();
                audioProcess = null;
            }
            
        } catch (Exception e) {
            if (audioProcess != null) {
                try {
                    audioProcess.destroyForcibly();
                } catch (Exception ex) {
                    // Ignore
                }
                audioProcess = null;
            }
            if (!shouldStop) {
                throw new Exception("Failed to play audio: " + e.getMessage());
            }
        }
    }
}
