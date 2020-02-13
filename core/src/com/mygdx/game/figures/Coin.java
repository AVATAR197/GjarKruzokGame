package com.mygdx.game.figures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Coin {
    public Body body;
    public Boolean isDestroyed = false;
    private ContactListener contactListener;

    public Coin(World world, Shape shape) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bd);
        body.createFixture(shape, 1);
        body.setUserData("coin");

        createCollisionListener(world);
    }
    private void createCollisionListener(final World world) {
        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if(fixtureA.getBody().getUserData() == "coin" && fixtureB.getBody().getUserData() == "character" || fixtureA.getBody().getUserData() == "character" && fixtureB.getBody().getUserData() == "coin") {
                    isDestroyed = true;
                    //consume(world);
                }
            }

        });
    }

    public void consume(World world) {
        world.destroyBody(body);
    }
}
