package com.facultate.licenta.stats;

public class BulletStats {

    private float lifeSpan;
    private float speed;
    private float damage;

    public float getLifeSpan() {
        return lifeSpan;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDamage() {
        return damage;
    }
    public BulletStats()
    {
        lifeSpan = 2;
        speed = 15;
        damage = 50;
    }


}
