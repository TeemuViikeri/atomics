package fi.tuni.atomics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class Game extends ApplicationAdapter {
	// Non-initiated fields
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Player player;
    private ArrayList<Bullet> bullets;
    private int room;

	// Initiated fields
	private float scale = 1/100f;
    private double accumulator = 0;
    private float TIME_STEP = 1 / 60f;
	private float TILE_LENGTH_PIXELS = 32;
	private float TILES_AMOUNT_WIDTH = 64;
	private float TILES_AMOUNT_HEIGHT = 16;
	private float WORLD_WIDTH_PIXELS =
			TILES_AMOUNT_WIDTH * TILE_LENGTH_PIXELS; // = 2048 pixels
	private float WORLD_HEIGHT_PIXELS =
			TILES_AMOUNT_HEIGHT * TILE_LENGTH_PIXELS; // = 512 pixels
    private float ROOM_TILES_AMOUNT = 16;
    private float ROOM_LENGTH_PIXELS = ROOM_TILES_AMOUNT * TILE_LENGTH_PIXELS ;
	private float PIPE_HORIZONTAL_TILES_AMOUNT = 8;
	private float PIPE_VERTICAL_TILES_AMOUNT = 2;
	private float PIPE_HORIZONTAL_PIXELS = PIPE_HORIZONTAL_TILES_AMOUNT * TILE_LENGTH_PIXELS;
	private float PIPE_VERTICAL_PIXELS = PIPE_VERTICAL_TILES_AMOUNT * TILE_LENGTH_PIXELS;
	float FIRST_SCREEN_RIGHT_SIDE = 17 * TILE_LENGTH_PIXELS * scale;
	float SECOND_SCREEN_LEFT_SIDE = 24 * TILE_LENGTH_PIXELS * scale;
	float SECOND_SCREEN_RIGHT_SIDE = 39 * TILE_LENGTH_PIXELS * scale;
	float THIRD_SCREEN_LEFT_SIDE = 48 * TILE_LENGTH_PIXELS * scale;
	float FIRST_SCREEN_SPAWN_POINT = 15 * TILE_LENGTH_PIXELS * scale;
	float SECOND_SCREEN_LEFT_SPAWN_POINT = 26 * TILE_LENGTH_PIXELS * scale;
	float SECOND_SCREEN_RIGHT_SPAWN_POINT = 37 * TILE_LENGTH_PIXELS * scale;
	float THIRD_SCREEN_SPAWN_POINT = 50 * TILE_LENGTH_PIXELS * scale;

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

		// Game objects
		player = new Player(
				WORLD_WIDTH_PIXELS / 2 * scale,
				WORLD_HEIGHT_PIXELS / 2 * scale);
		room = 2;
		bullets = new ArrayList<>();
	}

	@Override
	public void render () {
		batch.setProjectionMatrix(camera.combined);
		clearScreen(101/255f, 214/255f, 186/255f); // color: teal
		moveCamera(camera);

		tiledMapRenderer.render();
		tiledMapRenderer.setView(camera);
        debugRenderer.render(world, camera.combined);

		submarineMovement();
		checkIfChangeRoom(player.getSprite().getX());

		batch.begin();

		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).getSprite().setX(bullets.get(i).getSprite().getX()
					+ bullets.get(i).getSpeed()
                    * (float) Math.cos( Math.toRadians(bullets.get(i).getDegrees())));
			bullets.get(i).getSprite().setY(bullets.get(i).getSprite().getY()
                    + bullets.get(i).getSpeed()
                    * (float) Math.sin( Math.toRadians(bullets.get(i).getDegrees())));
			bullets.get(i).draw(batch);
		}

		player.draw(batch);

		batch.end();

        doPhysicsStep(Gdx.graphics.getDeltaTime());
	}

	private void submarineMovement() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            float x = player.getSprite().getX();
            float y = player.getSprite().getY();

			bullets.add(new Bullet(player.getDegrees(), x, y));
		}

		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			player.rotateRight();
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			player.rotateLeft();
		}

		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			player.getSprite().setX(player.getSprite().getX() + player.getSpeed() *
					(float) Math.cos( Math.toRadians( player.getDegrees())));
			player.getSprite().setY(player.getSprite().getY() + player.getSpeed() *
					(float) Math.sin( Math.toRadians( player.getDegrees())));
		}
	}

	// Fixed time step
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

    private void checkIfChangeRoom(float position) {
		checkInWhatRoom(position);

		System.out.println(room);

		if (
		position >= FIRST_SCREEN_RIGHT_SIDE
		&&
		room == 1
		) {
			player.getSprite().setX(SECOND_SCREEN_LEFT_SPAWN_POINT);
		} else if (
		position <= SECOND_SCREEN_LEFT_SIDE
		&&
		room == 2
		) {
			player.getSprite().setX(FIRST_SCREEN_SPAWN_POINT);
		} else if (
		position >= SECOND_SCREEN_RIGHT_SIDE
		&&
		room == 2) {
			player.getSprite().setX(THIRD_SCREEN_SPAWN_POINT);
		} else if (
		position <= THIRD_SCREEN_LEFT_SIDE
		&&
		room == 3) {
			player.getSprite().setX(SECOND_SCREEN_RIGHT_SPAWN_POINT);
		}
 	}

 	private void checkInWhatRoom(float position) {
		if ( // Check if in the first room
		position <= ROOM_LENGTH_PIXELS * scale
		) {
			room = 1;
		} else if ( // Check if in the second room
		 position >= (ROOM_LENGTH_PIXELS + PIPE_HORIZONTAL_PIXELS) * scale &&
		 position <= (ROOM_LENGTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS) * scale
		) {
			room = 2;
		} else if ( // Check if in the third room
		position >= (ROOM_LENGTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS) * scale
		) {
			room = 3;
		}
	}

	private void moveCamera(OrthographicCamera camera) {
		if (room == 1) {
			camera.position.x = ROOM_LENGTH_PIXELS / 2 * scale;
			camera.position.y = WORLD_HEIGHT_PIXELS / 2 * scale;
		} else if (room == 2) {
			camera.position.x = WORLD_WIDTH_PIXELS / 2 * scale;
			camera.position.y = WORLD_HEIGHT_PIXELS / 2 * scale;
		} else if (room == 3) {
			camera.position.x = (WORLD_WIDTH_PIXELS - ROOM_LENGTH_PIXELS / 2) * scale;
			camera.position.y = WORLD_HEIGHT_PIXELS / 2 * scale;
		}

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
