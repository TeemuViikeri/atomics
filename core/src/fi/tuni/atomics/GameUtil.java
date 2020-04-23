package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
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
            if (body.getUserData().equals("dead") || body.getUserData().equals("microbe")) {
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
            } else if (temp instanceof CollectablePhosphorus) {
                clearCollectables(temp, 5);
                batch.draw(((CollectablePhosphorus) temp)
                        .getAnimation()
                        .getKeyFrame(((CollectablePhosphorus)temp)
                                .setStateTime(), true),
                body.getPosition().x - 0.25f,
                body.getPosition().y - 0.25f,
                0.35f,
                0.35f
                );
            } else if (temp instanceof Microbe) {
                scaleObjects(temp, 0.005f);
                batch.draw(
                    ((Microbe) temp)
                        .getAnimation()
                        .getKeyFrame(((Microbe)temp)
                        .setStateTime(), true),
                    body.getPosition().x - 0.25f,
                    body.getPosition().y - 0.25f,
                    temp.getWidth(),
                    temp.getWidth());
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
            } else if (temp instanceof Item) {
                scaleObjects(temp, 0.0075f);
                batch.draw(
                    temp.getTexture(),
                    body.getPosition().x - temp.getWidth() / 2,
                    body.getPosition().y - temp.getHeight() / 2,
                    temp.getWidth() / 2,
                    temp.getHeight() / 2,
                    temp.getWidth(),
                    temp.getHeight(),
                    1.0f,
                    1.0f,
                    body.getAngle() * MathUtils.radiansToDegrees,
                    0,
                    0,
                    temp.getTexture().getWidth(),
                    temp.getTexture().getHeight(),
                    false,
                    false
                );
            } else if (temp instanceof Nitrogen) {
                batch.draw(
                    temp.getTexture(),
                    body.getPosition().x - temp.getWidth() / 2,
                    body.getPosition().y - temp.getHeight() / 2,
                    temp.getWidth() / 2,
                    temp.getHeight() / 2,
                    temp.getWidth(),
                    temp.getHeight(),
                    1.0f,
                    1.0f,
                    body.getAngle() * MathUtils.radiansToDegrees,
                    0,
                    0,
                    temp.getTexture().getWidth(),
                    temp.getTexture().getHeight(),
                    false,
                    false
                );
            }
        }
    }

    private void scaleObjects(GameObject gameObject, float scaleUp) {
        if (gameObject.width < gameObject.getTargetWidth()) {
            gameObject.width += scaleUp;
            gameObject.height += scaleUp * (gameObject.height / gameObject.width);
        }

        if (gameObject.width >= gameObject.getTargetWidth()) {
            gameObject.body.getFixtureList().get(0).setSensor(false);
        }
    }

    int getItemCount() {
        int count = 0;

        for (Body body: PlayScreen.bodies) {
            if (body.getUserData() instanceof Item) count++;
        }

        return count;
    }

    private void clearCollectables(GameObject collectable, float timer) {
        if (collectable.timeAlive >= timer) {
            collectable.body.setUserData("dead");
        } else {
            collectable.timeAlive += Gdx.graphics.getDeltaTime();
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

    TextureRegion[] to1d(TextureRegion[][] temp, int sheetRows, int sheetCols, Object object) {
        int index = 0;
        TextureRegion[] temporary = new TextureRegion[sheetRows * sheetCols];
        for (int i = 0; i < sheetRows; i++) {
            for (int j = 0; j < sheetCols; j++) {
                temporary[index++] = temp[i][j];
            }
        }
        return temporary;
    }

    void checkIfChangeRoom(Body playerBody, float position, float desiredAngle) {
        checkInWhatRoom(position);

        if (position >= PlayScreen.FIRST_SCREEN_RIGHT_SIDE && room == 1) {
           playerBody.setTransform(
                    PlayScreen.SECOND_SCREEN_LEFT_SPAWN_POINT,
                   playerBody.getPosition().y,
                    desiredAngle
           );
           Player.immortal = true;
        } else if (position <= PlayScreen.SECOND_SCREEN_LEFT_SIDE && room == 2) {
           playerBody.setTransform(
                   PlayScreen.FIRST_SCREEN_SPAWN_POINT,
                   playerBody.getPosition().y,
                    desiredAngle
           );
           Player.immortal = true;
        } else if (position >= PlayScreen.SECOND_SCREEN_RIGHT_SIDE && room == 2) {
           playerBody.setTransform(
                   PlayScreen.THIRD_SCREEN_SPAWN_POINT,
                   playerBody.getPosition().y,
                    desiredAngle
           );
           Player.immortal = true;
        } else if (position <= PlayScreen.THIRD_SCREEN_LEFT_SIDE && room == 3) {
           playerBody.setTransform(
                   PlayScreen.SECOND_SCREEN_RIGHT_SPAWN_POINT,
                   playerBody.getPosition().y,
                    desiredAngle
           );
           Player.immortal = true;
        }
    }

    void checkInWhatRoom(float position) {
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

    int getRoom() {
        return room;
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
        Gdx.gl.glClearColor((float) 0.38039216, (float) 0.5254902, (float) 0.41568628, 0f);
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
