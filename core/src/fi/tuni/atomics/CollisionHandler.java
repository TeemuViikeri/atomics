package fi.tuni.atomics;

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
                ((Bullet) bodyA.getUserData()).dispose();
                bodyA.setUserData("dead");
            } else {
                ((Bullet) bodyB.getUserData()).dispose();
                bodyB.setUserData("dead");
            }
        }

        if (isNitrogenContactingWall(bodyA, bodyB)) {
            if (bodyA.getUserData() instanceof Nitrogen) {
                if (GameUtil.room == 3) {
                    if (!SettingsScreen.isMuted)
                        GameAudio.playVacuumSound(0.08f);
                }

                ((Nitrogen) bodyA.getUserData()).dispose();
                Score.collectedNitrogenCounter++;
                bodyA.setUserData("dead");
            } else if (bodyB.getUserData() instanceof Nitrogen) {
                if (GameUtil.room == 3) {
                    if (!SettingsScreen.isMuted)
                        GameAudio.playVacuumSound(0.08f);
                }

                ((Nitrogen) bodyB.getUserData()).dispose();
                Score.collectedNitrogenCounter++;
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
            if (!SettingsScreen.isMuted)
                GameAudio.playBondingSound();

            if (bodyA.getUserData() instanceof Bullet) {
                ((Bullet) bodyA.getUserData()).dispose();
                ((Phosphorus) bodyB.getUserData()).dispose();

                bodyA.setUserData("dead");
                bodyB.setUserData("dead");
            } else {
                ((Phosphorus) bodyA.getUserData()).dispose();
                ((Bullet) bodyB.getUserData()).dispose();
                bodyA.setUserData("dead");
                bodyB.setUserData("dead");
            }
        }

        if (isPlayerContactingPhosphorus(bodyA, bodyB)) {
            if (!Player.immortal) {
                Player.loseHitpoint();

                if (!SettingsScreen.isMuted)
                    GameAudio.playLoseLifeSound();
            }

            if (bodyA.getUserData() instanceof Phosphorus) {
                ((Phosphorus) bodyA.getUserData()).dispose();
                bodyA.setUserData("dead");
            } else {
                ((Phosphorus) bodyB.getUserData()).dispose();
                bodyB.setUserData("dead");
            }
        }

        if (isPlayerContactingCollectablePhosphorus(bodyA, bodyB)) {
            if (bodyA.getUserData() instanceof CollectablePhosphorus) {
                Score.collectPhosphorus();

                if (!SettingsScreen.isMuted)
                    GameAudio.playCollectablePhosphorusPickedSound();

                ((CollectablePhosphorus) bodyA.getUserData()).dispose();
                Score.collectedPhosphorusCounter++;
                bodyA.setUserData("dead");
            } else {
                Score.collectPhosphorus();

                if (!SettingsScreen.isMuted)
                    GameAudio.playCollectablePhosphorusPickedSound();

                ((CollectablePhosphorus) bodyB.getUserData()).dispose();
                Score.collectedPhosphorusCounter++;
                bodyB.setUserData("dead");
            }
        }

        if (isPlayerContactingMicrobe(bodyA, bodyB)) {
            if (!Player.immortal) {
                Player.loseHitpoint();

                if (!SettingsScreen.isMuted)
                    GameAudio.playLoseLifeSound();
            }

            if (bodyA.getUserData() instanceof Microbe) {
                ((Microbe) bodyA.getUserData()).dispose();
                ((Microbe) bodyA.getUserData()).die();
                bodyA.setUserData("dead");
            } else {
                ((Microbe) bodyB.getUserData()).dispose();
                ((Microbe) bodyB.getUserData()).die();
                bodyB.setUserData("dead");
            }
        }

        if (isItemContactingWall(bodyA, bodyB)) {
            if (bodyA.getUserData() instanceof Wall) {
                Wall wall = (Wall) bodyA.getUserData();
                if (wall.thisLayer.getName().equals("cleaner-area")) {
                    if (GameUtil.room == 1) {
                        if (!SettingsScreen.isMuted)
                            GameAudio.playVacuumSound(0.5f);
                    }

                    if (bodyB.getUserData() instanceof RareItem) {
                        Score.collectRareItem();
                        ((RareItem) bodyB.getUserData()).dispose();


                        if (GameUtil.room == 1) {
                            if (!SettingsScreen.isMuted)
                                GameAudio.playRareItemPickedSound();
                        }
                    } else if (!(bodyB.getUserData() instanceof RareItem)){
                        Score.collectItem();
                        ((Item) bodyB.getUserData()).dispose();

                    }

                    ((Item) bodyB.getUserData()).dispose();
                    bodyB.setUserData("dead");
                }
            } else if (bodyB.getUserData() instanceof Wall) {
                Wall wall = (Wall) bodyB.getUserData();
                if (wall.thisLayer.getName().equals("cleaner-area")) {
                    if (GameUtil.room == 1) {
                        if (!SettingsScreen.isMuted)
                            GameAudio.playVacuumSound(0.5f);
                    }

                    if (bodyA.getUserData() instanceof RareItem) {
                        Score.collectRareItem();
                        ((RareItem) bodyA.getUserData()).dispose();

                        if (GameUtil.room ==1) {
                            if (!SettingsScreen.isMuted)
                                GameAudio.playRareItemPickedSound();
                        }
                    } else if (!(bodyA.getUserData() instanceof RareItem)) {
                        Score.collectItem();
                        ((Item) bodyA.getUserData()).dispose();
                    }

                    bodyA.setUserData("dead");
                }
            }
        }

        if (isPlayerContactingItem(bodyA, bodyB)) {
            if (!SettingsScreen.isMuted)
                GameAudio.playHitItemSound();
        }
    }

    @Override
    public void endContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        if (isPlayerContactingPipe(bodyA, bodyB)) {
            if (bodyA.getUserData() instanceof Pipe) {
                ((Pipe) bodyA.getUserData()).isTouched = false;

                if (!SettingsScreen.isMuted)
                    GameAudio.playFixSound.stop();
            } else if (bodyB.getUserData() instanceof Pipe) {
                ((Pipe) bodyB.getUserData()).isTouched = false;

                if (!SettingsScreen.isMuted)
                    GameAudio.playFixSound.stop();
            }
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
