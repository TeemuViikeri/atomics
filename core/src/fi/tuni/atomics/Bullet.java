package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet {
    private Body body;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;
    private Sprite sprite;
    private static Texture texture = new Texture("bullet.png");
    private float speed;
    private float degrees;

    Bullet(World world, Body playerBody, float degrees, float x, float y) {
        texture = new Texture("bullet.png");
        sprite = new Sprite(texture);
        speed = 3f; // Check the right speed variable
        this.degrees = degrees;
        createBulletBody(world, playerBody);
    }

    Bullet(World world) {
        createSimpleBulletBody(world);
    }

    private void createBulletBody(World world, Body playerBody) {
        body = world.createBody(getDefinitionOfBody(playerBody));
        body.createFixture(getFixtureDefinition());
        body.setUserData(this);
    }

    private void createSimpleBulletBody(World world) {
        body = world.createBody(getSimpleDefinitionOfBody());
        body.createFixture(getFixtureDefinition());
        body.setUserData("simple bullet");
    }

    private BodyDef getDefinitionOfBody(Body playerBody) {
        bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(playerBody.getWorldCenter());
        bodyDef.angle = playerBody.getAngle();

        return bodyDef;
    }

    private BodyDef getSimpleDefinitionOfBody() {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        return bodyDef;
    }

    private FixtureDef getFixtureDefinition() {
        fixtureDef = new FixtureDef();

        fixtureDef.filter.groupIndex = -1;
        // Mass per square meter (kg^m2)
        fixtureDef.density = 1f;
        // How bouncy object? Very bouncy [0,1]
        fixtureDef.restitution = 0f;
        // How slipper object? [0,1]
        fixtureDef.friction = 0f;

        CircleShape circleshape = new CircleShape();

        circleshape.setRadius(0.05f);
        fixtureDef.shape = circleshape;

        return fixtureDef;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public float getDegrees() {
        return degrees;
    }

    // Getters and setters
    Texture getTexture() {
        return texture;
    }

    float getSpeed() {
        return speed;
    }

    void setSpeed(float speed) {
        this.speed = speed;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Body getBody() { return body; }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public FixtureDef getFixture() {
        return fixtureDef;
    }
}