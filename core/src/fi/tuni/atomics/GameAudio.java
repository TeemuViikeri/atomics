package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

class GameAudio {
    Music backgroundMusic;

    void playBackgroundMusic() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("water_bgmusic.ogg"));
        backgroundMusic.setVolume(0.1f);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    void playGameOverSound() {
        Sound gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameover.ogg"));
        gameOverSound.play();
    }

    static void playBondingSound() {
        Sound bondingSound = Gdx.audio.newSound(Gdx.files.internal("phosphorusbonding.ogg"));
        bondingSound.play();
    }

    static void playShootingSound() {
        Sound shootingSound = Gdx.audio.newSound(Gdx.files.internal("shoot.ogg"));
        shootingSound.play();
    }

    static void playMicrobeSpawnSound() {
        Sound microbeSpawnSound = Gdx.audio.newSound(Gdx.files.internal("microbespawn.ogg"));
        microbeSpawnSound.play();
    }

    static void playPipeBrokenSound() {
        Sound playPipeBrokenSound = Gdx.audio.newSound(Gdx.files.internal("pipebroken.ogg"));
        playPipeBrokenSound.play();
    }

    static void playPipeFixedSound() {
        Sound playPipeFixedSound = Gdx.audio.newSound(Gdx.files.internal("pipefixed.ogg"));
        playPipeFixedSound.play();
    }

    static void playRareItemPickedSound() {
        Sound rareItemPicked = Gdx.audio.newSound(Gdx.files.internal("rareitempicked.ogg"));
        rareItemPicked.play();
    }

    void dispose() {
        this.dispose();
    }
}
