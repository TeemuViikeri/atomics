package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    static final float width = 0.5f;
    static private Texture animationSheet = new Texture("bubbleSequence.png");
    private Animation<TextureRegion> animation;
    private Vector2 spawnPoint;
    private float stateTime;
    private GameUtil gameUtil = new GameUtil();
    private Array<Pipe> pipes = new Array<>();
    private boolean dead = false; // is the pipe dead.
    private float aliveTimer; // how long the pipe has been alive.
    private float fixTimer; // how long the pipe has been in repair.
    public boolean isTouched = false; // is the pipe being touched.

    private Pipe(Vector2 position) {
        this.spawnPoint = position;
        stateTime = 1f;
        TextureRegion[] frames;
        TextureRegion[][] temp = TextureRegion.split(
                animationSheet,
                animationSheet.getWidth() / sheetCols,
                animationSheet.getHeight() / sheetRows);
        frames = gameUtil.to1d(temp, sheetRows, sheetCols);
        animation = new Animation<>(1 / 10f, frames);
        createBody();
    }

    Pipe() {
    }

    private float setStateTime() {
        return stateTime += Gdx.graphics.getDeltaTime();
    }

    private Animation<TextureRegion> getAnimation() {
        return animation;
    }

    void createPipes() {
        pipes.add(new Pipe(new Vector2((ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS * 2 + TILE_LENGTH_PIXELS * 5) * scale, (TILE_LENGTH_PIXELS * 3) * scale)));
        pipes.add(new Pipe(new Vector2((ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS * 2 + TILE_LENGTH_PIXELS * 11) * scale, (TILE_LENGTH_PIXELS * 3) * scale)));
        pipes.add(new Pipe(new Vector2((ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS * 2 + TILE_LENGTH_PIXELS * 18) * scale, (TILE_LENGTH_PIXELS * 3) * scale)));
        pipes.add(new Pipe(new Vector2((ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS * 2 + TILE_LENGTH_PIXELS * 24) * scale, (TILE_LENGTH_PIXELS * 3) * scale)));
    }

    void update() {
        for (Pipe i : pipes) {
            i.aliveTimer += Gdx.graphics.getDeltaTime();

            // if pipe has been alive over 10s
            if (i.aliveTimer > 10) {
                i.dead = true;
            }

            i.fixPipe();
        }
    }

    void draw(SpriteBatch batch) {
        for (Pipe i : pipes) {
            if (!i.dead) {
                batch.draw(i.getAnimation().getKeyFrame((i).setStateTime(), true),
                        i.spawnPoint.x, i.spawnPoint.y,0.32f,0.32f);
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
        System.out.println(Controls.shootButton.isPressed());
        System.out.println(isTouched);
        if (Controls.shootButton.isPressed() && isTouched) {
            fixTimer+=Gdx.graphics.getDeltaTime();

            // Player has been fixing the pipe for over 1s.
            if (fixTimer > 1) {
                aliveTimer = 0;
                dead = false;
            }
        }

        if (!Controls.shootButton.isPressed()) {
            fixTimer = 0;
        }
    }
}


