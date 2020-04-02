package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Microbe extends GameObject {
    private final int sheetRows = 1;
    private final int sheetCols = 2;
    final float width = 0.5f;
    static private Texture animationSheet = new Texture("happyMicrobe.png");
    private Animation<TextureRegion> animation;
    private Vector2 spawnPoint;
    private float stateTime;
    private float speed = 2;
    private float forceX = 1.5f;
    private float forceY = -1f;
    private GameUtil gameUtil = new GameUtil();

    Microbe(Vector2 spawnPoint) {
        this.spawnPoint = spawnPoint;
        stateTime = 1f;
        TextureRegion[] frames;
        TextureRegion[][] temp =  TextureRegion.split(
                animationSheet,
                animationSheet.getWidth() / sheetCols,
                animationSheet.getHeight() / sheetRows);
        frames = gameUtil.to1d(temp, sheetRows, sheetCols);
        animation = new Animation<>(1 / 1f, frames);
        createBody();
        body.applyLinearImpulse(new Vector2(forceX, forceY), body.getWorldCenter(), true);
    }

    float setStateTime() {
        return stateTime+= Gdx.graphics.getDeltaTime();
    }

    Animation<TextureRegion> getAnimation() {
        return animation;
    }

    private void createBody() {
        body = PlayScreen.world.createBody(getDefinitionOfBody());
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

        fixtureDef.filter.groupIndex = 1;
        fixtureDef.density = 0f;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 0f;

        shape = new PolygonShape();

        ((PolygonShape) shape).setAsBox(width / 2, width / 2);
        //shape = new CircleShape();

        //shape.setRadius(width / 2);
        fixtureDef.shape = shape;

        return fixtureDef;
    }


}
