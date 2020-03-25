package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Phosphorus extends GameObject{
    private final int sheetRows = 2;
    private final int sheetCols = 5;
    static private Texture animationSheet = new Texture("phosphorus.png");
    private Animation<TextureRegion> animation;
    private float positionX;
    private float positionY;
    private float stateTime;

    Phosphorus(float x, float y) {
        stateTime = 1f;
        positionX = x;
        positionY = y;
        TextureRegion[] frames;
        TextureRegion[][] temp =  TextureRegion.split(
                animationSheet,
                animationSheet.getWidth() / sheetCols,
                animationSheet.getHeight() / sheetRows);
        frames = to1d(temp);
        animation = new Animation<>(1 / 9f, frames);
        createBody();

        body.applyLinearImpulse(
            new Vector2(3, -3),
            body.getWorldCenter(),
            true);
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
        bodyDef.position.set(positionX, positionY);

        return bodyDef;
    }

    private FixtureDef getFixtureDefinition() {
        fixtureDef = new FixtureDef();

        fixtureDef.filter.groupIndex = -2;
        fixtureDef.density = 10f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        PolygonShape polygon = new PolygonShape();

        polygon.setAsBox(0.25f, 0.25f);
        fixtureDef.shape = polygon;

        return fixtureDef;
    }

    void draw(SpriteBatch batch) {
        batch.draw(
            animation.getKeyFrame(
            this.setStateTime(), true),
            body.getPosition().x - 0.25f,
            body.getPosition().y - 0.25f,0.5f, 0.5f
        );
    }
}
