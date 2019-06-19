package com.facultate.licenta.objects;

import com.badlogic.gdx.Gdx;
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
import com.facultate.licenta.stats.PlayerStats;
import com.facultate.licenta.tools.Constants;

public class Player extends Sprite {
    public enum State {STANDING,RUNNING_UP,RUNNING_DOWN,RUNNING_LEFT,RUNNING_RIGHT}
    private State currentState;
    private State previousState;
    private Animation runDown;
    private Animation runUp;
    private Animation runRight;
    private float stateTimer;
    private boolean right;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    private World world ;
    public Body playerBody;
    private Vector2 position;
    //sprite = o singura imagine(pozitie) din sprite sheet
    private TextureRegion sprite;
    private PlayScreen playScreen;
    private PlayerStats playerStats;
    private boolean destroyed;
    private boolean setToDestroy;

    public PlayerStats getPlayerStats() {
        return playerStats;
    }
    public Player(PlayScreen playScreen)
    {
        //obtine regiunea din sprite sheet  asociata cu caracterul.
        super(playScreen.getAtlas().findRegion("Character"));
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        position = new Vector2(getX(),getY());
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        right = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int j =0 ;j<3;j++) {
                frames.add(new TextureRegion(getTexture(),  j * 64, 0, 64, 64));
        }
        runDown = new Animation(0.1f,frames);
        frames.clear();
        for(int j =0 ;j<3;j++) {
            frames.add(new TextureRegion(getTexture(),  j * 64, 64, 64, 64));
        }
        runUp = new Animation(0.1f,frames);
        frames.clear();
        for(int j =0 ;j<4;j++) {
            frames.add(new TextureRegion(getTexture(),  j * 64, 128, 64, 64));
        }
        runRight = new Animation(0.1f,frames);
        frames.clear();
        definePlayer();
        //initializeaza sprite-ul cu textura de la coordonatele x,y de lungime si inaltime.
        sprite = new TextureRegion(getTexture(),0,128,64,64);
        setBounds(0,0,64/Constants.PPM,64/Constants.PPM);
        setRegion(sprite);
        playerStats = new PlayerStats();
        setToDestroy = false;
        destroyed = false;
        id = playScreen.getConnectionHandler().getSocket().id();
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

        if(setToDestroy && !destroyed) {
            destroy();
        }
        if(!destroyed) {
            //updateaza pozitia sprite=ului in functie de pozisia corpului fizic
            setPosition(playerBody.getPosition().x - getWidth() / 2, playerBody.getPosition().y - getHeight() / 2);
            setRegion(getFrame(deltaTime));
        }
    }
    private TextureRegion getFrame(float dt)
    {
        currentState = getState();
        TextureRegion region ;
        switch (currentState)
        {
            case RUNNING_DOWN:
                region = (TextureRegion) runDown.getKeyFrame(stateTimer,true);
                sprite = (TextureRegion) runDown.getKeyFrame(0);
                break;
            case RUNNING_RIGHT:
                region = (TextureRegion) runRight.getKeyFrame(stateTimer,true);
                sprite = (TextureRegion) runRight.getKeyFrame(0);
                if(region.isFlipX()) {
                    region.flip(true, false);
                }
                break;
            case RUNNING_LEFT:
                region = (TextureRegion) runRight.getKeyFrame(stateTimer,true);
                sprite = (TextureRegion) runRight.getKeyFrame(0);
                sprite.flip(true,false);
                if(!region.isFlipX()) {
                    region.flip(true, false);
                }
                break;
            case RUNNING_UP:
                region = (TextureRegion) runUp.getKeyFrame(stateTimer,true);
                sprite = (TextureRegion) runUp.getKeyFrame(0);
                break;
            case STANDING:
                default:
                    region = sprite;
                break;
        }
        stateTimer = currentState==previousState ? stateTimer+dt :0;
        previousState= currentState;
        return region;
    }

    //        if((playerBody.getLinearVelocity().x<0 || !right) && !region.isFlipX())
//        {
//            region.flip(true,false);
//            right=false;
//        }
//        if((playerBody.getLinearVelocity().x>0 || right) && region.isFlipX())
//        {
//            region.flip(true,false);
//            right=true;
//        }

    public State getState()
    {
        if (playerBody.getLinearVelocity().x == 0  && playerBody.getLinearVelocity().y ==0)
            return State.STANDING;
        if(playerBody.getLinearVelocity().x > 0)
            return State.RUNNING_RIGHT;
        if(playerBody.getLinearVelocity().x < 0)
            return State.RUNNING_LEFT;
        if(playerBody.getLinearVelocity().y <0)
            return State.RUNNING_DOWN;
        if(playerBody.getLinearVelocity().y >0)
            return State.RUNNING_UP;
        return State.RUNNING_DOWN;
    }

    public void definePlayer()
    {
        if(!playScreen.getWorld().isLocked()) {
            BodyDef bodyDef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            FixtureDef fixtureDef = new FixtureDef();
            bodyDef.position.set(3350 / Constants.PPM, 2300 / Constants.PPM); //3350 , 2300
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            playerBody = this.world.createBody(bodyDef);
            shape.setAsBox(16 / Constants.PPM, 30 / Constants.PPM);
            //defineste categoria fixturii ca fiind bit de jucator pentru coliziune selectiva.
            fixtureDef.filter.categoryBits = Constants.PLAYER_BIT;

            //biti cu care jucatorul poate avea coliziuni
            //MASCA
            fixtureDef.filter.maskBits = Constants.DEFAULT_BIT | Constants.OBJECT_BIT | Constants.PICKUP_BIT | Constants.ENEMY_BIT;
            fixtureDef.shape = shape;
            playerBody.createFixture(fixtureDef).setUserData(this);
            shape.dispose();
        }
        else
        {
            definePlayer();
        }
    }

    public void setToDestroy()
    {
        setToDestroy = true;
    }
    public void destroy()
    {
        world.destroyBody(playerBody);
        destroyed = true;
        //stateTime = 0;
        playerBody = null;
    }
}
