package fi.tuni.atomics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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
    private Body submarineBody;
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private Stage stage;

	// Initiated fields
    static float scale = 1/100f;
    private double accumulator = 0;
    private float magnitude = 1;
    private float TIME_STEP = 1 / 60f;
    static float TILE_LENGTH_PIXELS = 32;
    static float TILES_AMOUNT_WIDTH = 64;
    static float TILES_AMOUNT_HEIGHT = 16;
    static float WORLD_WIDTH_PIXELS =
			TILES_AMOUNT_WIDTH * TILE_LENGTH_PIXELS; // = 2048 pixels
	static float WORLD_HEIGHT_PIXELS =
			TILES_AMOUNT_HEIGHT * TILE_LENGTH_PIXELS; // = 512 pixels
    private float ROOM_TILES_AMOUNT = 16;
    private float ROOM_LENGTH_PIXELS = ROOM_TILES_AMOUNT * TILE_LENGTH_PIXELS ;
	private float PIPE_HORIZONTAL_TILES_AMOUNT = 8;
	private float PIPE_VERTICAL_TILES_AMOUNT = 2;
	private float PIPE_HORIZONTAL_PIXELS = PIPE_HORIZONTAL_TILES_AMOUNT * TILE_LENGTH_PIXELS;
	private float PIPE_VERTICAL_PIXELS = PIPE_VERTICAL_TILES_AMOUNT * TILE_LENGTH_PIXELS;
	float FIRST_SCREEN_RIGHT_SIDE = 16 * TILE_LENGTH_PIXELS * scale;
	float SECOND_SCREEN_LEFT_SIDE = 23 * TILE_LENGTH_PIXELS * scale;
	float SECOND_SCREEN_RIGHT_SIDE = 39 * TILE_LENGTH_PIXELS * scale;
	float THIRD_SCREEN_LEFT_SIDE = 47 * TILE_LENGTH_PIXELS * scale;
	float FIRST_SCREEN_SPAWN_POINT = 14 * TILE_LENGTH_PIXELS * scale;
	float SECOND_SCREEN_LEFT_SPAWN_POINT = 24 * TILE_LENGTH_PIXELS * scale;
	float SECOND_SCREEN_RIGHT_SPAWN_POINT = 38 * TILE_LENGTH_PIXELS * scale;
	float THIRD_SCREEN_SPAWN_POINT = 48 * TILE_LENGTH_PIXELS * scale;
	private boolean moving = false;

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
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();
        player = new Player(
                WORLD_WIDTH_PIXELS / 2 * scale,
                WORLD_HEIGHT_PIXELS / 2 * scale);
        submarineBody = world.createBody(player.getBodyDef());
        submarineBody.createFixture(player.getFixture());
        transformWallsToBodies("walls", "wall");
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        touchpad = new Touchpad(10, getTouchpadStyle());
        touchpad.setBounds(100, 100,100,100);
        touchpad.setPosition(50, 50);
        stage.addActor(touchpad);

    touchpad.addListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            float deltaX = ((Touchpad) actor).getKnobPercentX();
            float deltaY = ((Touchpad) actor).getKnobPercentY();
            if (deltaX != -0.0 && deltaY != -0.0) {
                moving = true;
            float desiredAngle = (float) Math.atan2(-deltaX, deltaY);
            submarineBody.setTransform(submarineBody.getPosition(),
                    desiredAngle + (float) Math.toRadians(90));
        } else {
                moving = false;
            }
        }
    });



		// Game objects
		room = 2;
		bullets = new ArrayList<>();
	}

	@Override
	public void render () {
		batch.setProjectionMatrix(camera.combined);
		clearScreen(97/255f, 134/255f, 106/255f); // color: teal
		moveCamera(camera);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
		tiledMapRenderer.render();
		tiledMapRenderer.setView(camera);

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
        player.draw(batch, submarineBody);

		batch.end();
        debugRenderer.render(world, camera.combined);
        doPhysicsStep(Gdx.graphics.getDeltaTime());
	}

	private Touchpad.TouchpadStyle getTouchpadStyle() {
	    touchpadSkin = new Skin();
	    touchpadSkin.add("touchBackground", new Texture("touchpadbg.png"));

	    touchpadSkin.add("touchKnob", new Texture("touchpadknob.png"));

	    touchpadStyle = new Touchpad.TouchpadStyle();

	    touchBackground = touchpadSkin.getDrawable("touchBackground");
	    touchKnob = touchpadSkin.getDrawable("touchKnob");

	    touchKnob.setMinHeight(35);
	    touchKnob.setMinWidth(35);

	    touchpadStyle.background = touchBackground;
	    touchpadStyle.knob = touchKnob;

	    return touchpadStyle;
    }

	private void submarineMovement() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            float x = player.getSprite().getX();
            float y = player.getSprite().getY();

			bullets.add(new Bullet(player.getDegrees(), x, y));
		}

		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            submarineBody.setTransform(submarineBody.getPosition().x, submarineBody.getPosition().y,
                    submarineBody.getAngle()
                            + (float) Math.toRadians(-200 * Gdx.graphics.getDeltaTime()));
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            submarineBody.setTransform(submarineBody.getPosition().x, submarineBody.getPosition().y,
                    submarineBody.getAngle()
                            + (float) Math.toRadians(200 * Gdx.graphics.getDeltaTime()));
		}

		if(moving) {
            Vector2 force = new Vector2((float) Math.cos(submarineBody.getAngle()) * magnitude,
                    (float) Math.sin(submarineBody.getAngle()) * magnitude);
            submarineBody.applyForce(force, submarineBody.getPosition(), true);
		}
		System.out.println(moving);
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

    private void transformWallsToBodies(String layer, String userData) {
        MapLayer collisionObjectLayer = tiledMap.getLayers().get(layer);
        MapObjects mapObjects = collisionObjectLayer.getObjects();
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();
            Rectangle rectangle = scaleRect(tmp, 1 / 100f);
            createStaticBody(rectangle, userData);
        }
    }

    private Rectangle scaleRect(Rectangle r, float scale) {
        Rectangle rectangle = new Rectangle();
        rectangle.x = r.x * scale;
        rectangle.y = r.y * scale;
        rectangle.width = r.width * scale;
        rectangle.height = r.height * scale;
        return rectangle;
    }

    public void createStaticBody(Rectangle rect, String userData) {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.StaticBody;

        float x = rect.getX();
        float y = rect.getY();
        float width = rect.width;
        float height = rect.height;

        float centerX = width / 2 + x;
        float centerY = height / 2 + y;

        myBodyDef.position.set(centerX, centerY);
        Body wall = world.createBody(myBodyDef);

        wall.setUserData(userData);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(width / 2, height / 2);
        wall.createFixture(groundBox, 0f);
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
