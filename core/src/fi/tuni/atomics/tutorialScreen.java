package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class tutorialScreen implements Screen {
    private Atomics atomics;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture background;
    private MenuButton nextButton;
    private GameUtil gameUtil;
    private Stage stage;
    private int tutorialScreenCounter = 1;


    tutorialScreen(Atomics atomics) {
        this.atomics = atomics;
        batch = Atomics.batch;
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        gameUtil = new GameUtil();
        background = new Texture(Localization.getBundle().get("tutorial1"));

        float buttonWidth = 400f * Gdx.graphics.getWidth() / 960;
        float buttonHeight = 80f * Gdx.graphics.getHeight() / 640;
        nextButton = new MenuButton(buttonWidth, buttonHeight,
                Gdx.graphics.getWidth() / 2f - buttonWidth / 2,
                16,
                new Texture(Localization.getBundle().get("next")));
        stage.addActor(nextButton);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        gameUtil.clearScreen();
        batch.setProjectionMatrix(camera.combined);

        if (nextButton.isTouched()) {
            if (tutorialScreenCounter == 1) {
                background = new Texture(Localization.getBundle().get("tutorial2"));
                tutorialScreenCounter++;
            } else if (tutorialScreenCounter == 2) {
                background = new Texture(Localization.getBundle().get("tutorial2"));
                tutorialScreenCounter++;
            } else if (tutorialScreenCounter == 3) {
                background = new Texture(Localization.getBundle().get("tutorial3"));
                tutorialScreenCounter++;
            } else if (tutorialScreenCounter == 4) {
                background = new Texture(Localization.getBundle().get("tutorial4"));
                tutorialScreenCounter++;
            } else if (tutorialScreenCounter == 5) {
                background = new Texture(Localization.getBundle().get("tutorial5"));
                tutorialScreenCounter++;
            } else {
                atomics.setScreen(new PlayScreen(atomics));
            }
            nextButton.setTouched(false);
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
