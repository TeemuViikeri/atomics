package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

class CollectablePhosphorus extends GameObject {
    private final int sheetRows = 2;
    private final int sheetCols = 6;
    private static final float width = 0.5f;
    static private Texture animationSheet = new Texture("kerattavasequence.png");
    private Animation<TextureRegion> animation;
    private float stateTime;
    private GameUtil gameUtil = new GameUtil();

    CollectablePhosphorus(float x, float y) {
        stateTime = 1f;
        TextureRegion[] frames;
        TextureRegion[][] temp =  TextureRegion.split(
                animationSheet,
                animationSheet.getWidth() / sheetCols,
                animationSheet.getHeight() / sheetRows);
        frames = gameUtil.to1d(temp, sheetRows, sheetCols, this);
        animation = new Animation<>(1 / 10f, frames);
        createBody(x, y);
    }

    private void createBody(float x, float y) {
        body = PlayScreen.world.createBody(getDefinitionOfBody(x, y));
        body.setGravityScale(0);
        body.createFixture(getFixtureDefinition());
        body.setUserData(this);
    }

    private BodyDef getDefinitionOfBody(float x, float y) {
        bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        return bodyDef;
    }

    private FixtureDef getFixtureDefinition() {
        fixtureDef = new FixtureDef();

        fixtureDef.filter.groupIndex = -3;
        fixtureDef.density = 10f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        shape = new PolygonShape();

        ((PolygonShape) shape).setAsBox(width / 2, width / 2);
        fixtureDef.shape = shape;

        return fixtureDef;
    }

    float setStateTime() {
        return stateTime+= Gdx.graphics.getDeltaTime();
    }

    Animation<TextureRegion> getAnimation() {
        return animation;
    }
}
