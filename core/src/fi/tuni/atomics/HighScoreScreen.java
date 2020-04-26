package fi.tuni.atomics;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;

import java.util.List;

public class HighScoreScreen implements HighScoreListener, Screen {
    GameUtil gameUtil = new GameUtil();

    HighScoreScreen(Atomics atomics) {
        HighScoreServer.readConfig("highscore.config");
        HighScoreServer.setVerbose(true);
        HighScoreEntry scoreEntry = new HighScoreEntry("Leevi", 1234);
        //HighScoreServer.sendNewHighScore(scoreEntry, this);
        //HighScoreServer.fetchHighScores(this);
    }

    @Override
    public void receiveHighScore(List<HighScoreEntry> highScores) {
        for (HighScoreEntry entry : highScores) {
            System.out.println(entry.getName() + " " + entry.getScore());
        }
    }

    @Override
    public void failedToRetrieveHighScores(Throwable t) {

    }

    @Override
    public void receiveSendReply(Net.HttpResponse httpResponse) {

    }

    @Override
    public void failedToSendHighScore(Throwable t) {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        gameUtil.clearScreen();

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
