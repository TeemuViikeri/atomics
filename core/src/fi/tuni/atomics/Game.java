package fi.tuni.atomics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Game extends ApplicationAdapter {
	// Non-initiated fields
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Player player;
    private Bullet bullet;
    private Array<Body> bodies;
    private Array<Body> bodiesToBeDestroyed;
    private Wall wall;
    private CollisionHandler collisionHandler;
    private GameUtil gameUtil;

    private Stage stage;
    private Skin buttonSkin;
    private Drawable up;
    private Drawable down;

    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private Table joysticTable;
    private Button.ButtonStyle speedButtonStyle;

    private Button shootButton;
    private Table shootButtonTable;

    private Button speedButton;
    private Table speedButtonTable;

    private Phosphorus phosphorus;

	// Initiated fields
    static float scale = 1/100f;
    static float TILE_LENGTH_PIXELS = 32;
    static float TILES_AMOUNT_WIDTH = 106;
    static float TILES_AMOUNT_HEIGHT = 20;
    static float WORLD_WIDTH_PIXELS = TILES_AMOUNT_WIDTH * TILE_LENGTH_PIXELS; // = 3392 px
    static float WORLD_HEIGHT_PIXELS = TILES_AMOUNT_HEIGHT * TILE_LENGTH_PIXELS; // = 640 px
    static float ROOM_TILES_AMOUNT_WIDTH = 30;
    static float ROOM_TILES_AMOUNT_HEIGHT = 20;
    static float ROOM_WIDTH_PIXELS = ROOM_TILES_AMOUNT_WIDTH * TILE_LENGTH_PIXELS; // 960 px
    static float ROOM_HEIGHT_PIXELS = ROOM_TILES_AMOUNT_HEIGHT * TILE_LENGTH_PIXELS; // 640 px
	static float PIPE_HORIZONTAL_TILES_AMOUNT = 8;
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
		// libGDX
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(
				false,
                ROOM_WIDTH_PIXELS * scale,
                ROOM_HEIGHT_PIXELS * scale);

		// TiledMap
		tiledMap = new TmxMapLoader().load("atomics.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, scale);

		// Box2D
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();
        world.setContactListener(new CollisionHandler());
        collisionHandler = new CollisionHandler();
        gameUtil = new GameUtil();

		// Game objects
        player = new Player(
                world,
                WORLD_WIDTH_PIXELS / 2 * scale,
                WORLD_HEIGHT_PIXELS / 2 * scale);
        bullet = new Bullet(world);
        bodies = new Array<>();
        bodiesToBeDestroyed = new Array<>();
        phosphorus = new Phosphorus(world, 13, 6.4f);
        wall = new Wall(tiledMap, world, "wall-rectangles", "wall");
//      createButtons();
//      multiplexer = new InputMultiplexer(stage);
//      Gdx.input.setInputProcessor(multiplexer);

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

		GameUtil.clearScreen(97/255f, 134/255f, 106/255f);

		batch.setProjectionMatrix(camera.combined);
		GameUtil.moveCamera(camera);

		tiledMapRenderer.render();
		tiledMapRenderer.setView(camera);
        debugRenderer.render(world, camera.combined);

		GameUtil.checkIfChangeRoom(
            player.getBody(),
            player.getBody().getPosition().x,
            player.getDesiredAngle()
		);

		world.getBodies(bodies);
        CollisionHandler.sendBodiesToBeDestroyed(bodies, bodiesToBeDestroyed);

//        if (touchpad.isTouched()) {
//            player.submarineRotation();
//        }
//
//        if (speedButton.isPressed()) {
//            moving = true;
//            player.setSpeed(maxSpeed);
//        } else {
//            moving = false;
//        }

        player.submarineMove();

        player.getControls().getStage().act(Gdx.graphics.getDeltaTime());
        player.getControls().getStage().draw();

		batch.begin();
        player.drawBullets(bodies, batch, bullet);
        player.draw(batch, player.getBody());
        batch.draw(phosphorus.getAnimation().getKeyFrame(
                    phosphorus.setStateTime(), true),
                phosphorus.getBody().getPosition().x - 0.25f,
                phosphorus.getBody().getPosition().y - 0.25f,0.5f, 0.5f);
		batch.end();

        GameUtil.doPhysicsStep(world, Gdx.graphics.getDeltaTime());
        collisionHandler.clearBullets(bodiesToBeDestroyed, world);

        //joystickTable.setDebug(true);
        //speedButtonTable.setDebug(true);
	}

    @Override
    public void resize(int width, int height) {
        player.getControls().getStage().getViewport().update(width, height, true);
        player.getControls().createButtons(world, player);
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
