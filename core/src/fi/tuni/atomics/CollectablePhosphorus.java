package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

class CollectablePhosphorus extends GameObject {
    private Texture animationSheet = new Texture("kerattavasequence.png");
    private Animation<TextureRegion> animation;
    private float stateTime;

    CollectablePhosphorus(float x, float y) {
        stateTime = 1f;
        width = 0.5f;
        TextureRegion[] frames;
        int sheetRows = 2;
        int sheetCols = 6;
        TextureRegion[][] temp =  TextureRegion.split(
                animationSheet,
                animationSheet.getWidth() / sheetCols,
                animationSheet.getHeight() / sheetRows);
        GameUtil gameUtil = new GameUtil();
        frames = gameUtil.to1d(temp, sheetRows, sheetCols);
        animation = new Animation<>(1 / 10f, frames);
        createBody(x, y);
    }

    public CollectablePhosphorus() {
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
        fixtureDef.isSensor = true;

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

    public void dispose() {
        animationSheet.dispose();
    }
}
