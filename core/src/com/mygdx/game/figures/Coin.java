package com.mygdx.game.figures;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Coin {
    public Body body;

    public Coin(World world, Shape shape) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bd);
        body.createFixture(shape, 1);
    }

    public void consume(World world) {
        world.destroyBody(body);
    }
}
