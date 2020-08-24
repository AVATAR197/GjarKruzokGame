package com.mygdx.game.figures;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Coin {
    public Body body;
    public Boolean isDestroyed = false;

    public Coin(World world, Shape shape, MapObject object) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bd);
        Fixture fixture = body.createFixture(shape, 1);
        fixture.setUserData("coin");
        fixture.setSensor(true);
        body.setUserData(this);
    }

    public void consume(World world) {
        world.destroyBody(body);
    }

    public void setForConsume() {isDestroyed = true;}
}
