package me.glatteis.supertask.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import me.glatteis.supertask.Supertask;
import me.glatteis.supertask.character.Grandpa;
import me.glatteis.supertask.items.ItemDrop;
import me.glatteis.supertask.items.ItemDropHandler;
import me.glatteis.supertask.items.event.BulletHitWallEvent;
import me.glatteis.supertask.objects.Bullet;
import me.glatteis.supertask.objects.Level;
import me.glatteis.supertask.objects.Player;

/**
 * Created by Linus on 09.12.2015.
 */
public class WorldHandler implements ContactListener {

    private World world;
    private LevelHandler levelHandler;
    protected OrthographicCamera thisCamera;

    private SpriteBatch batch;

    private CameraMovement movement;

    private Player player;
    private boolean playerOnGround;
    private Supertask supertask;

    private ItemDropHandler itemDropHandler;

    private static WorldHandler thisWorldHandler;

    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    public WorldHandler(OrthographicCamera camera, final Supertask supertask, SpriteBatch batch) {

        thisWorldHandler = this;

        this.batch = batch;
        this.supertask = supertask;

        thisCamera = camera;
        world = new World(new Vector2(0, -9.81f), true);
        world.setContactListener(this);

        BodyDef def = new BodyDef();
        def.position.set(0, 0);
        Body b = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(100, 1);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 1;
        b.createFixture(fixtureDef);

        levelHandler = new LevelHandler(world, this);

        player = new Player(world, 0, 10, supertask, new Grandpa());

        movement = new CameraMovement(player, camera);

        itemDropHandler = new ItemDropHandler(this);
    }

    public static void debug() {
        for (Level l : thisWorldHandler.levelHandler.levels) {
            Filter f = l.getBody().getFixtureList().get(0).getFilterData();
        }
    }

    public void render(float delta) {
        movement.update(delta);
        batch.setProjectionMatrix(thisCamera.combined);
        world.step(delta, 8, 2);
        for (Level l : levelHandler.getLevels()) {
            l.render(thisCamera);
        }
        if (player != null) {
            player.render(delta, batch);
            if (player.getBody().getPosition().y > levelHandler.getLevels().get(levelHandler.getLevels().size() - 1).getBody().getWorldCenter().y) {
                levelHandler.spawnNewLevel();
            }
        }
        itemDropHandler.render(batch);
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture[] fixtures = {contact.getFixtureA(), contact.getFixtureB()};
        for (int i = 0; i < fixtures.length; i++) {
            Fixture f = fixtures[i];
            Fixture f2 = fixtures[i == 0 ? 1 : 0];
            if (f.getUserData() == null) continue;
            if (f.getUserData().equals("player") && f2.getUserData() instanceof Level.LevelUserData) {
                player.resetJumpCount();
            }
            if (f.getUserData().equals("foot") && f2.getUserData() instanceof Level.LevelUserData) {
                player.addCollidingFixture();
            }
            if (f.getUserData().equals("player") && f2.getUserData() instanceof Level.LevelUserData) {
                if (((Level.LevelUserData) f2.getUserData()).type.equals("ladders")) {
                    player.addLadderTouch();
                }
            }
            if (f.getUserData() instanceof Level.LevelUserData && ((Level.LevelUserData)f.getUserData()).level.getBody().getType().equals(BodyDef.BodyType.DynamicBody)) {
                final Fixture finalF = f;
                Timer t = new Timer();
                t.scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        levelHandler.newLevelFellDown(((Level.LevelUserData)finalF.getUserData()).level);
                    }
                }, 1);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture[] fixtures = {contact.getFixtureA(), contact.getFixtureB()};
        for (int i = 0; i < fixtures.length; i++) {
            Fixture f = fixtures[i];
            Fixture f2 = fixtures[i == 0 ? 1 : 0];
            if (f.getUserData() == null) continue;
            if (f.getUserData().equals("player") && f2.getUserData() instanceof Level.LevelUserData) {
                if (((Level.LevelUserData) f2.getUserData()).type.equals("ladders")) {
                    player.removeLadderTouch();
                }
            }
            if (f.getUserData().equals("foot") && f2.getUserData() instanceof Level.LevelUserData) {
                player.removeCollidingFixture();
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture[] fixtures = {contact.getFixtureA(), contact.getFixtureB()};
        for (int i = 0; i < fixtures.length; i++) {
            final Fixture f = fixtures[i];
            final Fixture f2 = fixtures[i == 0 ? 1 : 0];
            if (f.getUserData() == null) continue;
            if (f.getUserData() instanceof ItemDrop && f2.getUserData().equals("player")) {
                itemDropHandler.pickUp((ItemDrop)f.getUserData());
                contact.setEnabled(false);
            }
            if (f.getUserData() instanceof Bullet) {
                if (f2.getUserData().equals("player")) {
                    contact.setEnabled(false);
                    return;
                }
                if (f2.getUserData() instanceof Level.LevelUserData) {
                    if (((Level.LevelUserData) f2.getUserData()).type.equals("ladders")) {
                        contact.setEnabled(false);
                        return;
                    }
                }
                Timer.post(new Timer.Task() {
                    @Override
                    public void run() {
                        player.getItemHandler().handleEvent(new BulletHitWallEvent((Bullet)f.getUserData()));
                    }
                });


            }
            if (f.getUserData().equals("player") && f2.getUserData() instanceof Level.LevelUserData) {
                if (((Level.LevelUserData) f2.getUserData()).type.equals("ladders")) {
                    contact.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
