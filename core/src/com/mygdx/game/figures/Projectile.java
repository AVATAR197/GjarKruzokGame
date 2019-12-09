package com.mygdx.game.figures;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Projectile {

    private Body body;

    public Projectile(float positionX, float positionY, World world) {
        BodyDef bDef = new BodyDef();
        bDef.position.set(positionX, positionY);
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bDef);
        body.setGravityScale(0);
        body.setBullet(true);


        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.2f);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = circleShape;
        fDef.density = 0.2f;

        body.setUserData(this);
        body.createFixture(fDef);
        circleShape.dispose();
    }

    public void projectileShot(boolean isFacingRight) {
        float speed = 10f;

        if(isFacingRight) {
            body.applyForceToCenter(speed, 0f, true);
        } else {
            body.applyForceToCenter(-speed, 0f, true);
        }
    }
}
