package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import static fi.tuni.atomics.PlayScreen.ROOM_HEIGHT_PIXELS;
import static fi.tuni.atomics.PlayScreen.ROOM_WIDTH_PIXELS;
import static fi.tuni.atomics.PlayScreen.scale;

public class SettingsScreen implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture background;
    private GameUtil gameUtil;
    private Stage stage;
    private MenuButton soundsButton;
    private MenuButton languageButton;
    private MenuButton exitButton;
    private Atomics atomics;

    SettingsScreen(Atomics atomics) {
        this.atomics = atomics;
        batch = Atomics.batch;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,
                ROOM_WIDTH_PIXELS * scale,
                ROOM_HEIGHT_PIXELS * scale);
        background = new Texture("badlogic.jpg");
        gameUtil = new GameUtil();
        stage = new Stage();
        float startWidth = 500f * Gdx.graphics.getWidth() / 960;
        float startHeight = 100f * Gdx.graphics.getHeight() / 640;
        soundsButton = new MenuButton(startWidth, startHeight,
                Gdx.graphics.getWidth() / 2f - startWidth / 2,
                Gdx.graphics.getHeight() - startHeight * 2f,
                new Texture("START.jpg"));
        languageButton = new MenuButton(startWidth, startHeight,
                Gdx.graphics.getWidth() / 2f - startWidth / 2,
                Gdx.graphics.getHeight() / 2f - startHeight / 2,
                new Texture("START.jpg"));
        exitButton = new MenuButton(startWidth, startHeight,
                Gdx.graphics.getWidth() / 2f - startWidth / 2,
                startHeight,
                new Texture("START.jpg"));
        stage.addActor(soundsButton);
        stage.addActor(languageButton);
        stage.addActor(exitButton);
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(255,255,255, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        stage.act();
        stage.draw();
        if (soundsButton.isTouched()) {

            if (Memory.getVolume() == 0) {
                Memory.setVolume(0.1f);
            } else {
                Memory.setVolume(0);
            }

            soundsButton.setTouched(false);
        }
        if (languageButton.isTouched()) {

            if (Localization.getBundle().getLocale().toString().equals("fi")) {
                Memory.setLanguage("en");
                Localization.setLocale("en");
                System.out.println(Localization.getBundle().getLocale());
            } else {
                Memory.setLanguage("fi");
                Localization.setLocale("fi");
                System.out.println(Localization.getBundle().getLocale());
            }
            languageButton.setTouched(false);
        }
        if (exitButton.isTouched()) {
            atomics.setScreen(new StartScreen(atomics));
            exitButton.setTouched(false);
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
