package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

class Phosphorus extends GameObject{
    private final int TOP = 1, TOPLEFT = 2, TOPRIGHT = 3, BOTTOM = 4, BOTTOMLEFT = 5,
            BOTTOMRIGHT = 6;
    private final int sheetRows = 2;
    private final int sheetCols = 5;
    static final float width = 0.5f;
    private Texture animationSheet = new Texture("phosphorus2.png");
    private float spawnTimer = 0;
    private float spawnFrequency = 2;
    private Animation<TextureRegion> animation;
    private Vector2 spawnPoint;
    private int spawnside;
    private float stateTime;
    private float speed = 2;
    private GameUtil gameUtil = new GameUtil();

    private Phosphorus(Vector2 spawnPoint) {
        this.spawnPoint = spawnPoint;
        stateTime = 1f;
        TextureRegion[] frames;
        TextureRegion[][] temp =  TextureRegion.split(
                animationSheet,
                animationSheet.getWidth() / sheetCols,
                animationSheet.getHeight() / sheetRows);
        frames = gameUtil.to1d(temp, sheetRows, sheetCols);
        animation = new Animation<>(1 / 9f, frames);
    }

    Phosphorus() {

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
        bodyDef.position.set(spawnPoint);

        return bodyDef;
    }

    private FixtureDef getFixtureDefinition() {
        fixtureDef = new FixtureDef();

        fixtureDef.filter.groupIndex = -2;
        fixtureDef.density = 10f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        shape = new PolygonShape();

        ((PolygonShape) shape).setAsBox(width / 2, width / 2);
        fixtureDef.shape = shape;

        return fixtureDef;
    }

    void spawnPhosphorus() {
        spawnFrequency = 2 - (2f/15f * (PlayScreen.levelMultiplier - 1));
        speed = 2 + (2/15f * (PlayScreen.levelMultiplier - 1));

        spawnTimer += Gdx.graphics.getDeltaTime();
        spawnside =  MathUtils.random(1,6);

        if (spawnTimer >= spawnFrequency) {
            spawnPoint = gameUtil.getSpawnPoint(spawnside);
            Phosphorus phosphorus = new Phosphorus(spawnPoint);
            phosphorus.createBody();
            spawnTimer = 0;

            if (spawnside == TOP) {
                phosphorus.body.applyLinearImpulse(new Vector2(0, -speed),
                        phosphorus.body.getWorldCenter(),  true);
            } else if (spawnside == TOPLEFT) {
                phosphorus.body.applyLinearImpulse(new Vector2(1.5f * speed,-speed),
                        phosphorus.body.getWorldCenter(), true);
            } else if (spawnside == TOPRIGHT) {
                phosphorus.body.applyLinearImpulse(new Vector2(-1.5f * speed,-speed),
                        phosphorus.body.getWorldCenter(), true);
            } else if (spawnside == BOTTOM) {
                phosphorus.body.applyLinearImpulse(new Vector2(0, speed),
                        phosphorus.body.getWorldCenter(),  true);
            } else if (spawnside == BOTTOMLEFT) {
                phosphorus.body.applyLinearImpulse(new Vector2(1.5f * speed,speed),
                        phosphorus.body.getWorldCenter(), true);
            } else if (spawnside == BOTTOMRIGHT) {
                phosphorus.body.applyLinearImpulse(new Vector2(-1.5f * speed,speed),
                        phosphorus.body.getWorldCenter(), true);
            }
        }
    }

    float setStateTime() {
        return stateTime+= Gdx.graphics.getDeltaTime();
    }

    Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void dispose() {
        animationSheet.dispose();
    }
}
