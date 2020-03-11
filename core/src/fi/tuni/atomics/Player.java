package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    private Sprite sprite;
    private Texture texture;
    private float speed;
    private float degrees = 0;

    Player(float x, float y) {
        // Texture area too big --> crop smaller
        texture = new Texture("subbikuva2.png");
        sprite = new Sprite(texture);

        setSpeed(0.05f); // Check the right speed variable

        sprite.setPosition(x, y);
        sprite.setSize(0.75f, 0.75f);
        sprite.setOriginCenter();
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
}
