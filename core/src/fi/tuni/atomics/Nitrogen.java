package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

class Nitrogen extends GameObject {
    private static Array<Nitrogen> nitrogens = new Array<>();
    private Vector2 spawnPoint;
    private float targetX;
    private float targetY;

    private Nitrogen(Vector2 pos) {
        texture = new Texture("typpi.png");
        targetX = (PlayScreen.ROOM_WIDTH_PIXELS * 2.5f + PlayScreen.PIPE_HORIZONTAL_PIXELS * 2)
                * PlayScreen.scale;
        targetY = PlayScreen.ROOM_HEIGHT_PIXELS * PlayScreen.scale - 0.64f;
        spawnPoint = pos;
        width = 0.25f;
        height = 0.25f;
        speed = 1.6f;
        createBody();
        applyForce();
    }

    Nitrogen() {

    }

    void createNitrogen(Vector2 pos) {
        nitrogens.add(new Nitrogen(pos));
    }

    private void createBody() {
        body = PlayScreen.world.createBody(getDefinitionOfBody());
        body.createFixture(getFixtureDefinition());
        body.setUserData(this);
    }

    private BodyDef getDefinitionOfBody() {
        bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.gravityScale = 0;
        bodyDef.position.set(spawnPoint);

        return bodyDef;
    }

    private FixtureDef getFixtureDefinition() {
        fixtureDef = new FixtureDef();

        fixtureDef.isSensor = true;
        fixtureDef.density = 0f;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 0f;

        shape = new PolygonShape();

        ((PolygonShape) shape).setAsBox(width / 2, width / 2);
        fixtureDef.shape = shape;

        return fixtureDef;
    }

    private void applyForce() {
        float angle = (float) Math.atan2(targetY - spawnPoint.y, targetX - spawnPoint.x);
        body.setTransform(spawnPoint, angle);
        Vector2 force = new Vector2(
                (float) Math.cos(body.getAngle()) * speed,
                (float) Math.sin(body.getAngle()) * speed);

        body.setLinearVelocity(force);
    }

    public void dispose() {
        texture.dispose();
    }
}
