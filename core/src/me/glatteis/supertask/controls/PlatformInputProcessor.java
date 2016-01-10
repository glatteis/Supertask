package me.glatteis.supertask.controls;

import com.badlogic.gdx.InputProcessor;

/**
 * Created by Linus on 13.12.2015.
 */
public interface PlatformInputProcessor extends InputProcessor {

    public void render(float delta);

    public void update(int screenX, int screenY);

}
