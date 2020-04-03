package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;

public class PlayScreen implements Screen {
    private Atomics game;
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
    private Score score;
    private PauseWindow pause;
    static float HUD_Y;
    private Microbe microbe;
    private Pipe pipes;

    static float scale = 1/100f;
    static boolean spawnCollectablePhosphorus = false;
    static float TILE_LENGTH_PIXELS = 32;
    static float TILES_AMOUNT_WIDTH = 106;
    static float TILES_AMOUNT_HEIGHT = 20;
    static float WORLD_WIDTH_PIXELS = TILES_AMOUNT_WIDTH * TILE_LENGTH_PIXELS;
    static float WORLD_HEIGHT_PIXELS = TILES_AMOUNT_HEIGHT * TILE_LENGTH_PIXELS;
    static float ROOM_TILES_AMOUNT_WIDTH = 30;
    static float ROOM_TILES_AMOUNT_HEIGHT = 20;
    static float ROOM_WIDTH_PIXELS = ROOM_TILES_AMOUNT_WIDTH * TILE_LENGTH_PIXELS;
    static float ROOM_HEIGHT_PIXELS = ROOM_TILES_AMOUNT_HEIGHT * TILE_LENGTH_PIXELS;
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

    PlayScreen(Atomics game) {
        this.game = game;

        // LibGDX
        camera = new OrthographicCamera();
        camera.setToOrtho(
                false,
                ROOM_WIDTH_PIXELS * scale,
                ROOM_HEIGHT_PIXELS * scale);
        HUD_Y = Gdx.graphics.getHeight() - TILE_LENGTH_PIXELS * 4;

        // TiledMap
        TiledMap tiledMap = new TmxMapLoader().load("atomics.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, scale);

        // Box2D
        world = new World(new Vector2(0, -1f), true);
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
        score = new Score();
        new Wall(tiledMap, "wall-rectangles");
        microbe = new Microbe(new Vector2((ROOM_WIDTH_PIXELS * 2  + PIPE_HORIZONTAL_PIXELS * 2 + 100f) * scale, 5));
        pipes = new Pipe();
        pipes.createPipes();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Render setup
        gameUtil.clearScreen();
        Atomics.batch.setProjectionMatrix(camera.combined);
        tiledMapRenderer.render();
        tiledMapRenderer.setView(camera);
        gameUtil.moveCamera(camera);
        gameUtil.checkIfChangeRoom(
                player.getBody(),
                player.getBody().getPosition().x,
                player.getDesiredAngle()
        );

        // Check destroyable bodies
        world.getBodies(bodies);
        collisionHandler.sendBodiesToBeDestroyed(bodies, bodiesToBeDestroyed);

        // Player input
        player.submarineMove();
        Controls.getStage().act(Gdx.graphics.getDeltaTime());
        Controls.getStage().draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            pause = new PauseWindow();
        }

        // Updates.
        pipes.update();

        // Spawn and draw
        Atomics.batch.begin();
        pipes.draw(Atomics.batch);
        phosphorus.spawnPhosphorus();
        gameUtil.drawBodies(bodies, Atomics.batch, player);
        Atomics.batch.end();

        // HUD render
        Atomics.HUDBatch.begin();
        player.drawHitpoints(Atomics.HUDBatch);
        score.draw(Atomics.HUDBatch);
        Atomics.HUDBatch.end();

        // Fixed step and destroy bodies
        gameUtil.doPhysicsStep(Gdx.graphics.getDeltaTime());
        collisionHandler.clearBodies(bodiesToBeDestroyed);

        // Debuggers
//        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        Controls.getStage().getViewport().update(width, height, true);
        player.getControls().createButtons(player);
        score = new Score();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}