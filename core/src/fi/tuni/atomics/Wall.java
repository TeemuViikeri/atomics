package fi.tuni.atomics;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

class Wall extends GameObject {
    Wall(TiledMap tiledMap, String layer, short filterIndex) {
        transformWallsToBodies(tiledMap, layer, filterIndex);
    }

    private void createStaticBody(Rectangle rect, short filterIndex) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        float x = rect.getX();
        float y = rect.getY();
        float width = rect.width;
        float height = rect.height;

        float centerX = width / 2 + x;
        float centerY = height / 2 + y;

        bodyDef.position.set(centerX, centerY);
        body = PlayScreen.world.createBody(bodyDef);
        body.setUserData(this);

        shape = new PolygonShape();
        ((PolygonShape) shape).setAsBox(width / 2, height / 2);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        fixtureDef.filter.groupIndex = filterIndex;
        body.createFixture(fixtureDef);
    }

    private Rectangle scaleRect(Rectangle r) {
        Rectangle rectangle = new Rectangle();
        rectangle.x = r.x * (float) 0.01;
        rectangle.y = r.y * (float) 0.01;
        rectangle.width = r.width * (float) 0.01;
        rectangle.height = r.height * (float) 0.01;
        return rectangle;
    }

    private void transformWallsToBodies(TiledMap tiledMap, String layer, short filterIndex) {
        MapLayer collisionObjectLayer = tiledMap.getLayers().get(layer);
        MapObjects mapObjects = collisionObjectLayer.getObjects();
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();
            Rectangle rectangle = scaleRect(tmp);
            createStaticBody(rectangle, filterIndex);
        }
    }
}
