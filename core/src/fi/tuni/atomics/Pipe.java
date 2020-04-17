package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import static fi.tuni.atomics.PlayScreen.PIPE_HORIZONTAL_PIXELS;
import static fi.tuni.atomics.PlayScreen.ROOM_WIDTH_PIXELS;
import static fi.tuni.atomics.PlayScreen.TILE_LENGTH_PIXELS;
import static fi.tuni.atomics.PlayScreen.scale;

public class Pipe extends GameObject {
    private final int sheetRows = 2;
    private final int sheetCols = 4;
    static private Texture animationSheet = new Texture("bubbleSequence.png");
    private Animation<TextureRegion> animation;
    private TextureRegion[] frames;
    static private Texture animationSheet2 = new Texture("bubble2.png");
    private Animation<TextureRegion> animation2;
    private TextureRegion[] frames2;
    private Vector2 spawnPoint;
    private float stateTime;
    private GameUtil gameUtil = new GameUtil();
    private Array<Pipe> pipes = new Array<>();
    private boolean dead = false; // is the pipe dead.
    private float aliveTimer; // how long the pipe has been alive.
    private float fixTimer; // how long the pipe has been in repair.
    public boolean isTouched = false; // is the pipe being touched.
    private float timeAlive;
    private Microbe microbe = new Microbe();
    private float amountOfPipesAlive = 0;

    private Pipe(Vector2 position) {
        this.spawnPoint = position;
        width = 0.5f;
        stateTime = 1f;
        timeAlive = MathUtils.random(60,180);
        TextureRegion[][] temp = TextureRegion.split(
                animationSheet,
                animationSheet.getWidth() / sheetCols,
                animationSheet.getHeight() / sheetRows);
        TextureRegion[][] temp2 = TextureRegion.split(
                animationSheet2,
                animationSheet2.getWidth() / sheetCols,
                animationSheet2.getHeight() / sheetRows);
        frames = gameUtil.to1d(temp, sheetRows, sheetCols, this);
        animation = new Animation<>(1 / 10f, frames);
        frames2 = gameUtil.to1d(temp2, sheetRows, sheetCols, this);
        animation2 = new Animation<>(1 / 10f, frames2);
        createBody();
        microbe.spawnMicrobes();
    }

    Pipe() {
    }

    private float setStateTime() {
        return stateTime += Gdx.graphics.getDeltaTime();
    }

    private Animation<TextureRegion> getAnimation() {
        return animation;
    }

    private Animation<TextureRegion> getAnimation2() {
        return animation2;
    }

    void createPipes() {
        Microbe.microbes.clear();
        amountOfPipesAlive = 4;
        pipes.add(new Pipe(new Vector2((ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS * 2 + TILE_LENGTH_PIXELS * 5) * scale, (TILE_LENGTH_PIXELS * 3) * scale)));
        pipes.add(new Pipe(new Vector2((ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS * 2 + TILE_LENGTH_PIXELS * 11) * scale, (TILE_LENGTH_PIXELS * 3) * scale)));
        pipes.add(new Pipe(new Vector2((ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS * 2 + TILE_LENGTH_PIXELS * 18) * scale, (TILE_LENGTH_PIXELS * 3) * scale)));
        pipes.add(new Pipe(new Vector2((ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS * 2 + TILE_LENGTH_PIXELS * 24) * scale, (TILE_LENGTH_PIXELS * 3) * scale)));
    }

    void update() {
        System.out.println(amountOfPipesAlive);
        for (Pipe i : pipes) {
            i.aliveTimer += Gdx.graphics.getDeltaTime();

            System.out.println(i.aliveTimer);

            if (i.aliveTimer > i.timeAlive && !i.dead) {
                i.dead = true;
                amountOfPipesAlive--;
                microbe.deSpawnMicrobes();
            }

            i.fixPipe();
        }
    }

    void draw(SpriteBatch batch) {
        for (Pipe i : pipes) {
            if (!i.dead) {
                batch.draw(i.getAnimation().getKeyFrame((i).setStateTime(), true),
                        i.spawnPoint.x, i.spawnPoint.y,0.32f,0.32f);
                batch.draw(i.getAnimation2().getKeyFrame((i).setStateTime(), true),
                        i.spawnPoint.x, i.spawnPoint.y + 0.32f,0.32f,0.32f);
            }
        }
    }

    private void createBody() {
        body = PlayScreen.world.createBody(getDefinitionOfBody());
        body.createFixture(getFixtureDefinition());
        body.setUserData(this);
    }

    private BodyDef getDefinitionOfBody() {
        bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.KinematicBody;
        float posX = spawnPoint.x + (TILE_LENGTH_PIXELS / 2) * scale;
        float posY = spawnPoint.y - (TILE_LENGTH_PIXELS * 1.25f) * scale;
        bodyDef.position.set(new Vector2(posX, posY));

        return bodyDef;
    }

    private FixtureDef getFixtureDefinition() {
        fixtureDef = new FixtureDef();

        fixtureDef.isSensor = true;

        shape = new PolygonShape();

        ((PolygonShape) shape).setAsBox(TILE_LENGTH_PIXELS / 2 * scale, (TILE_LENGTH_PIXELS * 1.25f) * scale);
        fixtureDef.shape = shape;

        return fixtureDef;
    }

    private void fixPipe() {
        if (Controls.shootButton.isPressed() && isTouched) {
            fixTimer+=Gdx.graphics.getDeltaTime();

            // Player has been fixing the pipe for over 1s.
            if (fixTimer > 1 && dead) {
                timeAlive = MathUtils.random(60,180);
                aliveTimer = 0;
                dead = false;
                amountOfPipesAlive++;
                microbe.spawnMicrobes();
            }
        }

        if (!Controls.shootButton.isPressed()) {
            fixTimer = 0;
        }
    }
}


