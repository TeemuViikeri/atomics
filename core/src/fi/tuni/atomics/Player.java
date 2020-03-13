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

        setSpeed(0.05f); // Check the right speed variable

        sprite.setPosition(x, y);
        sprite.setSize(0.75f, 0.75f);
        sprite.setOriginCenter();
        createSubmarine();
    }

    void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void rotateLeft() {
        if (degrees == 360) {
            degrees = 0;
        }
        sprite.setRotation(degrees+=250 * Gdx.graphics.getDeltaTime());
    }

    public void rotateRight() {
        if (degrees == -360) {
            degrees = 0;
        }
        sprite.setRotation(degrees-=250 * Gdx.graphics.getDeltaTime());
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

    public void createSubmarine() {

        subBodyDef = new BodyDef();
        subBodyDef.type = BodyDef.BodyType.DynamicBody;

        subBodyDef.position.set(Game.WORLD_WIDTH_PIXELS / 2 * Game.scale,
                Game.WORLD_HEIGHT_PIXELS / 2 * Game.scale);

        playerFixtureDef = new FixtureDef();

        playerFixtureDef.density     = 2;
        playerFixtureDef.restitution = 0.5f;
        playerFixtureDef.friction    = 0.5f;

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
