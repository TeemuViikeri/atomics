package fi.tuni.atomics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

class Item extends GameObject {
    private int UPPER_TOP = 1, UPPER_BOTTOM = 2, LOWER_TOP = 3, LOWER_BOTTOM = 4;
    private float spawnTimer = 0;
    private float spawnFrequency = 180;
    private int spawnside;
    private float speed = 3f; // Check correct (variable?) speed
    private Vector2 spawnPoint;

    private Item(Vector2 spawnPoint) {
        this.spawnPoint = spawnPoint;
        texture = new Texture("badlogic.jpg"); // What texture to insert?
        width = 0.5f; // Check correct width to variable textures
        height = 0.5f;
    }

    Item() {
    }

    private void createBody() {
        body = PlayScreen.world.createBody(getDefinitionOfBody());
        body.setGravityScale(0);
        body.createFixture(getFixtureDefinition());
        body.setUserData(this);
    }

    private BodyDef getDefinitionOfBody() {
        bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(spawnPoint); // Set correct spawnPoint for items

        return bodyDef;
    }

    private FixtureDef getFixtureDefinition() {
        fixtureDef = new FixtureDef();

        fixtureDef.filter.groupIndex = -2;

        // CHECK CORRECT VALUES
        fixtureDef.density = 10f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        shape = new PolygonShape();

        ((PolygonShape) shape).setAsBox(width / 2, width / 2);
        fixtureDef.shape = shape;

        return fixtureDef;
    }

    void spawnItem() {
        spawnTimer++;
        spawnside = MathUtils.random(1, 4);

        if (spawnTimer >= spawnFrequency) {
            spawnPoint = getItemSpawnPoint(spawnside);
            Item item = new Item(spawnPoint);
            item.createBody();
            spawnTimer = 0;

            item.body.applyLinearImpulse(
                new Vector2(1.5f * speed, 0),
                item.body.getWorldCenter(),
                true
            );
        }
    }

    private Vector2 getItemSpawnPoint(int spawnside) {
        if (spawnside == UPPER_TOP) {
            return new Vector2(0, PlayScreen.WORLD_HEIGHT_PIXELS * 5/6 * PlayScreen.scale);
        } else if (spawnside == UPPER_BOTTOM) {
            return new Vector2(0, PlayScreen.WORLD_HEIGHT_PIXELS * 4/6 * PlayScreen.scale);
        } else if (spawnside == LOWER_TOP) {
            return new Vector2(0, PlayScreen.WORLD_HEIGHT_PIXELS * 2/6 * PlayScreen.scale);
        } else if (spawnside == LOWER_BOTTOM) {
            return new Vector2(0, PlayScreen.WORLD_HEIGHT_PIXELS * 1/6 * PlayScreen.scale);
        } else {
            throw new RuntimeException("Couldn't get a real phosphorus spawnpoint");
        }
    }
}
