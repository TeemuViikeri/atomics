package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    static private Texture animationSheet = new Texture("phosphorus.png");
    private float spawnTimer = 0;
    private float spawnFrequency = 180;
    private Animation<TextureRegion> animation;
    private Vector2 spawnPoint;
    private int spawnside;
    private float stateTime;
    private GameUtil gameUtil = new GameUtil();

    private Phosphorus(Vector2 spawnPoint) {
        this.spawnPoint = spawnPoint;
        stateTime = 1f;
        TextureRegion[] frames;
        TextureRegion[][] temp =  TextureRegion.split(
                animationSheet,
                animationSheet.getWidth() / sheetCols,
                animationSheet.getHeight() / sheetRows);
        frames = to1d(temp);
        animation = new Animation<>(1 / 9f, frames);
    }

    Phosphorus() {

    }

    private TextureRegion[] to1d(TextureRegion[][] temp) {
        int index = 0;
        TextureRegion[] temporary = new TextureRegion[sheetRows * sheetCols];
        for (int i = 0; i < sheetRows; i++) {
            for (int j = 0; j < sheetCols; j++) {
                temporary[index++] = temp[i][j];
            }
        }
        return temporary;
    }

    float setStateTime() {
        return stateTime+= Gdx.graphics.getDeltaTime();
    }

    Animation<TextureRegion> getAnimation() {
        return animation;
    }

    private void createBody() {
        body = Game.world.createBody(getDefinitionOfBody());
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
        spawnTimer++;
        spawnside =  MathUtils.random(1,6);

        if (spawnTimer >= spawnFrequency) {
            spawnPoint = gameUtil.getSpawnPoint(spawnside);
            Phosphorus phosphorus = new Phosphorus(spawnPoint);
            phosphorus.createBody();
            spawnTimer = 0;

            if (spawnside == TOP) {
                phosphorus.body.applyLinearImpulse(new Vector2(0, -1),
                        phosphorus.body.getWorldCenter(),  true);
            } else if (spawnside == TOPLEFT) {
                phosphorus.body.applyLinearImpulse(new Vector2(1.5f,-1),
                        phosphorus.body.getWorldCenter(), true);
            } else if (spawnside == TOPRIGHT) {
                phosphorus.body.applyLinearImpulse(new Vector2(-1.5f,-1),
                        phosphorus.body.getWorldCenter(), true);
            } else if (spawnside == BOTTOM) {
                phosphorus.body.applyLinearImpulse(new Vector2(0, 1),
                        phosphorus.body.getWorldCenter(),  true);
            } else if (spawnside == BOTTOMLEFT) {
                phosphorus.body.applyLinearImpulse(new Vector2(1.5f,1),
                        phosphorus.body.getWorldCenter(), true);
            } else if (spawnside == BOTTOMRIGHT) {
                phosphorus.body.applyLinearImpulse(new Vector2(-1.5f,1),
                        phosphorus.body.getWorldCenter(), true);
            }
        }
    }
}
