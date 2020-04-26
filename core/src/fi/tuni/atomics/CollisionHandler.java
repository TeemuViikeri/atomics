package fi.tuni.atomics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

class CollisionHandler implements ContactListener {
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

        if (isNitrogenContactingWall(bodyA, bodyB)) {
                if (bodyA.getUserData() instanceof Nitrogen) {
                    if (GameUtil.room == 3) {
                        GameAudio.playVacuumSound(0.08f);
                    }

                    bodyA.setUserData("dead");
                } else if (bodyB.getUserData() instanceof Nitrogen) {
                    if (GameUtil.room == 3) {
                        GameAudio.playVacuumSound(0.08f);
                    }

                    bodyB.setUserData("dead");
                }

        }

        if (isPlayerContactingPipe(bodyA, bodyB)) {
            if (bodyA.getUserData() instanceof Pipe) {
                ((Pipe) bodyA.getUserData()).isTouched = true;
            } else if (bodyB.getUserData() instanceof Pipe) {
                ((Pipe) bodyB.getUserData()).isTouched = true;
            }
        }

        if (isBulletContactingPhosphorus(bodyA, bodyB)) {
            GameAudio.playBondingSound();
            bodyA.setUserData("dead");
            bodyB.setUserData("dead");
            }

        if (isMicrobeContactingWall(bodyA, bodyB)) {
            if (bodyA.getUserData() instanceof Microbe) {

            } else if (bodyB.getUserData() instanceof Microbe) {

            }
        }

        if (isPlayerContactingPhosphorus(bodyA, bodyB)) {
            if (!Player.immortal) {
                Player.loseHitpoint();
                GameAudio.playLoseLifeSound();
            }

            if (bodyA.getUserData() instanceof Phosphorus) {
                bodyA.setUserData("dead");
            } else {
                bodyB.setUserData("dead");
            }
        }

        if (isPlayerContactingCollectablePhosphorus(bodyA, bodyB)) {
            if (bodyA.getUserData() instanceof CollectablePhosphorus) {
                Score.collectPhosphorus();
                GameAudio.playCollectablePhosphorusPickedSound();
                bodyA.setUserData("dead");
            } else {
                Score.collectPhosphorus();
                GameAudio.playCollectablePhosphorusPickedSound();
                bodyB.setUserData("dead");
            }
        }

        if (isPlayerContactingMicrobe(bodyA, bodyB)) {
            if (!Player.immortal) {
                Player.loseHitpoint();
                GameAudio.playLoseLifeSound();
            }

            if (bodyA.getUserData() instanceof Microbe) {
                ((Microbe) bodyA.getUserData()).die();
                bodyA.setUserData("dead");
            } else {
                ((Microbe) bodyB.getUserData()).die();
                bodyB.setUserData("dead");
            }
        }

        if (isItemContactingWall(bodyA, bodyB)) {
            if (bodyA.getUserData() instanceof Wall) {
                Wall wall = (Wall) bodyA.getUserData();
                if (wall.thisLayer.getName().equals("cleaner-area")) {
                    if (GameUtil.room == 1) {
                        GameAudio.playVacuumSound(0.5f);
                    }

                    if (bodyB.getUserData() instanceof RareItem) {
                        Score.collectRareItem();
                        GameAudio.playRareItemPickedSound();
                    } else if (!(bodyB.getUserData() instanceof RareItem)){
                        Score.collectItem();
                    }

                    bodyB.setUserData("dead");
                }
            } else if (bodyB.getUserData() instanceof Wall) {
                Wall wall = (Wall) bodyB.getUserData();
                if (wall.thisLayer.getName().equals("cleaner-area")) {
                    if (GameUtil.room == 1) {
                        GameAudio.playVacuumSound(0.5f);
                    }

                    if (bodyA.getUserData() instanceof RareItem) {
                        Score.collectRareItem();
                        GameAudio.playRareItemPickedSound();
                    } else if (!(bodyB.getUserData() instanceof RareItem)) {
                        Score.collectItem();
                    }

                    bodyA.setUserData("dead");
                }
            }
        }

