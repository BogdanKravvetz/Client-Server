package com.facultate.licenta.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.facultate.licenta.Game;
import com.facultate.licenta.Scenes.Controller;
import com.facultate.licenta.Scenes.Hud;
import com.facultate.licenta.conections.ConnectionHandler;
import com.facultate.licenta.conections.SocketEventHandler;
import com.facultate.licenta.conections.UpdateServer;
import com.facultate.licenta.input.InputHandler;
import com.facultate.licenta.items.Item;
import com.facultate.licenta.items.ItemDef;
import com.facultate.licenta.items.LifeCrystal;
import com.facultate.licenta.objects.DefaultBullet;
import com.facultate.licenta.objects.Enemy;
import com.facultate.licenta.objects.Player;
import com.facultate.licenta.objects.VenomBullet;
import com.facultate.licenta.tools.Constants;
import com.facultate.licenta.tools.UpdateObjects;
import com.facultate.licenta.tools.WorldContactListener;
import com.facultate.licenta.tools.WorldCreator;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

public class PlayScreen implements Screen {
    public Game getMyGame() {
        return myGame;
    }

    private Game myGame;
    private UpdateServer updateServer;
    private SocketEventHandler socketEvents;
    private float inGameTimer;

    private ConnectionHandler connectionHandler;
    private Player player;
    private HashMap<String, Player> allPlayers;

    private InputHandler inputHandler;
    private UpdateObjects updateObjects;
    private Array<DefaultBullet> bullets;

    public Array<VenomBullet> getEnemyBullets() {
        return enemyBullets;
    }

    private Array<VenomBullet> enemyBullets;
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
    private WorldCreator worldCreator;
    //tot ce este in word este afectat de physics.
    private World world;
    //randeaza cutii verzi pentru collider-e TODO de sters mai tarziu
    private Box2DDebugRenderer debugRenderer;
    //Pachet care contine mai multe sprite sheet-uri
    private TextureAtlas atlas;

    public boolean test;
    private float timer;

    private Array<Item> items;
    private PriorityQueue<ItemDef> itemsToSpawn;

    private LobbyScreen lobbyScreen;

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

    public Controller getController() {
        return controller;
    }

