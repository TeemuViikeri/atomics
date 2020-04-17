package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class Localization {
    private static Locale locale = new Locale("en", "UK");
    private static I18NBundle myBundle =
            I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);

    static void setLocale(String language) {
        locale = new Locale(language);
        myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);
    }

    static I18NBundle getBundle() {
        return myBundle;
    }
}
