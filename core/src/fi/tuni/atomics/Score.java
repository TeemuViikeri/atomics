package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

class Score {
    private static int score;
    static int collectedPhosphorusCounter = 0;
    static int collectedNitrogenCounter = 0;
    private BitmapFont font;
    private BitmapFont fontHiscores;
    private BitmapFont fontInfoScreen;
    private GlyphLayout layout;
    private GameUtil gameUtil = new GameUtil();

    Score() {
        Score.collectedNitrogenCounter = 0;
        Score.collectedPhosphorusCounter = 0;
        score = 0;
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 37 * Gdx.graphics.getWidth() / 960;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        FreeTypeFontGenerator.FreeTypeFontParameter parameterHiscores = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterHiscores.size = 50 * Gdx.graphics.getHeight() / 640;
        parameterHiscores.shadowColor = Color.BLACK;
        parameterHiscores.shadowOffsetX = 3;
        parameterHiscores.shadowOffsetY = 3;
        FreeTypeFontGenerator.FreeTypeFontParameter parameterInfoScreen = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterInfoScreen.size = 22 * Gdx.graphics.getWidth() / 960;
        parameterInfoScreen.shadowColor = Color.BLACK;
        parameterInfoScreen.shadowOffsetX = 3;
        parameterInfoScreen.shadowOffsetY = 3;
        fontInfoScreen = fontGenerator.generateFont(parameterInfoScreen);
        fontHiscores = fontGenerator.generateFont(parameterHiscores);
        font = fontGenerator.generateFont(parameter);
        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.font = font;
        layout = new GlyphLayout();
        layout.setText(font, "Score:");
    }

    void drawScore(SpriteBatch batch) {
        font.draw(batch, Localization.getBundle().get("score") +
                        score + "    " + getScoreMultiplier() + "x" + "    " +
                        Localization.getBundle().get("items") + gameUtil.getItemCount() + "/40",
                PlayScreen.TILE_LENGTH_PIXELS * 2,
                PlayScreen.HUD_Y + layout.height);
    }

    void draw(SpriteBatch batch, String text, Vector2 pos) {
        font.draw(batch, text, pos.x, pos.y);
    }

    void drawHiscores(SpriteBatch batch, String text, Vector2 pos) {
        fontHiscores.draw(batch, text, pos.x, pos.y);
    }

    void drawInfoScreen(SpriteBatch batch, String text, Vector2 pos) {
        fontInfoScreen.draw(batch, text, pos.x, pos.y);
    }

    float getTextWidthHiscores(String text) {
        layout.setText(fontHiscores, text);
        return layout.width;
    }

    float getTextHeightHiscores(String text) {
        layout.setText(fontHiscores, text);
        return layout.height;
    }

    float getTextWidthInfoScreen(String text) {
        layout.setText(fontHiscores, text);
        return layout.width;
    }

    float getTextHeightInfoScreen(String text) {
        layout.setText(fontHiscores, text);
        return layout.height;
    }

    float getTextWidth(String text) {
        layout.setText(font, text);
        return layout.width;
    }

    float getTextHeight(String text) {
        layout.setText(font, text);
        return layout.height;
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

    static void collectRareItem() { score += 100; }

    static void collectItem() {
        score++;
    }

    public void dispose() {
        font.dispose();
        fontHiscores.dispose();
        fontInfoScreen.dispose();
    }
}
