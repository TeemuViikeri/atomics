package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Memory {
    static String getLanguage() {
        Preferences prefs = Gdx.app.getPreferences("MyPreferences");
        String language = prefs.getString("language");
        return language;
    }

    static void setLanguage(String language) {
        Preferences prefs = Gdx.app.getPreferences("MyPreferences");
        prefs.putString("language", language);
        prefs.flush();
    }

    static void setVolume(float volume) {
        Preferences prefs = Gdx.app.getPreferences("MyPreferences");
        prefs.putFloat("volume", volume);
        prefs.flush();
    }

    static float getVolume() {
        Preferences prefs = Gdx.app.getPreferences("MyPreferences");
        float volume = prefs.getFloat("volume");
        return volume;
    }

    static void setFirstStartup() {
        Preferences prefs = Gdx.app.getPreferences("MyPreferences");
        prefs.putBoolean("firstStartup", true);
        prefs.flush();
    }

    static boolean getFirstStartup() {
        Preferences prefs = Gdx.app.getPreferences("MyPreferences");
        boolean firstStartup = prefs.getBoolean("firstStartup");
        return firstStartup;
    }
}
