package com.mygdx.game.figures;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Sword {


    public Body body;

    public Sword(float positionX, float positionY, World world) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(positionX, positionY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setBullet(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.35f, 0.005f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        //fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setUserData(this);
    }
}
