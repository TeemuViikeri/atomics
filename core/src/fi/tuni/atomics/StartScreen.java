package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import static fi.tuni.atomics.PlayScreen.ROOM_HEIGHT_PIXELS;
import static fi.tuni.atomics.PlayScreen.ROOM_WIDTH_PIXELS;
import static fi.tuni.atomics.PlayScreen.scale;

public class StartScreen implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture background;
    private GameUtil gameUtil;
    private Stage stage;
    private MenuButton startButton;
    private MenuButton settingsButton;
    private MenuButton exitButton;
    private Atomics atomics;

    StartScreen(Atomics atomics) {
        this.atomics = atomics;
        batch = Atomics.HUDBatch;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,
                ROOM_WIDTH_PIXELS * scale,
                ROOM_HEIGHT_PIXELS * scale);
        background = new Texture("menubackground.png");
        gameUtil = new GameUtil();
        stage = new Stage();

        if (!Memory.getFirstStartup()) {
            Memory.setVolume(0.1f);
            Memory.setLanguage("en");
            Memory.setFirstStartup();
        }

        Localization.setLocale(Memory.getLanguage());
        GameAudio.masterVolume = Memory.getVolume();

        float startWidth = 500f * Gdx.graphics.getWidth() / 960;
        float startHeight = 100f * Gdx.graphics.getHeight() / 640;
        startButton = new MenuButton(startWidth, startHeight,
                Gdx.graphics.getWidth() - startWidth,
                Gdx.graphics.getHeight() - startHeight * 2f,
                new Texture(Localization.getBundle().get("play")));
        settingsButton = new MenuButton(startWidth, startHeight,
                Gdx.graphics.getWidth() - startWidth,
                Gdx.graphics.getHeight() / 2f - startHeight / 2,
                new Texture(Localization.getBundle().get("settings")));
        exitButton = new MenuButton(startWidth, startHeight,
                Gdx.graphics.getWidth() - startWidth,
                startHeight,
                new Texture(Localization.getBundle().get("hiscore")));
        stage.addActor(startButton);
        stage.addActor(settingsButton);
        stage.addActor(exitButton);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        gameUtil.clearScreen();
        batch.setProjectionMatrix(camera.combined);
        stage.act();
        if (startButton.isTouched()) {
            GameAudio.playPlayGameSound();
            atomics.setScreen(new PlayScreen(atomics));
            startButton.setTouched(false);
        }
        if (settingsButton.isTouched()) {
            atomics.setScreen(new SettingsScreen(atomics));
            startButton.setTouched(false);
        }
        if (exitButton.isTouched()) {
            atomics.setScreen(new HighScoreScreen(atomics));
            startButton.setTouched(false);
        }
        batch.begin();
        batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
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
