package com.facultate.licenta.stats;

public class EnemyStats {
    private float maxHp ;

    public void setCurrentHp(float currentHp) {
        this.currentHp = currentHp;
    }


    private float currentHp ;
    private float speed ;
    private float fireRate ;

    public float getMaxHp() {
        return maxHp;
    }

    public float getCurrentHp() {
        return currentHp;
    }

    public float getSpeed() {
        return speed;
    }

    public float getFireRate() {
        return fireRate;
    }
    public EnemyStats()
    {
        maxHp = 100;
        currentHp =100;
        speed = 3;
        fireRate = 5;
    }
}
