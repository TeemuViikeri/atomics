package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;

import static fi.tuni.atomics.Game.PIPE_HORIZONTAL_PIXELS;
import static fi.tuni.atomics.Game.ROOM_WIDTH_PIXELS;
import static fi.tuni.atomics.Game.WORLD_HEIGHT_PIXELS;
import static fi.tuni.atomics.Game.scale;

class GameUtil {
    private double accumulator = 0;
    private int room = 2;
    private final int TOP = 1, LEFT = 2, RIGHT = 3;

    void drawBodies(Array<Body> bodies, SpriteBatch batch, Bullet bullet) {
        for (Body body: bodies) {
            if (body.getUserData().equals("dead")) {
                continue;
            }
            GameObject temp = (GameObject) body.getUserData();
            Shape.Type tempShape = temp.getFixtureDef().shape.getType();
            if (tempShape == Shape.Type.Circle) {
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
                    body.getAngle() * MathUtils.radiansToDegrees,
                    0,
                    0,
                    bullet.getTexture().getWidth(),
                    bullet.getTexture().getHeight(),
                    false,
                    false
                );
            } else if (temp instanceof Phosphorus) {
                batch.draw(((Phosphorus) temp)
                                .getAnimation()
                                .getKeyFrame(((Phosphorus)temp)
                                .setStateTime(), true),
                        body.getPosition().x - 0.25f,
                        body.getPosition().y - 0.25f,
                        0.5f,
                        0.5f
                );
            }
        }
    }

    void doPhysicsStep(float deltaTime) {
        float frameTime = deltaTime;

        if (deltaTime > 1 / 4f) {
            frameTime = 1 / 4f;
        }

        accumulator += frameTime;

        float TIME_STEP = 1 / 60f;
        while (accumulator >= TIME_STEP) {
            Game.world.step(TIME_STEP, 8, 3);
            accumulator -= TIME_STEP;
        }
    }

    void checkIfChangeRoom(Body playerBody, float position, float desiredAngle) {
        checkInWhatRoom(position);

        if (position >= Game.FIRST_SCREEN_RIGHT_SIDE && room == 1) {
           playerBody.setTransform(
                    Game.SECOND_SCREEN_LEFT_SPAWN_POINT,
                   playerBody.getPosition().y,
                    desiredAngle
            );
        } else if (position <= Game.SECOND_SCREEN_LEFT_SIDE && room == 2) {
           playerBody.setTransform(
                   Game.FIRST_SCREEN_SPAWN_POINT,
                   playerBody.getPosition().y,
                    desiredAngle
            );
        } else if (position >= Game.SECOND_SCREEN_RIGHT_SIDE && room == 2) {
           playerBody.setTransform(
                   Game.THIRD_SCREEN_SPAWN_POINT,
                   playerBody.getPosition().y,
                    desiredAngle
            );
        } else if (position <= Game.THIRD_SCREEN_LEFT_SIDE && room == 3) {
           playerBody.setTransform(
                   Game.SECOND_SCREEN_RIGHT_SPAWN_POINT,
                   playerBody.getPosition().y,
                    desiredAngle
            );
        }
    }

    private void checkInWhatRoom(float position) {
        if ( // Check if in the first room
                position <= Game.ROOM_WIDTH_PIXELS * Game.scale
        ) {
            room = 1;
        } else if ( // Check if in the second room
                position >= (Game.ROOM_WIDTH_PIXELS + Game.PIPE_HORIZONTAL_PIXELS) * Game.scale &&
                        position <= (Game.ROOM_WIDTH_PIXELS * 2 + Game.PIPE_HORIZONTAL_PIXELS) * Game.scale
        ) {
            room = 2;
        } else if ( // Check if in the third room
                position >= (Game.ROOM_WIDTH_PIXELS * 2 + Game.PIPE_HORIZONTAL_PIXELS * 2) * Game.scale
        ) {
            room = 3;
        }
    }

    void moveCamera(OrthographicCamera camera) {
        if (room == 1) {
            camera.position.x = Game.ROOM_WIDTH_PIXELS / 2 * Game.scale;
            camera.position.y = Game.WORLD_HEIGHT_PIXELS / 2 * Game.scale;
        } else if (room == 2) {
            camera.position.x = Game.WORLD_WIDTH_PIXELS / 2 * Game.scale;
            camera.position.y = Game.WORLD_HEIGHT_PIXELS / 2 * Game.scale;
        } else if (room == 3) {
            camera.position.x = (Game.WORLD_WIDTH_PIXELS - Game.ROOM_WIDTH_PIXELS / 2) * Game.scale;
            camera.position.y = Game.WORLD_HEIGHT_PIXELS / 2 * Game.scale;
        }

        camera.update();
    }

    void clearScreen() {
        Gdx.gl.glClearColor((float) 0.38039216, (float) 0.5254902, (float) 0.41568628, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    Vector2 getSpawnPoint(int spawnside) {
        float y;
        float x;

        if (spawnside == TOP) {
            y = WORLD_HEIGHT_PIXELS * scale + Phosphorus.width;
            x = MathUtils.random((ROOM_WIDTH_PIXELS + PIPE_HORIZONTAL_PIXELS) * scale,
                    (ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS) * scale - Phosphorus.width);

            return new Vector2(x, y);
        } else if (spawnside == LEFT) {
            x = (ROOM_WIDTH_PIXELS + PIPE_HORIZONTAL_PIXELS) * scale - Phosphorus.width;
            y = MathUtils.random(WORLD_HEIGHT_PIXELS * 3/4 * scale,
                    WORLD_HEIGHT_PIXELS * scale + Phosphorus.width);

            return new Vector2(x,y);
        } else if (spawnside == RIGHT) {
            x = (ROOM_WIDTH_PIXELS * 2 + PIPE_HORIZONTAL_PIXELS) * scale + Phosphorus.width;
            y = MathUtils.random(WORLD_HEIGHT_PIXELS * 3/4 * scale, WORLD_HEIGHT_PIXELS * scale);

            return new Vector2(x,y);
        } else {
            throw new RuntimeException("Couldn't get a real phosphorus spawnpoint");
        }
    }
}
