package com.facultate.licenta.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.facultate.licenta.screens.PlayScreen;
import com.facultate.licenta.tools.Constants;

public class Player extends Sprite {
    public enum State {STANDING,RUNNING}
    private State currentState;
    private State previousState;
    private Animation run;
    private float stateTimer;
    private boolean right;

    private World world ;
    public Body playerBody;
    private Vector2 position;
    //sprite = o singura imagine(pozitie) din sprite sheet
    private TextureRegion sprite;
    private PlayScreen playScreen;
    public Player(PlayScreen playScreen)
    {
        //obtine regiunea din sprite sheet  asociata cu caracterul.
        super(playScreen.getAtlas().findRegion("sprite"));
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        position = new Vector2(getX(),getY());
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        right = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int j =0 ;j<=3;j++) {
            for (int i = 0; i < 6; i++) {
                frames.add(new TextureRegion(getTexture(), i * 90, j * 160, 90, 150));
            }
        }
        run = new Animation(1/30f,frames);
        frames.clear();
        definePlayer();
        //initializeaza sprite-ul cu textura de la coordonatele x,y de lungime si inaltime.
        sprite = new TextureRegion(getTexture(),0,0,80,150);
        setBounds(0,0,80/Constants.PPM,150/Constants.PPM);
        setRegion(sprite);
    }
    public boolean hasMoved()
    {
        if(position.x != playerBody.getPosition().x || position.y != playerBody.getPosition().y)
        {
            if(!playScreen.getWorld().isLocked()) {
                position.x = playerBody.getPosition().x;
                position.y = playerBody.getPosition().y;
                return true;
            }
        }
        return false;
    }
    public void update(float deltaTime) {
        //updateaza pozitia sprite=ului in functie de pozisia corpului fizic
        setPosition(playerBody.getPosition().x - getWidth()/2 , playerBody.getPosition().y - getHeight()/2);
        setRegion(getFrame(deltaTime));
    }
    private TextureRegion getFrame(float dt)
    {
        currentState = getState();
        TextureRegion region ;
        switch (currentState)
        {
            case RUNNING:
                region = (TextureRegion) run.getKeyFrame(stateTimer,true);
                break;
            case STANDING:
                default:
                    region = sprite;
                break;
        }
        if((playerBody.getLinearVelocity().x<0 || !right) && !region.isFlipX())
        {
            region.flip(true,false);
            right=false;
        }
        if((playerBody.getLinearVelocity().x>0 || right) && region.isFlipX())
        {
            region.flip(true,false);
            right=true;
        }
        stateTimer = currentState==previousState ? stateTimer+dt :0;
        previousState= currentState;
        return region;
    }
    public State getState()
    {
        if (playerBody.getLinearVelocity().x == 0  && playerBody.getLinearVelocity().y ==0)
            return State.STANDING;
        return State.RUNNING;
    }

    public void definePlayer()
    {
        if(!playScreen.getWorld().isLocked()) {
            BodyDef bodyDef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            FixtureDef fixtureDef = new FixtureDef();
            bodyDef.position.set(300 / Constants.PPM, 300 / Constants.PPM);
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            playerBody = this.world.createBody(bodyDef);
            shape.setAsBox(30 / Constants.PPM, 70 / Constants.PPM);
            //defineste categoria fixturii ca fiind bit de jucator pentru coliziune selectiva.
            fixtureDef.filter.categoryBits = Constants.PLAYER_BIT;

            //biti cu care jucatorul poate avea coliziuni
            //MASCA
            fixtureDef.filter.maskBits = Constants.DEFAULT_BIT | Constants.OBJECT_BIT;
            fixtureDef.shape = shape;
            playerBody.createFixture(fixtureDef).setUserData("player");
            shape.dispose();
        }
        else
        {
            definePlayer();
        }
    }
}
