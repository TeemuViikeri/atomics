package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Player {
    private Sprite sprite;
    private Texture texture;
    private float speed;
    private float degrees = 0;
    private FixtureDef playerFixtureDef;
    private BodyDef subBodyDef;

    Player(float x, float y) {
        // Texture area too big --> crop smaller
        texture = new Texture("subbikuva2.png");
        sprite = new Sprite(texture);

        setSpeed(2f); // Check the right speed variable

        sprite.setPosition(x, y);
        sprite.setSize(0.75f, 0.75f);
        sprite.setOriginCenter();
        createSubmarine();
    }

    void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    // Getters and setters
    public float getDegrees() {
        return degrees;
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
        return speed * Gdx.graphics.getDeltaTime();
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

    public void createSubmarine() {

        subBodyDef = new BodyDef();
        subBodyDef.type = BodyDef.BodyType.DynamicBody;

        subBodyDef.position.set(Game.WORLD_WIDTH_PIXELS / 2 * Game.scale + 0.375f,
                Game.WORLD_HEIGHT_PIXELS / 2 * Game.scale + 0.375f);

        playerFixtureDef = new FixtureDef();

        playerFixtureDef.density     = 0;
        playerFixtureDef.restitution = 0;
        playerFixtureDef.friction    = 0;

        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(0.25f, 0.125f);
        playerFixtureDef.shape = polygon;

    }

    public FixtureDef getFixture() {
        return playerFixtureDef;
    }

    public BodyDef getBodyDef() {
        return subBodyDef;
    }
}
