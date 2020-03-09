package fi.tuni.atomics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Game extends ApplicationAdapter {
	// Non-initiated fields
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;

	// Initiated fields
	private float scale = 1/100f;
	//width is 2048pixels
	private float WORLD_WIDTH_PIXELS = 64*32;
	//height is 512pixels
	private float WORLD_HEIGHT_PIXELS = 16*32;
	
	@Override
	public void create () {
		// libGDX
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 5.12f, 5.12f);

		// TiledMap
		tiledMap = new TmxMapLoader().load("atomicsdemo.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, scale);
	}

	@Override
	public void render () {
		batch.setProjectionMatrix(camera.combined);
		clearScreen(101/255f, 214/255f, 186/255f);
		moveCamera(camera);

		tiledMapRenderer.render();
		tiledMapRenderer.setView(camera);

		batch.begin();
		batch.end();
	}

	private void moveCamera(OrthographicCamera camera) {
		// Change camera position accordingly!!
		camera.position.x = 10.24f;
		camera.position.y = 2.56f;

		camera.update();
	}

	private void clearScreen(float r, float g, float b) {
		Gdx.gl.glClearColor(r, g, b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
