package fi.tuni.atomics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

class Bullet extends GameObject {
    private Texture texture = new Texture("bullet.png");

    Bullet(Body playerBody) {
        speed = 3f; // Check the right speed variable
        createBulletBody(playerBody);
    }

    Bullet() {
        createSimpleBulletBody();
    }

    private void createBulletBody(Body playerBody) {
        body = Game.world.createBody(getDefinitionOfBody(playerBody));
        body.createFixture(getFixtureDefinition());
        body.setUserData(this);
    }

    private void createSimpleBulletBody() {
        body = Game.world.createBody(getSimpleDefinitionOfBody());
        body.createFixture(getFixtureDefinition());
        body.setUserData(this);
    }

    private BodyDef getDefinitionOfBody(Body playerBody) {
        bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(playerBody.getWorldCenter());
        bodyDef.angle = playerBody.getAngle();

        return bodyDef;
    }

    private BodyDef getSimpleDefinitionOfBody() {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        return bodyDef;
    }

    FixtureDef getFixtureDefinition() {
        fixtureDef = new FixtureDef();

        fixtureDef.filter.groupIndex = -1;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        CircleShape circleshape = new CircleShape();

        circleshape.setRadius(0.05f);
        fixtureDef.shape = circleshape;

        return fixtureDef;
    }

    // Getters and setters
    Texture getTexture() {
        return texture;
    }

    float getSpeed() {
        return speed;
    }

    Body getBody() { return body; }

    FixtureDef getFixture() {
        return fixtureDef;
    }
}