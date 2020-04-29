package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

class GameAudio {
    static Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("water_bgmusic.ogg"));
    static Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menumusic.ogg"));
    static Sound clock = Gdx.audio.newSound(Gdx.files.internal("clock.ogg"));
    private static Sound gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameover.ogg"));
    private static Sound bondingSound = Gdx.audio.newSound(Gdx.files.internal("phosphorusbonding.ogg"));
    private static Sound shootingSound = Gdx.audio.newSound(Gdx.files.internal("shoot.ogg"));
    private static Sound microbeSpawnSound = Gdx.audio.newSound(Gdx.files.internal("microbedespawn.ogg"));
    private static Sound playPipeBrokenSound = Gdx.audio.newSound(Gdx.files.internal("pipebroken.ogg"));
    private static Sound playPipeFixedSound = Gdx.audio.newSound(Gdx.files.internal("pipefixed.ogg"));
    static Sound playFixSound = Gdx.audio.newSound(Gdx.files.internal("fix-sound.ogg"));
    private static Sound rareItemPickedSound = Gdx.audio.newSound(Gdx.files.internal("rareitempicked.ogg"));
    private static Sound collectablePhosphorusPickedSound = Gdx.audio.newSound(Gdx.files.internal("collectablephosphoruspicked.ogg"));
    private static Sound loseLifeSound = Gdx.audio.newSound(Gdx.files.internal("loselife.ogg"));
    private static Sound hitItemSound = Gdx.audio.newSound(Gdx.files.internal("hititem.ogg"));
    private static Sound vacuumSound = Gdx.audio.newSound(Gdx.files.internal("vacuum.ogg"));
    private static Sound pauseSound = Gdx.audio.newSound(Gdx.files.internal("pause.ogg"));
    private static Sound settingsSwitchSound = Gdx.audio.newSound(Gdx.files.internal("settings-switch.ogg"));
    private static Sound playGameSound = Gdx.audio.newSound(Gdx.files.internal("playgame.ogg"));
    private static Sound pauseToMenuSound = Gdx.audio.newSound(Gdx.files.internal("pausetomenu.ogg"));
    private static Sound backSound = Gdx.audio.newSound(Gdx.files.internal("backtostart.ogg"));
    static Sound tractorSound = Gdx.audio.newSound(Gdx.files.internal("traktor.ogg"));
    static float masterVolume;

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
        playFixSound.loop(masterVolume);
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
        tractorSound.loop(masterVolume / 2);
    }

    void dispose() {
        backgroundMusic.dispose();
        menuMusic.dispose();
        clock.dispose();
        gameOverSound.dispose();
        bondingSound.dispose();
        shootingSound.dispose();
        microbeSpawnSound.dispose();
        playPipeBrokenSound.dispose();
        playPipeFixedSound.dispose();
        playFixSound.dispose();
        rareItemPickedSound.dispose();
        collectablePhosphorusPickedSound.dispose();
        loseLifeSound.dispose();
        hitItemSound.dispose();
        vacuumSound.dispose();
        pauseSound.dispose();
        settingsSwitchSound.dispose();
        playGameSound.dispose();
        pauseToMenuSound.dispose();
        backSound.dispose();
        tractorSound.dispose();
    }
}
