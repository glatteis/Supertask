package me.glatteis.supertask.objects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import me.glatteis.supertask.Supertask;
import me.glatteis.supertask.character.Character;
import me.glatteis.supertask.constants.Constants;
import me.glatteis.supertask.items.Item;

import java.util.Arrays;

public class Player {

    private Supertask supertask;
    private Animation animation;
    private Body body;
    private World world;
    private boolean isWalking;
    private Direction direction;
    private TextureRegion[] textures;
    private float stateTime;
    private int collidingLadders = 0;
    private int collidingFixtures = 0;
    private boolean flip;


    private Character character;
    private PlayerStats playerStats;
    private PlayerItemHandler itemHandler;

    private int jumpCount;
    private int maxJumpCount = 1;

    public void resetJumpCount() {
        if (!isOnGround()) return;
        jumpCount = 0;
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public World getWorld() {
        return world;
    }

    public boolean ladderTouch() {
        return collidingLadders > 0;
    }

    public void addLadderTouch() {
        collidingLadders++;
    }

    public void removeLadderTouch() {
        collidingLadders--;
    }

    public void addCollidingFixture() {
        collidingFixtures++;
    }

    public void removeCollidingFixture() {
        collidingFixtures--;
    }

    public boolean isOnGround() {
        return collidingFixtures != 0;
    }

    public Body getBody() {
        return body;
    }

    public boolean getFlip() {
        return flip;
    }

    public PlayerItemHandler getItemHandler() {
        return itemHandler;
    }

    public Player(World w, float x, float y, Supertask supertask, Character character) {
        this.supertask = supertask;
        this.world = w;

        this.character = character;
        this.playerStats = character.getStats();

        itemHandler = new PlayerItemHandler(this);

        for (Item i : character.getItems()) {
            itemHandler.pickUp(i);
        }

        BodyDef def = new BodyDef();
        def.position.set(x, y);
        def.type = BodyDef.BodyType.DynamicBody;
        def.gravityScale = 2;

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.8f, .8f);
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.5f;
        fixtureDef.filter.categoryBits = Constants.COLLISION_PLAYER;
        body = world.createBody(def);
        body.createFixture(fixtureDef).setUserData("player");

        FixtureDef footDef = new FixtureDef();
        PolygonShape footShape = new PolygonShape();
        footShape.setAsBox(.7f, .1f, new Vector2(0, -.8f), 0);
        footDef.shape = footShape;
        footDef.isSensor = true;
        footDef.filter.groupIndex = -Constants.COLLISION_PLAYER;
        body.createFixture(footDef).setUserData("foot");

        textures = TextureRegion.split(new Texture(Gdx.files.internal("textures/char/grandpa.png")), 32, 32)[0];

        animation = new Animation(0.25f, Arrays.copyOfRange(textures, 0, 3));
    }

    private void update(float delta) {
        if (supertask.getInputProcessor().isKeyDown(Input.Keys.A)) {
            isWalking = true;
            direction = Direction.LEFT;
            body.setLinearVelocity(-playerStats.speed, body.getLinearVelocity().y);
        } else if (supertask.getInputProcessor().isKeyDown(Input.Keys.D)) {
            isWalking = true;
            direction = Direction.RIGHT;
            body.setLinearVelocity(playerStats.speed, body.getLinearVelocity().y);
        } else if (supertask.getInputProcessor().isKeyDown(Input.Keys.W) && ladderTouch()) {
            isWalking = true;
            direction = Direction.LADDER;
            body.setLinearVelocity(0, 5);
        } else if (direction != null) {
            isWalking = false;
            direction = null;
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }
        if (supertask.getInputProcessor().isKeyDown(Input.Keys.ENTER)) {
            itemHandler.use();
        }
        if (supertask.getInputProcessor().jumped() && isOnGround()) {
            jumpCount++;
            if (jumpCount > maxJumpCount) return;
            body.applyForceToCenter(new Vector2(0, playerStats.jumpForce), true);
            itemHandler.jump();
        }
        itemHandler.tick(delta);
    }

    public void render(float delta, SpriteBatch batch) {
        update(delta);
        stateTime += delta;
        if (stateTime >= 0.75f) stateTime-= 0.75f;
        TextureRegion renderFrame;
        if (isWalking && isOnGround()) {
            renderFrame = animation.getKeyFrame(stateTime, true);
        } else {
            renderFrame = textures[0];
        }
        if (!ladderTouch()) {
            if (!renderFrame.isFlipX() && direction != null && direction.equals(Direction.LEFT)) {
                renderFrame.flip(true, false);
            } else if (renderFrame.isFlipX() && direction != null && direction.equals(Direction.RIGHT)) {
                renderFrame.flip(true, false);
            }
            flip = renderFrame.isFlipX();
        }
        batch.begin();
        batch.draw(renderFrame, body.getPosition().x - 1, body.getPosition().y - .8f, 2, 2);
        batch.end();
        itemHandler.render(batch);
    }


    public enum Direction {
        LEFT, RIGHT, LADDER;
    }
}
