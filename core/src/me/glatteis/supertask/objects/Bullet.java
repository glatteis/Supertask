package me.glatteis.supertask.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import me.glatteis.supertask.constants.Constants;
import me.glatteis.supertask.items.Gun;

/**
 * Created by Linus on 07.01.2016.
 */
public class Bullet {

    private Body body;
    private Gun item;
    private float damage;
    private Sprite sprite;
    private World world;

    public float getDamage() {
        return damage;
    }

    public Bullet(Vector2 velocity, Vector2 position, float damage, World world, Gun item) {
        this.world = world;
        this.item = item;
        BodyDef bulletDef = new BodyDef();
        bulletDef.type = BodyDef.BodyType.DynamicBody;
        bulletDef.bullet = true;
        bulletDef.linearVelocity.set(velocity);
        bulletDef.position.set(position);
        bulletDef.gravityScale = 0;

        sprite = new Sprite(new Texture(Gdx.files.internal("textures/bullet.png")));
        sprite.setPosition(position.x, position.y);

        sprite.setSize(0.5f, 0.5f);

        FixtureDef def = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(0.25f);
        def.shape = shape;
        def.filter.categoryBits = Constants.COLLISION_BULLET;
        def.filter.maskBits = -Constants.COLLISION_PLAYER;
        def.filter.groupIndex = Constants.COLLISION_LEVEL;

        body = world.createBody(bulletDef);
        body.createFixture(def).setUserData(this);
    }

    public void render(SpriteBatch batch) {
        sprite.setPosition(body.getPosition().x - 0.25f, body.getPosition().y - 0.25f);
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    public void dispose() {
        Timer.post(new Timer.Task() {
            @Override
            public void run() {
                world.destroyBody(body);
            }
        });
    }

}
