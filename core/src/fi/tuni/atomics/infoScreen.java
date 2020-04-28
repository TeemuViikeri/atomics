package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class infoScreen implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture background;
    private Atomics atomics;
    private GameUtil gameUtil = new GameUtil();
    private MenuButton exitButton;
    private MenuButton tikoButton;
    private MenuButton orasButton;
    private MenuButton tamkButton;
    private Stage stage;

    infoScreen(Atomics atomics) {
        this.atomics = atomics;
        batch = Atomics.batch;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage = new Stage();
        background = new Texture("tyhjätausta.png");

        float exitWidth = 500f * Gdx.graphics.getWidth() / 960;
        float exitHeight = 100f * Gdx.graphics.getHeight() / 640;
        exitButton = new MenuButton(exitWidth, exitHeight,
                Gdx.graphics.getWidth() / 2f - exitWidth / 2,
                exitHeight / 2,
                new Texture(Localization.getBundle().get("back")));
        float width = 300f * Gdx.graphics.getWidth() / 960;
        float height = 100f * Gdx.graphics.getHeight() / 640;
        tikoButton = new MenuButton(width, height,
                16,
                height * 2f,
                new Texture("tiko.png"));
        orasButton = new MenuButton(width, height,
                Gdx.graphics.getWidth() /  2f -width / 2,
                height * 2f,
                new Texture("oras.png"));
        tamkButton = new MenuButton(width, height,
                Gdx.graphics.getWidth() - width - 16,
                height * 2f,
                new Texture("tamk.png"));
        stage.addActor(tamkButton);
        stage.addActor(orasButton);
        stage.addActor(exitButton);
        stage.addActor(tikoButton);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        gameUtil.clearScreen();
        batch.setProjectionMatrix(camera.combined);
        stage.act();

        if (exitButton.isTouched()) {
            atomics.setScreen(new StartScreen(atomics));
            exitButton.setTouched(false);
        }
        if (tikoButton.isTouched()) {
            Gdx.net.openURI("https://www.tuni.fi/fi/tule-opiskelemaan/tietojenkasittelyn-tutkinto-ohjelma");
            tikoButton.setTouched(false);
        }
        if (orasButton.isTouched()) {
            Gdx.net.openURI("https://www.tuni.fi/fi/tutkimus/opi-ravinteista-oras");
            orasButton.setTouched(false);
        }
        if (tamkButton.isTouched()) {
            Gdx.net.openURI("https://www.tuni.fi/fi/tutustu-meihin/tamk");
            tamkButton.setTouched(false);
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
