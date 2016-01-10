package me.glatteis.supertask.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.glatteis.supertask.items.event.CustomEvent;
import me.glatteis.supertask.objects.Player;

/**
 * Created by Linus on 08.01.2016.
 */
public abstract class Item {

    protected Texture texture;
    protected String name;
    protected String description;

    protected Item(String name, String description, String texture) {
        this.name = name;
        this.description = description;
        this.texture = new Texture(Gdx.files.internal("textures/items/" + texture + ".png"));
    }

    public abstract void init(Player player);
    public abstract void use(Player player);
    public abstract void tick(float delta);

    public void jumped(Player player) {}
    public void render(SpriteBatch batch) {}
    public void customEvent(CustomEvent event) {}
}
