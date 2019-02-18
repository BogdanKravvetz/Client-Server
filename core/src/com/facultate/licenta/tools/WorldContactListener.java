package com.facultate.licenta.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.facultate.licenta.objects.InteractiveTileObject;

public class WorldContactListener implements ContactListener {


    public WorldContactListener() {
    }

    //se apeleaza cand 2 fixtures intra in contact una cu cealalta.
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == "player" || fixB.getUserData() == "player")
        {
            Fixture player = fixA.getUserData() == "player" ? fixA : fixB;
            Fixture obj = player == fixA ? fixB : fixA;

            if(obj.getUserData()!=null)
                ((InteractiveTileObject) obj.getUserData()).onNoEnemies();
        }

    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("Colider:" , "No More Contact");
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