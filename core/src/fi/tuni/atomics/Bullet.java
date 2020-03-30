package fi.tuni.atomics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

class Bullet extends GameObject {
    Bullet(Body playerBody) {
        speed = 3f;
        texture = new Texture("bullet.png");
        createBulletBody(playerBody);
        width = fixtureDef.shape.getRadius() * 2;
        height = fixtureDef.shape.getRadius() * 2;
    }

    private void createBulletBody(Body playerBody) {
        body = Atomics.world.createBody(getDefinitionOfBody(playerBody));
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

    private FixtureDef getFixtureDefinition() {
        fixtureDef = new FixtureDef();

        fixtureDef.filter.groupIndex = -1;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        shape = new CircleShape();

        shape.setRadius(0.05f);
        fixtureDef.shape = shape;

        return fixtureDef;
    }
}