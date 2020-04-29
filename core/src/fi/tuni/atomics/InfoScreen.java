package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

class InfoScreen implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture background;
    private Atomics atomics;
    private GameUtil gameUtil = new GameUtil();
    private MenuButton exitButton;
    private MenuButton tikoButton;
    private MenuButton orasButton;
    private MenuButton tamkButton;
    private MenuButton tutorialButton;
    private Stage stage;
    static boolean tutorialFromInfoScreen;
    private Score score;

    InfoScreen(Atomics atomics) {
        this.atomics = atomics;
        batch = Atomics.batch;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage = new Stage();
        score = new Score();
        background = new Texture("tyhjätausta.png");

        float exitWidth = 500f * Gdx.graphics.getWidth() / 960;
        float exitHeight = 100f * Gdx.graphics.getHeight() / 640;
        float buttonWidth = 300f * Gdx.graphics.getWidth() / 960;
        float buttonHeight = 130f * Gdx.graphics.getHeight() / 640;
        exitButton = new MenuButton(exitWidth, exitHeight,
                Gdx.graphics.getWidth() / 2f - exitWidth / 2,
                exitHeight / 2,
                new Texture(Localization.getBundle().get("back")));
        tutorialButton = new MenuButton(exitWidth, exitHeight,
                Gdx.graphics.getWidth() / 2f - exitWidth / 2,
                exitHeight * 2f,
                new Texture(Localization.getBundle().get("tutorialbutton")));
        tikoButton = new MenuButton(buttonWidth, buttonHeight,
                16,
                exitHeight * 3.5f,
                new Texture("tiko.png"));
        orasButton = new MenuButton(buttonWidth, buttonHeight,
                Gdx.graphics.getWidth() /  2f -buttonWidth / 2,
                exitHeight * 3.5f,
                new Texture("oras.png"));
        tamkButton = new MenuButton(buttonWidth, buttonHeight,
                Gdx.graphics.getWidth() - buttonWidth - 16,
                exitHeight * 3.5f,
                new Texture("tamk.png"));
        stage.addActor(tamkButton);
        stage.addActor(orasButton);
        stage.addActor(exitButton);
        stage.addActor(tikoButton);
        stage.addActor(tutorialButton);
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
            if (!SettingsScreen.isMuted)
                GameAudio.playBackSound();

            atomics.setScreen(new StartScreen(atomics));
            exitButton.setTouched(false);
        }
        if (tikoButton.isTouched()) {
            Gdx.net.openURI("https://www.tuni.fi/fi/tule-opiskelemaan/" +
                    "tietojenkasittelyn-tutkinto-ohjelma");
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
        if (tutorialButton.isTouched()) {
            tutorialFromInfoScreen = true;
            atomics.setScreen(new TutorialScreen(atomics));
            tutorialButton.setTouched(false);
        }

        batch.begin();
        batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        score.drawInfoScreen(batch, Localization.getBundle().get("backgroundmusic"),
                new Vector2(32, Gdx.graphics.getHeight() -
                                score.getTextHeightInfoScreen("ABCDEFG") /  2));
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
        background.dispose();
        atomics.dispose();
    }
}
