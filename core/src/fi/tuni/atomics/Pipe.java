package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
import static fi.tuni.atomics.PlayScreen.tiledMap;

class Pipe extends GameObject {
    private TiledMapTileLayer tiledMapTileLayer =
            (TiledMapTileLayer) PlayScreen.tiledMap.getLayers().get("airpipes");
    private Texture animationSheet = new Texture("bubbleSequence.png");
    private Texture animationSheet2 = new Texture("bubble2.png");
    private Texture hammerAnimationSheet = new Texture("vasaraSequence-recolor.png");
    private Animation<TextureRegion> animation;
    private Animation<TextureRegion> animation2;
    private Animation<TextureRegion> hammerAnimation;
    private Vector2 spawnPoint;
    private float stateTime;
    private Array<Pipe> pipes = new Array<>();
    private boolean dead = false; // is the pipe dead.
    private float timeAlive;
    private float aliveTimer; // how long the pipe has been alive.
    private float fixTimer; // how long the pipe has been in repair.
    boolean isTouched = false; // is the pipe being touched.
    private Microbe microbe = new Microbe();
    private boolean isFixSoundPlaying = false;

        private Pipe(Vector2 position) {
        this.spawnPoint = position;
        width = 0.5f;
        stateTime = 1f;
        timeAlive = MathUtils.random(40, 120 - (40f/12f * (PlayScreen.levelMultiplier - 1)));
            int sheetRows = 2;
            int sheetCols = 4;
            TextureRegion[][] temp = TextureRegion.split(
                animationSheet,
                animationSheet.getWidth() / sheetCols,
                animationSheet.getHeight() / sheetRows);
        TextureRegion[][] temp2 = TextureRegion.split(
                animationSheet2,
                animationSheet2.getWidth() / sheetCols,
                animationSheet2.getHeight() / sheetRows);
        TextureRegion[][] hammerTemp = TextureRegion.split(
                hammerAnimationSheet,
                hammerAnimationSheet.getWidth() / 3,
                hammerAnimationSheet.getHeight());
            GameUtil gameUtil = new GameUtil();
            TextureRegion[] frames = gameUtil.to1d(temp, sheetRows, sheetCols);
        animation = new Animation<>(1 / 10f, frames);
            TextureRegion[] frames2 = gameUtil.to1d(temp2, sheetRows, sheetCols);
        animation2 = new Animation<>(1 / 10f, frames2);
            TextureRegion[] hammerFrames = gameUtil.to1d(hammerTemp, 1, 3);
        hammerAnimation = new Animation<>(1 / 10f, hammerFrames);
        createBody();
        microbe.spawnMicrobes();
    }

    Pipe() {
    }

    void createPipes() {
        Microbe.microbes.clear();
        pipes.add(new Pipe(new Vector2((ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS * 2 +
                TILE_LENGTH_PIXELS * 5) * scale, (TILE_LENGTH_PIXELS * 3) * scale)));
        pipes.add(new Pipe(new Vector2((ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS * 2 +
                TILE_LENGTH_PIXELS * 11) * scale, (TILE_LENGTH_PIXELS * 3) * scale)));
        pipes.add(new Pipe(new Vector2((ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS * 2 +
                TILE_LENGTH_PIXELS * 18) * scale, (TILE_LENGTH_PIXELS * 3) * scale)));
        pipes.add(new Pipe(new Vector2((ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS * 2 +
                TILE_LENGTH_PIXELS * 24) * scale, (TILE_LENGTH_PIXELS * 3) * scale)));
    }

    void update() {
        for (Pipe i : pipes) {
            i.aliveTimer += Gdx.graphics.getDeltaTime();
            float tileXPosition = i.spawnPoint.x / 0.32f;

            if (i.aliveTimer > i.timeAlive && !i.dead) {
                tiledMapTileLayer.getCell((int) tileXPosition, 0)
                        .setTile(tiledMap.getTileSets().getTile(24));
                tiledMapTileLayer.getCell((int) tileXPosition, 1)
                        .setTile(tiledMap.getTileSets().getTile(24));
                tiledMapTileLayer.getCell((int) tileXPosition, 2)
                        .setTile(tiledMap.getTileSets().getTile(16));
                i.dead = true;
                microbe.deSpawnMicrobes();

                if (!SettingsScreen.isMuted)
                    GameAudio.playPipeBrokenSound();
            }

            i.fixPipe((int)tileXPosition);
        }
    }

    private void fixPipe(int tileXPosition) {
        if (Controls.shootButton.isPressed() && isTouched) {
            fixTimer+=Gdx.graphics.getDeltaTime();

            if (!isFixSoundPlaying) {
                isFixSoundPlaying = true;

                if (!SettingsScreen.isMuted)
                    GameAudio.playFixSound();
            }

            // Player has been fixing the pipe for over 1s.
            if (fixTimer >= 1 && dead) {
                timeAlive = MathUtils.random(40, 120) - (40f/15f * (PlayScreen.levelMultiplier - 1));
                aliveTimer = 0;
                dead = false;
                microbe.spawnMicrobes();
                tiledMapTileLayer.getCell(tileXPosition, 0)
                        .setTile(tiledMap.getTileSets().getTile(13));
                tiledMapTileLayer.getCell(tileXPosition, 1)
                        .setTile(tiledMap.getTileSets().getTile(13));
                tiledMapTileLayer.getCell(tileXPosition, 2)
                        .setTile(tiledMap.getTileSets().getTile(5));

                if (!SettingsScreen.isMuted) {
                    GameAudio.playPipeFixedSound();
                    GameAudio.playMicrobeSpawnSound();
                    GameAudio.playFixSound.stop();
                }
            }
        }

        if (!Controls.shootButton.isPressed()) {
            fixTimer = 0;
            isFixSoundPlaying = false;

            if (!SettingsScreen.isMuted)
                GameAudio.playFixSound.stop();
        }
    }

    void draw(SpriteBatch batch) {
        for (Pipe i : pipes) {
            if (!i.dead) {
                batch.draw(i.getAnimation().getKeyFrame((i).setStateTime(), true),
                        i.spawnPoint.x, i.spawnPoint.y,0.32f,0.32f);
                batch.draw(i.getAnimation2().getKeyFrame((i).setStateTime(), true),
                        i.spawnPoint.x, i.spawnPoint.y + 0.32f,0.32f,0.32f);
            } else {
                if (i.fixTimer > 0 && i.isTouched) {
                    Atomics.batch.draw(i.getHammerAnimation().
                        getKeyFrame((i).setStateTime(),true),
                        i.spawnPoint.x - TILE_LENGTH_PIXELS / 3 * scale,
                        i.spawnPoint.y - TILE_LENGTH_PIXELS * 2 * scale,
                        0.5f, 0.5f);
                }
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

        ((PolygonShape) shape).setAsBox(TILE_LENGTH_PIXELS / 2 * scale,
                (TILE_LENGTH_PIXELS * 1.25f) * scale);
        fixtureDef.shape = shape;

        return fixtureDef;
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

    private Animation<TextureRegion> getHammerAnimation() {
        return hammerAnimation;
    }

    boolean isDead() {
        return dead;
    }

    public void dispose() {
        animationSheet.dispose();
        animationSheet2.dispose();
        hammerAnimationSheet.dispose();
        microbe.dispose();
    }
}


