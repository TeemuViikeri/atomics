package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class PauseWindow {
    Window pause;
    Window.WindowStyle style;
    Skin skin;
    BitmapFont font;
    FreeTypeFontGenerator fontGenerator;
    Label label;
    Label.LabelStyle labelStyle;
    Drawable background;
    Skin buttonSkin;
    float newWidth;
    float newHeight;
    Stage stage;

    public PauseWindow() {
        pause = new Window("Paused", getWindowStyle());
        pause.setMovable(false);
        pause.pack();
        newWidth = 400;
        newHeight = 400;
        pause.padTop(20);
        pause.padLeft(20);
        pause.setBounds(
                (Gdx.graphics.getWidth() - newWidth) / 2,
                (Gdx.graphics.getHeight() - newHeight) / 2,
            newWidth,
            newHeight);
        stage = Controls.getStage();
        stage.addActor(pause);
    }

    private Window.WindowStyle getWindowStyle() {
        style = new Window.WindowStyle();

        skin = new Skin();
        skin.add("pauseWindow", new Texture("pauseWindow.png"));

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Raleway-Black.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20 * Gdx.graphics.getWidth() / 640;
        font = fontGenerator.generateFont(parameter);

        background = skin.getDrawable("pauseWindow");

        style.titleFont = font;
        style.background = background;

        return style;
    }
}
