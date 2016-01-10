package me.glatteis.supertask.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.glatteis.supertask.items.event.BulletHitWallEvent;
import me.glatteis.supertask.items.event.CustomEvent;
import me.glatteis.supertask.objects.Bullet;
import me.glatteis.supertask.objects.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linus on 10.01.2016.
 */
public class Gun extends Item {

    private float passedTime;
    private List<Bullet> bulletList;

    public Gun() {
        super("Gun", "A classic Gun", "gun");
        bulletList = new ArrayList<Bullet>();
    }

    @Override
    public void init(Player player) {

    }

    @Override
    public void use(Player player) {
        if (passedTime >= 40) {
            System.out.println(passedTime);
            passedTime = 0;
            Bullet b = new Bullet(new Vector2(200, 0).scl(player.getFlip() ? -1 : 1), player.getBody().getTransform().getPosition(), 1, player.getWorld(), this);
            bulletList.add(b);
        }
    }

    @Override
    public void tick(float delta) {
        passedTime++;
    }

    public void render(SpriteBatch batch) {
        for (Bullet b : bulletList) {
            b.render(batch);
        }
    }

    public void customEvent(CustomEvent event) {
        if (event instanceof BulletHitWallEvent) {
            Bullet b = ((BulletHitWallEvent)event).bullet;
            if (b == null) return;
            b.dispose();
            bulletList.remove(b);

        }
    }


}
