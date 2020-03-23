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

public class Player {
    static Sprite sprite;
    private Texture texture;
    private float speed;
    private float degrees = 0;
    private Body body;
    private BodyDef subBodyDef;
    private FixtureDef playerFixtureDef;

    Player(World world, float x, float y) {
        texture = new Texture("cropped-subbi.png");
        sprite = new Sprite(texture);

        setSpeed(0); // Check the right speed variable

        sprite.setPosition(x, y);
        sprite.setSize(0.50f, 0.25f);
        sprite.setOriginCenter();
        createSubmarineBody(world);
    }

//    public void createSubmarine() {
//        subBodyDef = new BodyDef();
//        subBodyDef.type = BodyDef.BodyType.DynamicBody;
//
//        subBodyDef.position.set(Game.WORLD_WIDTH_PIXELS / 2 * Game.scale,
//                Game.WORLD_HEIGHT_PIXELS / 2 * Game.scale);
//
//        playerFixtureDef = new FixtureDef();
//
//        playerFixtureDef.filter.groupIndex = -1;
//        //playerFixtureDef.density     = 2f;
//        //playerFixtureDef.restitution = 1f;
//        //playerFixtureDef.friction    = 0;
//
//        PolygonShape polygon = new PolygonShape();
//        polygon.setAsBox(0.25f, 0.125f);
//        playerFixtureDef.shape = polygon;
//    }

    public void createSubmarineBody(World world) {
        body = world.createBody(getSubmarineBodyDef());
        body.createFixture(getSubmarineFixtureDef());
        body.setUserData(this);
    }

    private BodyDef getSubmarineBodyDef() {
        subBodyDef = new BodyDef();
        subBodyDef.type = BodyDef.BodyType.DynamicBody;

        subBodyDef.position.set(Game.WORLD_WIDTH_PIXELS / 2 * Game.scale,
                Game.WORLD_HEIGHT_PIXELS / 2 * Game.scale);

        return subBodyDef;
    }

    private FixtureDef getSubmarineFixtureDef() {
        playerFixtureDef = new FixtureDef();

        playerFixtureDef.filter.groupIndex = -1;
        //playerFixtureDef.density     = 2f;
        //playerFixtureDef.restitution = 1f;
        //playerFixtureDef.friction    = 0;

        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(0.25f, 0.125f);
        playerFixtureDef.shape = polygon;

        return playerFixtureDef;
    }

    void draw(SpriteBatch batch, Body body) {
       sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2f, body.getPosition().y - sprite.getHeight() / 2f);
       sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
       sprite.draw(batch);
    }

    // Getters and setters
    public float getDegrees() {
        return degrees;
    }

    public Body getBody() {
        return body;
    }

    void setDegrees(float degrees) {
        this.degrees = degrees;
    }

    Texture getTexture() {
        return texture;
    }

    void setTexture(Texture texture) {
        this.texture = texture;
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

    public FixtureDef getFixture() {
        return playerFixtureDef;
    }

    public BodyDef getBodyDef() {
        return subBodyDef;
    }
}
