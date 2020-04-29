package fi.tuni.atomics;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

import static fi.tuni.atomics.PlayScreen.ROOM_HEIGHT_PIXELS;
import static fi.tuni.atomics.PlayScreen.ROOM_WIDTH_PIXELS;
import static fi.tuni.atomics.PlayScreen.scale;

public class HighScoreScreen implements HighScoreListener, Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture background;
    private Atomics atomics;
    GameUtil gameUtil = new GameUtil();
    Score score = new Score();
    private List<HighScoreEntry> hiscores;
    private boolean fetched = false;
    private MenuButton exitButton;
    private Stage stage;


    HighScoreScreen(Atomics atomics) {
        this.atomics = atomics;
        batch = Atomics.batch;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        stage = new Stage();
        background = new Texture("tyhjätausta.png");
        HighScoreServer.readConfig("highscore.config");
        HighScoreServer.setVerbose(true);
        HighScoreServer.fetchHighScores(this);

        float buttonWidth = 500f * Gdx.graphics.getWidth() / 960;
        float buttonHeight = 100f * Gdx.graphics.getHeight() / 640;
        exitButton = new MenuButton(buttonWidth, buttonHeight,
                Gdx.graphics.getWidth() / 2 - buttonWidth / 2,
                buttonHeight / 2,
                new Texture(Localization.getBundle().get("back")));
        stage.addActor(exitButton);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void receiveHighScore(List<HighScoreEntry> highScores) {
        hiscores = highScores;
        fetched = true;
    }

    @Override
    public void failedToRetrieveHighScores(Throwable t) {

    }

    @Override
    public void receiveSendReply(Net.HttpResponse httpResponse) {

    }

    @Override
    public void failedToSendHighScore(Throwable t) {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        gameUtil.clearScreen();
        batch.setProjectionMatrix(camera.combined);

        if (exitButton.isTouched()) {
            if (!SettingsScreen.isMuted)
                GameAudio.playBackSound();

            atomics.setScreen(new StartScreen(atomics));
            exitButton.setTouched(false);
        }

        batch.begin();
        batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);

        float y = Gdx.graphics.getHeight() - score.getTextHeight("asdfasdfasfasf") * 2;
        if (fetched) {
            for (HighScoreEntry a : hiscores) {
                score.drawHiscores(batch, a.getName() + " " + a.getScore(),
                        new Vector2(Gdx.graphics.getWidth() / 2f -
                                score.getTextWidthHiscores(a.getName() + " " + a.getScore()) / 2, y));
                y-=score.getTextHeightHiscores("asdfasdfsdfasf") * 2;
            }
        }

        batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
