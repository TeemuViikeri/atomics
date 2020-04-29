package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

class Memory {
    static String getLanguage() {
        Preferences prefs = Gdx.app.getPreferences("MyPreferences");
        return prefs.getString("language");
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
        return prefs.getFloat("volume");
    }

    static void setFirstStartup() {
        Preferences prefs = Gdx.app.getPreferences("MyPreferences");
        prefs.putBoolean("firstStartup", true);
        prefs.flush();
    }

    static boolean getFirstStartup() {
        Preferences prefs = Gdx.app.getPreferences("MyPreferences");
        return prefs.getBoolean("firstStartup");
    }
}
