package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

class GameAudio {
    static Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("water_bgmusic.ogg"));
    static Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menumusic.ogg"));
    static Sound clock = Gdx.audio.newSound(Gdx.files.internal("clock.ogg"));
    static Sound gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameover.ogg"));
    static Sound bondingSound = Gdx.audio.newSound(Gdx.files.internal("phosphorusbonding.ogg"));
    static Sound shootingSound = Gdx.audio.newSound(Gdx.files.internal("shoot.ogg"));
    static Sound microbeSpawnSound = Gdx.audio.newSound(Gdx.files.internal("microbedespawn.ogg"));
    static Sound playPipeBrokenSound = Gdx.audio.newSound(Gdx.files.internal("pipebroken.ogg"));
    static Sound playPipeFixedSound = Gdx.audio.newSound(Gdx.files.internal("pipefixed.ogg"));
    static Sound playFixSound = Gdx.audio.newSound(Gdx.files.internal("fix-sound.ogg"));
    static Sound rareItemPickedSound = Gdx.audio.newSound(Gdx.files.internal("rareitempicked.ogg"));
    static Sound collectablePhosphorusPickedSound = Gdx.audio.newSound(Gdx.files.internal("collectablephosphoruspicked.ogg"));
    static Sound loseLifeSound = Gdx.audio.newSound(Gdx.files.internal("loselife.ogg"));
    static Sound hitItemSound = Gdx.audio.newSound(Gdx.files.internal("hititem.ogg"));
    static Sound vacuumSound = Gdx.audio.newSound(Gdx.files.internal("vacuum.ogg"));
    static Sound pauseSound = Gdx.audio.newSound(Gdx.files.internal("pause.ogg"));
    static Sound settingsSwitchSound = Gdx.audio.newSound(Gdx.files.internal("settings-switch.ogg"));
    static Sound playGameSound = Gdx.audio.newSound(Gdx.files.internal("playgame.ogg"));
    static Sound pauseToMenuSound = Gdx.audio.newSound(Gdx.files.internal("pausetomenu.ogg"));
    static Sound backSound = Gdx.audio.newSound(Gdx.files.internal("backtostart.ogg"));
    static Sound tractorSound = Gdx.audio.newSound(Gdx.files.internal("traktor.ogg"));
    static float masterVolume = 0.1f;

    static void playBackgroundMusic() {
        backgroundMusic.setVolume(masterVolume);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    static void playMenuMusic() {
        menuMusic.setVolume(masterVolume);
        menuMusic.setLooping(true);
        menuMusic.play();
    }

    static void playClock() {
        clock.loop(masterVolume);
    }

    static void playGameOverSound() {
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

    static void playFixSound() {
        playFixSound.play(masterVolume);
    }

    static void playRareItemPickedSound() {
        rareItemPickedSound.play(masterVolume);
    }

    static void playCollectablePhosphorusPickedSound() {
        collectablePhosphorusPickedSound.play(masterVolume / 3);
    }

    static void playLoseLifeSound() {
        loseLifeSound.play(masterVolume);
    }

    static void playHitItemSound() {
        hitItemSound.play(masterVolume);
    }

    static void playVacuumSound(float volumeMultiplier) {
        vacuumSound.play(masterVolume * volumeMultiplier);
    }

    static void playPauseSound() {
        pauseSound.play(masterVolume);
    }

    static void playSettingsSwitchSound(float volume) {
        settingsSwitchSound.play(volume);
    }

    static void playPlayGameSound() {
        playGameSound.play(masterVolume);
    }

    static void playPauseToMenuSound() {
        pauseToMenuSound.play(masterVolume);
    }

    static void playBackSound() {
        backSound.play(masterVolume);
    }

    static void playTractorSound() {
        tractorSound.loop(0.05f);
    }

    void dispose() {
        this.dispose();
    }
}
