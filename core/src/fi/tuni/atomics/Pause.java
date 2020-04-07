package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static fi.tuni.atomics.PlayScreen.ROOM_HEIGHT_PIXELS;
import static fi.tuni.atomics.PlayScreen.ROOM_WIDTH_PIXELS;

public class Pause {
    private MenuButton pauseButton;
    private MenuButton resume;
    private MenuButton exit;
    private Texture pauseBackground;
    private Stage pauseStage;
    private Atomics atomics;

    Pause(Atomics atomics) {
        this.atomics = atomics;
        pauseBackground = new Texture("badlogic.jpg");
        pauseStage = new Stage(new FitViewport(ROOM_WIDTH_PIXELS,
                ROOM_HEIGHT_PIXELS), Atomics.HUDBatch);

        float pauseWidth = 50f * Gdx.graphics.getWidth() / 960;
        pauseButton = new MenuButton(pauseWidth, pauseWidth, 0,
                Gdx.graphics.getHeight() - (pauseWidth),
                new Texture("badlogic.jpg"));

        float resumeWidth = 200f * Gdx.graphics.getWidth() / 960;
        float resumeHeight = 50f * Gdx.graphics.getWidth() / 960;
        resume = new MenuButton(resumeWidth, resumeHeight,
                Gdx.graphics.getWidth() / 2f - resumeWidth / 2,
                Gdx.graphics.getHeight() * 2.5f/4f,
                new Texture("RESUME.jpg"));

        exit = new MenuButton(resumeWidth, resumeHeight,
                Gdx.graphics.getWidth() / 2f - resumeWidth / 2,
                Gdx.graphics.getHeight() * 1.5f/4f,
                new Texture("EXIT.jpg"));

        resume.setVisible(false);
        exit.setVisible(false);
        pauseStage.addActor(resume);
        pauseStage.addActor(exit);
    }

    void pauseScreen() {
        if (resume.isTouched()) {
            pauseButton.setTouched(false);
            resume.setTouched(false);
            resume.setVisible(false);
            exit.setVisible(false);
            PlayScreen.Game_paused = false;
            Gdx.input.setInputProcessor(Controls.getStage());
        }

        if (exit.isTouched()) {
            atomics.setScreen(new StartScreen(atomics));
            exit.setTouched(false);
            pauseButton.setTouched(false);
            resume.setTouched(false);
            resume.setVisible(false);
            exit.setVisible(false);
            PlayScreen.Game_paused = false;
        }

        pauseStage.act();
        Atomics.HUDBatch.begin();
        if (pauseButton.isTouched()) {
            Gdx.input.setInputProcessor(pauseStage);
            resume.setVisible(true);
            exit.setVisible(true);
            float width = 500f * Gdx.graphics.getWidth() / 960;
            Atomics.HUDBatch.draw(pauseBackground,
                    Gdx.graphics.getWidth() / 2f - width / 2,
                    Gdx.graphics.getHeight() / 2f - width / 2,
                    width,
                    width);
            PlayScreen.Game_paused = true;
        }
        Atomics.HUDBatch.end();
        pauseStage.draw();
    }

    MenuButton getPauseButton() {
        return pauseButton;
    }

}