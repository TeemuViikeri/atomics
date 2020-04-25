package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    static TiledMap tiledMap;
    static World world;
    private Box2DDebugRenderer debugRenderer;
    private Player player;
    static Array<Body> bodies;
    private Array<Body> bodiesToBeDestroyed;
    private CollisionHandler collisionHandler;
    private GameAudio gameAudio;
    private GameUtil gameUtil;
    private Item item;
    private Phosphorus phosphorus;
    private Score score;
    private Pipe pipes;
    static float HUD_Y;
    private Pause pause;
    static float gameOverTimer;
    static boolean clockPlaying;

    static float scale = 1/100f;
    static int levelMultiplier = 1;
    static int breakpoint = 250;
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
    static boolean Game_paused = false;

    PlayScreen(Atomics game) {
        this.game = game;

        // LibGDX
        camera = new OrthographicCamera();
        camera.setToOrtho(
                false,
                ROOM_WIDTH_PIXELS * scale ,
                ROOM_HEIGHT_PIXELS * scale);
        HUD_Y = Gdx.graphics.getHeight() - TILE_LENGTH_PIXELS * 4;

        // TiledMap
        gameAudio = new GameAudio();
        GameAudio.playBackgroundMusic();
        tiledMap = new TmxMapLoader().load("atomics.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, scale);

        // Box2D
        world = new World(new Vector2(0, -0.5f), true);
        debugRenderer = new Box2DDebugRenderer();
        collisionHandler = new CollisionHandler();
        world.setContactListener(collisionHandler);
        gameUtil = new GameUtil();

        // Game objects
        player = new Player(
                WORLD_WIDTH_PIXELS / 2 * scale,
                WORLD_HEIGHT_PIXELS / 2 * scale);
        bodies = new Array<>();
        bodiesToBeDestroyed = new Array<>();
        item = new Item();
        phosphorus = new Phosphorus();
        score = new Score();
        new Wall(tiledMap, "wall-rectangles", (short) -2);
        new Wall(tiledMap, "blocks", (short) -1);
        new Wall(tiledMap, "cleaner-area", (short) -2);
        pipes = new Pipe();
        pipes.createPipes();
        pause = new Pause(game);
        clockPlaying = false;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Atomics.batch.setProjectionMatrix(camera.combined);
        gameUtil.clearScreen();
        tiledMapRenderer.render();
        tiledMapRenderer.setView(camera);
        world.getBodies(bodies);

        Atomics.batch.begin();
        gameUtil.drawBodies(bodies, Atomics.batch, player);
        pipes.draw(Atomics.batch);
        Atomics.batch.end();

        // HUD render
        Atomics.HUDBatch.begin();
        player.drawHitpoints(Atomics.HUDBatch);
        score.draw(Atomics.HUDBatch);
        Atomics.HUDBatch.end();

        Controls.getStage().draw();

        if (!Game_paused) {
            // Render setup
            Gdx.input.setInputProcessor(Controls.getStage());
            gameUtil.moveCamera(camera);
            gameUtil.checkIfChangeRoom(
                    player.getBody(),
                    player.getBody().getPosition().x,
                    player.getDesiredAngle()
            );

            // Check destroyable bodies
            gameUtil.destroyBullets();
            collisionHandler.sendBodiesToBeDestroyed(bodies, bodiesToBeDestroyed);

            // Player input
            player.submarineMove();
            Controls.getStage().addActor(pause.getPauseButton());
            Controls.getStage().act(Gdx.graphics.getDeltaTime());

            // Updates
            pipes.update();
            Microbe.update();
            player.update();

            if (score.getScore() >= breakpoint) {
                levelMultiplier++;
                breakpoint += 250;
            }

            if (player.checkIfDead()) {
                GameAudio.clock.stop();
                gameUtil.endGame(game);
            }

            if (gameUtil.getItemCount() >= 40 || gameUtil.getAmountOfDeadPipes() == 4) {
                if (!clockPlaying) {
                    clockPlaying = true;
                    GameAudio.playClock();
                } else {
                    gameOverTimer += Gdx.graphics.getDeltaTime();
                }

                if (gameOverTimer >= 10f) {
                    GameAudio.clock.stop();
                    gameUtil.endGame(game);
                }
            }

            if (gameUtil.getAmountOfDeadPipes() < 40 && gameUtil.getAmountOfDeadPipes() < 4) {
                clockPlaying = false;
                GameAudio.clock.stop();
            }

            // Spawns
            item.spawnItem();
            phosphorus.spawnPhosphorus();

            // Fixed step and destroy bodies
            gameUtil.doPhysicsStep(Gdx.graphics.getDeltaTime());
            collisionHandler.clearBodies(bodiesToBeDestroyed);

            // Debuggers
            //debugRenderer.render(world, camera.combined);
        }

        pause.pauseScreen();

    }

    @Override
    public void resize(int width, int height) {
        Controls.getStage().getViewport().update(width, height, true);
        player.getControls().createButtons(player);
        score = new Score();
    }

    @Override
    public void pause() {
        Game_paused = true;
    }

    @Override
    public void resume() {
        Game_paused = false;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        gameAudio.dispose();
    }
}
