package me.glatteis.supertask.items;

import me.glatteis.supertask.objects.Player;

/**
 * Created by Linus on 08.01.2016.
 */
public class SpringShoes extends Item {

    protected SpringShoes() {
        super("Spring Shoes", "Jump higher!", "springshoes");
    }


    @Override
    public void init(Player player) {
        player.getPlayerStats().jumpForce += 200;
    }

    @Override
    public void use(Player player) {

    }

    @Override
    public void tick(float delta) {

    }

    @Override
    public void jumped(Player player) {

    }
}
