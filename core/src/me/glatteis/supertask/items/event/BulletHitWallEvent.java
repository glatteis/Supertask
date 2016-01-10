package me.glatteis.supertask.items.event;

import me.glatteis.supertask.objects.Bullet;

/**
 * Created by Linus on 10.01.2016.
 */
public class BulletHitWallEvent extends CustomEvent {

    public final Bullet bullet;

    public BulletHitWallEvent(Bullet bullet) {
        this.bullet = bullet;
    }

}
