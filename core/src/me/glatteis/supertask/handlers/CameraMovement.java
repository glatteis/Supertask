package me.glatteis.supertask.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import me.glatteis.supertask.objects.Player;

/**
 * Created by Linus on 16.12.2015.
 */
public class CameraMovement {

    private Player player;
    private OrthographicCamera camera;
    private Vector2 velocity;
    private float waitTime;

    public CameraMovement(Player player, OrthographicCamera camera) {
        this.camera = camera;
        this.player = player;
        velocity = new Vector2();
        camera.position.set(player.getBody().getPosition(), 0);
        camera.zoom = 0.1f;
        waitTime = 0;
    }

    public void update(float delta) {
        camera.position.set(player.getBody().getPosition(), 0);
        if (player.getBody().getLinearVelocity().isZero(1)) {
            waitTime += delta;

            if (waitTime > 10 && camera.zoom > 0.05f) {
                camera.zoom = camera.zoom - 0.5f * delta * delta;
            }
        } else {
            waitTime = 0;
            if (camera.zoom < 0.1f) {
                camera.zoom += 4 * delta * delta;
            }
        }
        camera.update();
    }

}
