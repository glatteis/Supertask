package me.glatteis.supertask.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import me.glatteis.supertask.handlers.WorldHandler;

import java.util.ArrayList;

/**
 * Created by Linus on 08.01.2016.
 */
public class ItemDropHandler {

    private ArrayList<ItemDrop> itemDrops = new ArrayList<ItemDrop>();
    private WorldHandler worldHandler;

    public ItemDropHandler(WorldHandler worldHandler) {
        this.worldHandler = worldHandler;
        itemDrops.add(new ItemDrop(ItemDatabase.newRandomItem(), worldHandler.getWorld(), new Vector2(5, 5)));
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        for (ItemDrop drop : itemDrops) {
            drop.render(batch);
        }
        batch.end();
    }

    public void pickUp(final ItemDrop itemDrop) {
        Timer.post(new Timer.Task() {
            @Override
            public void run() {
                itemDrops.remove(itemDrop);
                worldHandler.getPlayer().getItemHandler().pickUp(itemDrop.dispose());
            }
        });
    }

}
