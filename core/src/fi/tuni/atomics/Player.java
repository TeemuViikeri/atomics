package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Stage;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

class Player extends GameObject {
    private static int hitpoints;
    static boolean immortal = false;
    private Controls controls;
    private float desiredAngle;
    private float deltaX;
    private float deltaY;
    private boolean moving;
    private float immortalTimer;
    private Texture hp3;
    private Texture hp2;
    private Texture hp1;
    private float shootingTimer = 0;
    static boolean playerLostHitPoint;
    private GameUtil gameUtil;

    Player(float x, float y) {
        texture = new Texture("highResSubbi.png");
        hp3 = new Texture("hp3.png");
        hp2 = new Texture("hp2.png");
        hp1 = new Texture("hp1.png");
        gameUtil = new GameUtil();
        controls = new Controls();
        width = 0.5f;
        height = 0.25f;
        speed = 0;
        hitpoints = 3;
        createSubmarineBody();
        controls.createButtons(this);
    }

    // Body creation
    private void createSubmarineBody() {
        body = PlayScreen.world.createBody(getSubmarineBodyDef());
        body.setGravityScale(0);
        body.createFixture(getSubmarineFixtureDef());
        body.setUserData(this);
    }

    private BodyDef getSubmarineBodyDef() {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(PlayScreen.WORLD_WIDTH_PIXELS / 2 * PlayScreen.scale,
                PlayScreen.WORLD_HEIGHT_PIXELS / 2 * PlayScreen.scale);

        return bodyDef;
    }

    private FixtureDef getSubmarineFixtureDef() {
        fixtureDef = new FixtureDef();

        fixtureDef.filter.groupIndex = -1;
        //fixtureDef.density     = 2f;
        //fixtureDef.restitution = 1f;
        //fixtureDef.friction    = 0;

        shape = new PolygonShape();
        ((PolygonShape) shape).setAsBox(0.25f, 0.125f);
        fixtureDef.shape = shape;

        return fixtureDef;
    }

    // Inputs
    void submarineMove() {
        float speedDecrement = 3f;

        checkInput();

        if (moving) {
            Vector2 force = new Vector2((float) Math.cos(body.getAngle())
                * speed * Gdx.graphics.getDeltaTime(),
                (float) Math.sin(body.getAngle())
                        * speed * Gdx.graphics.getDeltaTime());

            body.setLinearVelocity(force);
            System.out.println((float) Math.cos(body.getAngle())
                    * speed * Gdx.graphics.getDeltaTime());
        }

        if (!moving && speed >= speedDecrement) {
            setSpeed(speed - speedDecrement);
            Vector2 force = new Vector2((float) Math.cos(body.getAngle())
                * speed * Gdx.graphics.getDeltaTime(),
                (float) Math.sin(body.getAngle())
                        * speed * Gdx.graphics.getDeltaTime());

            body.setLinearVelocity(force);
        } else if (speed < speedDecrement) {
            setSpeed(0);
            Vector2 force = new Vector2((float) Math.cos(body.getAngle())
                * speed * Gdx.graphics.getDeltaTime(),
                (float) Math.sin(body.getAngle())
                    * speed * Gdx.graphics.getDeltaTime());

            body.setLinearVelocity(force);
        }
    }

    private void checkInput() {
        shootingTimer+=Gdx.graphics.getDeltaTime();
        float maxSpeed = 150f;
        if (controls.getShootButton().isPressed() && shootingTimer >= 0.2f &&
                gameUtil.getRoom() == 2) {
            fireBullet();
            shootingTimer = 0;
        }

        if (controls.getTouchpad().isTouched()) {
            submarineRotation();
        }

        if (controls.getSpeedButton().isPressed()) {
            moving = true;
            setSpeed(maxSpeed);
        } else {
            moving = false;
        }

        controls.setButtonStyle(controls.getShootButton());
    }

    private void submarineRotation() {
        desiredAngle = (float) Math.atan2( -deltaX, deltaY) + (float) Math.toRadians(90);
        float totalRotation = desiredAngle - body.getAngle();
        // Finds the shortest route
        while (totalRotation < -180 * MathUtils.degreesToRadians)
            totalRotation += 360 * MathUtils.degreesToRadians;
        while (totalRotation > 180 * MathUtils.degreesToRadians)
            totalRotation -= 360 * MathUtils.degreesToRadians;
        // maximum rotation per render
        float maxRotation = 1000 * MathUtils.degreesToRadians * Gdx.graphics.getDeltaTime();
        float newAngle = body.getAngle()
                + Math.min(maxRotation, Math.max(-maxRotation, totalRotation));
        body.setTransform(body.getPosition(), newAngle);
    }

    private void fireBullet() {
        Bullet bulletObj = new Bullet(body);

        Vector2 force = new Vector2(
            (float) Math.cos(bulletObj.getBody().getAngle())
            * bulletObj.getSpeed(),
            (float) Math.sin(bulletObj.getBody().getAngle())
                    * bulletObj.getSpeed());

        Body bulletBody = bulletObj.getBody();

        bulletBody.applyLinearImpulse(
            force,
            bulletBody.getWorldCenter(),
            true
        );

        GameAudio.playShootingSound();
    }

    static void loseHitpoint() {
        hitpoints--;
        playerLostHitPoint = true;
    }

    void drawHitpoints(SpriteBatch batch) {
        if (hitpoints == 3) {
            batch.draw(hp3, (float) Gdx.graphics.getWidth() * 3/4,
                    PlayScreen.HUD_Y,
                    (float) hp3.getWidth() * Gdx.graphics.getWidth() / 960,
                    (float) hp3.getHeight() * Gdx.graphics.getHeight() / 640);
        } else if (hitpoints == 2) {
            batch.draw(hp2, (float) Gdx.graphics.getWidth() * 3/4,
                    PlayScreen.HUD_Y,
                    (float) hp3.getWidth() * Gdx.graphics.getWidth() / 960,
                    (float) hp3.getHeight() * Gdx.graphics.getHeight() / 640);
        } else if (hitpoints == 1) {
            batch.draw(hp1, (float) Gdx.graphics.getWidth() * 3/4,
                    PlayScreen.HUD_Y,
                    (float) hp3.getWidth() * Gdx.graphics.getWidth() / 960,
                    (float) hp3.getHeight() * Gdx.graphics.getHeight() / 640);
        }
    }

    void update() {
        if (immortal) {
            immortalTimer+=Gdx.graphics.getDeltaTime();
        }
        if (immortalTimer >= 1) {
            immortalTimer = 0;
            immortal = false;
        }
    }

    boolean checkIfDead() {
        return hitpoints <= 0;
    }

    // Getters and setters
    float getDesiredAngle() {
        return desiredAngle;
    }

    Controls getControls() {
        return controls;
    }

    void setDeltaX(float deltaX) {
        this.deltaX = deltaX;
    }

    void setDeltaY(float deltaY) {
        this.deltaY = deltaY;
    }

    float getDeltaX() {
        return deltaX;
    }

    float getDeltaY() {
        return deltaY;
    }
}
