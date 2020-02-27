package com.mygdx.game.figures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
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
    private MapObject mapObject;
    public Boolean isDestroyed = false;
    private ContactListener contactListener;

    public Coin(World world, Shape shape, MapObject object) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bd);
        body.createFixture(shape, 1).setUserData("coin");
        body.setUserData(this);

        mapObject = object;
    }

    public void consume(World world) {
        world.destroyBody(body);
    }

    public void setForConsume() {isDestroyed = true;}

    public void getMapObjectOfCoin() {}
}
