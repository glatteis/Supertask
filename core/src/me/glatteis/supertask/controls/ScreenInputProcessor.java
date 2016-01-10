package me.glatteis.supertask.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.glatteis.supertask.Supertask;

public class ScreenInputProcessor implements PlatformInputProcessor {

    Supertask supertask;
    private Touchpad touchpad;
    private Viewport port;
    private OrthographicCamera camera;
    private Stage stage;

    private boolean positionTouched = false;
    private boolean jumpTouched = false;

    private Button buttonUp;
    private Button buttonLeft;
    private Button buttonRight;

    private Rectangle up;
    private Rectangle left;
    private Rectangle right;

    public ScreenInputProcessor(Supertask supertask) {
        this.supertask = supertask;

        camera = new OrthographicCamera(480, 320);
        camera.translate(480 / 2, 320 / 2);

        stage = new Stage();
        stage.setViewport(new FitViewport(480, 320, camera));

        TextureRegion up = new TextureRegion(new Texture(Gdx.files.internal("textures/controls/arrowUp.png")));
        TextureRegion left = new TextureRegion(new Texture(Gdx.files.internal("textures/controls/arrowLeft.png")));
        TextureRegion right = new TextureRegion(new Texture(Gdx.files.internal("textures/controls/arrowRight.png")));


        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = style.down = new TextureRegionDrawable(up);

        buttonUp = new Button(style);
        buttonUp.setBounds(400, 50, 50, 50);
        this.up = new Rectangle(400, 50, 50, 50);

        style = new Button.ButtonStyle();
        style.up = style.down = new TextureRegionDrawable(left);

        buttonLeft = new Button(style);
        buttonLeft.setBounds(20, 50, 50, 50);
        this.left = new Rectangle(20, 50, 50, 50);

        style = new Button.ButtonStyle();
        style.up = style.down = new TextureRegionDrawable(right);

        buttonRight = new Button(style);
        buttonRight.setBounds(100, 50, 50, 50);
        this.right = new Rectangle(100, 50, 50, 50);

        buttonUp.addAction(Actions.alpha(0.5f));
        buttonLeft.addAction(Actions.alpha(0.5f));
        buttonRight.addAction(Actions.alpha(0.5f));

        stage.addActor(buttonUp);
        stage.addActor(buttonLeft);
        stage.addActor(buttonRight);

        Gdx.input.setInputProcessor(this);
    }

    public void render(float delta) {
        stage.getViewport().apply(true);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void update(int screenX, int screenY) {
        stage.getViewport().update(screenX, screenY);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 realCoords = stage.getViewport().unproject(new Vector2(screenX, screenY));
        if (up.contains(realCoords)) {
            jumpTouched = true;
            handleEvent(0, true);
        } else if (left.contains(realCoords)) {
            handleEvent(2, true);
            positionTouched = true;
        } else if (right.contains(realCoords)) {
            handleEvent(1, true);
            positionTouched = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println(screenX + " " + screenY);
        Vector2 realCoords = stage.getViewport().unproject(new Vector2(screenX, screenY));
        System.out.println(realCoords.x + " " + realCoords.y);
        if (up.contains(realCoords)) {
            if (!jumpTouched && positionTouched) {
                handleEvent(1, false);
                handleEvent(2, false);
                positionTouched = false;
            }
            jumpTouched = false;
            handleEvent(0, false);
        } else if (left.contains(realCoords) || right.contains(realCoords)) {
            if (!positionTouched && jumpTouched) {
                jumpTouched = false;
            }
            handleEvent(1, false);
            handleEvent(2, false);
            positionTouched = false;
        }
        else {
            if (positionTouched && !jumpTouched) {
                handleEvent(1, false);
                handleEvent(2, false);
                positionTouched = false;
            } else if (positionTouched && jumpTouched) {
                if (realCoords.x < stage.getViewport().getScreenWidth()) {
                    handleEvent(1, false);
                    handleEvent(2, false);
                    positionTouched = false;
                } else {
                    handleEvent(0, false);
                    jumpTouched = false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 realCoords = stage.getViewport().unproject(new Vector2(screenX, screenY));

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private void handleEvent(int action, boolean touchDown) {
        switch (action) {
            case 0: //JUMP OR LADDER
                if (touchDown) {
                    supertask.getInputProcessor().setKeyDown(Input.Keys.SPACE);
                    supertask.getInputProcessor().setKeyDown(Input.Keys.W);
                }
                else {
                    supertask.getInputProcessor().setKeyUp(Input.Keys.SPACE);
                    supertask.getInputProcessor().setKeyUp(Input.Keys.W);
                }
                break;
            case 1: //RIGHT
                if (touchDown) supertask.getInputProcessor().setKeyDown(Input.Keys.D);
                else supertask.getInputProcessor().setKeyUp(Input.Keys.D);
                break;
            case 2: //LEFT
                if (touchDown) supertask.getInputProcessor().setKeyDown(Input.Keys.A);
                else supertask.getInputProcessor().setKeyUp(Input.Keys.A);
                break;
        }
    }

}
