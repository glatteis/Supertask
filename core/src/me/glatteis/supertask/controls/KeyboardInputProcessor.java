package me.glatteis.supertask.controls;

import com.badlogic.gdx.Gdx;
import me.glatteis.supertask.Supertask;

/**
 * Created by Linus on 13.12.2015.
 */
public class KeyboardInputProcessor implements PlatformInputProcessor {

    private Supertask supertask;

    public KeyboardInputProcessor(Supertask supertask) {
        this.supertask = supertask;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
        supertask.getInputProcessor().setKeyDown(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        supertask.getInputProcessor().setKeyUp(keycode);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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

    public void render(float delta) {

    }

    @Override
    public void update(int screenX, int screenY) {

    }

}
