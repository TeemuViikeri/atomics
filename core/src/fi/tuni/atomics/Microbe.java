package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

public class Microbe extends GameObject {
    private final int sheetRows = 1;
    private final int sheetCols = 2;
    static private Texture animationSheet = new Texture("happyMicrobe.png");
    private Animation<TextureRegion> animation;
    private Vector2 spawnPoint;
    private float stateTime;
    private float forceX = 3f;
    private float forceY = -2f;
    private GameUtil gameUtil = new GameUtil();
    static Array<Microbe> microbes = new Array<>();

    Microbe(Vector2 spawnPoint) {
        this.spawnPoint = spawnPoint;
        stateTime = 1f;
        width = 0.5f;
        height = 0.5f;
        speed = 2;
        TextureRegion[] frames;
        TextureRegion[][] temp =  TextureRegion.split(
                animationSheet,
                animationSheet.getWidth() / sheetCols,
                animationSheet.getHeight() / sheetRows);
        frames = gameUtil.to1d(temp, sheetRows, sheetCols, this);
        animation = new Animation<>(1 / 1f, frames);
        createBody();
        applyForce();
    }

    Microbe() {

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

        fixtureDef.filter.groupIndex = -10;
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

    public void spawnMicrobes() {

        microbes.add(new Microbe(new Vector2(
                MathUtils.random(PlayScreen.THIRD_SCREEN_LEFT_SIDE +
                                PlayScreen.TILE_LENGTH_PIXELS * 2 * PlayScreen.scale,
                        PlayScreen.THIRD_SCREEN_LEFT_SIDE + PlayScreen.ROOM_WIDTH_PIXELS
                                * PlayScreen.scale - width * 2),
                MathUtils.random(1, (PlayScreen.ROOM_HEIGHT_PIXELS
                        - PlayScreen.TILE_LENGTH_PIXELS * 2) * PlayScreen.scale))));

        /*
        Microbe microbe2 = new Microbe(new Vector2(
                MathUtils.random(PlayScreen.THIRD_SCREEN_LEFT_SIDE +
                                PlayScreen.TILE_LENGTH_PIXELS * 2 * PlayScreen.scale,
                        PlayScreen.THIRD_SCREEN_LEFT_SIDE + PlayScreen.ROOM_WIDTH_PIXELS
                                * PlayScreen.scale - width * 2),
                MathUtils.random(1, (PlayScreen.ROOM_HEIGHT_PIXELS
                        - PlayScreen.TILE_LENGTH_PIXELS * 2) * PlayScreen.scale)));

         */
    }

    public void deSpawnMicrobes() {
        microbes.get(microbes.size - 1).getBody().setUserData("dead");
        microbes.removeIndex(microbes.size - 1);
    }

    private void applyForce() {
        int forceDecider = MathUtils.random(1,2);

        if (forceDecider == 1) {
            body.applyLinearImpulse(new Vector2(forceX, forceY), body.getWorldCenter(), true);
        } else {
            body.applyLinearImpulse(new Vector2(-forceX, -forceY), body.getWorldCenter(),
                    true);
        }
    }

}
