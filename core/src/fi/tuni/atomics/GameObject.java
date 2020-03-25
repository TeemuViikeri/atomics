package fi.tuni.atomics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public abstract class GameObject {
    Body body;
    BodyDef bodyDef;
    FixtureDef fixtureDef;
    float speed;

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
}
