package me.glatteis.supertask.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Linus on 08.01.2016.
 */
public class ItemDrop {

    private Item item;
    private Body body;
    private World world;

    public Item dispose() {
        world.destroyBody(body);
        return item;
    }

    public ItemDrop(Item item, World world, Vector2 position) {
        this.item = item;
        BodyDef itemDef = new BodyDef();
        itemDef.type = BodyDef.BodyType.DynamicBody;
        itemDef.gravityScale = 0;
        itemDef.position.set(position);

        FixtureDef itemFixtureDef = new FixtureDef();
        PolygonShape itemShape = new PolygonShape();
        itemShape.setAsBox(2, 2);
        itemFixtureDef.shape = itemShape;

        body = world.createBody(itemDef);
        body.createFixture(itemFixtureDef).setUserData(this);

        this.world = world;
    }

    public void render(SpriteBatch batch) {
        batch.draw(item.texture, body.getPosition().x - 50 / 32, body.getPosition().y - 50 / 32, 100 / 32, 100 / 32);
    }

}
