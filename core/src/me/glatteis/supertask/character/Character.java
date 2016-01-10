package me.glatteis.supertask.character;

import me.glatteis.supertask.items.Item;
import me.glatteis.supertask.objects.PlayerStats;

import java.util.List;

/**
 * Created by Linus on 10.01.2016.
 */
public abstract class Character {

    public abstract List<Item> getItems();
    public abstract PlayerStats getStats();

}
