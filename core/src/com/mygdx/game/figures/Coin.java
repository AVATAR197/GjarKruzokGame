package com.mygdx.game.figures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Level;
import com.mygdx.game.Project1;

public class Coin {
    public Body body;
    public Boolean isDestroyed = false;
    public MapObject coinObject;
    private Map coinMap;
    private Fixture fixture;
    private Rectangle bounds;

    public Coin(World world, Shape shape, MapObject object, Map map) {
        this.bounds = ((RectangleMapObject) object).getRectangle();
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        bd.position.set((bounds.getX() + bounds.getWidth() / 2) / Project1.PPM, (bounds.getY() + bounds.getHeight() / 2) / Project1.PPM);
        body = world.createBody(bd);
        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(bounds.getWidth() / 2 / Project1.PPM, bounds.getHeight() / 2 / Project1.PPM);

        fixture = body.createFixture(polyShape, 1);
        fixture.setUserData("coin");
        fixture.setSensor(true);
        body.setUserData(this);

        coinMap = map;
        coinObject = object;


    }

    public void consume(World world) {
        world.destroyBody(body);
    }

    public MapObject getCoinObject() {
        return coinObject;
    }

    public void setForConsume() {isDestroyed = true;}

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) coinMap.getLayers().get(2);
        return layer.getCell((int)(body.getPosition().x * Project1.PPM / 16f),
                (int)(body.getPosition().y * Project1.PPM / 16f));
    }
}
