package me.glatteis.supertask.objects;

/**
 * Created by Linus on 10.01.2016.
 */
public class PlayerStats {

    public float speed;
    public float damage;
    public float jumpForce;

    public PlayerStats() {}

    public PlayerStats(float speed, float damage, float jumpForce) {
        this.speed = speed;
        this.damage = damage;
        this.jumpForce = jumpForce;
    }

}
