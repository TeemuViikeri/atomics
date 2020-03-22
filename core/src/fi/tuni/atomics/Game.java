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
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
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
    static Player player;
    private Bullet bullet;
    private Texture bulletTexture;
    private Array<Body> bodies;
    private Array<Body> bodiesToBeDestroyed;
    private Array<Body> bullets;
    static Body submarineBody;
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private Button speedButton;
    private Button.ButtonStyle speedButtonStyle;
    private Skin buttonSkin;
    private Drawable up;
    private Drawable down;
    private Stage stage;
    private Table joysticTable;
    private Table speedButtonTable;
    private float desiredAngle;
    private float deltaX;
    private float deltaY;

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
	private float PIPE_VERTICAL_PIXELS = PIPE_VERTICAL_TILES_AMOUNT * TILE_LENGTH_PIXELS;
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
    private float magnitude = 2.5f;
	private boolean moving = false;
	private float speedDecrement = 3f;
	private float maxSpeed = 150f;

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

		// Game objects
        player = new Player(
                WORLD_WIDTH_PIXELS / 2 * scale,
                WORLD_HEIGHT_PIXELS / 2 * scale);
        submarineBody = world.createBody(player.getBodyDef());
        submarineBody.createFixture(player.getFixture());
        submarineBody.setUserData("player");
        bullet = new Bullet(world);
        bodies = new Array<>();
        bodiesToBeDestroyed = new Array<>();
        transformWallsToBodies("wall-rectangles", "wall");
        createButtons();

