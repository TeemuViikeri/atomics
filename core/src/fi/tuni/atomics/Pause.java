package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static fi.tuni.atomics.PlayScreen.TILE_LENGTH_PIXELS;

class Pause {
    private MenuButton pauseButton;
    private MenuButton resume;
    private MenuButton exit;
    private Texture pauseBackground;
    private Stage pauseStage;
    private Atomics atomics;
    private float pauseScreenWidth = 500f * Gdx.graphics.getWidth() / 960f;
    private float pauseScreenHeight = 500f * Gdx.graphics.getHeight() / 640f;
    private boolean pauseSoundPlayed;

    Pause(Atomics atomics) {
        this.atomics = atomics;
        pauseBackground = new Texture("pausemenu.png");
        pauseStage = new Stage(new FitViewport(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()), Atomics.HUDBatch);

        float pauseWidth = 50f * Gdx.graphics.getWidth() / 960;
        float pauseHeight = 50f * Gdx.graphics.getHeight() / 640;
        pauseButton = new MenuButton(
                pauseWidth, pauseHeight,
                Gdx.graphics.getWidth() - (pauseWidth + TILE_LENGTH_PIXELS),
                Gdx.graphics.getHeight() - (pauseHeight + TILE_LENGTH_PIXELS - 6f),
                new Texture("pausebutton.png"));

        float buttonWidth = 400f * Gdx.graphics.getWidth() / 960;
        float buttonHeight = 100f * Gdx.graphics.getHeight() / 640;
        resume = new MenuButton(buttonWidth, buttonHeight,
                Gdx.graphics.getWidth() / 2f - buttonWidth / 2,
                Gdx.graphics.getHeight() / 2f - pauseScreenHeight / 2 + pauseScreenHeight - buttonHeight * 2,
                new Texture(Localization.getBundle().get("resume")));

        exit = new MenuButton(buttonWidth, buttonHeight,
                Gdx.graphics.getWidth() / 2f - buttonWidth / 2,
                Gdx.graphics.getHeight() / 2f - pauseScreenHeight / 2 + buttonHeight,
                new Texture(Localization.getBundle().get("menu")));

        resume.setVisible(false);
        exit.setVisible(false);
        pauseStage.addActor(resume);
        pauseStage.addActor(exit);
        pauseSoundPlayed = false;
    }

    void pauseScreen() {
        if (resume.isTouched()) {
            pauseButton.setTouched(false);
            resume.setTouched(false);
            resume.setVisible(false);
            exit.setVisible(false);
            pauseSoundPlayed = false;
            PlayScreen.Game_paused = false;

            if (!SettingsScreen.isMuted)
                GameAudio.playBackgroundMusic();
        }

        if (exit.isTouched()) {
            atomics.setScreen(new StartScreen(atomics));
            exit.setTouched(false);
            pauseButton.setTouched(false);
            resume.setTouched(false);
            resume.setVisible(false);
            exit.setVisible(false);
            pauseSoundPlayed = false;
            PlayScreen.Game_paused = false;

            if (!SettingsScreen.isMuted)
                GameAudio.playPauseToMenuSound();
        }

        pauseStage.act();
        Atomics.HUDBatch.begin();
        if (pauseButton.isTouched()) {
            if (!pauseSoundPlayed) {
                pauseSoundPlayed = true;

                if (!SettingsScreen.isMuted)
                    GameAudio.playPauseSound();
            }

            Gdx.input.setInputProcessor(pauseStage);
            resume.setVisible(true);
            exit.setVisible(true);
            Atomics.HUDBatch.draw(pauseBackground,
                    Gdx.graphics.getWidth() / 2f - pauseScreenWidth / 2,
                    Gdx.graphics.getHeight() / 2f - pauseScreenHeight / 2,
                    pauseScreenWidth,
                    pauseScreenHeight);
            PlayScreen.Game_paused = true;

            if (!SettingsScreen.isMuted)
                GameAudio.backgroundMusic.stop();
        }
        Atomics.HUDBatch.end();
        pauseStage.draw();
    }

    MenuButton getPauseButton() {
        return pauseButton;
    }

    public void dispose() {
        atomics.dispose();
        pauseBackground.dispose();
        pauseStage.dispose();
    }
}
