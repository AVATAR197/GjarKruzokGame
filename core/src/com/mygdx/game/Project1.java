package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.figures.Character;

public class Project1 extends ApplicationAdapter {
	//spritebatch definition
	private SpriteBatch spriteBatch;

	//world
	private World world;

    private float worldWidth;
    private float worldHeight;

	//camera definition
	private OrthographicCamera camera;
	private float cameraHeight;
	private float cameraWidth;
	private Box2DDebugRenderer debugRenderer;

	private float PPM = 16f;
	//character
	Character character;
	//level
	private Level level;

	private GameListener listener;

	public Project1 (GameListener listener) {
		super();
		this.listener = listener;
	}

	public Project1 () {
		super();
	}

	@Override
	public void create () {
		float w  = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		//camera size
		cameraHeight = 15f;
		cameraWidth = cameraHeight * (w / h);

        //world size
        worldWidth = 250 * 128 / PPM;
        worldHeight = 50 * 128 / PPM;

		//camera definiton
		camera = new OrthographicCamera(cameraWidth, cameraHeight);
		//camera position set
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		//updating the camera -- very important
		camera.update();
		//world creation
		world = new World(new Vector2(0, -9.81f), true);


		//contact listener
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {


				//listener.updateScore(score);
			}

			@Override
			public void endContact(Contact contact) {

			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {

			}
		});

		//spriteBatch + debugRenderer
		spriteBatch = new SpriteBatch();
		debugRenderer = new Box2DDebugRenderer();

		//creating character
		character = new Character(world);

		//creating level
		level = new Level(world, PPM);
	}

	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();
		//running update function
		update(deltaTime);
		//handling user input
		handleUserInput();
		//setting the backgound to red color
		Gdx.gl.glClearColor(1, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		level.draw(spriteBatch, camera);

		//drawing objects
		spriteBatch.begin();
		//drawing character
		character.draw(spriteBatch, deltaTime);
		spriteBatch.end();

		//debugRenderer
		//debugRenderer.render(world, camera.combined);
	}

	public void update(float deltaTime) {
		world.step(1/60f, 6, 2);
		//updating camera
		updateCameraPosition();
		//updating camera
		camera.update();
		//updating character
		character.setGrounded(isPlayerGrounded());
		character.update(world, deltaTime);

		spriteBatch.setProjectionMatrix(camera.combined);
	}
	private void handleUserInput() {
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
			character.moveLeft();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			character.moveRight();
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
			character.jump();
		if(Gdx.input.isKeyPressed(Input.Keys.A))
			character.attack();
		if(Gdx.input.isKeyPressed(Input.Keys.S))
			character.shoot(world);
		//todo make shooting one shot every 0.2s
	}
	private void updateCameraPosition() {
		if(character.getPostion().x > cameraWidth / 2 && character.getPostion().x < worldWidth - cameraWidth / 2) {
			camera.position.x = character.getPostion().x;
		}
		if(character.getPostion().y > cameraHeight / 2 && character.getPostion().y < worldHeight - cameraHeight / 2) {
			camera.position.y = character.getPostion().y;
		}
	}
	private boolean isPlayerGrounded() {
		Array<Contact> contactList = world.getContactList();
		for(int i = 0; i < contactList.size; i++) {
			Contact contact = contactList.get(i);
			if(contact.isTouching() && (contact.getFixtureA() == character.getSensorFixture() || contact.getFixtureB() == character.getSensorFixture())) {
				Vector2 position = character.getPostion();
				WorldManifold manifold = contact.getWorldManifold();
				boolean touching = true;
				for (int j = 0; j < manifold.getNumberOfContactPoints(); j++) {
					touching &= (manifold.getPoints()[j].y < position.y + 0.0001f);
				}

				return touching;
			}
		}
		return false;
	}

    @Override
    public void dispose () {
        //disposing the content for the better performance of the game
        world.dispose();
        spriteBatch.dispose();
        debugRenderer.dispose();
    }
}
