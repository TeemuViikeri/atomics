package fi.tuni.atomics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    private Sprite sprite;
    private Texture texture;
    private float speed;

    Player(float x, float y) {
        // Texture area too big --> crop smaller
        texture = new Texture("subbikuva2.png");
        sprite = new Sprite(texture);
        speed = 5.0f; // Check the right speed variable

        sprite.setPosition(x, y);
        sprite.setSize(1f, 1f);
        sprite.setOriginCenter();
    }

    void draw(SpriteBatch batch) {
        batch.draw(
                getTexture(),
                sprite.getX(),
                sprite.getY(),
                sprite.getWidth(),
                sprite.getHeight());
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
}
