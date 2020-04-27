package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import static fi.tuni.atomics.PlayScreen.ROOM_HEIGHT_PIXELS;
import static fi.tuni.atomics.PlayScreen.ROOM_WIDTH_PIXELS;
import static fi.tuni.atomics.PlayScreen.scale;

public class EndScreen implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture background;
    private GameUtil gameUtil;
    private Stage stage;
    private MenuButton restartButton;
    private MenuButton exitButton;
    private Atomics atomics;
    private Score score;

    EndScreen(Atomics atomics) {
        this.atomics = atomics;
        batch = Atomics.batch;
        camera = new OrthographicCamera();
        score = new Score();
        camera.setToOrtho(false,
                960,640);
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
            atomics.setScreen(new PlayScreen(atomics));
        }
        if (exitButton.isTouched()) {
            atomics.setScreen(new StartScreen(atomics));
            exitButton.setTouched(false);
        }
        batch.begin();
        System.out.println(score.getTextWidth("oot huono pelaa opettele pelaan"));
        System.out.println(score.getTextWidth("oot huono pelaa opettele pelaan") / 2f);
        System.out.println(480f - (score.getTextWidth("oot huono pelaa opettele pelaan") / 2f));
        score.draw(batch, "oot huono opettele pelaa", new Vector2(64f,600));
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
