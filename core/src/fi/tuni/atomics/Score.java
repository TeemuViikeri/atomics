package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

class Score {
    private static int score;
    private FreeTypeFontGenerator fontGenerator;
    private BitmapFont font;
    private Label.LabelStyle textStyle;
    private GlyphLayout layout;

    Score() {
        score = 0;
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Raleway-Black.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30 * Gdx.graphics.getWidth() / 960;
        font = fontGenerator.generateFont(parameter);
        textStyle = new Label.LabelStyle();
        textStyle.font = font;
        layout = new GlyphLayout();
        layout.setText(font, "Score:");
    }

    void draw(SpriteBatch batch) {
        font.draw(batch, Localization.getBundle().get("score") +
                        score + "    " + getScoreMultiplier() + "x",
                PlayScreen.TILE_LENGTH_PIXELS * 2,
                PlayScreen.HUD_Y + layout.height);
    }

    void draw(SpriteBatch batch, String text, Vector2 pos) {
        font.draw(batch, text, pos.x, pos.y);
    }

    float getTextWidth(String text) {
        layout.setText(font, text);
        return layout.width;
    }

    float getScore() {
        return score;
    }

    private static int getScoreMultiplier() {
        return Microbe.microbes.size;
    }

    static void collectPhosphorus() {
        score += 5 * getScoreMultiplier();
    }

    static void collectRareItem() { score += 50; }

    static void collectItem() {
        score++;
    }
}
