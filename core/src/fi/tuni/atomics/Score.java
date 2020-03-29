package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Score {
    private static int score = 0;
    private FreeTypeFontGenerator fontGenerator;
    private BitmapFont font;
    private Label text;
    private Label.LabelStyle textStyle;
    private Stage stage;
    private Table table;

    Score() {
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Raleway-Black.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20 * Gdx.graphics.getWidth() / 640;
        font = fontGenerator.generateFont(parameter);
        textStyle = new Label.LabelStyle();
        textStyle.font = font;
        text = new Label("Score: " + score, textStyle);
        text.setBounds(Game.TILE_LENGTH_PIXELS * 2,
                Gdx.graphics.getHeight() - Game.TILE_LENGTH_PIXELS * 2, 0,0);
        stage = new Stage();
        table = new Table();
        stage.addActor(text);
    }

    Stage getStage() {
        text.setText("Score: " + score);
        return stage;
    }

    static void collectPhosphorus() {
        score += 5;
    }
}
