package me.glatteis.supertask.handlers;

import com.badlogic.gdx.Input;

import java.util.ArrayList;

/**
 * Created by Linus on 13.12.2015.
 */
public class SupertaskInputProcessor { //We have a independent processor because we have input methods for Android and Desktop

    private ArrayList<Integer> keysDown = new ArrayList<Integer>();

    public boolean jumped() { //If this method is called, the spacebar is removed from the pressed keys.
        if (keysDown.contains(Input.Keys.SPACE)) {
            keysDown.remove((Integer) Input.Keys.SPACE);
            return true;
        }
        return false;
    }

    public void setKeyDown(int key) {
        if (key == Input.Keys.F4) WorldHandler.debug();
        keysDown.add(key);
    }

    public void setKeyUp(int key) {
        keysDown.remove((Integer) key);
    }

    public boolean isKeyDown(int key) {
        return keysDown.contains(key);
    }



}
