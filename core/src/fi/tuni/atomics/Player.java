package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Player {
    static Sprite sprite;
    private Texture texture;
    private float speed;
    private float degrees = 0;
    private Body body;
    private BodyDef subBodyDef;
    private FixtureDef playerFixtureDef;

    Player(float x, float y) {
        texture = new Texture("cropped-subbi.png");
        sprite = new Sprite(texture);

        setSpeed(0); // Check the right speed variable

        sprite.setPosition(x, y);
        sprite.setSize(0.50f, 0.25f);
        sprite.setOriginCenter();
        createSubmarine();
    }

    public void createSubmarine() {
        subBodyDef = new BodyDef();
        subBodyDef.type = BodyDef.BodyType.DynamicBody;

        subBodyDef.position.set(Game.WORLD_WIDTH_PIXELS / 2 * Game.scale,
                Game.WORLD_HEIGHT_PIXELS / 2 * Game.scale);

        playerFixtureDef = new FixtureDef();

        playerFixtureDef.filter.groupIndex = -1;
        //playerFixtureDef.density     = 1f;
        //playerFixtureDef.restitution = 0;
        //playerFixtureDef.friction    = 0;

        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(0.25f, 0.125f);
        playerFixtureDef.shape = polygon;
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
