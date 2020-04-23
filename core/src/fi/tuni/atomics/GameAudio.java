package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

class GameAudio {
    static Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("water_bgmusic.ogg"));
    Sound gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameover.ogg"));
    static float masterVolume = 0.1f;
    static Sound bondingSound = Gdx.audio.newSound(Gdx.files.internal("phosphorusbonding.ogg"));
    static Sound shootingSound = Gdx.audio.newSound(Gdx.files.internal("shoot.ogg"));
    static Sound microbeSpawnSound = Gdx.audio.newSound(Gdx.files.internal("shoot.ogg"));
    static Sound playPipeBrokenSound = Gdx.audio.newSound(Gdx.files.internal("shoot.ogg"));
    static Sound playPipeFixedSound = Gdx.audio.newSound(Gdx.files.internal("shoot.ogg"));
    static Sound rareItemPicked = Gdx.audio.newSound(Gdx.files.internal("shoot.ogg"));

    GameAudio() {

    }

    static void playBackgroundMusic() {
        backgroundMusic.setVolume(masterVolume);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    void playGameOverSound() {
        gameOverSound.play(masterVolume);
    }

    static void playBondingSound() {
        bondingSound.play(masterVolume);
    }

    static void playShootingSound() {
        shootingSound.play(masterVolume);
    }

    static void playMicrobeSpawnSound() {
        microbeSpawnSound.play(masterVolume);
    }

    static void playPipeBrokenSound() {
        playPipeBrokenSound.play(masterVolume);
    }

    static void playPipeFixedSound() {
        playPipeFixedSound.play(masterVolume);
    }

    static void playRareItemPickedSound() {
        rareItemPicked.play(masterVolume);
    }

    void dispose() {
        this.dispose();
    }
}
