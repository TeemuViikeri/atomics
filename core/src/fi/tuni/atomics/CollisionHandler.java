package fi.tuni.atomics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class CollisionHandler implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        if (isBulletContactingWall(bodyA, bodyB)) {
            if (bodyA.getUserData() instanceof Bullet) {
                bodyA.setUserData("dead");
            } else {
                bodyB.setUserData("dead");
            }
        }

        if (isBulletContactingPhosphorus(bodyA, bodyB)) {
            Score.collectPhosphorus();
            bodyA.setUserData("dead");
            bodyB.setUserData("dead");
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private boolean isBulletContactingWall(Body a, Body b) {
        if (a.getUserData() instanceof Bullet && b.getUserData() instanceof Wall) {
            return true;
        } else return a.getUserData() instanceof Wall && b.getUserData() instanceof Bullet;
    }

    private boolean isBulletContactingPhosphorus(Body a, Body b) {
        if (
            a.getUserData() instanceof Bullet &&
            b.getUserData() instanceof Phosphorus
            ) {
            return true;
        } else
            return  a.getUserData() instanceof Phosphorus &&
                    b.getUserData() instanceof Bullet;
    }

    void sendBodiesToBeDestroyed(Array<Body> bodies, Array<Body> bodiesToBeDestroyed) {
        for (Body body: bodies) {
            if (body.getUserData().equals("dead")) {
                bodiesToBeDestroyed.add(body);
            }
        }
    }

    void clearBodies(Array<Body> bodiesToBeDestroyed) {
        for (Iterator<Body> i = bodiesToBeDestroyed.iterator(); i.hasNext();) {
            Body body = i.next();
            Atomics.world.destroyBody(body);
            i.remove();
        }
    }
}
