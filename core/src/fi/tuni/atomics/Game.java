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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class Game extends ApplicationAdapter {
	// Non-initiated fields
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
    private World world;
    private Box2DDebugRenderer debugRenderer;

	// Initiated fields
	private float scale = 1/100f;
	private float TILE_LENGTH_PIXELS = 32;
	private float TILES_AMOUNT_WIDTH = 64;
	private float TILES_AMOUNT_HEIGHT = 16;
	private float WORLD_WIDTH_PIXELS =
			TILES_AMOUNT_WIDTH * TILE_LENGTH_PIXELS; // = 2048 pixels
	private float WORLD_HEIGHT_PIXELS =
			TILES_AMOUNT_HEIGHT * TILE_LENGTH_PIXELS; // = 512 pixels
    private double accumulator = 0;
    private float TIME_STEP = 1 / 60f;
	
	@Override
	public void create () {
		// libGDX
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,
                WORLD_HEIGHT_PIXELS * scale,
                WORLD_HEIGHT_PIXELS * scale);

		// TiledMap
		tiledMap = new TmxMapLoader().load("atomicsdemo.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, scale);

		// Box2D
        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void render () {
		batch.setProjectionMatrix(camera.combined);
		clearScreen(101/255f, 214/255f, 186/255f); // color: teal
		moveCamera(camera);

		tiledMapRenderer.render();
		tiledMapRenderer.setView(camera);
        debugRenderer.render(world, camera.combined);
        doPhysicsStep(Gdx.graphics.getDeltaTime());

		batch.begin();
		batch.end();
	}

	// fixed timestep
    private void doPhysicsStep(float deltaTime) {

        float frameTime = deltaTime;

        if(deltaTime > 1 / 4f) {
            frameTime = 1 / 4f;
        }

        accumulator += frameTime;

        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, 8, 3);
            accumulator -= TIME_STEP;
        }
    }

	private void moveCamera(OrthographicCamera camera) {
		// Change camera position accordingly!! Camera is centered at the moment.
		camera.position.x = WORLD_WIDTH_PIXELS / 2 * scale;
		camera.position.y = WORLD_HEIGHT_PIXELS / 2 * scale;

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
