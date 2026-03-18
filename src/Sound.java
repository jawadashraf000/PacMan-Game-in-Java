import javax.sound.sampled.*;
import java.net.URL;
import java.util.HashMap;

public class Sound {

    private static final HashMap<String, Clip> sounds = new HashMap<>();

    public static void loadAll() {
        load("logo", "/sounds/logo.wav");
        load("start", "/sounds/start.wav");
        load("death", "/sounds/death.wav");
        load("waka", "/sounds/waka.wav");
        load("bonus", "/sounds/bonus.wav");
        load("siren", "/sounds/siren.wav");
    }

    private static void load(String name, String path) {
        try {
            URL url = Sound.class.getResource(path);
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            sounds.put(name, clip);
        } catch (Exception e) {
            System.out.println("ERROR loading sound: " + path);
        }
    }

    public static void play(String name) {
        Clip clip = sounds.get(name);
        if (clip == null) return;

        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public static void loop(String name) {
        Clip clip = sounds.get(name);
        if (clip == null) return;

        clip.stop();
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void stop(String name) {
        Clip clip = sounds.get(name);
        if (clip == null) return;
        clip.stop();
    }
}