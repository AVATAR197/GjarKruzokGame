package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.figures.Coin;
import com.mygdx.game.utils.TileMapRenderer;

public class Level {

    //tiles
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private TiledMap map;
    private World world;
    private Array<Coin> coins;
    private Array<Coin> arrayToAddCoins;


    public Level(World world, float PPM) {
        //rendering all the tiles of you TiledMap
        map = new TmxMapLoader().load("level-2.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);

        //creating objects with tileMapRenderer
        TileMapRenderer.buildShapes(map, world, PPM);
        TileMapRenderer.buildCoins(map, world, PPM);

        coins = TileMapRenderer.coinsArray;

        arrayToAddCoins = new Array<Coin>();
    }

    public void draw(SpriteBatch spriteBatch, OrthographicCamera camera, World world) {
        update(camera, world);

        //rendering tiles
        tiledMapRenderer.render();
    }

    private void update(OrthographicCamera camera, World world) {
        tiledMapRenderer.setView(camera);

        for(int i = 0; i < coins.size; i++) {
            if (coins.get(i).isDestroyed) {
                coins.get(i).consume(world);
            } else {
                arrayToAddCoins.add(coins.get(i));
            }
        }
        coins.removeRange(0, coins.size - 1 );
        coins.addAll(arrayToAddCoins);

    }
}

