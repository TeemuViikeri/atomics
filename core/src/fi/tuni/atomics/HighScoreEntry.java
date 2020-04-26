package fi.tuni.atomics;

/**
 * HighScoreEntry needs to have variables for all the attributes from the
 * HighScore server.In the demo case, we need variables String name and
 * int score.
 *
 * The variable names need to match the json keys.
 * Also each of the attribute needs to have a getter and setter for json parsing.
 */
public class HighScoreEntry {
    private String name;
    private int score;

    // We need this no argument constructor for the json parsing!
    public HighScoreEntry() {
    }

    public HighScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
