package fi.tuni.atomics;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Atomics extends Game {
	static SpriteBatch batch;
	static SpriteBatch HUDBatch;
    private StartScreen startScreen;
    private PlayScreen playScreen;

	@Override
	public void create () {
        batch = new SpriteBatch();
        HUDBatch = new SpriteBatch();
        playScreen = new PlayScreen(this);
        startScreen = new StartScreen(this);
        setScreen(startScreen);
    }

    @Override
	public void render() {
        super.render();
	}

	@Override
	public void pause() {
	}

    @Override
    public void resume() {
    }

	@Override
	public void dispose () {
		batch.dispose();
		HUDBatch.dispose();
	}
}
