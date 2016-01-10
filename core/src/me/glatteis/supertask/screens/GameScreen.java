package me.glatteis.supertask.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.glatteis.supertask.Supertask;
import me.glatteis.supertask.controls.KeyboardInputProcessor;
import me.glatteis.supertask.controls.PlatformInputProcessor;
import me.glatteis.supertask.controls.ScreenInputProcessor;
import me.glatteis.supertask.handlers.WorldHandler;

/**
 * Created by Linus on 09.12.2015.
 */
public class GameScreen implements Screen {

    private OrthographicCamera camera;

    private Box2DDebugRenderer renderer;
    private Viewport port;
    private WorldHandler worldHandler;

    private PlatformInputProcessor platformInputProcessor;

    private SpriteBatch batch;

    private Supertask supertask;

    public GameScreen(Supertask supertask) {
        this.supertask = supertask;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        renderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(420, 360);
        port = new ExtendViewport(420, 320);
        port.setCamera(camera);

        worldHandler = new WorldHandler(camera, supertask, batch);

        switch (Gdx.app.getType()) {
            case Android:
                platformInputProcessor = new ScreenInputProcessor(supertask);
                break;
            default:
                platformInputProcessor = new KeyboardInputProcessor(supertask);
                break;
        }

    }

    @Override
    public void render(float delta) {
        port.apply();
        worldHandler.render(delta);
        //renderer.render(worldHandler.getWorld(), camera.combined);
        platformInputProcessor.render(delta);

    }

    @Override
    public void resize(int width, int height) {
        port.update(width, height);
        platformInputProcessor.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
