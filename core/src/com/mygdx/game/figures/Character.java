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
import com.badlogic.gdx.utils.Array;

public class Character {
    //chatacter body
    private Body body;
    private float stateTime;
    private CharacterState state;
    private float movementStop;
    private static final float MAX_MOVEMENT_TIMEOUT = 0.2f;
    private static final float VELOCITY_MARGIN = 0.05f;
    private boolean isFacingRight = true;

    //basic parameters as height...
    private float characterHeight;
    private float MAX_SPEED = 4f;

    //for contactListener
    private Fixture fixture;

    private boolean grounded;
    //animations
    Animation<TextureRegion> idleAnimation;
    Animation<TextureRegion> runAnimation;
    Animation<TextureRegion> attackAnimation;
    Animation<TextureRegion> runAttackAnimation;
    Animation<TextureRegion> jumpAnimation;
    TextureAtlas textureAtlas;

    //projectile
    private Projectile projectile;
    private Array<Projectile> projectilesArr = new Array<Projectile>();

    public Character(World world) {
        //setting the characterHeight
        this.characterHeight = 1.5f;

        BodyDef bdef = new BodyDef();
        //setting the body to Dynamic body
        bdef.type = BodyDef.BodyType.DynamicBody;
        //giving the starting position for the character
        bdef.position.set(10f, 10f);
        body = world.createBody(bdef);
        //body.setBullet(true);
        body.setUserData("character");

        CircleShape circleShape = new CircleShape();
        //setting the radius of the circle
        circleShape.setRadius(characterHeight / 3);
        FixtureDef fdef = new FixtureDef();
        //giving the fixtureDef shape
        fdef.shape = circleShape;
        //setting the density
        fdef.density = 0.5f;
        fdef.friction = 0f;
        fdef.restitution = 0f;

        fixture = body.createFixture(fdef);
        //body.setUserData(this);
        circleShape.dispose();


        //testing
        initBodies(world);

        //setting the state for beginning
        state = CharacterState.Idle;
        stateTime = 0;
        textureAtlas = new TextureAtlas("Ninja.atlas");
        //idle animation
        idleAnimation = new Animation<TextureRegion>(0.07f, textureAtlas.findRegions("0_Fallen_Angels_Idle"), Animation.PlayMode.LOOP);
        //walking animation
        runAnimation = new Animation<TextureRegion>(0.07f, textureAtlas.findRegions("0_Fallen_Angels_Running"), Animation.PlayMode.LOOP);
        //attack animation
        attackAnimation = new Animation<TextureRegion>(0.07f, textureAtlas.findRegions("0_Fallen_Angels_Slashing"), Animation.PlayMode.NORMAL);
        //jump animation start
        jumpAnimation = new Animation<TextureRegion>(0.07f, textureAtlas.findRegions("0_Fallen_Angels_Jump Loop"), Animation.PlayMode.NORMAL);
        //jumpattack animation
        runAttackAnimation= new Animation<TextureRegion>(0.07f, textureAtlas.findRegions("0_Fallen_Angels_Run Slashing"), Animation.PlayMode.NORMAL);
    }
    public void update(World world, float deltaTime) {
        //animation update
        stateTime += deltaTime;
        if (movementStop <= 0 && body.getLinearVelocity().x != 0) {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        } else {
            movementStop -= deltaTime;
        }

        if ((body.getLinearVelocity().x > VELOCITY_MARGIN || body.getLinearVelocity().x < -VELOCITY_MARGIN)) {
            if (!state.equals(CharacterState.Running) && !state.equals(CharacterState.RunAttack) && !state.equals(CharacterState.Jumping)) {
                state = CharacterState.Running;
                stateTime = 0;
            }
        } else if (state != CharacterState.Idle && state != CharacterState.Attacking && state != CharacterState.RunAttack && state != CharacterState.Jumping) {
                state = CharacterState.Idle;
                stateTime = 0;
        }
        if((attackAnimation.isAnimationFinished(stateTime) && state == CharacterState.Attacking) || (runAttackAnimation.isAnimationFinished(stateTime) && state == CharacterState.RunAttack) || (jumpAnimation.isAnimationFinished(stateTime) && state == CharacterState.Jumping)) {
            state = CharacterState.Idle;
            stateTime = 0;
        }

    }
    public void moveLeft() {
        movementStop = MAX_MOVEMENT_TIMEOUT;

        if(body.getLinearVelocity().x > -MAX_SPEED) {
            //body.applyLinearImpulse(-0.5f, 0f, body.getPosition().x + characterHeight / 2, body.getPosition().y + characterHeight / 2, true);
            body.applyForceToCenter(-80f, 0, true);
                //checking if is the character facing right, if so than flipping the character on Y axis and setting isFacingRight to false
                if(isFacingRight) {
                    flipAnimations();
                    isFacingRight = false;
                }
                //if character speed is more than max speed applying force to the another direction as is character moving that the final speed of the character <= MAX SPEED
                if(body.getLinearVelocity().x <= -MAX_SPEED) {
                    body.applyForceToCenter(-MAX_SPEED + body.getLinearVelocity().x, 0f, true);
                }
        }

    }
    public void moveRight() {
        movementStop = MAX_MOVEMENT_TIMEOUT;

        if(body.getLinearVelocity().x < MAX_SPEED) {
            //body.applyLinearImpulse(0.5f, 0f, body.getPosition().x + characterHeight / 2, body.getPosition().y + characterHeight / 2, true);
            body.applyForceToCenter(80f, 0, true);
            //checking if is the character isn't facing right, if so than flipping the character on Y axis and setting isFacingRight to true
                if(!isFacingRight) {
                    flipAnimations();
                    isFacingRight = true;
                }
                //if character speed is more than max speed applying force to the another direction as is character moving that the final speed of the character <= MAX SPEED
                if(body.getLinearVelocity().x >= MAX_SPEED) {
                    body.applyForceToCenter(-body.getLinearVelocity().x - MAX_SPEED, 0f, true);
                }
        }
    }

