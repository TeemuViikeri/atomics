package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.util.List;

import static fi.tuni.atomics.PlayScreen.ROOM_HEIGHT_PIXELS;
import static fi.tuni.atomics.PlayScreen.ROOM_WIDTH_PIXELS;
import static fi.tuni.atomics.PlayScreen.scale;

public class EndScreen implements Screen, Input.TextInputListener, HighScoreListener {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture background;
    private GameUtil gameUtil;
    private Stage stage;
    private MenuButton restartButton;
    private MenuButton exitButton;
    private Atomics atomics;
    private String name;


    EndScreen(Atomics atomics) {
        this.atomics = atomics;
        batch = Atomics.batch;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,
                Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        background = new Texture("badlogic.jpg");
        gameUtil = new GameUtil();
        stage = new Stage();
        float startWidth = 200f * Gdx.graphics.getWidth() / 960;
        float startHeight = 80f * Gdx.graphics.getHeight() / 640;
        restartButton = new MenuButton(startWidth, startHeight,
                Gdx.graphics.getWidth() / 2f - startWidth,
                startHeight / 2f,
                new Texture("START.jpg"));
        exitButton = new MenuButton(startWidth, startHeight,
                Gdx.graphics.getWidth() / 2f,
                startHeight / 2f,
                new Texture(Localization.getBundle().get("menu")));
        stage.addActor(restartButton);
        stage.addActor(exitButton);
        Gdx.input.setInputProcessor(stage);
        GameAudio.backgroundMusic.stop();
        HighScoreServer.readConfig("highscore.config");
        HighScoreServer.setVerbose(true);

        if (PlayScreen.score.getScore() > HighScoreServer.top5Score) {
            Gdx.input.getTextInput(this, Localization.getBundle().get("newhiscore"), "", "");
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(255,255,0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        stage.act();
        stage.draw();
        if (restartButton.isTouched()) {
            GameAudio.playPlayGameSound();
            atomics.setScreen(new PlayScreen(atomics));
        }
        if (exitButton.isTouched()) {
            atomics.setScreen(new StartScreen(atomics));
            exitButton.setTouched(false);
        }
        batch.begin();
        PlayScreen.score.draw(batch, "oot huono opettele pelaa", new Vector2(64f,600));
        batch.end();
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

    @Override
    public void input(String text) {
        name = text;
        if (name.length() <= 10 && name.length() >= 2) {
            HighScoreEntry scoreEntry = new HighScoreEntry(name, (int) PlayScreen.score.getScore());
            HighScoreServer.sendNewHighScore(scoreEntry, this);
        } else if (name.length() < 2) {
            Gdx.input.getTextInput(this, Localization.getBundle().get("newhiscore"), "", Localization.getBundle().get("shortname"));
        } else {
            Gdx.input.getTextInput(this, Localization.getBundle().get("newhiscore"), "", Localization.getBundle().get("longname"));
        }
    }

    @Override
    public void canceled() {
        name = "default";
        HighScoreEntry scoreEntry = new HighScoreEntry(name, (int) PlayScreen.score.getScore());
        HighScoreServer.sendNewHighScore(scoreEntry, this);
    }

    @Override
    public void receiveHighScore(List<HighScoreEntry> highScores) {

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
}
