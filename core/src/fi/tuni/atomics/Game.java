package fi.tuni.atomics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Game extends ApplicationAdapter {
	// Non-initiated fields
	private SpriteBatch batch;
	private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    static World world;
    private Box2DDebugRenderer debugRenderer;
    private Player player;
    private Array<Body> bodies;
    private Array<Body> bodiesToBeDestroyed;
    private CollisionHandler collisionHandler;
    private GameUtil gameUtil;
    private Phosphorus phosphorus;

	// Initiated fields
    static float scale = 1/100f;
    private static float TILE_LENGTH_PIXELS = 32;
    private static float TILES_AMOUNT_WIDTH = 106;
    private static float TILES_AMOUNT_HEIGHT = 20;
    static float WORLD_WIDTH_PIXELS = TILES_AMOUNT_WIDTH * TILE_LENGTH_PIXELS; // = 3392 px
    static float WORLD_HEIGHT_PIXELS = TILES_AMOUNT_HEIGHT * TILE_LENGTH_PIXELS; // = 640 px
    private static float ROOM_TILES_AMOUNT_WIDTH = 30;
    private static float ROOM_TILES_AMOUNT_HEIGHT = 20;
    static float ROOM_WIDTH_PIXELS = ROOM_TILES_AMOUNT_WIDTH * TILE_LENGTH_PIXELS; // 960 px
    private static float ROOM_HEIGHT_PIXELS = ROOM_TILES_AMOUNT_HEIGHT * TILE_LENGTH_PIXELS; // 640 px
	private static float PIPE_HORIZONTAL_TILES_AMOUNT = 8;
	static float PIPE_HORIZONTAL_PIXELS = PIPE_HORIZONTAL_TILES_AMOUNT * TILE_LENGTH_PIXELS;
	static float FIRST_SCREEN_RIGHT_SIDE = 31 * TILE_LENGTH_PIXELS * scale;
    static float SECOND_SCREEN_LEFT_SIDE = 37 * TILE_LENGTH_PIXELS * scale;
    static float SECOND_SCREEN_RIGHT_SIDE = 69 * TILE_LENGTH_PIXELS * scale;
    static float THIRD_SCREEN_LEFT_SIDE = 75 * TILE_LENGTH_PIXELS * scale;
    static float FIRST_SCREEN_SPAWN_POINT = 30 * TILE_LENGTH_PIXELS * scale;
    static float SECOND_SCREEN_LEFT_SPAWN_POINT = 38 * TILE_LENGTH_PIXELS * scale;
    static float SECOND_SCREEN_RIGHT_SPAWN_POINT = 68 * TILE_LENGTH_PIXELS * scale;
    static float THIRD_SCREEN_SPAWN_POINT = 76 * TILE_LENGTH_PIXELS * scale;

    private State state = State.RUN;

    public enum State {
        PAUSE,
        RUN,
        RESUME,
        STOPPED
    }

	@Override
	public void create () {
		// LibGDX
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(
				false,
                ROOM_WIDTH_PIXELS * scale,
                ROOM_HEIGHT_PIXELS * scale);

		// TiledMap
        TiledMap tiledMap = new TmxMapLoader().load("atomics.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, scale);

		// Box2D
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();
        world.setContactListener(new CollisionHandler());
        collisionHandler = new CollisionHandler();
        gameUtil = new GameUtil();

		// Game objects
        player = new Player(
                WORLD_WIDTH_PIXELS / 2 * scale,
                WORLD_HEIGHT_PIXELS / 2 * scale);
        bodies = new Array<>();
        bodiesToBeDestroyed = new Array<>();
        phosphorus = new Phosphorus();
        new Wall(tiledMap, "wall-rectangles");
	}

	@Override
	public void render () {
        // switch (state) {
        //     case RUN:
        //
        //         break;
        //     case PAUSE:
        //
        //         break;
        //     case RESUME:
        //
        //         break;
        //     default:
        //
        //         break;
        // }

		// Debuggers
        debugRenderer.render(world, camera.combined);

        // Render setup
		gameUtil.clearScreen();
		batch.setProjectionMatrix(camera.combined);
		tiledMapRenderer.render();
		tiledMapRenderer.setView(camera);
		gameUtil.moveCamera(camera);
		gameUtil.checkIfChangeRoom(
            player.getBody(),
            player.getBody().getPosition().x,
            player.getDesiredAngle()
		);

        // Check if
		world.getBodies(bodies);
        collisionHandler.sendBodiesToBeDestroyed(bodies, bodiesToBeDestroyed);
        phosphorus.spawnPhosphorus();

        // Player input
        player.submarineMove();
        player.getControls().getStage().act(Gdx.graphics.getDeltaTime());
        player.getControls().getStage().draw();

        // Draw
		batch.begin();
        gameUtil.drawBodies(bodies, batch, player);
		batch.end();

        // Fixed step and destroy bodies
        gameUtil.doPhysicsStep(Gdx.graphics.getDeltaTime());
        collisionHandler.clearBodies(bodiesToBeDestroyed);

        // player.getControls().getJoystickTable().setDebug(true);
        // player.getControls().getSpeedButtonTable().setDebug(true);
	}

	public void resize(int width, int height) {
        player.getControls().getStage().getViewport().update(width, height, true);
        player.getControls().createButtons(player);
    }

	@Override
	public void pause() {
        this.state = State.PAUSE;
	}

    @Override
    public void resume() {
        this.state = State.RESUME;
    }

	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
	}
}
