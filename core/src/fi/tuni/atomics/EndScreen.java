package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

import static fi.tuni.atomics.PlayScreen.score;

public class EndScreen implements Screen, Input.TextInputListener, HighScoreListener {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture animationSheet = new Texture("endscreen.png");
    private Animation<TextureRegion> animation;
    private float stateTime;
    private Stage stage;
    private MenuButton restartButton;
    private MenuButton exitButton;
    private Atomics atomics;
    private String name;
    private boolean isTractorSoundPlaying;


    EndScreen(Atomics atomics) {
        this.atomics = atomics;
        batch = Atomics.batch;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,
                Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        GameUtil gameUtil = new GameUtil();
        stage = new Stage();
        float buttonWidth = 300f * Gdx.graphics.getWidth() / 960;
        float buttonHeight = 100f * Gdx.graphics.getHeight() / 640;
        restartButton = new MenuButton(buttonWidth, buttonHeight,
                32f,
                Gdx.graphics.getHeight() / 2f - buttonHeight / 2,
                new Texture(Localization.getBundle().get("endscreenplay")));
        exitButton = new MenuButton(buttonWidth, buttonHeight,
                Gdx.graphics.getWidth() - buttonWidth - 32f,
                Gdx.graphics.getHeight() / 2f - buttonHeight / 2,
                new Texture(Localization.getBundle().get("menu")));
        stage.addActor(restartButton);
        stage.addActor(exitButton);
        Gdx.input.setInputProcessor(stage);
        HighScoreServer.readConfig("highscore.config");
        HighScoreServer.setVerbose(true);

        stateTime = 1f;
        TextureRegion[] frames;
        int sheetRows = 1;
        int sheetCols = 13;
        TextureRegion[][] temp =  TextureRegion.split(
                animationSheet,
                animationSheet.getWidth() / sheetCols,
                animationSheet.getHeight() / sheetRows);
        frames = gameUtil.to1d(temp, sheetRows, sheetCols);
        animation = new Animation<>(0.2f, frames);
        isTractorSoundPlaying = false;

        if (PlayScreen.score.getScore() > HighScoreServer.top5Score) {
            Gdx.input.getTextInput(this,
                    Localization.getBundle().get("newhiscore"), "", "");
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

        if (!isTractorSoundPlaying) {
            if (!SettingsScreen.isMuted) {
                isTractorSoundPlaying = true;
                GameAudio.backgroundMusic.stop();
                GameAudio.playTractorSound();
            }
        }

        if (restartButton.isTouched()) {
            if (!SettingsScreen.isMuted) {
                GameAudio.playPlayGameSound();
                GameAudio.tractorSound.stop();
            }

            atomics.setScreen(new PlayScreen(atomics));
            restartButton.setTouched(false);
        }

        if (exitButton.isTouched()) {
            if (!SettingsScreen.isMuted)
                GameAudio.tractorSound.stop();

            atomics.setScreen(new StartScreen(atomics));
            exitButton.setTouched(false);
        }

        batch.begin();
        batch.draw(animation.getKeyFrame(setStateTime(), true),
                0,
                0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );
        PlayScreen.score.draw(batch, "Ansaitsit " + (int) PlayScreen.score.getScore() +
                        " pistettä! Sait kerättyä talteen " + Score.collectedPhosphorusCounter +
                        " fosforia ja " + Score.collectedNitrogenCounter +
                        " typpeä. \nRavinteet menevät eteenpäin kiertoon ja hyötykäyttöön.",
                new Vector2(32,
                        Gdx.graphics.getHeight() -
                                score.getTextHeight("psdglk,spgfsålgksdpågok")));
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
        batch.dispose();
        stage.dispose();
        animationSheet.dispose();
        atomics.dispose();
    }

    @Override
    public void input(String text) {
        name = text;
        if (name.length() <= 10 && name.length() >= 2) {
            HighScoreEntry scoreEntry = new HighScoreEntry(name, (int) PlayScreen.score.getScore());
            HighScoreServer.sendNewHighScore(scoreEntry, this);
        } else if (name.length() < 2) {
            Gdx.input.getTextInput(this, Localization.getBundle().get("newhiscore"),
                    "", Localization.getBundle().get("shortname"));
        } else {
            Gdx.input.getTextInput(this, Localization.getBundle().get("newhiscore"),
                    "", Localization.getBundle().get("longname"));
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

    private float setStateTime() {
        return stateTime+= Gdx.graphics.getDeltaTime();
    }
}
