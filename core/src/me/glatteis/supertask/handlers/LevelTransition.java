package me.glatteis.supertask.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import me.glatteis.supertask.constants.Constants;
import me.glatteis.supertask.objects.Level;

/**
 * Created by Linus on 16.12.2015.
 */
public class LevelTransition {

    private LevelHandler handler;
    private OrthographicCamera camera;

    public LevelTransition(LevelHandler handler, OrthographicCamera camera) {
        this.camera = camera;
        this.handler = handler;
        camera.zoom = 0.1f;
        camera.update();
    }

    public void transition(Level nextLevel) {
        camera.position.set(new Vector3(nextLevel.getBody().getWorldCenter().x, nextLevel.getBody().getWorldCenter().y + nextLevel.getHeight(), 0).scl(1 / Constants.PIXELS_TO_TILES));
        camera.update();
    }

}