        if (isPlayerContactingItem(bodyA, bodyB)) {
            GameAudio.playHitItemSound();
        }
    }

    @Override
    public void endContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        if (bodyA.getUserData() instanceof Pipe) {
            ((Pipe) bodyA.getUserData()).isTouched = false;
        } else if (bodyB.getUserData() instanceof Pipe) {
            ((Pipe) bodyB.getUserData()).isTouched = false;
        }
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

    private boolean isPlayerContactingPipe(Body a, Body b) {
        if (a.getUserData() instanceof Player && b.getUserData() instanceof Pipe) {
            return true;
        } else return a.getUserData() instanceof Pipe && b.getUserData() instanceof Player;
    }

    private boolean isPlayerContactingPhosphorus(Body a, Body b) {
        if (a.getUserData() instanceof Player && b.getUserData() instanceof Phosphorus) {
            return true;
        } else return a.getUserData() instanceof Phosphorus && b.getUserData() instanceof Player;
    }

    private boolean isMicrobeContactingWall(Body a, Body b) {
        if (a.getUserData() instanceof Microbe && b.getUserData() instanceof Wall) {
            return true;
        } else return a.getUserData() instanceof Wall && b.getUserData() instanceof Microbe;
    }

    private boolean isPlayerContactingMicrobe(Body a, Body b) {
        if (a.getUserData() instanceof Player && b.getUserData() instanceof Microbe) {
            return true;
        } else return a.getUserData() instanceof Microbe && b.getUserData() instanceof Player;
    }

    private boolean isNitrogenContactingWall(Body a, Body b) {
        if (a.getUserData() instanceof Nitrogen && b.getUserData() instanceof Wall) {
            return true;
        } else return a.getUserData() instanceof Wall && b.getUserData() instanceof Nitrogen;
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

    private boolean isPlayerContactingCollectablePhosphorus(Body a, Body b) {
        if (
            a.getUserData() instanceof Player &&
            b.getUserData() instanceof CollectablePhosphorus
            ) {
            return true;
        } else {
            return  a.getUserData() instanceof CollectablePhosphorus &&
                    b.getUserData() instanceof Player;
        }
    }

    private boolean isItemContactingWall(Body a, Body b) {
        if (
            a.getUserData() instanceof Item &&
            b.getUserData() instanceof Wall
            ) {
            return true;
        } else {
            return  a.getUserData() instanceof Wall &&
                    b.getUserData() instanceof Item;
        }
    }

    private boolean isPlayerContactingItem(Body a, Body b) {
        if (
            a.getUserData() instanceof Player &&
            b.getUserData() instanceof Item
            ) {
            return true;
        } else {
            return  a.getUserData() instanceof Item &&
                    b.getUserData() instanceof Player;
        }
    }



    void sendBodiesToBeDestroyed(Array<Body> bodies, Array<Body> bodiesToBeDestroyed) {
        for (Body body: bodies) {
            if (body.getPosition().y >= PlayScreen.WORLD_HEIGHT_PIXELS * PlayScreen.scale
                    + Phosphorus.width * 2) {
                body.setUserData("dead");
            } else if (body.getPosition().y <= - Phosphorus.width * 2) {
                body.setUserData("dead");
            }

            if (body.getUserData().equals("dead")) {
                bodiesToBeDestroyed.add(body);
            }
        }
    }

    void clearBodies(Array<Body> bodiesToBeDestroyed) {
        for (Iterator<Body> i = bodiesToBeDestroyed.iterator(); i.hasNext();) {
            Body body = i.next();

            if (body.getFixtureList().get(0).getFilterData().groupIndex == -2
                    && !Player.playerLostHitPoint) {
                new CollectablePhosphorus(body.getPosition().x, body.getPosition().y);
            } else {
                Player.playerLostHitPoint = false;
            }

            PlayScreen.world.destroyBody(body);
            i.remove();
        }
    }
}
