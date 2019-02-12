package com.facultate.licenta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.facultate.licenta.Game;
import com.facultate.licenta.Scenes.Controller;
import com.facultate.licenta.Scenes.Hud;
import com.facultate.licenta.conections.ConnectionHandler;
import com.facultate.licenta.conections.SocketEventHandler;
import com.facultate.licenta.conections.UpdateServer;
import com.facultate.licenta.input.InputHandler;
import com.facultate.licenta.objects.Player;
import java.util.HashMap;


public class PlayScreen implements Screen {

    private Game myGame;
    private UpdateServer updateServer;
    private SocketEventHandler socketEvents;

    private float inGameTimer;
    private ConnectionHandler connectionHandler;
    private Player player;
    private Texture playerSprite;
    private HashMap<String,Player> allPlayers;
    private InputHandler inputHandler;

    //camera jocului
    private OrthographicCamera gameCamera;
    private Viewport gamePort;
    //interfata (Timer)
    private Hud hud;
    //Sagetile pentru mobile.
    private Controller controller;


    private TmxMapLoader mapLoader;
    //level-ul in sine importat din Tiled
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //tot ce este in word este afectat de physics.
    private World world;
    //randeaza cutii verzi pentru collider-e TODO de ster mai tarziu
    private Box2DDebugRenderer debugRenderer;


    public float getInGameTimer() {
        return inGameTimer;
    }
    public void setInGameTimer(float inGameTimer) {
        this.inGameTimer = inGameTimer;
    }
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public Texture getPlayerSprite() {
        return playerSprite;
    }
    public Controller getController() {
        return controller;
    }

    public OrthographicCamera getGameCamera() {
        return gameCamera;
    }
    public HashMap<String, Player> getAllPlayers() {
        return allPlayers;
    }
    public PlayScreen(Game game) {
        this.myGame = game;
        playerSprite = new Texture("Character.png");
        allPlayers = new HashMap<String, Player>();
        connectionHandler = new ConnectionHandler();
        connectionHandler.connectSocket();
        updateServer = new UpdateServer(this,connectionHandler);
        socketEvents = new SocketEventHandler(this,connectionHandler);
        socketEvents.configSocketEvents();
        inputHandler = new InputHandler(this);
        gameCamera = new OrthographicCamera();
        gamePort = new FitViewport(myGame.WIDTH,myGame.HEIGHT,gameCamera);
        hud = new Hud(game.batch,this);
        controller = new Controller();
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        //centreaza camera in mijocul lumii? in loc de 0 0
        gameCamera.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2,0);
        //creaza lumea, Vector 0 0 inseamna nu e exista gravitatie.
        world = new World(new Vector2(0,0),true); //true indica faptl ce obiectele car nu se misca sunt puse in sleep, nu sunt calculate pyhx simulation.
        debugRenderer = new Box2DDebugRenderer();
        //inainte de a crea in Body trebuie mai intai specificat ceea ce contine
        BodyDef bodyDef = new BodyDef();
        //polygon shape pentru Fixture
        PolygonShape shape = new PolygonShape();
        //inainte de a crea fixture trebuie definita mai intai inainte de a fi adaugata in body
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class))//!!!!!!!!GET 2 HARD CODAT !!!!!!!!!!!!! layer-ele se numara de jos in sus de la 0 in tiled
        {
            //dreptunigiul care definste coliziunea
            Rectangle rect = ((RectangleMapObject) object) .getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX()+rect.getWidth()/2, rect.getY()+rect.getHeight()/2);
            //adauga Body-ul in lumea Box2d
            body = world.createBody(bodyDef);
            //defineste forma poligonului
            shape.setAsBox(rect.getWidth()/2,rect.getHeight()/2);
            fixtureDef.shape = shape;
            //adauga fixture in body
            body.createFixture(fixtureDef);
        }
    }
    @Override
    public void show() {

    }
    public void update( float delta)
    {
        inputHandler.movementInput(Gdx.graphics.getDeltaTime());
        gameCamera.update();
        renderer.setView(gameCamera);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        updateServer.updatePosition(Gdx.graphics.getDeltaTime());
        myGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.update(Gdx.graphics.getDeltaTime());
        hud.stage.draw();
        controller.draw();
        debugRenderer.render(world,gameCamera.combined);
        myGame.batch.begin();

        if(player!=null)
        {
            player.draw(myGame.batch);
        }
        //randeaza toti jucatorii in functie de lista primita de la server.
        for (HashMap.Entry<String,Player> entry : allPlayers.entrySet())
        {
            entry.getValue().draw(myGame.batch);
        }
        myGame.batch.end();
    }

    @Override
    public void resize(int width, int height) {

        gamePort.update(width,height);
        controller.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        playerSprite.dispose();
    }
}