    public World getWorld() {
        return world;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public OrthographicCamera getGameCamera() {
        return gameCamera;
    }

    public HashMap<String, Player> getAllPlayers() {
        return allPlayers;
    }

    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public WorldCreator getWorldCreator() {
        return worldCreator;
    }

    public SocketEventHandler getSocketEvents() {
        return socketEvents;
    }

    public Array<DefaultBullet> getBullets() {
        return bullets;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public UpdateServer getUpdateServer() {
        return updateServer;
    }

    public TiledMap getMap() {
        return map;
    }

    public PlayScreen(Game game,LobbyScreen lobbyScreen) {
        this.myGame = game;
        world = new World(new Vector2(0, 0), true); //true indica faptul ce obiectele care nu se misca sunt puse in sleep, nu sunt calculate pyhx simulation.
        atlas = new TextureAtlas("Characters.pack");
        allPlayers = new HashMap<String, Player>();
        //connectionHandler = new ConnectionHandler();
        connectionHandler =  lobbyScreen.getConnectionHandler();
        Random rdm =  new Random();
        try {
            Thread.sleep(rdm.nextInt(3000)+200);
        }
        catch (InterruptedException e)
        {
            System.out.print(e);
        }

        connectionHandler.connectSocket();
        updateServer = new UpdateServer(this, connectionHandler);
        socketEvents = new SocketEventHandler(this, connectionHandler);
        socketEvents.configSocketEvents();

        //
        player = new Player(this);
        inputHandler = new InputHandler(this);
        gameCamera = new OrthographicCamera();
        gamePort = new FitViewport(896 / Constants.PPM, 504 / Constants.PPM, gameCamera);
        controller = new Controller(myGame);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);
        //centreaza camera in mijocul lumii? in loc de 0 0
        gameCamera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        //creaza lumea, Vector 0 0 inseamna nu e exista gravitatie.
        debugRenderer = new Box2DDebugRenderer();
        //initializeaza atlasul cu datele din pack-ul generat de texture packer.

        test = false;
        timer = 0;
        world.setContactListener(new WorldContactListener());
        worldCreator = new WorldCreator(this);
        updateObjects = new UpdateObjects(this);
        bullets = new Array<DefaultBullet>();
        enemyBullets = new Array<VenomBullet>();
        items = new Array<Item>();
        itemsToSpawn = new PriorityQueue<ItemDef>();
        hud = new Hud(game.batch, this);

    }
    public void spawnItem(ItemDef itemDef){


        itemsToSpawn.add(itemDef);
    }
    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty())
        {
            ItemDef iDef = itemsToSpawn.poll();
            if(iDef.type == LifeCrystal.class){
                items.add(new LifeCrystal(this,iDef.position.x,iDef.position.y));
            }
        }
    }
    @Override
    public void show() {

    }
    public void update(float delta) {
        //update de 60  de ori pe secunda.
        world.step(1 / 60f, 6, 2);

        timer += delta;
        if (getWorldCreator().getSpiders().isEmpty() || getWorldCreator().getSpiders() == null) {
            updateObjects.getSpiders();
        }

        if (allPlayers.isEmpty()) {
            updateObjects.getOtherPlayers();
        }
        updateServer.updatePlayerPosition(Gdx.graphics.getDeltaTime());
        updateServer.updateSpiders(Gdx.graphics.getDeltaTime());
        updateObjects.moveOtherPlayers();
        updateObjects.moveSpiders();
        updateObjects.updateBullets();
        updateObjects.enemyShoot();
        handleSpawningItems();
        if ((int) timer == 5) {
            updateObjects.stopSpiders();//nu se vor opri pentru ca move e apelat fiecare frame. dar tot se executa asta.
            timer = 0;
        }
        inputHandler.movementInput(delta);
        if (player != null) {
            gameCamera.position.x = player.playerBody.getPosition().x;
            gameCamera.position.y = player.playerBody.getPosition().y;
            player.update(delta);
        }
        for (DefaultBullet bullet : bullets) {
            bullet.update(delta);
        }
        for (VenomBullet bullet : enemyBullets) {
            bullet.update(delta);
        }
        for (Enemy enemy : worldCreator.getSpiders()) {
            enemy.update(delta);
        }
        for(Item item : items)
        {
            item.update(delta);
        }
        gameCamera.update();
        hud.update(delta);
        renderer.setView(gameCamera);
    }

    @Override
    public void render(float delta) {
        //separa logica update de render
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //randeaza mapa
        renderer.render();
        //randeaza liniile coliziunilor
        //debugRenderer.render(world, gameCamera.combined);
        myGame.batch.setProjectionMatrix(gameCamera.combined);
        myGame.batch.begin();

        if (player != null) {
            player.draw(myGame.batch);
        }
        for (Enemy enemy : worldCreator.getSpiders()) {
            if (enemy != null && myGame.batch != null && !worldCreator.getSpiders().isEmpty() && worldCreator.getSpiders() != null)
                enemy.draw(myGame.batch);
        }
        //randeaza toti jucatorii in functie de lista primita de la server.
        for (HashMap.Entry<String, Player> entry : allPlayers.entrySet()) {
            entry.getValue().update(delta);
            entry.getValue().draw(myGame.batch);
        }
        for (Item item : items) {
            item.draw(myGame.batch);
        }
        for (DefaultBullet bullet : bullets) {
            bullet.draw(myGame.batch);
        }
        for (VenomBullet bullet : enemyBullets) {
            bullet.draw(myGame.batch);
        }

        myGame.batch.end();
        //seteaza batch-ul ca acum sa randeze ceea ce camera de la hud vede.
        myGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);

        hud.stage.draw();
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            controller.draw();
        }
    }

    @Override
    public void resize(int width, int height) {

        gamePort.update(width, height);
        controller.resize(width, height);
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

        map.dispose();
        renderer.dispose();
        world.dispose();
        debugRenderer.dispose();
        hud.dispose();
    }
}
