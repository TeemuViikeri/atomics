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
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private Button speedButton;
    private Button shootButton;
    private Button.ButtonStyle speedButtonStyle;
    private Skin buttonSkin;
    private Drawable up;
    private Drawable down;
    private Stage stage;
    private Table joysticTable;
    private Table speedButtonTable;
    private Table shootButtonTable;
    private float desiredAngle;
    private float deltaX;
    private float deltaY;
    private Phosphorus phosphorus;

	// Initiated fields
    static float TILE_LENGTH_PIXELS = 32;
    static float TILES_AMOUNT_WIDTH = 106;
    static float TILES_AMOUNT_HEIGHT = 20;
    static float WORLD_WIDTH_PIXELS = TILES_AMOUNT_WIDTH * TILE_LENGTH_PIXELS; // = 3392 px
	static float WORLD_HEIGHT_PIXELS = TILES_AMOUNT_HEIGHT * TILE_LENGTH_PIXELS; // = 640 px
    private float ROOM_TILES_AMOUNT_WIDTH = 30;
    private float ROOM_TILES_AMOUNT_HEIGHT = 20;
    private float ROOM_WIDTH_PIXELS = ROOM_TILES_AMOUNT_WIDTH * TILE_LENGTH_PIXELS; // 960 px
    private float ROOM_HEIGHT_PIXELS = ROOM_TILES_AMOUNT_HEIGHT * TILE_LENGTH_PIXELS; // 640 px
	private float PIPE_HORIZONTAL_TILES_AMOUNT = 8;
	private float PIPE_VERTICAL_TILES_AMOUNT = 2;
	private float PIPE_HORIZONTAL_PIXELS = PIPE_HORIZONTAL_TILES_AMOUNT * TILE_LENGTH_PIXELS;
	private float FIRST_SCREEN_RIGHT_SIDE = 31 * TILE_LENGTH_PIXELS * scale;
    private float SECOND_SCREEN_LEFT_SIDE = 37 * TILE_LENGTH_PIXELS * scale;
    private float SECOND_SCREEN_RIGHT_SIDE = 69 * TILE_LENGTH_PIXELS * scale;
    private float THIRD_SCREEN_LEFT_SIDE = 75 * TILE_LENGTH_PIXELS * scale;
    private float FIRST_SCREEN_SPAWN_POINT = 30 * TILE_LENGTH_PIXELS * scale;
    private float SECOND_SCREEN_LEFT_SPAWN_POINT = 38 * TILE_LENGTH_PIXELS * scale;
    private float SECOND_SCREEN_RIGHT_SPAWN_POINT = 68 * TILE_LENGTH_PIXELS * scale;
    private float THIRD_SCREEN_SPAWN_POINT = 76 * TILE_LENGTH_PIXELS * scale;

    static float scale = 1/100f;
    private double accumulator = 0;
    private float TIME_STEP = 1 / 60f;
    private int room = 2;
	private boolean moving = false;
	private float maxSpeed = 150f;
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
        world.setContactListener(new GameContactListener());

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
        createButtons();
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

		clearScreen(97/255f, 134/255f, 106/255f);

		batch.setProjectionMatrix(camera.combined);
		moveCamera(camera);

		tiledMapRenderer.render();
		tiledMapRenderer.setView(camera);
        debugRenderer.render(world, camera.combined);

		checkIfChangeRoom(player.getBody().getPosition().x);

		world.getBodies(bodies);
        sendBodiesToBeDestroyed();

        if (touchpad.isTouched()) {
            player.submarineRotation(deltaX, deltaY);
        }

        if (speedButton.isPressed()) {
            moving = true;
            player.setSpeed(maxSpeed);
        } else {
            moving = false;
        }

        player.submarineMove(moving);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

		batch.begin();
        drawBullets();
        player.draw(batch, player.getBody());
        batch.draw(phosphorus.getAnimation().getKeyFrame
                        (phosphorus.setStateTime(), true),
                phosphorus.getBody().getPosition().x - 0.25f,
                phosphorus.getBody().getPosition().y - 0.25f,0.5f, 0.5f);
		batch.end();

        doPhysicsStep(Gdx.graphics.getDeltaTime());
        clearBullets();

        //joystickTable.setDebug(true);
        //speedButtonTable.setDebug(true);
	}

    public void sendBodiesToBeDestroyed() {
        for (Body body: bodies) {
            if (body.getUserData().equals("dead")) {
                bodiesToBeDestroyed.add(body);
            }
        }
    }

    // For debugging button responsivity. delete later.
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,true);
        createButtons();
    }

	private void createButtons() {
        stage = new Stage();
        touchpad = new Touchpad(10, getTouchpadStyle());
        joysticTable = new Table();
        joysticTable.setFillParent(true);
        speedButtonTable = new Table();
        speedButtonTable.setFillParent(true);
        shootButtonTable = new Table();
        shootButtonTable.setFillParent(true);

        joysticTable.add(touchpad).width(Gdx.graphics.getHeight() / 6.0f)
                .height(Gdx.graphics.getHeight() / 6.0f)
                .padLeft(-Gdx.graphics.getWidth() / 3f)
                .padBottom(-Gdx.graphics.getHeight() / 3.5f)
                .padTop(Gdx.graphics.getHeight() / 3.5f)
                .padRight(Gdx.graphics.getWidth() / 3f)
                .fill();
        touchpadStyle.knob.setMinWidth(Gdx.graphics.getWidth() / 16.0f);
        touchpadStyle.knob.setMinHeight(Gdx.graphics.getWidth() / 16.0f);

        speedButton = new Button(getButtonStyle());
        speedButtonTable.add(speedButton).width(Gdx.graphics.getHeight() / 6.0f)
                .height(Gdx.graphics.getHeight() / 6.0f)
                .padLeft(+Gdx.graphics.getWidth() / 3f)
                .padBottom(-Gdx.graphics.getHeight() / 3.5f)
                .padTop(+Gdx.graphics.getHeight() / 3.5f)
                .padRight(-Gdx.graphics.getWidth() / 3f)
                .fill();

        shootButton = new Button(getButtonStyle());
        shootButtonTable.add(shootButton).width((float) Gdx.graphics.getHeight() / 6.0f)
                .height((float) Gdx.graphics.getHeight() / 6.0f)
                .padLeft((float) Gdx.graphics.getWidth() / 3f)
                .padBottom((float) -Gdx.graphics.getHeight() / 3.5f)
                .padTop((float) Gdx.graphics.getHeight() / 3.5f)
                .padRight((float) -Gdx.graphics.getWidth() / 40f)
                .fill();

        stage.addActor(joysticTable);
        stage.addActor(speedButtonTable);
        stage.addActor(shootButtonTable);

        Gdx.input.setInputProcessor(stage);

        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                    deltaX = ((Touchpad) actor).getKnobPercentX();
                    deltaY = ((Touchpad) actor).getKnobPercentY();
            }
        });

        shootButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                fireBullet();
            }
        });

    }

    private Button.ButtonStyle getButtonStyle() {
        buttonSkin = new Skin();
        speedButtonStyle = new Button.ButtonStyle();

        buttonSkin.add("down", new Texture("down.png"));
        buttonSkin.add("up", new Texture("up.png"));

        up = buttonSkin.getDrawable("up");
        down = buttonSkin.getDrawable("down");

        speedButtonStyle.up = up;
        speedButtonStyle.down = down;

        return speedButtonStyle;
    }

	private Touchpad.TouchpadStyle getTouchpadStyle() {
	    touchpadSkin = new Skin();
	    touchpadSkin.add("touchBackground", new Texture("touchpadbg.png"));

	    touchpadSkin.add("touchKnob", new Texture("touchpadknob.png"));

	    touchpadStyle = new Touchpad.TouchpadStyle();

	    touchBackground = touchpadSkin.getDrawable("touchBackground");
	    touchKnob = touchpadSkin.getDrawable("touchKnob");

	    touchpadStyle.background = touchBackground;
	    touchpadStyle.knob = touchKnob;

	    return touchpadStyle;
    }

    private void fireBullet() {
        Bullet bulletObj = new Bullet(
                world,
                player.getBody(),
                player.getBody().getAngle(),
                player.getBody().getPosition().x,
                player.getBody().getPosition().y
        );

        Vector2 force = new Vector2((float) Math.cos(bulletObj.getBody().getAngle())
                * bulletObj.getSpeed() * Gdx.graphics.getDeltaTime(),
                (float) Math.sin(bulletObj.getBody().getAngle())
                        * bulletObj.getSpeed() * Gdx.graphics.getDeltaTime());


        Body bulletBody = bulletObj.getBody();

        bulletBody.applyLinearImpulse(
                force,
                bulletBody.getWorldCenter(),
                true
        );
    }

    private void drawBullets() {
        for (Body body: bodies) {
            Object temp = body.getUserData();
            if (temp instanceof Bullet) {
                batch.draw(
                    bullet.getTexture(),
                    body.getPosition().x - bullet.getFixture().shape.getRadius(),
                    body.getPosition().y - bullet.getFixture().shape.getRadius(),
                    bullet.getFixture().shape.getRadius(),
                    bullet.getFixture().shape.getRadius(),
                    bullet.getFixture().shape.getRadius() * 2,
                    bullet.getFixture().shape.getRadius() * 2,
                    1.0f,
                    1.0f,
                    player.getBody().getAngle() * MathUtils.radiansToDegrees,
                    0,
                    0,
                    bullet.getTexture().getWidth(),
                    bullet.getTexture().getHeight(),
                    false,
                    false
                );
            }
        }
    }

    private void clearBullets() {
	    for (Iterator<Body> i = bodiesToBeDestroyed.iterator(); i.hasNext();) {
	        Body body = i.next();
	        world.destroyBody(body);
	        i.remove();
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

		if (position >= FIRST_SCREEN_RIGHT_SIDE && room == 1) {
			player.getBody().setTransform(
				SECOND_SCREEN_LEFT_SPAWN_POINT,
				player.getBody().getPosition().y,
				player.getDesiredAngle()
			);
		} else if (position <= SECOND_SCREEN_LEFT_SIDE && room == 2) {
			player.getBody().setTransform(
				FIRST_SCREEN_SPAWN_POINT,
				player.getBody().getPosition().y,
				player.getDesiredAngle()
			);
		} else if (position >= SECOND_SCREEN_RIGHT_SIDE && room == 2) {
			player.getBody().setTransform(
				THIRD_SCREEN_SPAWN_POINT,
				player.getBody().getPosition().y,
				player.getDesiredAngle()
			);
		} else if (position <= THIRD_SCREEN_LEFT_SIDE && room == 3) {
			player.getBody().setTransform(
				SECOND_SCREEN_RIGHT_SPAWN_POINT,
				player.getBody().getPosition().y,
				player.getDesiredAngle()
			);
		}
	}

 	private void checkInWhatRoom(float position) {
		if ( // Check if in the first room
			position <= ROOM_WIDTH_PIXELS * scale
		) {
			room = 1;
		} else if ( // Check if in the second room
		 	position >= (ROOM_WIDTH_PIXELS + PIPE_HORIZONTAL_PIXELS) * scale &&
		 	position <= (ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS) * scale
		) {
			room = 2;
		} else if ( // Check if in the third room
			position >= (ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS * 2) * scale
		) {
			room = 3;
		}
	}

	private void moveCamera(OrthographicCamera camera) {
		if (room == 1) {
			camera.position.x = ROOM_WIDTH_PIXELS / 2 * scale;
			camera.position.y = WORLD_HEIGHT_PIXELS / 2 * scale;
		} else if (room == 2) {
			camera.position.x = WORLD_WIDTH_PIXELS / 2 * scale;
			camera.position.y = WORLD_HEIGHT_PIXELS / 2 * scale;
		} else if (room == 3) {
			camera.position.x = (WORLD_WIDTH_PIXELS - ROOM_WIDTH_PIXELS / 2) * scale;
			camera.position.y = WORLD_HEIGHT_PIXELS / 2 * scale;
		}

		camera.update();
	}

	private void clearScreen(float r, float g, float b) {
		Gdx.gl.glClearColor(r, g, b, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
	}

	@Override
	public void pause() {
        this.state = State.PAUSE;
	}

    @Override
    public void resume() {
        this.state = State.RESUME;
    }
}
