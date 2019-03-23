package com.facultate.licenta.stats;

public class PlayerStats {

    private float maxHp ;
    private float currentHp ;
    private float speed ;
    private float fireRate ;

    public float getAccelerationRate() {
        return accelerationRate;
    }

    private float accelerationRate;

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
    public PlayerStats()
    {
        maxHp = 100;
        currentHp =100;
        speed = 3;
        fireRate = 5;
        accelerationRate = 1;
    }


}
