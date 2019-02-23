package com.facultate.licenta.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.facultate.licenta.Game;
import com.facultate.licenta.objects.Bullet;
import com.facultate.licenta.objects.Enemy;
import com.facultate.licenta.objects.InteractiveTileObject;

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

            if(obj.getUserData()!=null)
                ((InteractiveTileObject) obj.getUserData()).onNoEnemies();
        }
        switch (cDef){
            case Game.BULLET_BIT | Game.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == Game.BULLET_BIT) {
                    ((Bullet) fixA.getUserData()).onHit();
                    Gdx.app.log("Cont", "1");
                    ((Enemy) fixB.getUserData()).onEnemyHit();
                }
                else if(fixB.getFilterData().categoryBits == Game.BULLET_BIT)
                {
                    Gdx.app.log("Cont", "2");
                    ((Bullet)fixB.getUserData()).onHit();
                    if(((Enemy) fixA.getUserData()).enemyBody !=null)
                    ((Enemy) fixA.getUserData()).onEnemyHit();
                }
                break;

            case Game.BULLET_BIT | Game.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Game.BULLET_BIT) {
                    ((Bullet) fixA.getUserData()).onHit();
                }
                else if(fixB.getFilterData().categoryBits == Game.BULLET_BIT)
                {
                    ((Bullet)fixB.getUserData()).onHit();
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
