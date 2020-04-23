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
    private GameAudio gameAudio;

    StartScreen(Atomics atomics) {
        this.atomics = atomics;
        batch = Atomics.batch;
        camera = new OrthographicCamera();
        gameAudio = new GameAudio();
        camera.setToOrtho(false,
                ROOM_WIDTH_PIXELS * scale,
                ROOM_HEIGHT_PIXELS * scale);
        background = new Texture("badlogic.jpg");
        gameUtil = new GameUtil();
        stage = new Stage();
        float startWidth = 500f * Gdx.graphics.getWidth() / 960;
        float startHeight = 100f * Gdx.graphics.getHeight() / 640;
        startButton = new MenuButton(startWidth, startHeight,
                Gdx.graphics.getWidth() / 2f - startWidth / 2,
                Gdx.graphics.getHeight() - startHeight * 2f,
                new Texture("START.jpg"));
        settingsButton = new MenuButton(startWidth, startHeight,
                Gdx.graphics.getWidth() / 2f - startWidth / 2,
                Gdx.graphics.getHeight() / 2f - startHeight / 2,
                new Texture("START.jpg"));
        exitButton = new MenuButton(startWidth, startHeight,
                Gdx.graphics.getWidth() / 2f - startWidth / 2,
                startHeight,
                new Texture("START.jpg"));
        stage.addActor(startButton);
        stage.addActor(settingsButton);
        stage.addActor(exitButton);
        Gdx.input.setInputProcessor(stage);
        System.out.println(Memory.getFirstStartup());
        if (!Memory.getFirstStartup()) {
            Memory.setVolume(0.1f);
            Memory.setLanguage("en");
            Memory.setFirstStartup();
        }

        Localization.setLocale(Memory.getLanguage());
        GameAudio.masterVolume = Memory.getVolume();
        System.out.println(GameAudio.masterVolume);
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
        stage.draw();
        if (startButton.isTouched()) {
            atomics.setScreen(new PlayScreen(atomics));
            startButton.setTouched(false);
        }
        if (settingsButton.isTouched()) {
            atomics.setScreen(new SettingsScreen(atomics));
            startButton.setTouched(false);
        }
        if (exitButton.isTouched()) {
            java.lang.System.exit(0);
            startButton.setTouched(false);
        }
        batch.begin();
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
}
