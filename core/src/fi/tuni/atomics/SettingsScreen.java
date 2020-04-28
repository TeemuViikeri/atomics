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
    private Texture soundsButtonOnTexture = new Texture("ääni_on.png");
    private Texture soundsButtonOffTexture = new Texture("ääni_off.png");

    SettingsScreen(Atomics atomics) {
        this.atomics = atomics;
        batch = Atomics.batch;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,
                ROOM_WIDTH_PIXELS * scale,
                ROOM_HEIGHT_PIXELS * scale);
        background = new Texture("menubackground.png");
        gameUtil = new GameUtil();
        stage = new Stage();

        float buttonWidth = 500f * Gdx.graphics.getWidth() / 960;
        float buttonHeight = 100f * Gdx.graphics.getHeight() / 640;
        if (Memory.getVolume() == 0.0f) {
            soundsButton = new MenuButton(buttonWidth, buttonHeight,
                    Gdx.graphics.getWidth() - buttonWidth,
                    Gdx.graphics.getHeight() - buttonHeight * 2f,
                    soundsButtonOffTexture);
        } else {
            soundsButton = new MenuButton(buttonWidth, buttonHeight,
                    Gdx.graphics.getWidth() - buttonWidth,
                    Gdx.graphics.getHeight() - buttonHeight * 2f,
                    soundsButtonOnTexture);
        }

        languageButton = new MenuButton(buttonWidth, buttonHeight,
                Gdx.graphics.getWidth() - buttonWidth,
                Gdx.graphics.getHeight() / 2f - buttonHeight / 2,
                new Texture(Localization.getBundle().get("languagebutton")));

        exitButton = new MenuButton(buttonWidth, buttonHeight,
                Gdx.graphics.getWidth() - buttonWidth,
                buttonHeight,
                new Texture(Localization.getBundle().get("back")));
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

        if (soundsButton.isTouched()) {

            if (Memory.getVolume() == 0) {
                soundsButton.setTexture(soundsButtonOnTexture);
                GameAudio.playSettingsSwitchSound();
                Memory.setVolume(0.1f);
            } else {
                soundsButton.setTexture(soundsButtonOffTexture);
                Memory.setVolume(0);
            }

            soundsButton.setTouched(false);
        }

        if (languageButton.isTouched()) {
            GameAudio.playSettingsSwitchSound();

            if (Localization.getBundle().getLocale().toString().equals("fi")) {
                Memory.setLanguage("en");
                Localization.setLocale("en");
                languageButton.setTexture(new Texture(Localization.getBundle().get("languagebutton")));
                exitButton.setTexture(new Texture(Localization.getBundle().get("back")));
            } else {
                Memory.setLanguage("fi");
                Localization.setLocale("fi");
                languageButton.setTexture(new Texture(Localization.getBundle().get("languagebutton")));
                exitButton.setTexture(new Texture(Localization.getBundle().get("back")));
            }

            languageButton.setTouched(false);
        }

        if (exitButton.isTouched()) {
            GameAudio.playBackSound();
            atomics.setScreen(new StartScreen(atomics));
            exitButton.setTouched(false);
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
