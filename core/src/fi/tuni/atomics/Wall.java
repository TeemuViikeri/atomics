package fi.tuni.atomics;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Wall {
    private Body body;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;

    public Wall(TiledMap tiledMap, World world, String layer, String userData) {
        transformWallsToBodies(tiledMap, world, layer, userData);
    }

    private void createStaticBody(World world, Rectangle rect, String userData) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        float x = rect.getX();
        float y = rect.getY();
        float width = rect.width;
        float height = rect.height;

        float centerX = width / 2 + x;
        float centerY = height / 2 + y;

        bodyDef.position.set(centerX, centerY);
        body = world.createBody(bodyDef);
        body.setUserData(userData);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(width / 2, height / 2);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = groundBox;

        fixtureDef.filter.groupIndex = -2;
        body.createFixture(fixtureDef);
    }

    private Rectangle scaleRect(Rectangle r, float scale) {
        Rectangle rectangle = new Rectangle();
        rectangle.x = r.x * scale;
        rectangle.y = r.y * scale;
        rectangle.width = r.width * scale;
        rectangle.height = r.height * scale;
        return rectangle;
    }

    private void transformWallsToBodies(TiledMap tiledMap, World world, String layer, String userData) {
        MapLayer collisionObjectLayer = tiledMap.getLayers().get(layer);
        MapObjects mapObjects = collisionObjectLayer.getObjects();
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();
            Rectangle rectangle = scaleRect(tmp, 1 / 100f);
            createStaticBody(world, rectangle, userData);
        }
    }
}
