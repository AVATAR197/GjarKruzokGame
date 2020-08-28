package com.mygdx.game.figures;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet {
    private Body body;
    public float SPEED = 200f;
    public static final float BULLET_HEIGHT = 0.9f;
    private static TextureAtlas textureAtlas;
    private float stateTime;

    Animation<TextureRegion> flyingAnimation;

    public boolean remove = false;

    public Bullet (float x, float y, boolean isFacingRight, World world) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.gravityScale = 0f;
        bd.position.set(x, y);
        body = world.createBody(bd);

        CircleShape shape = new CircleShape();
        shape.setRadius(BULLET_HEIGHT/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.isSensor = true;
        fixtureDef.friction = 0;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("bullet");
        body.setUserData(this);

        if (textureAtlas == null) {
            textureAtlas = new TextureAtlas("bullet.atlas");
        }
        flyingAnimation = new Animation<TextureRegion>(0.07f, textureAtlas.findRegions("FB001"), Animation.PlayMode.LOOP);
        if (!isFacingRight) {
            SPEED *= -1;
            flipAnimation();
        };
    }

    public void flipAnimation () {
        for (int i = 0; i < flyingAnimation.getKeyFrames().length; i++) {
            flyingAnimation.getKeyFrames()[i].flip(true, false);
        }
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
    }

    public void setToRemove() {
        remove = true;
    }

    public void destroy(World world) {
        world.destroyBody(body);
    }

    public void shoot() {
        body.applyForceToCenter(SPEED, 0, true);
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void render (SpriteBatch spriteBatch, float deltaTime) {
        TextureRegion currentFrame;
        currentFrame = flyingAnimation.getKeyFrame(stateTime, true);
        float frameWidth = (float) currentFrame.getRegionWidth() / (float) currentFrame.getRegionHeight() * BULLET_HEIGHT;
        spriteBatch.draw(currentFrame, getPosition().x - frameWidth/2, getPosition().y - 0.5f, frameWidth, BULLET_HEIGHT);
    }
}
