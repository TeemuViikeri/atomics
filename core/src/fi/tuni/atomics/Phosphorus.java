package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.awt.Polygon;

public class Phosphorus {
    private final int sheetRows = 2;
    private final int sheetCols = 5;
    static private Texture animationSheet = new Texture("phosphorus.png");
    static private TextureRegion[] frames;
    private Animation<TextureRegion> animation;
    private Body body;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;
    private Sprite sprite;
    private float speed;
    private float positionX;
    private float positionY;
    private float stateTime;

    Phosphorus(World world, float x, float y) {
        stateTime = 1f;
        positionX = x;
        positionY = y;
        frames = new TextureRegion[sheetRows*sheetCols];
        TextureRegion[][] temp =  TextureRegion.split(
                animationSheet,
                animationSheet.getWidth() / sheetCols,
                animationSheet.getHeight() / sheetRows);
        frames = to1d(temp);
        animation = new Animation<>(1 / 9f, frames);
        createBulletBody(world);
        speed = 3;

        body.applyLinearImpulse(
            new Vector2(3, -3),
            body.getWorldCenter(),
            true);
    }

    public TextureRegion[] to1d(TextureRegion[][] temp) {
        int index = 0;
        TextureRegion[] temporary = new TextureRegion[sheetRows * sheetCols];
        for (int i = 0; i < sheetRows; i++) {
            for (int j = 0; j < sheetCols; j++) {
                temporary[index++] = temp[i][j];
            }
        }
        return temporary;
    }

    public float setStateTime() {
        return stateTime+= Gdx.graphics.getDeltaTime();
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    private void createBulletBody(World world) {
        body = world.createBody(getDefinitionOfBody());
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
        // Mass per square meter (kg^m2)
        fixtureDef.density = 10f;
        // How bouncy object? Very bouncy [0,1]
        fixtureDef.restitution = 0f;
        // How slipper object? [0,1]
        fixtureDef.friction = 0f;

        PolygonShape polygon = new PolygonShape();

        polygon.setAsBox(0.25f, 0.25f);
        fixtureDef.shape = polygon;

        return fixtureDef;
    }

    public void draw(SpriteBatch batch) { sprite.draw(batch); }

    float getSpeed() {
        return speed;
    }

    void setSpeed(float speed) {
        this.speed = speed;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Body getBody() { return body; }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public FixtureDef getFixture() {
        return fixtureDef;
    }
}
