package me.glatteis.supertask.character;

import me.glatteis.supertask.items.Gun;
import me.glatteis.supertask.items.Item;
import me.glatteis.supertask.objects.PlayerStats;

import java.util.Collections;
import java.util.List;

/**
 * Created by Linus on 10.01.2016.
 */
public class Grandpa extends Character {
    @Override
    public List<Item> getItems() {
        return Collections.singletonList((Item) new Gun());
    }

    @Override
    public PlayerStats getStats() {
        return new PlayerStats(6, 1, 600);
    }
}
