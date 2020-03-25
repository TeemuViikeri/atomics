package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

class Player {
    private Sprite sprite;
    private Body body;
    private Controls controls;
    private float speed;
    private float desiredAngle;
    private float deltaX;
    private float deltaY;
    private boolean moving;

    Player(World world, float x, float y) {
        Texture texture = new Texture("cropped-subbi.png");
        sprite = new Sprite(texture);
        controls = new Controls();

        setSpeed(0); // Check the right speed variable

        sprite.setPosition(x, y);
        sprite.setSize(0.50f, 0.25f);
        sprite.setOriginCenter();
        createSubmarineBody(world);
        controls.createButtons(world, this);
    }

    // Body creation
    private void createSubmarineBody(World world) {
        body = world.createBody(getSubmarineBodyDef());
        body.createFixture(getSubmarineFixtureDef());
        body.setUserData(this);
    }

    private BodyDef getSubmarineBodyDef() {
        BodyDef subBodyDef = new BodyDef();
        subBodyDef.type = BodyDef.BodyType.DynamicBody;

        subBodyDef.position.set(Game.WORLD_WIDTH_PIXELS / 2 * Game.scale,
                Game.WORLD_HEIGHT_PIXELS / 2 * Game.scale);

        return subBodyDef;
    }

    private FixtureDef getSubmarineFixtureDef() {
        FixtureDef playerFixtureDef = new FixtureDef();

        playerFixtureDef.filter.groupIndex = -1;
        //playerFixtureDef.density     = 2f;
        //playerFixtureDef.restitution = 1f;
        //playerFixtureDef.friction    = 0;

        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(0.25f, 0.125f);
        playerFixtureDef.shape = polygon;

        return playerFixtureDef;
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
        float maxSpeed = 150f;

        if (controls.getTouchpad().isTouched()) {
            submarineRotation();
        }

        if (controls.getSpeedButton().isPressed()) {
            moving = true;
            setSpeed(maxSpeed);
        } else {
            moving = false;
        }
    }

    private void submarineRotation() {
        System.out.println("deltaX: " + deltaX + ", deltaY: " + deltaY);

        desiredAngle = (float) Math.atan2( -deltaX, deltaY) + (float) Math.toRadians(90);
        float totalRotation = desiredAngle - body.getAngle();
        // Finds the shortest route
        while (totalRotation < -180 * MathUtils.degreesToRadians)
            totalRotation += 360 * MathUtils.degreesToRadians;
        while (totalRotation > 90 * MathUtils.degreesToRadians)
            totalRotation -= 360 * MathUtils.degreesToRadians;
        // maximum rotation per render
        float maxRotation = 1000 * MathUtils.degreesToRadians * Gdx.graphics.getDeltaTime();
        float newAngle = body.getAngle()
                + Math.min(maxRotation, Math.max(-maxRotation, totalRotation));
        body.setTransform(body.getPosition(), newAngle);
    }

    void fireBullet(World world) {
        Bullet bulletObj = new Bullet(
                world,
                body,
                body.getAngle(),
                body.getPosition().x,
                body.getPosition().y
        );

        Vector2 force = new Vector2((float) Math.cos(bulletObj.getBody().getAngle())
                * bulletObj.getSpeed() * Gdx.graphics.getDeltaTime(),
                (float) Math.sin(bulletObj.getBody().getAngle())
                        * bulletObj.getSpeed() * Gdx.graphics.getDeltaTime());


        Body bulletBody = bulletObj.getBody();

        bulletBody.applyLinearImpulse(
                force,
                bulletBody.getWorldCenter(),
                true
        );
    }

    // Draw methods
    void draw(SpriteBatch batch, Body body) {
       sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2f, body.getPosition().y - sprite.getHeight() / 2f);
       sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
       sprite.draw(batch);
    }

     void drawBullets(Array<Body> bodies, SpriteBatch batch, Bullet bullet) {
        for (Body body: bodies) {
            Object temp = body.getUserData();
            if (temp instanceof Bullet) {
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
            }
        }
    }

    // Getters and setters
    Body getBody() {
        return body;
    }

    private void setSpeed(float speed) {
        this.speed = speed;
    }

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
}