    public void attack() {
        if (body.getLinearVelocity().x != 0) {
            state = CharacterState.RunAttack;
            stateTime = 0;
        } else {
            //todo create a rectangle appearing like a sword
            state = CharacterState.Attacking;
            stateTime = 0;
        }
    }
    public void shoot(World world) {
        //todo add some texture to the projectile
        projectile = new Projectile(body.getPosition().x, body.getPosition().y, world);
        projectilesArr.add(projectile);
        projectile.projectileShot(isFacingRight);
    }
    public void jump() {
        //apply the force to the top
            //body.applyLinearImpulse(0f, 0.5f, body.getPosition().x + characterHeight / 2, body.getPosition().y + characterHeight / 2, true);
        if(grounded) {
            body.applyForceToCenter(0f, 100f, true);
            state = CharacterState.Jumping;
            stateTime = 0;
        }
    }
    public Vector2 getPostion() {
        return body.getPosition();
    }

    public void setGrounded(boolean gounded) {
        this.grounded = gounded;
        if(gounded && state == CharacterState.Jumping) {
            state = CharacterState.Idle;
            stateTime = 0;
        }
    }

    private void flipAnimations() {
        //todo make animtaions to array do save space and render them all in one
        for (int i = 0; i < idleAnimation.getKeyFrames().length; i++) {
            idleAnimation.getKeyFrames()[i].flip(true, false);
        }
        for (int i = 0; i < runAnimation.getKeyFrames().length; i++) {
            runAnimation.getKeyFrames()[i].flip(true, false);
        }
        for (int i = 0; i < attackAnimation.getKeyFrames().length; i++) {
            attackAnimation.getKeyFrames()[i].flip(true, false);
        }
        for (int i = 0; i < runAttackAnimation.getKeyFrames().length; i++) {
            runAttackAnimation.getKeyFrames()[i].flip(true, false);
        }
        for (int i = 0; i < jumpAnimation.getKeyFrames().length; i++) {
            jumpAnimation.getKeyFrames()[i].flip(true, false);
        }
    }

    public Fixture getSensorFixture() {
        return fixture;
    }

    private enum CharacterState {
        Idle, Running, Attacking, RunAttack, Jumping
    }
    public void draw(SpriteBatch spriteBatch, float deltaTime) {
        TextureRegion currentFrame;
        switch(state) {
            case Jumping:
                currentFrame = jumpAnimation.getKeyFrame(stateTime, true);
                break;
            case RunAttack:
                currentFrame = runAttackAnimation.getKeyFrame(stateTime, false);
                break;
            case Running:
                currentFrame = runAnimation.getKeyFrame(stateTime, false);
                break;
            case Attacking:
                currentFrame = attackAnimation.getKeyFrame(stateTime, false);
                break;
            default:
                currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        }
        float frameWidth = (float) currentFrame.getRegionWidth() / (float) currentFrame.getRegionHeight() * characterHeight;
        spriteBatch.draw(currentFrame, body.getPosition().x - frameWidth / 2, body.getPosition().y - characterHeight / 2f, frameWidth, characterHeight);
    }

    private void initBodies(World world) {
        Sword sword = new Sword(body.getPosition().x, body.getPosition().y, world);
        //todo connecting together character body + sword body and creating joint
    }
}
