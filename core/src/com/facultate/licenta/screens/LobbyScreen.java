package com.facultate.licenta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.facultate.licenta.Game;
import com.facultate.licenta.conections.ConnectionHandler;
import com.facultate.licenta.conections.LobbyEventHandler;
import com.facultate.licenta.tools.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class LobbyScreen implements Screen {
    public Game getMyGame() {
        return myGame;
    }

    private com.facultate.licenta.Game myGame;

    private int buttonWidth = 300;
    private int buttonHeight = 100;

    private OrthographicCamera menuCamera;
    private Viewport menuPort;
    private Stage stage;
    private Texture background;


    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }
    private LobbyEventHandler lobbyEventHandler;
    private LobbyScreen lobby;

    public Array<String> getAllPlayers() {
        return allPlayers;
    }

    private Array<String> allPlayers;
    private ConnectionHandler connectionHandler;
    private List<String> list;

    private boolean readyStatus;
    private  Image ready;
    private Texture readyTexture;
    public LobbyScreen(Game game)
    {
        myGame = game;
        connectionHandler = new ConnectionHandler();
        connectionHandler.connectSocket();
        lobbyEventHandler = new LobbyEventHandler(connectionHandler,this);
        allPlayers = new Array<String>();
        background = new Texture("bg3.png");
        //camera pentru hud(controller)
        menuCamera = new OrthographicCamera();
        //hud-ul are nevoie de propriul viewport
        menuPort = new FitViewport(Constants.WIDTH,Constants.HEIGHT,menuCamera);
        //setup stage, stage este si input listener.
        stage = new Stage(menuPort,myGame.batch);
        Gdx.input.setInputProcessor(stage);

        readyStatus = false;
        //table e ca o masa intr=o camera(stage) pe care asezi etichete(lable)
        menuCamera.position.set(Constants.WIDTH/2f, Constants.HEIGHT/2f, 0);
        lobby = this;
        //creaza o imagine cu textura butonului
        Image back = new Image(new Texture("Back.png"));
        //scaleaza butonul
        back.setSize(buttonWidth,buttonHeight);
        //input events
        back.addListener(new InputListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true; //returneaza true pentru a intra in touch up
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                connectionHandler.getSocket().disconnect();
                myGame.setScreen(new MenuScreen(myGame));
                super.touchUp(event, x, y, pointer, button);
            }
        });
        readyTexture = new Texture("Pregatit.png");
        ready = new Image(new Texture("Ready.png"));
        //scaleaza butonul
        ready.setSize(buttonWidth,buttonHeight);
        //input events
        ready.addListener(new InputListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true; //returneaza true pentru a intra in touch up
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(!lobbyEventHandler.started) {
                    readyStatus = true;
                    connectionHandler.getSocket().emit("ready", readyStatus);
                    Gdx.app.log("LOBBYSCREEN", "I'm ready");
                } else{
                    Gdx.app.log("LOBBYSCREEN", "change scene");
                    connectionHandler.getSocket().disconnect();
                    myGame.setScreen(new MenuScreen(myGame));
                }



                //myGame.setScreen(new PlayScreen(myGame,lobby));
                super.touchUp(event, x, y, pointer, button);
            }
        });
        if (readyStatus) {
            ready.setVisible(false);
        }
        ScrollPane scrollPane;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"),atlas);
        list = new List<String>(uiSkin);
        lobbyEventHandler.lobbyConfig();

        scrollPane = new ScrollPane(list);
        scrollPane.setBounds(0, 0, Constants.WIDTH, Constants.HEIGHT + 100);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setPosition(100,100 /*Constants.WIDTH / 2f - scrollPane.getWidth() / 4,
                Constants.HEIGHT / 2f - scrollPane.getHeight() / 4*/);
        scrollPane.setTransform(true);
        scrollPane.setScale(1f);
        Label titlu = new Label("Camera de Asteptare",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        titlu.setFontScale(2);
        Table table = new Table();
        table.setWidth(Constants.WIDTH);
        table.setHeight(Constants.HEIGHT);
        table.top();//aliniaza
        table.padTop(20);
        table.add(titlu).expandX();
        table.row().pad(20,5,5,5);
        table.add(scrollPane).size(400,300).expandX();

        table.row().pad(20,5,5,5);
        //table.add();
        table.add(ready).size(ready.getWidth(),ready.getHeight());
        table.row().pad(5,5,5,5);
        table.add(back).size(back.getWidth(),back.getHeight());
        stage.addActor(table);
        if(lobbyEventHandler.getPlayersFromServer() == null)
        {
            JSONObject data = new JSONObject();
            try {
                data.put("name", Constants.name.getText());
                connectionHandler.getSocket().emit("givePlayers",data);
            }
            catch (JSONException e)
            {
                Gdx.app.log("lobbyScreen", "nameError");
            }
        }

    }

    @Override
    public void show() {

    }
    public void update()
    {
        if(allPlayers.isEmpty()) {
            if (lobbyEventHandler.getPlayersFromServer() != null) {
                Gdx.app.log("LOBBYSCREEN", "Test2");
                for (int i = 0; i < lobbyEventHandler.getPlayersFromServer().length(); i++) {
                    Gdx.app.log("LOBBYSCREEN", "Test FOR");
                    try {
                        if (!allPlayers.contains(lobbyEventHandler.getPlayersFromServer().getJSONObject(i).getString("name"), true)) {
                            allPlayers.add(lobbyEventHandler.getPlayersFromServer().getJSONObject(i).getString("name"));
                            Gdx.app.log("LOBBYSCREEN", "PLAYER - " + lobbyEventHandler.getPlayersFromServer().getJSONObject(i).getString("name"));
                        }
                    } catch (JSONException e) {
                        Gdx.app.log("LOBBYSCREEN", "error list update");
                    }
                }
                if (!allPlayers.contains(Constants.name.getText(),false))
                {
                    allPlayers.add(Constants.name.getText());
                }
            }
        }


        list.clear();
        list.setItems(allPlayers);

        if(readyStatus)
        {
            ready.setDrawable(new SpriteDrawable(new Sprite(readyTexture)));
        }

        if(lobbyEventHandler.isReadyToStart())
        {
            connectionHandler.getSocket().disconnect();
            myGame.setScreen(new PlayScreen(myGame,lobby));
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        myGame.batch.begin();
        myGame.batch.draw(background,0,0,Constants.WIDTH,Constants.HEIGHT);
        myGame.batch.end();
        stage.draw();
        stage.act(delta);
        update();
    }

    @Override
    public void resize(int width, int height) {
        menuPort.update(width,height);
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
        stage.dispose();
    }
}
