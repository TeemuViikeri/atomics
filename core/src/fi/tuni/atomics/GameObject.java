package fi.tuni.atomics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

abstract class GameObject {
    Body body;
    BodyDef bodyDef;
    FixtureDef fixtureDef;
    Shape shape;
    Texture texture;
    float width;
    float height;
    float targetWidth;
    float targetHeight;
    float speed;
    float timeAlive;

    Body getBody() {
        return body;
    }

    void setBody(Body body) {
        this.body = body;
    }

    BodyDef getBodyDef() {
        return bodyDef;
    }

    void setBodyDef(BodyDef bodyDef) {
        this.bodyDef = bodyDef;
    }

    FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    void setFixtureDef(FixtureDef fixtureDef) {
        this.fixtureDef = fixtureDef;
    }

    Shape getShape() {
        return shape;
    }

    void setShape(Shape shape) {
        this.shape = shape;
    }

    Texture getTexture() {
        return texture;
    }

    void setTexture(Texture texture) {
        this.texture = texture;
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

    public float getTargetWidth() {
        return targetWidth;
    }

    public void setTargetWidth(float targetWidth) {
        this.targetWidth = targetWidth;
    }

    public float getTargetHeight() {
        return targetHeight;
    }

    public void setTargetHeight(float targetHeight) {
        this.targetHeight = targetHeight;
    }

    float getSpeed() {
        return speed;
    }

    void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getTimeAlive() {
        return timeAlive;
    }

    public void setTimeAlive(float timeAlive) {
        this.timeAlive = timeAlive;
    }
}
