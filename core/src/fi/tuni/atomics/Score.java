package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Score {
    private static int score = 0;
    private FreeTypeFontGenerator fontGenerator;
    private BitmapFont font;
    private Label text;
    private Label.LabelStyle textStyle;
    private Stage stage;
    private GlyphLayout layout;

    Score() {
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Raleway-Black.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20 * Gdx.graphics.getWidth() / 640;
        font = fontGenerator.generateFont(parameter);
        textStyle = new Label.LabelStyle();
        textStyle.font = font;
        layout = new GlyphLayout();
        layout.setText(font, "Score:");
    }

    void draw(SpriteBatch batch) {
        font.draw(batch, "Score " + score, Atomics.TILE_LENGTH_PIXELS * 2,
                Atomics.HUD_Y + layout.height);
    }

    static void collectPhosphorus() {
        score += 5;
    }
}
