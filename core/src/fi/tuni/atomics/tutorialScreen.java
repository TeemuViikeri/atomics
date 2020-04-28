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


    tutorialScreen(Atomics atomics) {
        this.atomics = atomics;
        batch = Atomics.batch;
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        gameUtil = new GameUtil();
        background = new Texture("tyhjätausta.png");

        float buttonWidth = 500f * Gdx.graphics.getWidth() / 960;
        float buttonHeight = 100f * Gdx.graphics.getHeight() / 640;
        nextButton = new MenuButton(buttonWidth, buttonHeight,
                Gdx.graphics.getWidth() / 2 - buttonWidth / 2,
                buttonHeight / 2,
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