//    Use ContactListener with:
//    - Submarine collides with phosphorus
//    - Bullet collides with wall
//    - Bullet collides with phosphorus
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();

                if (isBulletContactingWall(bodyA, bodyB)) {
                    if (bodyA.getUserData() instanceof Bullet) {
                        bodiesToBeDestroyed.add(bodyA);
                    } else {
                        bodiesToBeDestroyed.add(bodyB);
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
	}

    private boolean isBulletContactingWall(Body a, Body b) {
        if (a.getUserData() instanceof Bullet && b.getUserData().equals("wall")) {
            return true;
        } else return a.getUserData().equals("wall") && b.getUserData() instanceof Bullet;
    }

	@Override
	public void render () {
        doPhysicsStep(Gdx.graphics.getDeltaTime());
            clearBullets();
            bodiesToBeDestroyed.clear();
		batch.setProjectionMatrix(camera.combined);
		clearScreen(97/255f, 134/255f, 106/255f); // color: teal
		tiledMapRenderer.render();
		tiledMapRenderer.setView(camera);
        debugRenderer.render(world, camera.combined);
		moveCamera(camera);
		checkIfChangeRoom(submarineBody.getPosition().x);
		world.getBodies(bodies);

        if (touchpad.isTouched()) {
            submarineRotation();
        }

        if (speedButton.isPressed()) {
            moving = true;
            player.setSpeed(maxSpeed);
        } else {
            moving = false;
        }

        submarineMove();
        submarineShoot();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

		batch.begin();

        player.draw(batch, submarineBody);
        drawBullets();

		batch.end();

        //joystickTable.setDebug(true);
        //speedButtonTable.setDebug(true);
	}

    // For debugging button responsivity. delete later.
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,true);
        createButtons();
    }

	private void createButtons() {
        stage = new Stage();
        joysticTable = new Table();
        speedButtonTable = new Table();
        joysticTable.setFillParent(true);
        speedButtonTable.setFillParent(true);
        Gdx.input.setInputProcessor(stage);
        touchpad = new Touchpad(10, getTouchpadStyle());
        joysticTable.add(touchpad).width(Gdx.graphics.getWidth() / 8.0f)
                .height(Gdx.graphics.getWidth() / 8.0f)
                .left()
                .bottom()
                .padLeft(-Gdx.graphics.getWidth() / 3f)
                .padBottom(-Gdx.graphics.getHeight() / 3.5f)
                .padTop(Gdx.graphics.getHeight() / 3.5f)
                .padRight(Gdx.graphics.getWidth() / 3f)
                .fill();
        touchpadStyle.knob.setMinWidth(Gdx.graphics.getWidth() / 16.0f);
        touchpadStyle.knob.setMinHeight(Gdx.graphics.getWidth() / 16.0f);
        speedButton = new Button(getButtonStyle());
        speedButtonTable.add(speedButton).width(Gdx.graphics.getWidth() / 8.0f)
                .height(Gdx.graphics.getWidth() / 8.0f)
                .right()
                .bottom()
                .padLeft(+Gdx.graphics.getWidth() / 3f)
                .padBottom(-Gdx.graphics.getHeight() / 3.5f)
                .padTop(+Gdx.graphics.getHeight() / 3.5f)
                .padRight(-Gdx.graphics.getWidth() / 3f)
                .fill();
        stage.addActor(joysticTable);
        stage.addActor(speedButtonTable);
        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                deltaX = ((Touchpad) actor).getKnobPercentX();
                deltaY = ((Touchpad) actor).getKnobPercentY();
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

    private void submarineMove() {
        if (moving) {
            Vector2 force = new Vector2((float) Math.cos(submarineBody.getAngle())
                    * player.getSpeed() * Gdx.graphics.getDeltaTime(),
                    (float) Math.sin(submarineBody.getAngle())
                            * player.getSpeed() * Gdx.graphics.getDeltaTime());

            submarineBody.setLinearVelocity(force);
        }

        if (!moving && player.getSpeed() >= speedDecrement) {
            player.setSpeed(player.getSpeed() - speedDecrement);
            Vector2 force = new Vector2((float) Math.cos(submarineBody.getAngle())
                    * player.getSpeed() * Gdx.graphics.getDeltaTime(),
                    (float) Math.sin(submarineBody.getAngle())
                            * player.getSpeed() * Gdx.graphics.getDeltaTime());

            submarineBody.setLinearVelocity(force);
        } else if (player.getSpeed() < speedDecrement) {
            player.setSpeed(0);
            Vector2 force = new Vector2((float) Math.cos(submarineBody.getAngle())
                    * player.getSpeed() * Gdx.graphics.getDeltaTime(),
                    (float) Math.sin(submarineBody.getAngle())
                            * player.getSpeed() * Gdx.graphics.getDeltaTime());

            submarineBody.setLinearVelocity(force);
        }
    }

	private void submarineRotation() {
        desiredAngle = (float) Math.atan2( -deltaX, deltaY) + (float) Math.toRadians(90);
        float totalRotation = desiredAngle - submarineBody.getAngle();
        // Finds the shortest route
        while (totalRotation < -180 * MathUtils.degreesToRadians)
            totalRotation += 360 * MathUtils.degreesToRadians;
        while (totalRotation > 180 * MathUtils.degreesToRadians)
            totalRotation -= 360 * MathUtils.degreesToRadians;
        // maximum rotation per render
        float maxRotation = 1000 * MathUtils.degreesToRadians * Gdx.graphics.getDeltaTime();
        float newAngle = submarineBody.getAngle()
                + Math.min(maxRotation, Math.max(-maxRotation, totalRotation));
        submarineBody.setTransform(submarineBody.getPosition(), newAngle);
	}

    private void submarineShoot() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            fireBullet();
        }
    }

    private void fireBullet() {
        Bullet bulletObj = new Bullet(
                world,
                submarineBody.getAngle(),
                submarineBody.getPosition().x,
                submarineBody.getPosition().y
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
                    submarineBody.getAngle() * MathUtils.radiansToDegrees,
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
        for (Body body: bodiesToBeDestroyed) {
            world.destroyBody(body);
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
			submarineBody.setTransform(
				SECOND_SCREEN_LEFT_SPAWN_POINT,
				submarineBody.getPosition().y,
				desiredAngle
			);
		} else if (position <= SECOND_SCREEN_LEFT_SIDE && room == 2) {
			submarineBody.setTransform(
				FIRST_SCREEN_SPAWN_POINT,
				submarineBody.getPosition().y,
				desiredAngle
			);
		} else if (position >= SECOND_SCREEN_RIGHT_SIDE && room == 2) {
			submarineBody.setTransform(
				THIRD_SCREEN_SPAWN_POINT,
				submarineBody.getPosition().y,
				desiredAngle
			);
		} else if (position <= THIRD_SCREEN_LEFT_SIDE && room == 3) {
			submarineBody.setTransform(
				SECOND_SCREEN_RIGHT_SPAWN_POINT,
				submarineBody.getPosition().y,
				desiredAngle
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

    private void createStaticBody(Rectangle rect, String userData) {
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
		Gdx.gl.glClearColor(r, g, b, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
	}
}
