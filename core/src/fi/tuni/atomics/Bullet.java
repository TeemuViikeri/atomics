package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {
    private Sprite sprite;
    private Texture texture;
    private float speed;
    private float degrees;

    Bullet(float degrees, float x, float y) {
        // Texture area too big --> crop smaller
        texture = new Texture("subbikuva2.png");
        sprite = new Sprite(texture);
        speed = 0.05f; // Check the right speed variable
        this.degrees = degrees;

        sprite.setPosition(x, y);
        sprite.setSize(0.75f, 0.75f);
        sprite.setOriginCenter();
        sprite.setRotation(degrees);
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
