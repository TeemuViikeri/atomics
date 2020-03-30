package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

class GameUtil {
    private double accumulator = 0;
    private static int room = 2;
    private final int TOP = 1, TOPLEFT = 2, TOPRIGHT = 3, BOTTOM = 4, BOTTOMLEFT = 5,
            BOTTOMRIGHT = 6;

    void drawBodies(Array<Body> bodies, SpriteBatch batch, Player player) {
        for (Body body: bodies) {
            if (body.getUserData().equals("dead")) {
                continue;
            }

            GameObject temp = (GameObject) body.getUserData();
            if (temp instanceof Bullet) {
                Bullet bulletTemp = (Bullet) temp;
                batch.draw(
                    bulletTemp.getTexture(),
                    body.getPosition().x - bulletTemp.getWidth() / 2,
                    body.getPosition().y - bulletTemp.getHeight() / 2,
                    bulletTemp.getWidth() / 2,
                    bulletTemp.getHeight() / 2,
                    bulletTemp.getWidth(),
                    bulletTemp.getHeight(),
                    1.0f,
                    1.0f,
                    body.getAngle() * MathUtils.radiansToDegrees,
                    0,
                    0,
                    bulletTemp.getTexture().getWidth(),
                    bulletTemp.getTexture().getHeight(),
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
            } else if (temp instanceof Player) {
                batch.draw(
                    player.getTexture(),
                    body.getPosition().x - player.getWidth() / 2,
                    body.getPosition().y - player.getHeight() / 2,
                    player.getWidth() / 2,
                    player.getHeight() / 2,
                    player.getWidth(),
                    player.getHeight(),
                    1.0f,
                    1.0f,
                    body.getAngle() * MathUtils.radiansToDegrees,
                    0,
                    0,
                    player.getTexture().getWidth(),
                    player.getTexture().getHeight(),
                    false,
                    false
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
            PlayScreen.world.step(TIME_STEP, 8, 3);
            accumulator -= TIME_STEP;
        }
    }

    void checkIfChangeRoom(Body playerBody, float position, float desiredAngle) {
        checkInWhatRoom(position);

        if (position >= PlayScreen.FIRST_SCREEN_RIGHT_SIDE && room == 1) {
           playerBody.setTransform(
                    PlayScreen.SECOND_SCREEN_LEFT_SPAWN_POINT,
                   playerBody.getPosition().y,
                    desiredAngle
            );
        } else if (position <= PlayScreen.SECOND_SCREEN_LEFT_SIDE && room == 2) {
           playerBody.setTransform(
                   PlayScreen.FIRST_SCREEN_SPAWN_POINT,
                   playerBody.getPosition().y,
                    desiredAngle
            );
        } else if (position >= PlayScreen.SECOND_SCREEN_RIGHT_SIDE && room == 2) {
           playerBody.setTransform(
                   PlayScreen.THIRD_SCREEN_SPAWN_POINT,
                   playerBody.getPosition().y,
                    desiredAngle
            );
        } else if (position <= PlayScreen.THIRD_SCREEN_LEFT_SIDE && room == 3) {
           playerBody.setTransform(
                   PlayScreen.SECOND_SCREEN_RIGHT_SPAWN_POINT,
                   playerBody.getPosition().y,
                    desiredAngle
            );
        }
    }

    private void checkInWhatRoom(float position) {
        if ( // Check if in the first room
                position <= PlayScreen.ROOM_WIDTH_PIXELS * PlayScreen.scale
        ) {
            room = 1;
        } else if ( // Check if in the second room
                position >= (PlayScreen.ROOM_WIDTH_PIXELS + PlayScreen.PIPE_HORIZONTAL_PIXELS) * PlayScreen.scale &&
                        position <= (PlayScreen.ROOM_WIDTH_PIXELS * 2 + PlayScreen.PIPE_HORIZONTAL_PIXELS) * PlayScreen.scale
        ) {
            room = 2;
        } else if ( // Check if in the third room
                position >= (PlayScreen.ROOM_WIDTH_PIXELS * 2 + PlayScreen.PIPE_HORIZONTAL_PIXELS * 2) * PlayScreen.scale
        ) {
            room = 3;
        }
    }

    void moveCamera(OrthographicCamera camera) {
        if (room == 1) {
            camera.position.x = PlayScreen.ROOM_WIDTH_PIXELS / 2 * PlayScreen.scale;
            camera.position.y = PlayScreen.WORLD_HEIGHT_PIXELS / 2 * PlayScreen.scale;
        } else if (room == 2) {
            camera.position.x = PlayScreen.WORLD_WIDTH_PIXELS / 2 * PlayScreen.scale;
            camera.position.y = PlayScreen.WORLD_HEIGHT_PIXELS / 2 * PlayScreen.scale;
        } else if (room == 3) {
            camera.position.x = (PlayScreen.WORLD_WIDTH_PIXELS - PlayScreen.ROOM_WIDTH_PIXELS / 2) * PlayScreen.scale;
            camera.position.y = PlayScreen.WORLD_HEIGHT_PIXELS / 2 * PlayScreen.scale;
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
            x = MathUtils.random((PlayScreen.ROOM_WIDTH_PIXELS + PlayScreen.PIPE_HORIZONTAL_PIXELS) * PlayScreen.scale,
                    (PlayScreen.ROOM_WIDTH_PIXELS * 2 + PlayScreen.PIPE_HORIZONTAL_PIXELS) * PlayScreen.scale - Phosphorus.width);
            y = PlayScreen.WORLD_HEIGHT_PIXELS * PlayScreen.scale + Phosphorus.width;

            return new Vector2(x, y);
        } else if (spawnside == TOPLEFT) {
            x = (PlayScreen.ROOM_WIDTH_PIXELS + PlayScreen.PIPE_HORIZONTAL_PIXELS) * PlayScreen.scale - Phosphorus.width;
            y = MathUtils.random(PlayScreen.WORLD_HEIGHT_PIXELS * 3/4 * PlayScreen.scale,
                    PlayScreen.WORLD_HEIGHT_PIXELS * PlayScreen.scale + Phosphorus.width);

            return new Vector2(x,y);
        } else if (spawnside == TOPRIGHT) {
            x = (PlayScreen.ROOM_WIDTH_PIXELS * 2 + PlayScreen.PIPE_HORIZONTAL_PIXELS) * PlayScreen.scale + Phosphorus.width;
            y = MathUtils.random(PlayScreen.WORLD_HEIGHT_PIXELS * 3/4 * PlayScreen.scale, PlayScreen.WORLD_HEIGHT_PIXELS * PlayScreen.scale);

            return new Vector2(x,y);
        } else if (spawnside == BOTTOM) {
            x = MathUtils.random((PlayScreen.ROOM_WIDTH_PIXELS + PlayScreen.PIPE_HORIZONTAL_PIXELS) * PlayScreen.scale
                            + Phosphorus.width,
                    (PlayScreen.ROOM_WIDTH_PIXELS * 2 + PlayScreen.PIPE_HORIZONTAL_PIXELS) * PlayScreen.scale - Phosphorus.width);
            y = - Phosphorus.width;

            return new Vector2(x,y);
        } else if (spawnside == BOTTOMLEFT) {
            x = (PlayScreen.ROOM_WIDTH_PIXELS + PlayScreen.PIPE_HORIZONTAL_PIXELS) * PlayScreen.scale - Phosphorus.width;
            y = MathUtils.random(-Phosphorus.width,
                    PlayScreen.WORLD_HEIGHT_PIXELS * 1/4 * PlayScreen.scale);

            return new Vector2(x,y);
        } else if (spawnside == BOTTOMRIGHT) {
            x = (PlayScreen.ROOM_WIDTH_PIXELS * 2 + PlayScreen.PIPE_HORIZONTAL_PIXELS) * PlayScreen.scale + Phosphorus.width;
            y = MathUtils.random(-Phosphorus.width, PlayScreen.WORLD_HEIGHT_PIXELS * 1/4 * PlayScreen.scale);

            return new Vector2(x,y);
        } else {
            throw new RuntimeException("Couldn't get a real phosphorus spawnpoint");
        }
    }
}
