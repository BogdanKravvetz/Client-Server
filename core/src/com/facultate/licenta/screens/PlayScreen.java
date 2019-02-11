package com.facultate.licenta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.facultate.licenta.Game;
import com.facultate.licenta.Scenes.Hud;
import com.facultate.licenta.conections.ConnectionHandler;
import com.facultate.licenta.conections.SocketEventHandler;
import com.facultate.licenta.conections.UpdateServer;
import com.facultate.licenta.input.InputHandler;
import com.facultate.licenta.objects.Player;
import java.util.HashMap;
import java.util.Locale;


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
    private OrthographicCamera gameCamera;
    private Viewport gamePort;
    private Hud hud;


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
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        inputHandler.movementInput(Gdx.graphics.getDeltaTime());
        updateServer.updatePosition(Gdx.graphics.getDeltaTime());
        myGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.update(Gdx.graphics.getDeltaTime());
        hud.stage.draw();

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
