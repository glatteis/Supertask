package me.glatteis.supertask.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.glatteis.supertask.items.Item;
import me.glatteis.supertask.items.event.CustomEvent;

import java.util.ArrayList;

/**
 * Created by Linus on 08.01.2016.
 */
public class PlayerItemHandler {
    private ArrayList<Item> items = new ArrayList<Item>();
    private Player player;

    public PlayerItemHandler(Player player) {
        this.player = player;
    }

    public void jump() {
        for (Item i : items) {
            i.jumped(player);
        }
    }

    public void render(SpriteBatch batch) {
        for (Item i : items) {
            i.render(batch);
        }
    }

    public void handleEvent(CustomEvent event) {
        for (Item i : items) {
            i.customEvent(event);
        }
    }

    public void use() {
        for (Item i : items) {
            i.use(player);
        }
    }

    public void tick(float delta) {
        for (Item i : items) {
            i.tick(delta);
        }
    }

    public void pickUp(Item item) {
        items.add(item);
        item.init(player);
    }
}
