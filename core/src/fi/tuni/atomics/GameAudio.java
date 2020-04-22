package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

class GameAudio {
    Music backgroundMusic;
    Sound gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameover.ogg"));
    static Sound bondingSound = Gdx.audio.newSound(Gdx.files.internal("phosphorusbonding.ogg"));
    static Sound shootingSound = Gdx.audio.newSound(Gdx.files.internal("shoot.ogg"));
    static Sound microbeSpawnSound = Gdx.audio.newSound(Gdx.files.internal("shoot.ogg"));
    static Sound playPipeBrokenSound = Gdx.audio.newSound(Gdx.files.internal("shoot.ogg"));
    static Sound playPipeFixedSound = Gdx.audio.newSound(Gdx.files.internal("shoot.ogg"));
    static Sound rareItemPicked = Gdx.audio.newSound(Gdx.files.internal("shoot.ogg"));

    void playBackgroundMusic() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("water_bgmusic.ogg"));
        backgroundMusic.setVolume(0.1f);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    void playGameOverSound() {
        gameOverSound.play();
    }

    static void playBondingSound() {
        bondingSound.play();
    }

    static void playShootingSound() {
        shootingSound.play();
    }

    static void playMicrobeSpawnSound() {
        microbeSpawnSound.play();
    }

    static void playPipeBrokenSound() {
        playPipeBrokenSound.play();
    }

    static void playPipeFixedSound() {
        playPipeFixedSound.play();
    }

    static void playRareItemPickedSound() {
        rareItemPicked.play();
    }

    void dispose() {
        this.dispose();
    }
}
