package com.facultate.licenta.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.facultate.licenta.items.LifeCrystal;
import com.facultate.licenta.objects.Bullet;
import com.facultate.licenta.objects.Enemy;
import com.facultate.licenta.objects.InteractiveTileObject;
import com.facultate.licenta.objects.Player;
import com.facultate.licenta.objects.Spider;

public class WorldContactListener implements ContactListener {


    public WorldContactListener() {
    }

    //se apeleaza cand 2 fixtures intra in contact una cu cealalta.
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if(fixA.getUserData() == "player" || fixB.getUserData() == "player")
        {
            Fixture player = fixA.getUserData() == "player" ? fixA : fixB;
            Fixture obj = player == fixA ? fixB : fixA;

//            if(obj.getUserData()!=null)
//                ((InteractiveTileObject) obj.getUserData()).onNoEnemies();
        }
        switch (cDef){
            case Constants.BULLET_BIT | Constants.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == Constants.BULLET_BIT) {
                    ((Bullet) fixA.getUserData()).onHit();
                    ((Enemy) fixB.getUserData()).onEnemyHit(((Bullet) fixA.getUserData()),((Bullet) fixA.getUserData()).getPlayScreen().getPlayer());
                }
                else if(fixB.getFilterData().categoryBits == Constants.BULLET_BIT)
                {
                    ((Bullet)fixB.getUserData()).onHit();
                    ((Enemy) fixA.getUserData()).onEnemyHit(((Bullet) fixB.getUserData()),((Bullet) fixB.getUserData()).getPlayScreen().getPlayer());
                }
                break;

            case Constants.BULLET_BIT | Constants.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Constants.BULLET_BIT) {
                    ((Bullet) fixA.getUserData()).onHit();
                }
                else if(fixB.getFilterData().categoryBits == Constants.BULLET_BIT)
                {
                    ((Bullet)fixB.getUserData()).onHit();
                }
                break;
            case  Constants.PLAYER_BIT | Constants.PICKUP_BIT:
                if(fixA.getFilterData().categoryBits == Constants.PICKUP_BIT) {
                    ((LifeCrystal) fixA.getUserData()).use(((Player) fixB.getUserData()));
                   // if(((Player) fixB.getUserData()).getId().equals())
                }
                else if(fixB.getFilterData().categoryBits == Constants.PICKUP_BIT)
                {
                    ((LifeCrystal) fixB.getUserData()).use(((Player) fixA.getUserData()));
                }
                break;
            case Constants.PLAYER_BIT | Constants.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == Constants.ENEMY_BIT) {
                    ((Spider) fixA.getUserData()).hit(((Player) fixB.getUserData()));
                }
                else if(fixB.getFilterData().categoryBits == Constants.ENEMY_BIT)
                {
                    ((Spider) fixB.getUserData()).hit(((Player) fixA.getUserData()));
                }
                break;
            case Constants.ENEMY_BULLET_BIT | Constants.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Constants.ENEMY_BULLET_BIT) {
                    ((Bullet) fixA.getUserData()).onHit();
                }
                else if(fixB.getFilterData().categoryBits == Constants.ENEMY_BULLET_BIT)
                {
                    ((Bullet)fixB.getUserData()).onHit();
                }
                break;
            case Constants.ENEMY_BULLET_BIT | Constants.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == Constants.ENEMY_BULLET_BIT) {
                    ((Bullet) fixA.getUserData()).onHit();
                    ((Player) fixB.getUserData()).hit((Player) fixB.getUserData());
                }
                else if(fixB.getFilterData().categoryBits == Constants.ENEMY_BULLET_BIT)
                {
                    ((Bullet)fixB.getUserData()).onHit();
                    ((Player) fixA.getUserData()).hit((Player) fixA.getUserData());
                }
                break;

        }
    }


    @Override
    public void endContact(Contact contact) {
        //Gdx.app.log("Colider:" , "No More Contact");
    }
    //cand ceva a intrat in coloziune se pot schimba caracteristicile acelui colider.
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }
    //rezultatul a ceea ce s-a intamplat dupa coloziune.
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
