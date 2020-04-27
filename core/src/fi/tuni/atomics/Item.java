package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

class Item extends GameObject {
    private float spawnTimer = 0;
    Vector2 spawnPoint;

    private Item(Vector2 spawnPoint, int itemInt) {
        this.spawnPoint = spawnPoint;
        getRandomTexture(itemInt);
        width = texture.getWidth() / 1000f;
        height = texture.getHeight() / 1000f;
        targetWidth = width * 10;
        targetHeight = height * 10;
    }

    Item() {
    }

    void createBody() {
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

        fixtureDef.density = 10f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 10f;
        fixtureDef.isSensor = true;

        shape = new PolygonShape();

        ((PolygonShape) shape).setAsBox(this.targetWidth / 2,this.targetHeight / 2);
        fixtureDef.shape = shape;

        return fixtureDef;
    }

    void spawnItem() {
        float spawnFrequency = 7 - (7f/12f * (PlayScreen.levelMultiplier - 1));
        spawnTimer += Gdx.graphics.getDeltaTime();

        if (spawnTimer >= spawnFrequency) {
            spawnPoint = getItemSpawnPoint();

            int itemInt = MathUtils.random(1, 40);
            int rotation = MathUtils.random(0, 360);

            if (itemInt < 36) {
                Item item = new Item(spawnPoint, itemInt);
                item.createBody();
                item.body.setTransform(spawnPoint, rotation);
            } else {
                RareItem rareItem = new RareItem(spawnPoint, itemInt);
                rareItem.createBody();
                rareItem.body.setTransform(spawnPoint, rotation);
            }

            spawnTimer = 0;
        }
    }

    private Vector2 getItemSpawnPoint() {
        float x, y;

        x = MathUtils.random(
            PlayScreen.ROOM_WIDTH_PIXELS * 2/10 * PlayScreen.scale,
            PlayScreen.ROOM_WIDTH_PIXELS * 9/10 * PlayScreen.scale);

        y = MathUtils.random(
                PlayScreen.WORLD_HEIGHT_PIXELS * 1/6 * PlayScreen.scale,
                PlayScreen.WORLD_HEIGHT_PIXELS * 5/6 * PlayScreen.scale);

        return new Vector2(x, y);
    }

    void getRandomTexture(int itemInt) {
        if      (itemInt <= 4)  this.texture = new Texture("kala2.png");
        else if (itemInt <= 8)  this.texture = new Texture("kortsu.png");
        else if (itemInt <= 12) this.texture = new Texture("lasit.png");
        else if (itemInt <= 16) this.texture = new Texture("slime.png");
        else if (itemInt <= 20) this.texture = new Texture("lusikka.png");
        else if (itemInt <= 24) this.texture = new Texture("omena.png");
        else if (itemInt <= 28) this.texture = new Texture("ruuvi.png");
        else if (itemInt <= 32) this.texture = new Texture("sukka.png");
        else if (itemInt <= 36) this.texture = new Texture("tutti.png");
        else if (itemInt == 37) this.texture = new Texture("kello.png");
        else if (itemInt == 38) this.texture = new Texture("puhelin.png");
        else if (itemInt == 39) this.texture = new Texture("sormus.png");
        else if (itemInt == 40) this.texture = new Texture("cropped-tekarit.png");
    }
}
