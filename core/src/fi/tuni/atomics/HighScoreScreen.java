package fi.tuni.atomics;

import com.badlogic.gdx.Net;

import java.util.List;

public class HighScoreScreen implements HighScoreListener {
    @Override
    public void receiveHighScore(List<HighScoreEntry> highScores) {

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
}
