package fi.tuni.atomics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    private Sprite sprite;
    private Texture texture;
    private float x;
    private float y;
    private float width;
    private float height;
    private float speed;

    Player(float x, float y) {
        // Texture area too big --> crop smaller
        texture = new Texture("subbikuva2.png");
        sprite = new Sprite(texture);

        setX(x);
        setY(y);

        sprite.setPosition(getX(), getY());
        sprite.setSize(1f, 1f);
        sprite.setOriginCenter();
    }

    void draw(SpriteBatch batch) {
        x = getX();
        y = getY();
        width = getWidth();
        height = getHeight();

        batch.draw(getTexture(), x, y, width, height);
    }

    // Getters and setters
    Texture getTexture() {
        return texture;
    }

    void setTexture(Texture texture) {
        this.texture = texture;
    }

    float getX() {
        return x;
    }

    void setX(float x) {
        this.x = x;
    }

    float getY() {
        return y;
    }

    void setY(float y) {
        this.y = y;
    }

    float getWidth() {
        return width;
    }

    void setWidth(float width) {
        this.width = width;
    }

    float getHeight() {
        return height;
    }

    void setHeight(float height) {
        this.height = height;
    }

    float getSpeed() {
        return speed;
    }

    void setSpeed(float speed) {
        this.speed = speed;
    }
}
