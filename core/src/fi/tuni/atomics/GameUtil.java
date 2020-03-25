package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class GameUtil {
    private static double accumulator = 0;
    private static float TIME_STEP = 1 / 60f;
    private static int room = 2;

    static void doPhysicsStep(World world, float deltaTime) {

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

    static void checkIfChangeRoom(Body playerBody, float position, float desiredAngle) {
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

    private static void checkInWhatRoom(float position) {
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

    static void moveCamera(OrthographicCamera camera) {
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

    static void clearScreen(float r, float g, float b) {
        Gdx.gl.glClearColor(r, g, b, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
